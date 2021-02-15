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
            <NavigationLayout title={"종일·생활"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding">
              <p className="mt-4"></p>
              <h5>
                <img
                  src="/assets/cust/img/svg/icon-guide-time-all.svg"
                  alt=""
                  className="vam"
                />{" "}
                종일·생활
              </h5>
              <p>
                아침부터 저녁까지 시간제약없이 여유롭게 즐길수 있는 낚시입니다.
              </p>
              <p>
                가까운 근해로 이동시간이 짧고 '생활 속에서 부담 없이 할 수 있는
                낚시로 생활낚시, 잡어낚시, 동네낚시라고도 합니다.
              </p>

              <hr />
              <h6>특징</h6>
              <p>
                낚시 방법이 간단해 초보자도 쉽게 즐길 수 있고, 시기와 포인트를
                잘 맞추면 다수확을 할 수 있고 맛좋은 어종들로 인기가 많습니다.
              </p>

              <hr />
              <h6>기타</h6>
              <p>
                회사 야유회, 동호회 등 단체로 이용하는 '독배' 상품도 있어 맞춤형
                낚시로 이용이 가능합니다.
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
