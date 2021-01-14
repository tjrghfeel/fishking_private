import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
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
        this.setState({ text: resolve.email });
      };
      onChange = async () => {
        const { APIStore, PageStore, DataStore } = this.props;
        const { text } = this.state;
        if (text !== "" && !DataStore.isEmail(text)) {
          this.text.current?.classList.add("is-invalid");
          return;
        } else {
          this.text.current?.classList.remove("is-invalid");
        }
        const resolve = await APIStore._put("/v2/api/profileManage/email", {
          email: text,
        });
        if (resolve) {
          PageStore.goBack();
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"이메일 입력"} showBackIcon={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-12 pl-2">
                  등록할 이메일을 입력해 주세요. <br />
                  등록하신 이메일로 다양한 소식을 받아보실 수 있습니다.
                </div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <label htmlFor="inputNickname" className="sr-only">
                      이메일
                    </label>
                    <input
                      ref={this.text}
                      type="email"
                      className="form-control"
                      placeholder="이메일"
                      value={this.state.text}
                      onChange={(e) => this.setState({ text: e.target.value })}
                    />
                  </div>
                </form>
              </div>
              <a
                onClick={this.onChange}
                className="btn btn-primary btn-lg btn-block"
              >
                등록하기
              </a>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
