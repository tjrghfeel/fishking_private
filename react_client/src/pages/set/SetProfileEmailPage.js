/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject(
  "MemberStore",
  "ValidStore"
)(
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
        this.setState({ text: resolve.email || "" });
      }
      update = async () => {
        const {
          ValidStore: { isEmail },
        } = this.props;
        const { text } = this.state;
        if (text.length === 0 || !isEmail(text)) {
          this.input.current?.classList.add("is-invalid");
          return;
        } else {
          this.input.current?.classList.remove("is-invalid");
        }

        const {
          MemberStore: { REST_PUT_profileManage_email },
          history,
        } = this.props;
        const resolve = await REST_PUT_profileManage_email(text);
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
            <Navigation title={"이메일 입력"} visibleBackIcon={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-12 pl-2">
                  등록할 이메일을 입력해 주세요.
                  <br />
                  등록하신 이메일로 다양한 소식을 받아보실 수 있습니다.
                </div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <label htmlFor="input" className="sr-only">
                      이메일
                    </label>
                    <input
                      ref={this.input}
                      type="email"
                      className="form-control"
                      id="input"
                      placeholder="이메일"
                      value={this.state.text}
                      onChange={(e) => this.setState({ text: e.target.value })}
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
