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
        this.setState({ text: resolve.statusMessage });
      };
      onChange = async () => {
        const { APIStore, PageStore } = this.props;
        const { text } = this.state;
        const resolve = await APIStore._put(
          "/v2/api/profileManage/statusMessage",
          {
            statusMessage: text,
          }
        );
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
            <NavigationLayout title={"상태메시지"} showBackIcon={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-9 pl-2">상태메시지를 입력해주세요.</div>
                <div className="col-3 text-right pr-2">
                  <small className="grey">{this.state.text.length} / 60</small>
                </div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <label htmlFor="inputNickname" className="sr-only">
                      상태메시지
                    </label>
                    <input
                      ref={this.text}
                      type="tex"
                      className="form-control"
                      placeholder="상태메시지를 입력해주세요."
                      value={this.state.text}
                      onChange={(e) =>
                        this.setState({ text: e.target.value.substr(0, 60) })
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
