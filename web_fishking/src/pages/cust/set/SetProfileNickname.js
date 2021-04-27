import React from "react";
import { inject, observer } from "mobx-react";
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
    class extends React.Component {
      constructor(props) {
        super(props);
        this.text = React.createRef(null);
        this.state = { text: "" };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const { APIStore } = this.props;
        const resolve = await APIStore._get("/v2/api/profileManage");
        this.setState({ text: resolve.nickName });
      };
      onChange = async () => {
        const { APIStore, DataStore, PageStore, ModalStore } = this.props;
        const { text } = this.state;
        if (text === "" || !DataStore.isNickName(text)) {
          this.text.current?.classList.add("is-invalid");
          return;
        } else {
          this.text.current?.classList.remove("is-invalid");
          try{
              const resolve = await APIStore._put(
                  "/v2/api/profileManage/nickName",
                  { nickName: text }
              );
              if (resolve) {
                  PageStore.goBack();
              }
          }
          catch(err){
              if(err.response.data.msg !== undefined){
                  ModalStore.openModal("Alert", { body: err.response.data.msg })
              }
              else {
                  ModalStore.openModal("Alert", {body: "닉네임 변경에 실패하였습니다."});
              }
          }

        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"닉네임 변경"} showBackIcon={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-9 pl-2">사용할 닉네임을 입력해주세요.</div>
                <div className="col-3 text-right pr-2">
                  <small className="grey">{this.state.text.length} / 7</small>
                </div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <label htmlFor="inputNickname" className="sr-only">
                      닉네임
                    </label>
                    <input
                      ref={this.text}
                      type="tex"
                      className="form-control"
                      placeholder="닉네임"
                      value={this.state.text}
                      onChange={(e) =>
                        this.setState({ text: e.target.value.substr(0, 7) })
                      }
                    />
                  </div>
                </form>
              </div>
              <a
                onClick={this.onChange}
                className="btn btn-primary btn-lg btn-block"
              >
                변경하기
              </a>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
