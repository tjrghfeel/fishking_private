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
            <NavigationLayout title={"갯바위 낚시"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding">
              <p className="mt-4"></p>
              <h5>
                <img
                  src="/assets/cust/img/svg/icon-guide-rock.svg"
                  alt=""
                  className="vam"
                />{" "}
                갯바위 낚시
              </h5>
              <p>
                해안가에 형성된 갯바위에서 즐기는 낚시 형태 갯가에 있는 바위로,
                연안에 자연적으로 형성된 암반지대를 이루며 물가로부터 급하게
                깊어지는 부분이 많고 원활한 조류로 물고기가 좋아하는 서식처를
                제공하는 장소입니다.
              </p>
              <p>
                갯바위 주변은 비교적 물가로부터 급하게 깊어지는 부분이 많고
                조류의 영향을 강하게 받아 많은 물고기가 모이는 장소입니다.
                초보자의 경우 외딴섬 갯바위는 다소 위험할 수도 있으므로 단독
                출조를 피하고 경험자와 동행하는 것이 좋습니다.
              </p>
              <hr />
              <h6>특징</h6>
              <p>
                배를 타고 섬으로 들어가서 즐기는 갯바위 낚시는 조류의 영향을
                강하게 받아 낚시인들이 가장 선호하는 대형어 등의 확실한 조과를
                기대할 수 있으나, 초보자의 경우 들쑥날쑥한 지형이 험한 만큼 다소
                위험할 수 있어 숙달된 경험자와 동행하는 것이 좋습니다.
              </p>
              <hr />
              <h6>주 어종</h6>
              <p>감성돔, 벵에돔, 참돔, 돌돔, 농어, 볼락, 부시리 등</p>
              <hr />
              <h6>주의사항</h6>
              <p>
                간만의 차이로 퇴로가 없어 질 수 있기에 썰물과 밀물을 염두에
                두어야 합니다.{" "}
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
