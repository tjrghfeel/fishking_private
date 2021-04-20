import React from "react";
import { inject, observer } from "mobx-react";
import APIStore from "../../stores/APIStore";
import ModalStore from "../../stores/ModalStore";

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.companyName = React.createRef(null);
        this.phoneNumber = React.createRef(null);
        this.companyAddress = React.createRef(null);
        this.harbor = React.createRef(null);
        this.memberName = React.createRef(null);
        this.tel = React.createRef(null);
        this.bizNoFile = React.createRef(null);
        this.representFile = React.createRef(null);
        this.accountFile = React.createRef(null);
        this.adtId = React.createRef(null);
        this.adtPw = React.createRef(null);
        this.nhnId = React.createRef(null);
        this.nhnPw = React.createRef(null);
        this.state = {
          companyName: "",
          phoneNumber: "", // 휴대폰번호
          companyAddress: "", // 지역명
          harbor: "",
          memberName: "",
          tel: "",
          bizNoFile: -1,
          representFile: -1,
          accountFile: -1,
          adtId: "",
          adtPw: "",
          nhnId: "",
          nhnPw: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore, pass = false } = this.props;
        if (!pass) this.checkCanApply();
        // console.log(canApply)
        // if (canApply == null) {
        //   ModalStore.openModal("Alert", {
        //     body: (
        //         <React.Fragment>
        //           <p>
        //             로그인 후 신청가능합니다.
        //           </p>
        //         </React.Fragment>
        //     ),
        //     onOk: () => {
        //       PageStore.goBack();
        //     },
        //   });
        // } else if (canApply) {
        //   ModalStore.openModal("Alert", {
        //     body: (
        //         <React.Fragment>
        //           <p>
        //             기신청 상태입니다.
        //             <br />
        //             처리 후 연락드리겠습니다.
        //           </p>
        //         </React.Fragment>
        //     ),
        //     onOk: () => {
        //       PageStore.goBack();
        //     },
        //   });
        // }
      }

      openAlert = () => {
        ModalStore.openModal("Alert", {
          body: (
            <React.Fragment>
              <p>필수 입력값을 확인해주세요.</p>
            </React.Fragment>
          ),
        });
      };

      checkCanApply = async () => {
        const { APIStore, PageStore } = this.props;
        await APIStore._get("/v2/api/company/checkRequestExist").then(
          (result) => {
            console.log(result);
            if (result == "") {
              ModalStore.openModal("Alert", {
                body: (
                  <React.Fragment>
                    <p>로그인 후 신청가능합니다.</p>
                  </React.Fragment>
                ),
                onOk: () => {
                  PageStore.goBack();
                },
              });
            } else if (result) {
              ModalStore.openModal("Alert", {
                body: (
                  <React.Fragment>
                    <p>
                      기신청 상태입니다.
                      <br />
                      처리 후 연락드리겠습니다.
                    </p>
                  </React.Fragment>
                ),
                onOk: () => {
                  PageStore.goBack();
                },
              });
            }
          }
        );
      };

      requestSubmit = async () => {
        const { PageStore, APIStore, DataStore, successPathname } = this.props;
        const {
          companyName,
          phoneNumber,
          companyAddress,
          bizNoFile,
          representFile,
          accountFile,
        } = this.state;
        let needInput = false;

        if (companyName === "") {
          this.companyName.current?.classList.add("is-invalid");
          needInput = true;
        } else {
          this.companyName.current?.classList.remove("is-invalid");
          needInput = false;
        }
        if (phoneNumber === "" || !DataStore.isMobile(phoneNumber)) {
          this.phoneNumber.current?.classList.add("is-invalid");
          needInput = true;
        } else {
          this.phoneNumber.current?.classList.remove("is-invalid");
          needInput = false;
        }
        if (companyAddress === "") {
          this.companyAddress.current?.classList.add("is-invalid");
          needInput = true;
        } else {
          this.companyAddress.current?.classList.remove("is-invalid");
          needInput = false;
        }
        if (bizNoFile === -1) {
          this.bizNoFile.current?.classList.add("is-invalid");
          needInput = true;
        } else {
          this.bizNoFile.current?.classList.remove("is-invalid");
          needInput = false;
        }
        if (representFile === -1) {
          this.representFile.current?.classList.add("is-invalid");
          needInput = true;
        } else {
          this.representFile.current?.classList.remove("is-invalid");
          needInput = false;
        }
        if (accountFile === -1) {
          this.accountFile.current?.classList.add("is-invalid");
          needInput = true;
        } else {
          this.accountFile.current?.classList.remove("is-invalid");
          needInput = false;
        }

        if (needInput) {
          this.openAlert();
          return;
        } else {
          const resolve = await APIStore._post("/v2/api/company", this.state);
          if ((resolve || 0) !== 0) {
            PageStore.push(successPathname);
          }
        }
      };

      uploadFile = async (type) => {
        let file = null;
        if (type === "bizNo" && this.bizNoFile.current?.files.length > 0) {
          file = this.bizNoFile.current?.files[0];
        } else if (
          type === "represent" &&
          this.representFile.current?.files.length > 0
        ) {
          file = this.representFile.current?.files[0];
        } else if (
          type === "account" &&
          this.accountFile.current?.files.length > 0
        ) {
          file = this.accountFile.current?.files[0];
        }

        if (file === null) return;

        const form = new FormData();
        form.append("file", file);
        form.append("filePublish", "companyRequest");

        const { APIStore } = this.props;
        const upload = await APIStore._post_upload(
          "/v2/api/filePreUpload",
          form
        );
        if (upload && type === "bizNo") {
          this.setState({ bizNoFile: upload.fileId });
        } else if (upload && type === "represent") {
          this.setState({ representFile: upload.fileId });
        } else if (upload && type === "account") {
          this.setState({ accountFile: upload.fileId });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수입력
              </p>

              <form>
                <div className="row">
                  <div className="col-xs-12 col-sm-12 apply ">
                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputCompany"
                        >
                          업체명<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.companyName}
                          type="text"
                          className="form-control"
                          placeholder="업체명을 입력하세요"
                          value={this.state.companyName}
                          onChange={(e) =>
                            this.setState({ companyName: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputPhone"
                        >
                          휴대폰번호<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.phoneNumber}
                          type="number"
                          className="form-control"
                          placeholder="휴대폰번호를 입력하세요"
                          value={this.state.phoneNumber}
                          onChange={(e) =>
                            this.setState({ phoneNumber: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputRocal"
                        >
                          지역명<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.companyAddress}
                          type="text"
                          className="form-control"
                          placeholder="지역명을 입력하세요"
                          value={this.state.companyAddress}
                          onChange={(e) =>
                            this.setState({ companyAddress: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputPort"
                        >
                          항구명
                        </label>
                        <input
                          ref={this.harbor}
                          type="text"
                          className="form-control"
                          placeholder="항구명을 입력하세요"
                          value={this.state.harbor}
                          onChange={(e) =>
                            this.setState({ harbor: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputCname"
                        >
                          대표자명
                        </label>
                        <input
                          ref={this.memberName}
                          type="text"
                          className="form-control"
                          placeholder="대표자명을 입력하세요"
                          value={this.state.memberName}
                          onChange={(e) =>
                            this.setState({ memberName: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon" htmlFor="inputTel">
                          전화번호
                        </label>
                        <input
                          ref={this.tel}
                          type="number"
                          className="form-control"
                          placeholder="전화번호를 입력하세요"
                          value={this.state.tel}
                          onChange={(e) =>
                            this.setState({ tel: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputBiznum"
                        >
                          사업자등록증 <strong className="required"></strong>
                        </label>
                        <input
                          ref={this.bizNoFile}
                          type="file"
                          className="form-control"
                          placeholder="사업자등록증을 첨부하세요."
                          onChange={() => this.uploadFile("bizNo")}
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputBizName"
                        >
                          대표자 신분증<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.representFile}
                          type="file"
                          className="form-control"
                          placeholder="대표자 신분증을 첨부하세요."
                          onChange={() => this.uploadFile("represent")}
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputPhone"
                        >
                          정산 통장사본<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.accountFile}
                          type="file"
                          className="form-control"
                          placeholder="정산 통장사본을 첨부하세요 "
                          onChange={() => this.uploadFile("account")}
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon">
                          SKB 캡스 아이디
                        </label>
                        <input
                          ref={this.adtId}
                          type="text"
                          className="form-control"
                          placeholder="SKB 캡스 아이디를 입력하세요"
                          value={this.state.adtId}
                          onChange={(e) =>
                            this.setState({ adtId: e.target.value })
                          }
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon">
                          SKB 캡스 비밀번호
                        </label>
                        <input
                          ref={this.adtPw}
                          type="password"
                          className="form-control"
                          placeholder="SKB 캡스 비밀번호를 입력하세요"
                          value={this.state.adtPw}
                          onChange={(e) =>
                            this.setState({ adtPw: e.target.value })
                          }
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon">
                          NHN 토스트캠 아이디
                        </label>
                        <input
                          ref={this.nhnId}
                          type="text"
                          className="form-control"
                          placeholder="NHN 토스트캠 아이디를 입력하세요"
                          value={this.state.nhnId}
                          onChange={(e) =>
                            this.setState({ nhnId: e.target.value })
                          }
                        />
                      </div>
                    </div>
                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon">
                          NHN 토스트캠 비밀번호
                        </label>
                        <input
                          ref={this.nhnPw}
                          type="password"
                          className="form-control"
                          placeholder="NHN 토스트캠 비밀번호를 입력하세요"
                          value={this.state.nhnPw}
                          onChange={(e) =>
                            this.setState({ nhnPw: e.target.value })
                          }
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </form>
            </div>

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.requestSubmit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    요청하기
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
