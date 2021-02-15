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
            <NavigationLayout title={"선상 낚시"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding">
              <p className="mt-4"></p>
              <h5>
                <img
                  src="/assets/cust/img/svg/icon-guide-boat.svg"
                  alt=""
                  className="vam"
                />{" "}
                선상 낚시
              </h5>
              <p>
                배낚시라고도 하며 한바다 위로 나아가 배 위에서 즐기는 낚시로
                항구 인근의 내해 포인트로부터 멀리 공해상의 포인트까지, 오가는
                낚싯배 위에서 숙박을 하는 원정낚시도 가능합니다.
              </p>
              <p>
                대상어는 각 해당 수역에 서식하는 어종 전반에 걸치는데, 지역과
                시즌에 따라 또는 승선하는 낚싯배에 따라 노리는 어종이
                달라집니다. 우럭 · 대구 · 볼락 · 열기 · 가자미 · 보리멸 · 갈치 ·
                부시리 등 온갖 고급 어종을 풍성하게 낚을 수 있고, 어종에 따라선
                초보자도 쉽게 즐길 수 있다는 점이 선상낚시의 장점입니다.
              </p>

              <hr />
              <h6>특징</h6>
              <p>
                물고기가 서식하기 좋은 환경을 제공하기 때문에 늘 먹잇감이
                풍부하며, 사철 대상어가 머무릅니다. 고기가 있는 곳을 찾아가
                능동적인 낚시를 즐기실 수 있습니다.
              </p>

              <hr />
              <h6>주의사항</h6>
              <p>
                승선을 위해 신분증과 안정 장구 착용이 필수입니다. 낚시를
                하다보면 옆 사람 채비와 엉키는 일이 있으므로 잘잘못을 따지기
                보다 내 채비를 먼저 자를 줄 아는 너그러움과 배려심을 갖는것이
                좋습니다.
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
