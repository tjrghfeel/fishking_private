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
        this.setState({ text: resolve.nickName || "" });
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
          MemberStore: { REST_PUT_profileManage_nickName },
          history,
        } = this.props;
        const resolve = await REST_PUT_profileManage_nickName(text);
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
            <Navigation title={"닉네임 변경"} visibleBackIcon={true} />

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
                      ref={this.input}
                      type="text"
                      className="form-control"
                      id="inputNickname"
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
