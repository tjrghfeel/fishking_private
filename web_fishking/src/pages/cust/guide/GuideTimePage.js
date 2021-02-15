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
            <NavigationLayout title={"시간·체험"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding">
              <p className="mt-4"></p>
              <h5>
                <img
                  src="/assets/cust/img/svg/icon-guide-time.svg"
                  alt=""
                  className="vam"
                />{" "}
                시간·체험
              </h5>
              <p>
                1일 2회 오전, 오후 선택 출조가 가능한 시간제 낚시와 개인 또는
                모임의 특성과 일정에 맞춰 체험 가능한 낚시입니다.
              </p>
              <p>
                초보자에게 낚시하는 방법과 낚시 지도 제공으로 누구나 쉽고
                재미있게 낚시를 즐길 수 있습니다.
              </p>

              <hr />
              <h6>특징</h6>
              <p>
                하루 한 번 출조하는 일반적인 낚시와 달리 부담 없는 금액과 낚시
                방법으로 보통 5시간 이내로 이용가능합니다.
              </p>

              <hr />
              <h6>기타</h6>
              <p>
                선박별로 회를 떠드리고 라면 및 다양한 제공으로 휴게실, 화장실,
                그늘막 등 각종 편의시설로 인해 편리하게 즐길 수 있습니다.
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
