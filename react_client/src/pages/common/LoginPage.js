/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useState, useCallback } from "react";
import Navigation from "../../components/layouts/Navigation";
import Container from "../../components/layouts/Container";
import RowForm from "../../components/forms/RowForm";
import Clearfix from "../../components/layouts/Clearfix";
import Button from "../../components/forms/Button";
import { inject, observer } from "mobx-react";

const LoginPage = inject("SNSStore")(
  observer(({ SNSStore }) => {
    const [email, setEmail] = useState(null);
    const [password, setPassword] = useState(null);
    const onChangeEmail = useCallback(
      (value) => {
        setEmail(value);
      },
      [setEmail]
    );
    const onChangePassword = useCallback(
      (value) => {
        setPassword(value);
      },
      [setPassword]
    );
    /** 로그인 콜백 */
    const onClickLogin = () => {
      console.log("로그인");
    };
    /** 비밀번호찾기 */
    const onClickForgetPassword = () => {
      console.log("비밀번호찾기");
    };
    /** 이메일로가입하기 */
    const onClickJoinByEmail = () => {
      console.log("이메일로가입하기");
    };
    /** SNSPage 로그인 */
    const onClickLoingBySNS = (sns) => {
      console.log("SNS로그인 --> " + sns);
      if (sns === "kakao") {
        SNSStore.kakaoAuthorize();
      } else if (sns === "facebook") {
        SNSStore.facebookAuthorize();
      } else if (sns === "naver") {
        SNSStore.naverAuthorize();
      } else if (sns === "apple") {
        SNSStore.appleAuthorize();
      }
    };
    /** 정적 문서 보기 */
    const onClickViewDoc = (doc) => {
      console.log("문서보기 --> " + doc);
    };
    return (
      <>
        {/** 네비게이션 */}
        <Navigation bgClear={true} canGoBack={true} />

        {/** 정보 */}
        <Container cls={"nopadding mt-1"}>
          <h5 className="text-center">
            <img src="/assets/img/svg/logo.svg" alt="" />
          </h5>
        </Container>
        {/** 입력 */}
        <Container cls={"nopadding"}>
          <div className="mt-4">
            <RowForm
              rows={[
                {
                  formType: "input",
                  id: "inputEmail",
                  label: "이메일",
                  type: "email",
                  placeholder: "이메일",
                  value: email,
                  onChange: onChangeEmail,
                },
                {
                  formType: "input",
                  id: "inputPassword",
                  label: "비밀번호",
                  type: "password",
                  placeholder:
                    "비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)",
                  value: password,
                  onChange: onChangePassword,
                },
              ]}
            />
            <Button cls={"btn-primary"} onClick={onClickLogin}>
              로그인
            </Button>
            <p className="text-center mt-3">
              <a onClick={() => onClickForgetPassword()}>
                <small className="grey">비밀번호를 잊으셨나요?</small>
              </a>
            </p>
            <Button cls={"btn-grey mt-4"} onClick={onClickJoinByEmail}>
              이메일로 가입하기
            </Button>
            <div className="row no-gutters-md row-padding mt-3">
              <div className="col-3">
                <Button
                  sns={"kakao"}
                  onClick={() => onClickLoingBySNS("kakao")}
                >
                  <img
                    src="/assets/img/svg/icon-sns-kakao.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </Button>
              </div>
              <div className="col-3">
                <Button
                  sns={"facebook"}
                  onClick={() => onClickLoingBySNS("facebook")}
                >
                  <img
                    src="/assets/img/svg/icon-sns-facebook.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </Button>
              </div>
              <div className="col-3">
                <Button
                  sns={"naver"}
                  onClick={() => onClickLoingBySNS("naver")}
                >
                  <img
                    src="/assets/img/svg/icon-sns-naver.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </Button>
              </div>
              <div className="col-3">
                <Button
                  sns={"apple"}
                  onClick={() => onClickLoingBySNS("apple")}
                >
                  <img
                    src="/assets/img/svg/icon-sns-apple.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;시작
                </Button>
              </div>
            </div>
            <p className="text-center mt-4">
              어복황제 로그인/회원가입시 <br />
              <a
                onClick={() => onClickViewDoc("policy_terms.html")}
                className="text-primary"
              >
                이용약관
              </a>
              및{" "}
              <a
                onClick={() => onClickViewDoc("policy_privacy.html")}
                className="text-primary"
              >
                개인정보취급방침
              </a>
              에 동의하게 됩니다.
            </p>
          </div>
          <Clearfix>
            <br />
            <br />
          </Clearfix>
        </Container>
      </>
    );
  })
);

export default LoginPage;
