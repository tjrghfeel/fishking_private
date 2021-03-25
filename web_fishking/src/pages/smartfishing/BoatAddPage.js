/* global daum, kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.profileImage = React.createRef(null);
        this.videoId = React.createRef(null);
        this.ifrmAddress = React.createRef(null);
        this.zonecode = React.createRef(null);
        this.textAddr = React.createRef(null);
        this.state = {
          name: "", // 선박명
          fishingType: "ship", // 구분 : 선상 = ship , 갯바위 = seaRocks
          fishSpecies: [],
          services: [],
          facilities: [],
          events: [],
          noticeTitle: null,
          notice: null,
          profileImage: null, // 선박사진
          videoId: null, // 녹화영상
          sido: null,
          sigungu: null,
          latitude: null,
          longitude: null,
          router: null,

          arr_fishSpecies: [],
          arr_services: [],
          arr_facilities: [],
          arr_adtCameras: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { DataStore, APIStore } = this.props;
        // 주요어종
        const arr_fishSpecies = await DataStore.getCodes("80", 3);
        this.setState({ arr_fishSpecies });
        // 서비스제공
        const arr_services = await DataStore.getCodes("85", 3);
        this.setState({ arr_services });
        // 편의시설
        const arr_facilities = await DataStore.getCodes("87", 3);
        this.setState({ arr_facilities });
        // 카메라 리스트
        const arr_adtCameras = await APIStore._get(`/v2/api/ship/cameras`);
        this.setState({ arr_adtCameras });
        console.log(JSON.stringify(arr_adtCameras));
      }
      uploadFile = async (uploadType) => {
        const { APIStore } = this.props;
        if (uploadType === "profileImage") {
          if (this.profileImage.current?.files.length > 0) {
            const file = this.profileImage.current?.files[0];
            const form = new FormData();
            form.append("file", file);
            form.append("filePublish", "ship");
            const upload = await APIStore._post_upload(
              `/v2/api/filePreUpload`,
              form
            );
            if (upload && upload["downloadUrl"])
              this.setState({ profileImage: upload["downloadUrl"] });
          }
        } else if (uploadType === "videoId") {
          if (this.videoId.current?.files.length > 0) {
            const file = this.videoId.current?.files[0];
            const form = new FormData();
            form.append("file", file);
            form.append("filePublish", "ship");
            const upload = await APIStore._post_upload(
              `/v2/api/filePreUpload`,
              form
            );
            if (upload && upload["fileId"])
              this.setState({ videoId: upload["fileId"] });
          }
        }
      };
      openFindAddress = () => {
        if (this.ifrmAddress.current.style.display === "block") return;

        const currentScroll = Math.max(
          document.body.scrollTop,
          document.documentElement.scrollTop
        );
        new daum.Postcode({
          width: "100%",
          height: "100%",
          oncomplete: (data) => {
            console.log(JSON.stringify(data));
            let addr = "";
            if (data.userSelectedType === "R") {
              addr = data.roadAddress;
            } else {
              addr = data.jibunAddress;
            }
            this.setState({
              sido: addr.split(" ")[0],
              sigungu: addr.split(" ")[1],
            });
            this.zonecode.current.value = data["zonecode"];
            this.textAddr.current.value = addr;
            this.ifrmAddress.current.style.display = "none";
            document.body.scrollTop = currentScroll;
            // 좌표변환
            const geocoder = new kakao.maps.services.Geocoder();
            geocoder.addressSearch(addr, (result, status) => {
              console.log(JSON.stringify(result));
              if (status === kakao.maps.services.Status.OK) {
                const x = result[0].x;
                const y = result[0].y;
              }
            });
          },
        }).embed(this.ifrmAddress.current);
        this.ifrmAddress.current.style.display = "block";
      };
      submit = async () => {
        console.log(JSON.stringify(this.state));
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore, DataStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"선박등록"} showBackIcon={true} />

            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수입력
              </p>
              <form>
                <div className="form-group">
                  <label htmlFor="InputGName">
                    선박명 <strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="InputGName"
                    placeholder="선박명을 입력하세요"
                  />
                </div>
                <div className="form-group mb-1">
                  <label htmlFor="InputGPrice" className="d-block">
                    구분 <strong className="required"></strong>
                  </label>
                  <label className="control radio">
                    <input
                      name={"checkFishingType"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      defaultChecked={this.state.fishingType}
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ fishingType: "ship" });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">선상</span>
                  </label>{" "}
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <label className="control radio">
                    <input
                      name={"checkFishingType"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ fishingType: "seaRocks" });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">갯바위</span>
                  </label>
                </div>
                <div className="space mt-1 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={() => {
                      const arr_ele = document.querySelectorAll(
                        '[name="checkbox-fishSpecies"]'
                      );
                      for (let ele of arr_ele) {
                        ele.checked = true;
                      }
                      const fishSpecies = [];
                      for (let item of this.state.arr_fishSpecies) {
                        fishSpecies.push(item["code"]);
                      }
                      this.setState({ fishSpecies });
                    }}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    주요어종 <strong className="required"></strong>
                  </label>
                  {(this.state.arr_fishSpecies || []).map((data, index) => (
                    <div key={index} className="row">
                      {data.map((item, index2) => (
                        <div key={index2} className="col">
                          {item["id"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"checkbox-fishSpecies"}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  if (e.target.checked) {
                                    this.setState({
                                      fishSpecies: this.state.fishSpecies.concat(
                                        item["code"]
                                      ),
                                    });
                                  } else {
                                    this.setState({
                                      fishSpecies: DataStore.removeItemOfArrayByItem(
                                        this.state.fishSpecies,
                                        item["code"]
                                      ),
                                    });
                                  }
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mt-1 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={() => {
                      const arr_ele = document.querySelectorAll(
                        '[name="checkbox-services"]'
                      );
                      for (let ele of arr_ele) {
                        ele.checked = true;
                      }
                      const services = [];
                      for (let item of this.state.arr_services) {
                        services.push(item["code"]);
                      }
                      this.setState({ services });
                    }}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    서비스제공 <strong className="required"></strong>
                  </label>
                  {(this.state.arr_services || []).map((data, index) => (
                    <div key={index} className="row">
                      {data.map((item, index2) => (
                        <div key={index2} className="col">
                          {item["id"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"checkbox-services"}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  if (e.target.checked) {
                                    this.setState({
                                      services: this.state.services.concat(
                                        item["code"]
                                      ),
                                    });
                                  } else {
                                    this.setState({
                                      services: DataStore.removeItemOfArrayByItem(
                                        this.state.services,
                                        item["code"]
                                      ),
                                    });
                                  }
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mt-1 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={() => {
                      const arr_ele = document.querySelectorAll(
                        '[name="checkbox-facilities"]'
                      );
                      for (let ele of arr_ele) {
                        ele.checked = true;
                      }
                      const facilities = [];
                      for (let item of this.state.arr_facilities) {
                        facilities.push(item["code"]);
                      }
                      this.setState({ facilities });
                    }}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    편의시설 <strong className="required"></strong>
                  </label>
                  {(this.state.arr_facilities || []).map((data, index) => (
                    <div key={index} className="row">
                      {data.map((item, index2) => (
                        <div key={index2} className="col">
                          {item["id"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"checkbox-facilities"}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  if (e.target.checked) {
                                    this.setState({
                                      facilities: this.state.facilities.concat(
                                        item["code"]
                                      ),
                                    });
                                  } else {
                                    this.setState({
                                      facilities: DataStore.removeItemOfArrayByItem(
                                        this.state.facilities,
                                        item["code"]
                                      ),
                                    });
                                  }
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <span className="float-right">
                    <a
                      className="btn btn btn-round-grey btn-xs-round"
                      onClick={() =>
                        this.setState({
                          events: this.state.events.concat({
                            eventId: null,
                            title: null,
                            contents: null,
                          }),
                        })
                      }
                    >
                      <span className="large">+</span> 추가
                    </a>
                    &nbsp;
                    <a
                      className="btn btn btn-round-grey btn-xs-round"
                      onClick={() =>
                        this.setState({
                          events: this.state.events.slice(
                            0,
                            this.state.events.length - 1
                          ),
                        })
                      }
                    >
                      ― 삭제
                    </a>
                  </span>
                  <label htmlFor="InputGPrice" className="d-block">
                    이벤트 <strong className="required"></strong>
                  </label>
                  {this.state.events.map((data, index) => (
                    <React.Fragment key={index}>
                      <input
                        type="text"
                        className="form-control mt-3"
                        placeholder="제목을 입력하세요"
                        value={data["title"]}
                        onChange={(e) => (data["title"] = e.target.value)}
                      />
                      <textarea
                        className="form-control mt-2"
                        rows="7"
                        placeholder="내용을 작성하세요"
                        value={data["contents"]}
                        onChange={(e) => (data["contents"] = e.target.value)}
                      ></textarea>
                    </React.Fragment>
                  ))}
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputGPrice" className="d-block">
                    공지사항 <strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control mt-3"
                    placeholder="제목을 입력하세요"
                    value={this.state.noticeTitle}
                    onChange={(e) =>
                      this.setState({ noticeTitle: e.target.value })
                    }
                  />
                  <textarea
                    className="form-control mt-2"
                    rows="7"
                    placeholder="내용을 작성하세요"
                    value={this.state.notice}
                    onChange={(e) => this.setState({ notice: e.target.value })}
                  ></textarea>
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputVodFile">
                    선박사진 등록 <strong className="required"></strong>
                  </label>
                  <input
                    ref={this.profileImage}
                    type="file"
                    className="form-control"
                    placeholder="선박사진을 등록하세요."
                    onChange={() => this.uploadFile("profileImage")}
                  />
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputVodFile">
                    녹화영상 파일등록 <strong className="required"></strong>
                  </label>
                  <input
                    ref={this.videoId}
                    type="file"
                    className="form-control"
                    placeholder="녹화영상을 등록하세요."
                    onChange={() => this.uploadFile("videoId")}
                  />
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label>
                    승선위치 <strong className="required"></strong>
                  </label>
                  <div className="input-group">
                    <input
                      ref={this.zonecode}
                      type="text"
                      className="form-control col-4"
                      placeholder="우편번호"
                      readOnly
                    />
                    <div className="input-group-append">
                      <button
                        className="btn btn-third btn-sm"
                        type="button"
                        onClick={this.openFindAddress}
                      >
                        우편번호검색
                      </button>
                    </div>
                  </div>
                  <div
                    ref={this.ifrmAddress}
                    style={{
                      display: "none",
                      border: "1px solid",
                      height: "300px",
                      margin: "5px 0",
                      position: "relative",
                    }}
                  >
                    <img
                      src="//t1.daumcdn.net/postcode/resource/images/close.png"
                      id="btnFoldWrap"
                      style={{
                        cursor: "pointer",
                        position: "absolute",
                        right: "0px",
                        top: "-1px",
                        zIndex: "1",
                      }}
                      onClick={() =>
                        (this.ifrmAddress.current.style.display = "none")
                      }
                      alt="접기 버튼"
                    />
                  </div>
                  <input
                    ref={this.textAddr}
                    type="text"
                    className="form-control mt-2"
                    placeholder="기본주소"
                    readOnly
                  />
                  <input
                    type="text"
                    className="form-control mt-2"
                    placeholder="상세주소"
                  />
                </div>
                <div className="form-group">
                  <label className="d-block">
                    좌표 <strong className="required"></strong>
                  </label>
                  <div className="input-group mb-3">
                    <div className="input-group-prepend">
                      <span className="input-group-text">X</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder=""
                      readOnly
                      value={this.state.latitude}
                    />
                    <div className="input-group-append">
                      <span className="input-group-text">Y</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder=""
                      readOnly
                      value={this.state.longitude}
                    />
                  </div>
                </div>
                <div className="space mt-0 mb-4"></div>
                <h6 className="mb-3 mt-3">ADT캡스 카메라등록</h6>

                <div className="form-group">
                  <label htmlFor="InputLTE">LTE 라우터 IMEI </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="LTE 라우터 IMEI를 입력하세요."
                    value={this.state.router}
                    onChange={(e) => this.setState({ router: e.target.value })}
                  />
                </div>
              </form>
              <p className="clearfix">
                <br />
                <br />
              </p>
            </div>
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.submit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    확인
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
