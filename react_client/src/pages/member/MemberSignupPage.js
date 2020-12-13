import React, { useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject()(
  observer(() => {
    const [stage, setStage] = useState(1);
    return (
      <>
        {/** Navigation */}
        <Navigation title={"회원가입"} visibleBackIcon={true} />

        {/** 정보 */}
        <div className="container nopadding mt-1">
          <h5 className="text-center">
            <img src="/assets/img/svg/logo.svg" alt="" />
          </h5>
          <div className="text-right">
            <div className="pay-bg">
              <ol className="pay-step">
                <li className={stage === 1 ? "active" : ""}>1. 약관동의</li>
                <li className={stage === 2 ? "active" : ""}>2. 정보입력</li>
                <li className={stage === 3 ? "active" : ""}>3. 본인인증</li>
              </ol>
            </div>
          </div>
        </div>

        {/** 1.약관동의 > 입력 */}
        {stage === 1 && (
          <div className="container nopadding mt-4">
            <form>
              <label className="control radio mt-2">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  <strong>전체동의</strong>
                </span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  이용약관 동의 <small className="red">(필수)</small>
                </span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  개인정보 수집 및 이용동의{" "}
                  <small className="red">(필수)</small>
                  <br />
                  <small className="grey">
                    예약 및 서비스 이용을 위한 필수 개인정보 수집
                  </small>
                </span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  개인정보 수집 및 이용동의{" "}
                  <small className="grey">(선택)</small>
                  <br />
                  <small className="grey">
                    서비스 혜택 등의 정보 제공을 위한 개인정보 수집 및 활동
                  </small>
                </span>
              </label>
              <hr className="full mt-2 mb-3" />
              <small>
                회원가입시 본인이 만14세 이상임에 동의하게 됩니다.
                <br />
                선택 항목을 동의하지 않아도 서비스를 이용할 수 있습니다.{" "}
              </small>
              <div className="form-group mt-4">
                <a
                  href="signup2.html"
                  className="btn btn-primary btn-lg btn-block"
                >
                  다음
                </a>
              </div>
            </form>
            <p className="clearfix">
              <br />
            </p>
          </div>
        )}
      </>
    );
  })
);
