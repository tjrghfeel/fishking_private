import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject()(
  observer(() => {
    return (
      <>
        {/** Navigation */}
        <Navigation title={"비밀번호 재설정"} visibleBackIcon={true} />

        {/** 입력 */}
        <div className="container nopadding mt-4">
          <div className="row no-gutters align-items-center">
            <div className="col-12 pl-2">새로운 비밀번호를 입력해 주세요.</div>
          </div>
          <div className="card-round-box-grey mt-3">
            <form className="form-line mt-3">
              <div className="form-group">
                <input
                  type="password"
                  className="form-control"
                  id="inputPassword"
                  placeholder="현재 비밀번호"
                  value=""
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  className="form-control"
                  id="inputPassword"
                  placeholder="새 비밀번호 (영문/숫자/특수문자 중 2가지 이상, 8~15자 이내)"
                  value=""
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  className="form-control"
                  id="inputPasswordC"
                  placeholder="새 비밀번호 확인"
                  value=""
                />
              </div>
            </form>
          </div>
          <a href="#none" className="btn btn-primary btn-lg btn-block">
            등록하기
          </a>
        </div>
      </>
    );
  })
);
