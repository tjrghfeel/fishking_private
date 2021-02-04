import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"약관 및 정책"} showBackIcon={true} />

            <div className="container nopadding mt-0">
              <div className="pt-0">
                <hr className="full mt-0 mb-3" />
                <a onClick={() => PageStore.push(`/policy/terms`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">이용약관</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/policy/privacy`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">개인정보처리방침</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/policy/cancel`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">취소 및 환불 규정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/policy/lbs`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">위치기반 서비스 이용약관</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/policy/agree`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">개인정보 제 3자 제공 동의</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
