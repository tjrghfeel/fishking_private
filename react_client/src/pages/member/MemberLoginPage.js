/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback, useRef, useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject(
  "SNSStore",
  "ValidStore"
)(
  observer(({ SNSStore, ValidStore, history }) => {
    const emailRef = useRef(null);
    const passwordRef = useRef(null);
    const [valid, setValid] = useState(true);

    /** 로그인 클릭 */
    const onClickLogin = useCallback(() => {
      const email = emailRef.current?.value;
      const password = passwordRef.current?.value;

      if (!ValidStore.isEmail(email)) {
        console.log("이메일을 확인해주세요.");
        return;
      } else if (
        !(
          ValidStore.isMultiCheck1(password) ||
          ValidStore.isMultiCheck2(password) ||
          ValidStore.isMultiCheck3(password)
        )
      ) {
        setValid(false);
        return;
      }

      console.log(email);
      console.log(password);
    }, [emailRef, passwordRef, setValid]);
    return (
      <>
        {/** Navigation */}
        <Navigation backgroundTheme={"blank"} visibleBackIcon={true} />

        {/** 정보 */}
        <div className="container nopadding mt-1">
          <h5 className="text-center">
            <img src="/assets/img/svg/logo.svg" alt="" />
          </h5>
        </div>

        {/** 입력 */}
        <div className="container nopadding">
          <div className="mt-4">
            <form className="form-line mt-	1">
              <div className="form-group">
                <label htmlFor="inputName" className="sr-only">
                  이메일
                </label>
                <input
                  ref={emailRef}
                  type="email"
                  className="form-control"
                  id="inputName"
                  placeholder="이메일"
                />
              </div>
              <div className="form-group">
                <label htmlFor="inputPhone" className="sr-only">
                  비밀번호
                </label>
                <input
                  ref={passwordRef}
                  type="password"
                  className="form-control"
                  id="inputPhone"
                  placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                />
                {!valid && (
                  <p className="text-muted">
                    <small className="red">비밀번호를 확인해주세요.</small>
                  </p>
                )}
              </div>
            </form>
            <a
              onClick={onClickLogin}
              className="btn btn-primary btn-lg btn-block"
            >
              로그인
            </a>
            <p className="text-center mt-3">
              <a onClick={() => history.push(`/member/findpw`)}>
                <small className="grey">비밀번호를 잊으셨나요?</small>
              </a>
            </p>
            <a
              onClick={() => history.push(`/member/signup`)}
              className="btn btn-grey btn-lg btn-block mt-4"
            >
              이메일로 가입하기
            </a>

            <div className="row no-gutters-md row-padding mt-3">
              <div className="col-3">
                <a
                  onClick={() => SNSStore.authorizeKakao()}
                  className="btn btn-sns-kakao btn-yellow btn-lg btn-block"
                >
                  <img
                    src="/assets/img/svg/icon-sns-kakao.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </a>
              </div>
              <div className="col-3">
                <a
                  onClick={() => SNSStore.authorizeFacebook()}
                  className="btn btn-sns-facebook btn-lg btn-block"
                >
                  <img
                    src="/assets/img/svg/icon-sns-facebook.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </a>
              </div>
              <div className="col-3">
                <a
                  onClick={() => SNSStore.authorizeNaver()}
                  className="btn btn-sns-naver btn-lg btn-block"
                >
                  <img
                    src="/assets/img/svg/icon-sns-naver.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </a>
              </div>
              <div className="col-3">
                <a
                  onClick={() => SNSStore.authorizeApple()}
                  className="btn btn-sns-apple btn-lg btn-block"
                >
                  <img
                    src="/assets/img/svg/icon-sns-apple.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </a>
              </div>
            </div>
            <p className="text-center mt-4">
              어복황제 로그인/회원가입시 <br />
              <a
                onClick={() => history.push(`/docs/policy-terms`)}
                className="text-primary"
              >
                이용약관
              </a>{" "}
              및{" "}
              <a
                onClick={() => history.push(`/docs/policy-privacy`)}
                className="text-primary"
              >
                개인정보취급방침
              </a>
              에 동의하게 됩니다.
            </p>
          </div>
          <p className="clearfix">
            <br />
            <br />
          </p>
        </div>
      </>
    );
  })
);
