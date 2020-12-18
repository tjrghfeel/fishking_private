import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import ScrollList04 from "../../components/list/ScrollList04";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"알림"} visibleBackIcon={true} />

            {/** 데이터 */}
            <ScrollList04
              itemType={"ListItem14"}
              list={[
                {
                  sectionTitle: "2020.08.23",
                  imgSrc: "/assets/img/svg/alarm-notice.svg",
                  title: "쿠폰 만료 알림",
                  content:
                    "보유하고 계신 쿠폰 사용기간 만료일이 1일 남았습니다. 1일 이후에는 자동 소멸됩니다.",
                  time: "14:30",
                },
              ]}
            />
          </>
        );
      }
    }
  )
);
