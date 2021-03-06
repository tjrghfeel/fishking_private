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
        const { DataStore, PageStore } = this.props;
        const options = await DataStore.getEnums("questionType");
        this.setState({ options });

        // # Parameter Setting
        const { q } = PageStore.getQueryParams();
        if (q) {
          document.querySelector(
            '#selCategory option[value="accuse"]'
          ).selected = true;
          const { contents } = JSON.parse(q);
          if (contents) this.setState({ contents });
        }
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
        const {
          APIStore,
          ModalStore,
          DataStore,
          PageStore,
          targetRole = "member",
        } = this.props;
        const {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
        } = this.state;

        if (questionType === "") {
          ModalStore.openModal("Alert", {
            title: "??????",
            body: "??????????????? ??????????????????.",
          });
          return;
        }
        if (contents === "") {
          ModalStore.openModal("Alert", {
            title: "??????",
            body: "????????? ??????????????????.",
          });
          return;
        }
        if (returnType === "tel" && !DataStore.isMobile(returnAddress)) {
          ModalStore.openModal("Alert", {
            title: "??????",
            body: "???????????? ??????????????????.",
          });
          return;
        } else if (
          returnType === "email" &&
          !DataStore.isEmail(returnAddress)
        ) {
          ModalStore.openModal("Alert", {
            title: "??????",
            body: "???????????? ??????????????????.",
          });
          return;
        }
        // if (fileList.length === 0) {
        //   ModalStore.openModal("Alert", {
        //     title: "??????",
        //     body: "??????????????? ??????????????????.",
        //   });
        //   return;
        // }
        const resolve = await APIStore._post("/v2/api/post/one2one", {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
          targetRole,
        });
        if (resolve) {
          ModalStore.openModal("Alert", {
            title: "????????????",
            body: (
              <React.Fragment>
                <p>
                  ???????????? ????????? ??????????????? ?????????????????????.
                  <br />
                  ???????????? ?????? ???????????? ?????????????????? ???????????????.
                  <br />
                  ??????????????? ?????????????????? ????????? ???????????????.
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
                <strong className="required"></strong> ?????????????????????.
              </p>

              <form>
                <div className="row">
                  <div className="col-xs-12 col-sm-12 apply ">
                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon">
                          ????????????<strong className="required"></strong>
                        </label>
                        <select
                          className="form-control"
                          id="selCategory"
                          onChange={(e) =>
                            this.setState({ questionType: e.target.value })
                          }
                        >
                          <option>??????????????? ???????????????</option>
                          {this.state.options &&
                            this.state.options.map((data, index) => (
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
                          ??????<strong className="required"></strong>
                        </label>
                        <textarea
                          className="form-control"
                          rows="9"
                          placeholder="????????? ???????????????"
                          onChange={(e) =>
                            this.setState({ contents: e.target.value })
                          }
                          value={this.state.contents}
                        ></textarea>
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
                      <span className="control-text">?????????</span>
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
                      <span className="control-text">?????????</span>
                    </label>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputRocal"
                        >
                          {this.state.returnType === "tel"
                            ? "?????????"
                            : "?????????"}
                          <strong className="required"></strong>
                        </label>
                        <input
                          type={
                            this.state.returnType === "tel" ? "number" : "email"
                          }
                          className="form-control"
                          placeholder={
                            "?????? ?????? " +
                            (this.state.returnType === "tel"
                              ? "????????????"
                              : "????????????") +
                            " ????????? ?????????."
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
                          ????????????
                        </label>
                        <input
                          ref={this.file}
                          type="file"
                          className="form-control"
                          placeholder="????????? ???????????????."
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
                  >
                    ????????????
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
