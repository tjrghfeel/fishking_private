/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore",
  "ModalStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.file = React.createRef(null);
          this.tasteScore = React.createRef(null);
          this.serviceScore = React.createRef(null);
          this.cleanScore = React.createRef(null);
          this.state = {
            content: "",
            fileList: [],
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          this.loadPageData();
        }

        loadPageData = async () => {
          const {
            PageStore,
            APIStore,
            match: {
              params: { id: ordersId },
            },
          } = this.props;
          const resolve = await APIStore._get("/v2/api/OrdersDetail", {
            ordersId,
          });
          this.setState({ ...resolve });

          // # 별점 스크립트 로드
          PageStore.injectScript("/assets/cust/js/jquery.rateit.min.js", {
            global: true,
          });
        };

        uploadFile = async () => {
          const { ModalStore } = this.props;
          if (this.state.fileList.length >= 20) {
            ModalStore.openModal("Alert", {
              body: "최대 20장까지 가능합니다.",
            });
            this.file.current.value = null;
            return;
          }

          if (this.file.current?.files.length > 0) {
            const file = this.file.current?.files[0];

            const form = new FormData();
            form.append("file", file);
            form.append("filePublish", "review");

            const { APIStore } = this.props;
            const upload = await APIStore._post_upload(
              "/v2/api/filePreUpload",
              form
            );

            if (upload) {
              this.setState({ fileList: this.state.fileList.concat(upload) });
            }
            this.file.current.value = null;
          }
        };

        removeUploadFile = (fileId) => {
          const { DataStore } = this.props;
          const fileList = DataStore.removeItemOfArrayByKey(
            this.state.fileList,
            "fileId",
            fileId
          );
          this.setState({ fileList });
        };

        submit = async () => {
          const tasteScore = $(this.tasteScore.current).rateit("value");
          const serviceScore = $(this.serviceScore.current).rateit("value");
          const cleanScore = $(this.cleanScore.current).rateit("value");
          const { goodsId, content, fileList } = this.state;
          const pFileList = [];
          for (let file of fileList) {
            pFileList.push(file["fileId"]);
          }

          const params = {
            fishingDate: this.state.fishingDate,
            tasteScore,
            serviceScore,
            cleanScore,
            goodsId,
            content,
            fileList: pFileList,
          };

          const { APIStore, ModalStore, PageStore } = this.props;
          const resolve = await APIStore._post(`/v2/api/review`, params);
          if (resolve) {
            ModalStore.openModal("Alert", {
              body: "등록되었습니다.",
              onOk: () => {
                PageStore.goBack();
              },
            });
          }
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          return (
            <React.Fragment>
              <NavigationLayout title={"리뷰 작성"} showBackIcon={true} />

              <div className="container nopadding bg-grey-title text-center">
                <h5 className="mb-1">{this.state.shipName}</h5>
                <h6 className="mt-0 mb-3">
                  {this.state.fishingDate && (
                    <React.Fragment>
                      {this.state.fishingDate.substr(0, 4).concat(".")}
                      {this.state.fishingDate.substr(4, 2).concat(".")}
                      {this.state.fishingDate.substr(6, 2).concat(" ")}
                    </React.Fragment>
                  )}
                  {/*<small className="grey">/</small> 우럭(오후){" "}*/}
                </h6>
              </div>
              <hr className="mt-0" />
              <p></p>

              {/** 리뷰 */}
              <div className="container nopadding mt-4">
                <div className="row no-gutters align-items-center">
                  <div className="col-12 text-center">
                    <h6 className="mb-2">낚시의 매력인 손맛 어떠셨나요? </h6>
                    <div
                      ref={this.tasteScore}
                      className="rateit"
                      data-rateit-value="5"
                      data-rateit-ispreset="true"
                      data-rateit-resetable="false"
                      data-rateit-starwidth="16"
                      data-rateit-starheight="16"
                    ></div>
                    <h6 className="mt-3 mb-2">친절한 서비스를 받으셨나요?</h6>
                    <div
                      ref={this.serviceScore}
                      className="rateit"
                      data-rateit-value="5"
                      data-rateit-ispreset="true"
                      data-rateit-resetable="false"
                      data-rateit-starwidth="16"
                      data-rateit-starheight="16"
                    ></div>
                    <h6 className="mt-3 mb-2">
                      선실 내부와 화장실은 청결했나요?
                    </h6>
                    <div
                      ref={this.cleanScore}
                      className="rateit"
                      data-rateit-value="5"
                      data-rateit-ispreset="true"
                      data-rateit-resetable="false"
                      data-rateit-starwidth="16"
                      data-rateit-starheight="16"
                    ></div>
                  </div>
                </div>
                <p className="space"></p>
                <div className="row no-gutters align-items-center">
                  <div className="col-12 text-center">
                    <h6 className="mt-3 mb-2">
                      어떤 점이 좋았나요? <small>(선택)</small>
                    </h6>
                  </div>
                </div>
              </div>

              {/** 내용 */}
              <div className="container nopadding mt-3">
                <div className="row no-gutters">
                  <div className="form-group col-12">
                    <div className="input-group">
                      <textarea
                        className="form-control"
                        rows="15"
                        style={{ height: "170px !important" }}
                        placeholder="업체명, 상품명, 어종, 이용일, 날씨 정보는 자동으로 입력됩니다.
사진은 최대 50장까지 등록 가능합니다. (GIF, 동영상 불가)
이용약관을 위반한 리뷰는 통보 없이 삭제 될 수 있습니다."
                        value={this.state.content}
                        onChange={(e) =>
                          this.setState({ content: e.target.value })
                        }
                      ></textarea>
                    </div>
                  </div>
                </div>
              </div>

              {/** 이미지 */}
              <input
                ref={this.file}
                type="file"
                accept="image/*"
                style={{ display: "none" }}
                onChange={this.uploadFile}
              />
              <div className="container nopadding mt-3">
                <div className="row no-gutters d-flex align-items-center">
                  <div className="col-3">
                    <a onClick={() => this.file.current?.click()}>
                      <div className="box-round-grey">
                        <img
                          src="/assets/cust/img/svg/icon-plus-blue.svg"
                          alt=""
                          className="icon-sm icon-plus"
                        />
                      </div>
                    </a>
                  </div>
                  {this.state.fileList.map((data, index) => (
                    <div className="col-3" key={index}>
                      <div className="box-round-grey">
                        <a
                          onClick={() => this.removeUploadFile(data.fileId)}
                          className="del"
                        >
                          <img src="/assets/cust/img/svg/icon_close_white.svg" />
                        </a>
                        <img
                          src={data.downloadUrl}
                          className="d-block w-100 photo-img"
                          alt=""
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              {/** 안내 */}
              <div className="container nopadding mt-3">
                <div className="card card-box">
                  <h6 className="card-header-white text-center">
                    <img
                      src="/assets/cust/img/svg/icon-alert.svg"
                      alt=""
                      className="vam"
                    />
                    &nbsp;알려드립니다.
                  </h6>
                  <div className="card-body">
                    <ul className="list mb-2">
                      <li>
                        업체명, 상품명, 어종, 이용일, 날씨 정보는 자동으로
                        입력됩니다.
                      </li>
                      <li>
                        사진은 최대 50장까지 등록 가능합니다. (GIF, 동영상 불가)
                      </li>
                      <li>
                        이용약관을 위반한 리뷰는 통보 없이 삭제 될 수 있습니다.
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <p className="clearfix">
                <br />
                <br />
              </p>

              <div className="fixed-bottom">
                <div className="row no-gutters">
                  <div className="col-12">
                    <a
                      onClick={this.submit}
                      className="btn btn-primary btn-lg btn-block"
                    >
                      리뷰 등록
                    </a>
                  </div>
                </div>
              </div>
            </React.Fragment>
          );
        }
      }
    )
  )
);
