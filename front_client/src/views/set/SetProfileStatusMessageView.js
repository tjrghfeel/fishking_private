import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import Http from "../../Http";

export default inject(
  "DataStore",
  "AlertStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.text = React.createRef(null);
        this.state = {
          text: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const resolve = await Http._get("/v2/api/profileManage");
        this.setState({ text: resolve.statusMessage });
      }
      requestUpdate = async () => {
        const { text: statusMessage } = this.state;
        const { history } = this.props;

        const resolve = await Http._put("/v2/api/profileManage/statusMessage", {
          statusMessage,
        });
        if (resolve) {
          this.props.AlertStore.openAlert("알림", "변경되었습니다.", () => {
            history.goBack();
          });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"상태메시지"} showBack={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-9 pl-2">상태메시지를 입력해주세요.</div>
                <div className="col-3 text-right pr-2">
                  <small className="grey">
                    {Intl.NumberFormat().format(this.state.text.length)} / 60
                  </small>
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
                      type="text"
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
                onClick={this.requestUpdate}
                className="btn btn-primary btn-lg btn-block"
              >
                변경하기
              </a>
            </div>
          </>
        );
      }
    }
  )
);
