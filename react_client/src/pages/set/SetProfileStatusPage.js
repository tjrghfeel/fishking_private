/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject("MemberStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.input = React.createRef(null);
        this.state = {
          text: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          MemberStore: { REST_GET_profileManage },
        } = this.props;
        const resolve = await REST_GET_profileManage();
        this.setState({ text: resolve.statusMessage || "" });
      }
      update = async () => {
        const { text } = this.state;
        if (text.length === 0) {
          this.input.current?.classList.add("is-invalid");
          return;
        } else {
          this.input.current?.classList.remove("is-invalid");
        }

        const {
          MemberStore: { REST_PUT_profileManage_statusMessage },
          history,
        } = this.props;
        const resolve = await REST_PUT_profileManage_statusMessage(text);
        if (resolve) {
          history.push(`/set/profile`);
        } else {
          this.input.current?.classList.add("is-invalid");
        }
      };

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"상태메시지"} visibleBackIcon={true} />

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
                      ref={this.input}
                      type="text"
                      className="form-control"
                      id="inputNickname"
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
                onClick={this.update}
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
