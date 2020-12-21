import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import Http from "../../Http";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.password = React.createRef(null);
        this.newPassword1 = React.createRef(null);
        this.newPassword2 = React.createRef(null);
        this.state = {
          password: "",
          newPassword1: "",
          newPassword2: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      requestUpdate = async () => {
        const { password, newPassword1, newPassword2 } = this.state;
        const {
          DataStore: { isValidPassword },
          history,
        } = this.props;

        if (password === "" || !isValidPassword(password)) {
          this.password.current?.classList.add("is-invalid");
          return;
        } else {
          this.password.current?.classList.remove("is-invalid");
        }
        if (newPassword1 === "" || !isValidPassword(newPassword1)) {
          this.newPassword1.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword1.current?.classList.remove("is-invalid");
        }
        if (newPassword1 !== newPassword2) {
          this.newPassword2.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword2.current?.classList.remove("is-invalid");
        }

        const resolve = await Http._put("/v2/api/profileManage/password", {
          currentPw: password,
          newPw: newPassword1,
        });

        if (resolve) {
          history.goBack();
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"비밀번호 재설정"} showBack={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-12 pl-2">
                  새로운 비밀번호를 입력해 주세요.
                </div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <input
                      ref={this.password}
                      type="password"
                      className="form-control"
                      placeholder="현재 비밀번호"
                      value={this.state.password}
                      onChange={(e) =>
                        this.setState({
                          password: e.target.value.substr(0, 15),
                        })
                      }
                    />
                  </div>
                  <div className="form-group">
                    <input
                      ref={this.newPassword1}
                      type="password"
                      className="form-control"
                      placeholder="새 비밀번호 (영문/숫자/특수문자 중 2가지 이상, 8~15자 이내)"
                      value={this.state.newPassword1}
                      onChange={(e) =>
                        this.setState({
                          newPassword1: e.target.value.substr(0, 15),
                        })
                      }
                    />
                  </div>
                  <div className="form-group">
                    <input
                      ref={this.newPassword2}
                      type="password"
                      className="form-control"
                      placeholder="새 비밀번호 확인"
                      value={this.state.newPassword2}
                      onChange={(e) =>
                        this.setState({
                          newPassword2: e.target.value.substr(0, 15),
                        })
                      }
                    />
                  </div>
                </form>
              </div>
              <a
                onClick={this.requestUpdate}
                className="btn btn-primary btn-lg btn-block"
              >
                등록하기
              </a>
            </div>
          </>
        );
      }
    }
  )
);
