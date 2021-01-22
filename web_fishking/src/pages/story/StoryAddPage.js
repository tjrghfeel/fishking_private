import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
  MODAL: {
    SelectFishModal,
    SelectDateModal,
    SelectTideModal,
    SelectTechnicModal,
    SelectLureModal,
    SelectPlaceModal,
    SelectLocationModal,
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file = React.createRef(null);
        this.state = {
          uploaded: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      uploadFile = async () => {
        if (this.file.current?.files.length > 0) {
          const file = this.file.current?.files[0];

          console.log(file);

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "one2one");

          const { APIStore } = this.props;
          const upload = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );

          if (upload) {
            this.setState({ uploaded: this.state.uploaded.concat(upload) });
          }
          this.file.current.value = null;
        }
      };
      removeUploadFile = (fileId) => {
        const { DataStore } = this.props;
        const uploaded = DataStore.removeItemOfArrayByKey(
          this.state.uploaded,
          "fileId",
          fileId
        );
        this.setState({ uploaded });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectTideModal
              id={"selTideModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectTechnicModal
              id={"selTechnicModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectLureModal
              id={"selLureModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectPlaceModal
              id={"selPlaceModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectLocationModal id={"selLocationModal"} />

            <NavigationLayout title={"글쓰기"} showBackIcon={true} />

            {/** 카테고리 / 제목 */}
            <div className="container nopadding">
              <div className="card">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <select
                      className="form-control"
                      id="exampleFormControlSelect1"
                    >
                      <option>카테고리를 선택해 주세요.</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      id="inputPhone"
                      placeholder="제목을 입력해 주세요. (30자 이하) "
                      value=""
                    />
                  </div>
                </form>
              </div>
            </div>
            {/** 어종 / 날짜 / 물때 / 낚시기법 / 미끼 / 낚시장소 */}
            <div className="container nopadding">
              <div className="card-box-grey">
                <dl className="dl-horizontal-round dl-line">
                  <dt>
                    어종 <span className="red">*</span>
                  </dt>
                  <a data-toggle="modal" data-target="#selFishModal">
                    <dd>
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>
                    날짜 <span className="red">*</span>
                  </dt>
                  <a data-toggle="modal" data-target="#selDateModal">
                    <dd>
                      2020년 8월 17일 월요일{" "}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>물때</dt>
                  <a data-toggle="modal" data-target="#selTideModal">
                    <dd>
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>낚시 기법</dt>
                  <a data-toggle="modal" data-target="#selTechnicModal">
                    <dd>
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>미끼</dt>
                  <a data-toggle="modal" data-target="#selLureModal">
                    <dd>
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>낚시 장소</dt>
                  <a data-toggle="modal" data-target="#selPlaceModal">
                    <dd>
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                </dl>
              </div>
            </div>
            {/** 선상 */}
            <div className="container nopadding mt-3">
              <a data-toggle="modal" data-target="#selLocationModal">
                <div className="card-round-box pt-0 pb-0 pl-0">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/img/sample/boat2.jpg"
                        className="img-fluid"
                        alt=""
                      />
                    </div>
                    <div className="cardInfoWrap">
                      <div className="card-body">
                        <img
                          src="/assets/img/svg/arrow-right.svg"
                          alt=""
                          className="float-right-arrow"
                        />
                        <h6>어복황제3호</h6>
                        <p>인천 중구 축항대로 142</p>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
            </div>
            {/** 문구 */}
            <div className="container nopadding mt-3">
              <div className="row no-gutters">
                <div className="form-group col-12">
                  <div className="input-group">
                    <textarea
                      className="form-control"
                      rows="15"
                      style={{ height: "170px !important" }}
                      placeholder="대표 어종, 조황 날짜, 업체 또는 지역을 등록하지 않으면 글이 등록되지 않습니다.
사진(필수항목)은 최대 50장 까지, 동영상(선택항목)은 1개만 등록 가능합니다.
계좌번호, 전화번호, 홈페이지, 중복 게시글이 포함된 경우 삭제됩니다."
                      id="inputMemo"
                    ></textarea>
                  </div>
                </div>
              </div>
            </div>
            {/** 첨부 이미지 */}
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
                        src="/assets/img/svg/icon-plus-blue.svg"
                        alt=""
                        className="icon-sm icon-plus"
                      />
                    </div>
                  </a>
                </div>
                {this.state.uploaded.map((data, index) => (
                  <div className="col-3">
                    <div className="box-round-grey">
                      <a
                        onClick={() => this.removeUploadFile(data.fileId)}
                        className="del"
                      >
                        <img src="/assets/img/svg/icon_close_white.svg" />
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
            <div className="container nopadding mt-5">
              <div className="card card-box">
                <h6 className="card-header-white text-center">
                  <img
                    src="/assets/img/svg/icon-alert.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;알려드립니다.
                </h6>
                <div className="card-body">
                  <ul className="list mb-2">
                    <li>
                      대표 어종, 조황 날짜, 업체 또는 지역을 등록하지 않으면
                      글이 등록되지 않습니다.
                    </li>
                    <li>
                      사진(필수항목)은 최대 50장 까지, 동영상(선택항목)은 1개만
                      등록 가능합니다.
                    </li>
                    <li>
                      계좌번호, 전화번호, 홈페이지, 중복 게시글이 포함된 경우
                      삭제됩니다.
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <p className="clearfix">
              <br />
              <br />
            </p>
            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a href="#none" className="btn btn-primary btn-lg btn-block">
                    올리기
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
