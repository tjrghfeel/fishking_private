/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback, useEffect, useRef } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ history }) => {
    const fileProfile = useRef(null);
    useEffect(() => {
      document.querySelector("body").classList.add("pofile");
      return () => {
        document.querySelector("body").classList.remove("pofile");
      };
    }, []);
    /** 프로필 이미지 변경 클릭 */
    const onClickProfile = useCallback(() => {
      document.querySelector("#fileProfile").click();
    }, []);
    /** 프로필 이미지 변경 */
    const onChangeProfile = useCallback(() => {
      console.log("파일 변경");
      console.log(fileProfile.current?.files[0]);
    }, [fileProfile]);
    return (
      <>
        {/** Navigation & Profile wrap */}
        <input
          ref={fileProfile}
          type="file"
          id="fileProfile"
          name="fileProfile"
          style={{ display: "none" }}
          onChange={onChangeProfile}
        />
        <div className="mt-0">
          <div className="profilewrap">
            <div className="float-top-left">
              <a onClick={() => history.goBack()}>
                <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
              </a>
            </div>
            <a
              href="story-add-photo.html"
              className="img-upload btn btn-circle btn-circle-sm btn-white float_btm_right"
              data-toggle="modal"
              data-target="#photobgModal"
            >
              <img
                src="/assets/img/svg/icon-photo.svg"
                alt=""
                className="icon-photo"
              />
            </a>
            <div className="imgWrap">
              <img
                src="/assets/img/bg-profile04.jpg"
                className="d-block w-100"
                alt=""
              />
            </div>
            <figure>
              <span>
                <img
                  className="media-object profile"
                  src="/assets/img/sample/profile3.jpg"
                  alt=""
                />
                <a
                  onClick={onClickProfile}
                  className="img-upload btn btn-circle btn-circle-sm btn-white float_btn"
                >
                  <img
                    src="/assets/img/svg/icon-photo.svg"
                    alt=""
                    className="icon-photo"
                  />
                </a>
              </span>
            </figure>
          </div>
        </div>

        {/** 입력 */}
        <div className="container nopadding">
          <div className="pt-0">
            <hr className="full mt-3 mb-3" />
            <div className="row no-gutters align-items-center">
              <div className="col-3 pl-2">
                <small className="grey">계정</small>
              </div>
              <div className="col-8 text-right">gildong@gmail.com</div>
              <div className="col-1 text-right pl-1"></div>
            </div>
            <hr className="full mt-3 mb-3" />
            <a onClick={() => history.push(`/set/profile/nickname`)}>
              <div className="row no-gutters align-items-center">
                <div className="col-3 pl-2">
                  <small className="grey">닉네임</small>
                </div>
                <div className="col-8 text-right">바다황제</div>
                <div className="col-1 text-right pl-1">
                  <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                </div>
              </div>
            </a>
            <hr className="full mt-3 mb-3" />
            <a onClick={() => history.push(`/set/profile/status`)}>
              <div className="row no-gutters align-items-center">
                <div className="col-3 pl-2">
                  <small className="grey">상태메시지</small>
                </div>
                <div className="col-8 text-right">
                  상태메시지를 입력해 주세요.
                </div>
                <div className="col-1 text-right pl-1">
                  <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                </div>
              </div>
            </a>
            <hr className="full mt-3 mb-3" />
            <a href="my-phone.html">
              <div className="row no-gutters align-items-center">
                <div className="col-3 pl-2">
                  <small className="grey">휴대폰 번호</small>
                </div>
                <div className="col-8 text-right">01012345678</div>
                <div className="col-1 text-right pl-1">
                  <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                </div>
              </div>
            </a>
            <hr className="full mt-3 mb-3" />
            <a onClick={() => history.push(`/set/profile/email`)}>
              <div className="row no-gutters align-items-center">
                <div className="col-3 pl-2">
                  <small className="grey">이메일 변경</small>
                </div>
                <div className="col-8 text-right">gil****@hotmail.com</div>
                <div className="col-1 text-right pl-1">
                  <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                </div>
              </div>
            </a>
            <hr className="full mt-3 mb-3" />
            <a onClick={() => history.push(`/set/profile/password`)}>
              <div className="row no-gutters align-items-center">
                <div className="col-3 pl-2">
                  <small className="grey">비밀번호 변경</small>
                </div>
                <div className="col-8 text-right"></div>
                <div className="col-1 text-right pl-1">
                  <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                </div>
              </div>
            </a>
            <hr className="full mt-3 mb-3" />
          </div>
        </div>
      </>
    );
  })
);
