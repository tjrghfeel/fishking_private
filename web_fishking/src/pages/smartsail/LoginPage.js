import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      login = async () => {
        const { PageStore } = this.props;
        PageStore.push(`/dashboard`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <nav className="navbar fixed-top navbar-dark">
              <a onClick={() => PageStore.goBack()} className="nav-left">
                <img
                  src="/assets/smartsail/img/svg/navbar-back-black.svg"
                  alt="뒤로가기"
                />
              </a>
              <span className="navbar-title"></span>
            </nav>

            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/smartsail/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            <div class="container nopadding">
              <div class="mt-4">
                <form class="form-line mt-	1">
                  <div class="form-group">
                    <label for="inputName" class="sr-only">
                      이메일
                    </label>
                    <input
                      type="email"
                      class="form-control"
                      id="inputName"
                      placeholder="이메일"
                      value=""
                    />
                  </div>
                  <div class="form-group">
                    <label for="inputPhone" class="sr-only">
                      비밀번호
                    </label>
                    <input
                      type="email"
                      class="form-control"
                      id="inputPhone"
                      placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                      value=""
                    />
                  </div>
                </form>
                <a
                  onClick={this.login}
                  class="btn btn-primary btn-lg btn-block"
                >
                  로그인
                </a>
                <p class="text-center mt-3">
                  <a>
                    <small class="grey">비밀번호를 잊으셨나요?</small>
                  </a>
                </p>

                <p class="text-center mt-4">
                  스마트승선 로그인/업체등록시 <br />
                  <a class="text-primary">이용약관</a> 및{" "}
                  <a class="text-primary">개인정보취급방침</a>에 동의하게
                  됩니다.
                </p>
              </div>
              <p class="clearfix">
                <br />
                <br />
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
