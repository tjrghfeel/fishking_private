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
      /** functions **/
      /********** ********** ********** ********** **********/
      update = async () => {
        const { password, newPassword1, newPassword2 } = this.state;
        const {
          history,
          ValidStore: { isMultiCheck1, isMultiCheck2, isMultiCheck3 },
          MemberStore: { REST_PUT_profileManage_password },
        } = this.props;

        if (password === "" || password.length < 8) {
          this.password.current?.classList.add("is-invalid");
          return;
        } else {
          this.password.current?.classList.remove("is-invalid");
        }
        if (
          newPassword1 === "" ||
          newPassword1.length < 8 ||
          (!isMultiCheck1(newPassword1) &&
            !isMultiCheck2(newPassword1) &&
            !isMultiCheck3(newPassword1))
        ) {
          this.newPassword1.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword1.current?.classList.remove("is-invalid");
        }
        if (
          newPassword2 === "" ||
          newPassword2.length < 8 ||
          (!isMultiCheck1(newPassword2) &&
            !isMultiCheck2(newPassword2) &&
            !isMultiCheck3(newPassword2))
        ) {
          this.newPassword2.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword2.current?.classList.remove("is-invalid");
        }
        if (newPassword1 !== newPassword2) {
          this.newPassword1.current?.classList.add("is-invalid");
          this.newPassword2.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword1.current?.classList.remove("is-invalid");
          this.newPassword2.current?.classList.remove("is-invalid");
        }

        const resolve = await REST_PUT_profileManage_password(
          password,
          newPassword1
        );

        if (resolve) {
          history.push(`/set/profile`);
        } else {
          this.password.current?.classList.remove("is-invalid");
          this.newPassword1.current?.classList.remove("is-invalid");
          this.newPassword2.current?.classList.remove("is-invalid");
        }
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"비밀번호 재설정"} visibleBackIcon={true} />

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
                onClick={this.update}
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
