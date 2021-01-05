import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import CsTabs from "../../components/layouts/CsTabs";
import CsQnaTabs from "../../components/layouts/CsQnaTabs";
import Http from "../../Http";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.questionType = React.createRef(null);
        this.contents = React.createRef(null);
        this.returnAddress = React.createRef(null);
        this.uploads = React.createRef(null);
        this.state = {
          questionTypeOptions: [], // 카테고리 옵션
          contents: "",
          returnType: 0,
          returnAddress: "",
          fileList: [],
        };
      }

      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          DataStore: { getEnums },
        } = this.props;
        const questionTypeOptions = await getEnums("questionType");
        this.setState({ questionTypeOptions });
      }

      onChangeFile = async () => {
        if (this.uploads.current?.files.length > 0) {
          const file = this.uploads.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "one2one");

          const resolve = await Http._post_upload(
            "/v2/api/filePreUpload",
            form
          );
          this.setState({ fileList: [resolve.fileId] });
        }
      };

      onPost = async () => {
        if (this.uploads.current?.files.length === 0) {
          this.uploads.current?.classList.add("is-invalid");
          return;
        } else {
          const file = this.uploads.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "one2one");

          const resolve = await Http._post_upload(
            "/v2/api/filePreUpload",
            form
          );
          await this.setState({ fileList: [resolve.fileId] });

          this.uploads.current?.classList.remove("is-invalid");
        }

        const {
          DataStore: { isValidMobile },
        } = this.props;

        const questionType = this.questionType.current?.selectedOptions[0]
          .value;
        const {
          contents,
          returnType,
          returnAddress = null,
          fileList,
        } = this.state;

        if (questionType === "") {
          this.questionType.current.classList.add("is-invalid");
          return;
        } else {
          this.questionType.current.classList.remove("is-invalid");
        }
        if (contents === "") {
          this.contents.current.classList.add("is-invalid");
          return;
        } else {
          this.contents.current.classList.remove("is-invalid");
        }
        if (returnType === 1 && !isValidMobile(returnAddress)) {
          this.returnAddress.current.classList.add("is-invalid");
          return;
        } else {
          this.returnAddress.current.classList.remove("is-invalid");
        }

        const resolve = await Http._post("/v2/api/post/one2one", {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
        });

        if (resolve) {
          const { history } = this.props;
          history.push(`/common/cs/qna/list`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"고객센터"} showBack={true} />

            {/** 탭메뉴 */}
            <CsTabs />

            {/** 탭메뉴 */}
            <CsQnaTabs />

            {/** 데이터 */}
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
                          ref={this.questionType}
                          className="form-control"
                        >
                          <option value="">카테고리를 선택하세요</option>
                          {this.state.questionTypeOptions.map((data, index) => (
                            <option key={index} value={index}>
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
                          ref={this.contents}
                          className="form-control"
                          rows="9"
                          placeholder="내용을 작성하세요"
                          value={this.state.contents}
                          onChange={(e) =>
                            this.setState({ contents: e.target.value })
                          }
                        ></textarea>
                      </div>
                    </div>

                    <label className="control radio mt-3 mr-3">
                      <input
                        name="returnType"
                        type="radio"
                        className="add-contrast"
                        data-role="collar"
                        defaultChecked={this.state.returnType === 1}
                        onChange={(e) => {
                          if (e.target.checked)
                            this.setState({ returnType: 1 });
                        }}
                      />
                      <span className="control-indicator"></span>
                      <span className="control-text">연락처</span>
                    </label>
                    <label className="control radio mt-3">
                      <input
                        name="returnType"
                        type="radio"
                        className="add-contrast"
                        data-role="collar"
                        defaultChecked={this.state.returnType === 0}
                        onChange={(e) => {
                          if (e.target.checked)
                            this.setState({ returnType: 0 });
                        }}
                      />
                      <span className="control-indicator"></span>
                      <span className="control-text">이메일</span>
                    </label>

                    <div
                      className="form-group"
                      style={{
                        display: this.state.returnType === 1 ? "block" : "none",
                      }}
                    >
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputRocal"
                        >
                          연락처<strong className="required"></strong>
                        </label>
                        <input
                          ref={this.returnAddress}
                          type="number"
                          className="form-control"
                          placeholder="답변 받을 연락처를 입력해 주세요. "
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
                          ref={this.uploads}
                          type="file"
                          className="form-control"
                          placeholder="파일을 첨부하세요."
                          onChange={this.onChangeFile}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </form>
            </div>

            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.onPost}
                    className="btn btn-primary btn-lg btn-block"
                    data-toggle="modal"
                    data-target="#inputModal"
                  >
                    문의하기
                  </a>
                </div>
              </div>
            </div>
          </>
        );
      }
    }
  )
);
