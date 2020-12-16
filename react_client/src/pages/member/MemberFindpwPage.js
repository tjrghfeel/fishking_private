/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback, useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject("ValidStore")(
  observer(({ ValidStore }) => {
    const [mobileNo, setMobileNo] = useState("");
    const [validNo, setValidNo] = useState("");
    const [validNoValid, setValidNoValid] = useState(null);
    const [authorized, setAuthorized] = useState(false);

    const [newPassword, setNewPassword] = useState("");
    const [newPasswordR, setNewPasswordR] = useState("");
    const [newPasswordValid, setNewPasswordValid] = useState(true);

    /** SMS 발송 요청 */
    const onClickSMS = useCallback(() => {
      if (!ValidStore.isPhoneNo(mobileNo)) {
        console.log("휴대폰번호를 확인해주세요.");
        return;
      } else {
        console.log("발송 -> " + mobileNo);
      }
    }, [mobileNo, ValidStore]);
    /** 인증번호 확인 요청 */
    const requestValid = useCallback(() => {
      console.log("인증요청 -> " + mobileNo + " : " + validNo);
      if (mobileNo !== "" && validNo !== "") {
        setValidNoValid(true);
        setAuthorized(true);
      } else {
        setValidNoValid(false);
      }
    }, [mobileNo, validNo, setValidNoValid, setAuthorized]);
    /** 비밀번호 변경 요청 */
    const onReset = useCallback(() => {
      if (
        !(
          ValidStore.isMultiCheck1(newPassword) ||
          ValidStore.isMultiCheck2(newPassword) ||
          ValidStore.isMultiCheck3(newPassword)
        ) ||
        newPassword !== newPasswordR
      ) {
        setNewPasswordValid(false);
        return;
      }
      console.log("변경 요청");
    }, [newPassword, newPasswordR, setNewPasswordValid, ValidStore]);
    return (
      <>
        {/** Navigation */}
        <Navigation title={"비밀번호 재설정"} visibleBackIcon={true} />

        {/** 정보 */}
        <div className="container nopadding mt-1">
          <h5 className="text-center">
            <img src="/assets/img/svg/logo.svg" alt="" />
          </h5>
        </div>

        {/** 인증 > 입력 */}
        {!authorized && (
          <div className="container nopadding">
            <div className="mt-4">
              <form className="form-line mt-	1">
                <div className="form-group">
                  <label htmlFor="inputName" className="sr-only">
                    휴대폰 번호
                  </label>
                  <input
                    type="number"
                    className="form-control"
                    id="inputName"
                    placeholder="휴대폰 번호를 입력해 주세요."
                    value={mobileNo}
                    onChange={(e) => setMobileNo(e.target.value.substr(0, 13))}
                  />
                  <a onClick={onClickSMS} className="text-link text-primary">
                    발송
                  </a>
                </div>
                <div className="form-group">
                  <label htmlFor="inputPhone" className="sr-only">
                    인증번호
                  </label>
                  <input
                    type="number"
                    className={
                      validNoValid === false
                        ? "form-control is-invalid"
                        : validNoValid === true
                        ? "form-control is-valid"
                        : "form-control"
                    }
                    id="inputPhone"
                    placeholder="인증번호 6자리를 입력해 주세요."
                    value={validNo}
                    onChange={(e) => setValidNo(e.target.value.substr(0, 6))}
                  />
                  {validNoValid === false && (
                    <p className="text-muted">
                      <small className="red">
                        인증번호가 일치하지 않습니다.
                      </small>
                    </p>
                  )}
                  {validNoValid === true && (
                    <p className="text-muted">
                      <small className="text-success">인증되었습니다.</small>
                    </p>
                  )}
                </div>
                <div className="form-group pt-2">
                  <a
                    onClick={requestValid}
                    className={
                      "btn btn-lg btn-block" +
                      (mobileNo !== "" && validNo !== ""
                        ? " btn-primary"
                        : " btn-grey")
                    }
                  >
                    비밀번호 찾기
                  </a>
                </div>
              </form>
            </div>
          </div>
        )}

        {/** 비밀번호 재설정 > 입력 */}
        {authorized && (
          <div className="container nopadding">
            <div className="mt-4">
              <form className="form-line mt-	1">
                <div className="form-group">
                  <label htmlFor="inputName" className="sr-only">
                    새 비밀번호
                  </label>
                  <input
                    type="password"
                    className="form-control"
                    id="inputName"
                    placeholder="새 비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                    value={newPassword}
                    onChange={(e) =>
                      setNewPassword(e.target.value.substr(0, 15))
                    }
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="inputPhone" className="sr-only">
                    새 비밀번호 확인
                  </label>
                  <input
                    type="password"
                    className="form-control"
                    id="inputPhone"
                    placeholder="새 비밀번호 확인"
                    value={newPasswordR}
                    onChange={(e) =>
                      setNewPasswordR(e.target.value.substr(0, 15))
                    }
                  />
                  {newPasswordValid === false && (
                    <p className="text-muted">
                      <small className="red">비밀번호를 확인해주세요.</small>
                    </p>
                  )}
                </div>
                <div className="form-group pt-2">
                  <a
                    onClick={onReset}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    비밀번호 변경
                  </a>
                </div>
              </form>
            </div>
          </div>
        )}
      </>
    );
  })
);
