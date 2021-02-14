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
        return (
          <React.Fragment>
            <NavigationLayout title={"실시간 조황"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding">
              <p className="mt-4"></p>
              <h5>
                <img
                  src="/assets/cust/img/svg/icon-guide-live.svg"
                  alt=""
                  className="vam"
                />{" "}
                실시간 조황
              </h5>
              <p>
                선상에 설치된 CCTV를 통해 선상낚시의 실시간 조황 영상을 실시간
                라이브로 제공하여 HD급 생생한 낚시 현장을 체험할 수 있습니다.
              </p>
              <hr />
              <h6>특징</h6>
              <p>
                클라우드 저장방식을 통해 실시간 CCTV 영상서비스를 제공합니다.
              </p>
              <hr />
              <h6>기타</h6>
              <p>
                매일매일 업데이트 되는 LIVE 조황 영상으로 직접 가지 않아도
                영상을 통해 생생한 현장 체험 가능합니다.
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
