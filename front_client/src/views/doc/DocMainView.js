import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";

export default inject()(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"약관 및 정책"} showBack={true} />

            {/** 입력 */}
            <div className="container nopadding mt-0">
              <div className="pt-0">
                <hr className="full mt-0 mb-3" />
                <a onClick={() => history.push(`/doc/policy-terms`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">이용약관</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/doc/policy-privacy`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">개인정보처리방침</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/doc/policy-cancel`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">취소 및 환불 규정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/doc/policy-lbs`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">위치기반 서비스 이용약관</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/doc/policy-agree`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">개인정보 제 3자 제공 동의</div>
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
      }
    }
  )
);
