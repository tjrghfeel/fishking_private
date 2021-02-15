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
            <NavigationLayout title={"스마트 인검"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding">
              <p className="mt-4"></p>
              <h5>
                <img
                  src="/assets/cust/img/svg/icon-guide-smart.svg"
                  alt=""
                  className="vam"
                />{" "}
                스마트 인검
              </h5>
              <p>
                승선신고를 현장에서 승선명부에 수기로 작성하는 방식이 아니라
                모바일 앱으로 지문인식을 통해 편리하게 작성할 수 있습니다.{" "}
              </p>
              <p>
                어복황제로 예약을 한 경우 별도의 작성없이 간단히 지문인식으로
                편리하게 승선하실 수 있습니다.{" "}
              </p>
              <hr />
              <h6>특징</h6>
              <p>
                최초 1회만 작성하면 어복황제에서 승선명부를 제공하는 다른 지역의
                선박 및 출조점에서 재입력없이 지문인식으로 간편하게 사용이
                가능합니다.
              </p>
              <hr />
              <h6>기타</h6>
              <p>
                승선시 시간을 절약하고 해경에게 바로 안전하고 정확한 정보 전달이
                가능합니다.
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
