/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";

export default inject(
  "PageStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          location: false,
          alarm: false,
          storage: false,
          contact: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        document.querySelector("body").classList.add("full");
        $("#accessModal").modal("show");
      }
      requestPermissions = async () => {
        const { NativeStore } = this.props;
        NativeStore.postMessage("Permissions", this.state);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <div className="fullwrap">
              <img
                src="/assets/cust/img/bg-fish1.jpg"
                alt=""
                className="bgfull"
              />
              <div className="full-content">
                <div className="logo">
                  <span>똑똑한 바다낚시</span>
                  <img src="/assets/cust/img/svg/logo-w.svg" alt="어복황제" />
                </div>
              </div>
            </div>

            {/** Modal */}
            <div
              className="modal fade"
              id="accessModal"
              tabIndex="-1"
              aria-labelledby="accessModalLabel"
              aria-hidden="true"
            >
              <div className="modal-dialog modal-md modal-dialog-centered">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5
                      className="modal-title text-center"
                      id="accessModalLabel"
                    >
                      선택적 앱 접근 권한 안내
                    </h5>
                  </div>
                  <div className="modal-body text-left">
                    <div className="row no-gutters align-items-center">
                      <div className="col-12 pl-2 text-left">
                        어복황제에서 요청하는 모든 권한은 선택적 접근권한으로,
                        동의하지 않아도 서비스 이용이 가능합니다.
                      </div>
                    </div>
                    <p className="mt-3 mb-1"></p>
                    <div className="row no-gutters">
                      <div className="col-2 text-left">
                        <img
                          src="/assets/cust/img/svg/alarm-map.svg"
                          alt=""
                          className="icon-md"
                        />
                      </div>
                      <div className="col-8 text-left">
                        <h6>
                          위치 <small>(선택)</small>
                        </h6>
                        <small className="grey">
                          내 주변 낚시점 및 포인트 정보 제공을 위해 필요합니다.
                        </small>
                      </div>
                      <div className="col-2 text-right">
                        <label className="control radio mt-1">
                          <input
                            type="checkbox"
                            className="add-contrast"
                            data-role="collar"
                            onChange={(e) =>
                              this.setState({ location: e.target.checked })
                            }
                          />
                          <span className="control-indicator"></span>{" "}
                          <small></small>
                        </label>
                      </div>
                    </div>
                    <hr className="mt-2 mb-2" />

                    <div className="row no-gutters">
                      <div className="col-2 text-left">
                        <img
                          src="/assets/cust/img/svg/alarm-notice.svg"
                          alt=""
                          className="icon-md"
                        />
                      </div>
                      <div className="col-8 text-left">
                        <h6>
                          알림 <small>(선택)</small>
                        </h6>
                        <small className="grey">
                          어복황제에서 제공하는 할인, 이벤트 정보와 예약 알람을
                          받게 됩니다.
                        </small>
                      </div>
                      <div className="col-2 text-right">
                        <label className="control radio mt-1">
                          <input
                            type="checkbox"
                            className="add-contrast"
                            data-role="collar"
                            onChange={(e) =>
                              this.setState({ alarm: e.target.checked })
                            }
                          />
                          <span className="control-indicator"></span>{" "}
                          <small></small>
                        </label>
                      </div>
                    </div>
                    <hr className="mt-2 mb-2" />

                    <div className="row no-gutters">
                      <div className="col-2 text-left">
                        <img
                          src="/assets/cust/img/svg/alarm-camera.svg"
                          alt=""
                          className="icon-md"
                        />
                      </div>
                      <div className="col-8 text-left">
                        <h6>
                          카메라/앨범 <small>(선택)</small>
                        </h6>
                        <small className="grey">
                          저장공간, 사용자의 이미지나 영상을 첨부하기 위한
                          접근을 허용합니다.{" "}
                        </small>
                      </div>
                      <div className="col-2 text-right">
                        <label className="control radio mt-1">
                          <input
                            type="checkbox"
                            className="add-contrast"
                            data-role="collar"
                            onChange={(e) =>
                              this.setState({ storage: e.target.checked })
                            }
                          />
                          <span className="control-indicator"></span>{" "}
                          <small></small>
                        </label>
                      </div>
                    </div>
                    <hr className="mt-2 mb-2" />

                    <div className="row no-gutters">
                      <div className="col-2 text-left">
                        <img
                          src="/assets/cust/img/svg/alarm-call.svg"
                          alt=""
                          className="icon-md"
                        />
                      </div>
                      <div className="col-8 text-left">
                        <h6>
                          전화/주소록 <small>(선택)</small>
                        </h6>
                        <small className="grey">
                          낚시 예약 및 문의, 예약 완료 후 문자 메시지 보내기에
                          이용합니다.
                        </small>
                      </div>
                      <div className="col-2 text-right">
                        <label className="control radio mt-1">
                          <input
                            type="checkbox"
                            className="add-contrast"
                            data-role="collar"
                            onChange={(e) =>
                              this.setState({ contact: e.target.checked })
                            }
                          />
                          <span className="control-indicator"></span>{" "}
                          <small></small>
                        </label>
                      </div>
                    </div>
                    <hr className="mt-2 mb-2" />
                    <small className="grey">
                      설정 > 어플리케이션관리 > 어복황제앱 > 앱 권한에서 설정을
                      변경하실 수 있습니다.{" "}
                    </small>
                  </div>
                  <div className="modal-footer-btm">
                    <div className="row no-gutters">
                      <div className="col-12">
                        <a
                          onClick={this.requestPermissions}
                          className="btn btn-primary btn-lg btn-block"
                        >
                          확인
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
