import React from "react";
import { inject, observer } from "mobx-react";

export default inject(
  "PageStore",
  "DataStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file = React.createRef(null);
        this.state = {
          options: [],
          questionType: "",
          contents: "",
          returnType: "tel",
          returnAddress: "",
          fileList: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { DataStore } = this.props;
        const options = await DataStore.getEnums("questionType");
        this.setState({ options });
      }
      uploadFile = async () => {
        const { APIStore } = this.props;
        if (this.file.current?.files.length > 0) {
          const file = this.file.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "one2one");

          const { fileId } = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );
          this.setState({ fileList: [fileId] });
        }
      };
      requestSubmit = async () => {
        const { APIStore, ModalStore, DataStore, PageStore } = this.props;
        const {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
        } = this.state;

        if (questionType === "") {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "카테고리를 선택해주세요.",
          });
          return;
        }
        if (contents === "") {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "내용을 입력해주세요.",
          });
          return;
        }
        if (returnType === "tel" && !DataStore.isMobile(returnAddress)) {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "연락처를 확인해주세요.",
          });
          return;
        } else if (
          returnType === "email" &&
          !DataStore.isEmail(returnAddress)
        ) {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "이메일을 확인해주세요.",
          });
          return;
        }
        if (fileList.length === 0) {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "첨부파일을 선택해주세요.",
          });
          return;
        }
        const resolve = await APIStore._post("/v2/api/post/one2one", {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
          targetRole: "member",
        });
        if (resolve) {
          ModalStore.openModal("Alert", {
            title: "등록완료",
            body: (
              <React.Fragment>
                <p>
                  문의하신 내용이 정상적으로 등록되었습니다.
                  <br />
                  담당자가 빠른 시일내에 답변드리도록 하겠습니다.
                  <br />
                  답변내용은 문의내역에서 확인이 가능합니다.
                </p>
              </React.Fragment>
            ),
            onOk: () => {
              PageStore.reload();
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
            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수항목입니다.
              </p>

              <form>
                <div className="row">
                  <div className="col-xs-12 col-sm-12 apply ">
                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon">
                          카테고리<strong className="required"></strong>
                        </label>
                        <select
                          className="form-control"
                          id="selCategory"
                          onChange={(e) =>
                            this.setState({ questionType: e.target.value })
                          }
                        >
                          <option>카테고리를 선택하세요</option>
                          {this.state.options.map((data, index) => (
                            <option key={index} value={data.key}>
                              {data.value}
                            </option>
                          ))}
                        </select>
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-text-addon" htmlFor="inputMemo">
                          내용<strong className="required"></strong>
                        </label>
                        <textarea
                          className="form-control"
                          rows="9"
                          placeholder="내용을 작성하세요"
                          onChange={(e) =>
                            this.setState({ contents: e.target.value })
                          }
                        >
                          {this.state.contents}
                        </textarea>
                      </div>
                    </div>

                    <label className="control radio mt-3 mr-3">
                      <input
                        type="radio"
                        name="radio01"
                        className="add-contrast"
                        data-role="collar"
                        defaultChecked={true}
                        onClick={() => this.setState({ returnType: "tel" })}
                      />
                      <span className="control-indicator"></span>
                      <span className="control-text">연락처</span>
                    </label>
                    <label className="control radio mt-3">
                      <input
                        type="radio"
                        name="radio01"
                        className="add-contrast"
                        data-role="collar"
                        onClick={() => this.setState({ returnType: "email" })}
                      />
                      <span className="control-indicator"></span>
                      <span className="control-text">이메일</span>
                    </label>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputRocal"
                        >
                          {this.state.returnType === "tel"
                            ? "연락처"
                            : "이메일"}
                          <strong className="required"></strong>
                        </label>
                        <input
                          type={
                            this.state.returnType === "tel" ? "number" : "email"
                          }
                          className="form-control"
                          placeholder={
                            "답변 받을 " +
                            (this.state.returnType === "tel"
                              ? "연락처를"
                              : "이메일을") +
                            " 입력해 주세요."
                          }
                          onChange={(e) =>
                            this.setState({ returnAddress: e.target.value })
                          }
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputBizName"
                        >
                          첨부파일<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.file}
                          type="file"
                          className="form-control"
                          placeholder="파일을 첨부하세요."
                          onChange={this.uploadFile}
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
                    data-toggle="modal"
                    data-target="#inputModal"
                  >
                    문의하기
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
