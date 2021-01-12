import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import PageStore from "../../../stores/PageStore";
const {
  Common: {
    Layout: { Navigation },
  },
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
        this.currentPw = React.createRef(null);
        this.newPw = React.createRef(null);
        this.newPwRe = React.createRef(null);
        this.state = { currentPw: "", newPw: "", newPwRe: "" };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onChange = async () => {
        const { APIStore, PageStore, DataStore } = this.props;
        const { currentPw, newPw, newPwRe } = this.state;

        if (!DataStore.isPassword(currentPw)) {
          this.currentPw.current?.classList.add("is-invalid");
          return;
        } else {
          this.currentPw.current?.classList.remove("is-invalid");
        }
        if (!DataStore.isPassword(newPw)) {
          this.newPw.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPw.current?.classList.remove("is-invalid");
        }
        if (newPw !== newPwRe) {
          this.newPwRe.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPwRe.current?.classList.remove("is-invalid");
        }

        const resolve = await APIStore._put("/v2/api/profileManage/password", {
          currentPw,
          newPw,
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
            <Navigation title={"비밀번호 재설정"} showBackIcon={true} />

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
                      ref={this.currentPw}
                      type="password"
                      className="form-control"
                      placeholder="현재 비밀번호"
                      value={this.state.currentPw}
                      onChange={(e) =>
                        this.setState({
                          currentPw: e.target.value.substr(0, 15),
                        })
                      }
                    />
                  </div>
                  <div className="form-group">
                    <input
                      ref={this.newPw}
                      type="password"
                      className="form-control"
                      placeholder="새 비밀번호 (영문/숫자/특수문자 중 2가지 이상, 8~15자 이내)"
                      value={this.state.newPw}
                      onChange={(e) =>
                        this.setState({ newPw: e.target.value.substr(0, 15) })
                      }
                    />
                  </div>
                  <div className="form-group">
                    <input
                      ref={this.newPwRe}
                      type="password"
                      className="form-control"
                      placeholder="새 비밀번호 확인"
                      value={this.state.newPwRe}
                      onChange={(e) =>
                        this.setState({ newPwRe: e.target.value.substr(0, 15) })
                      }
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
