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
        // TODO : [PUB-NO/API-NO] 내 알림 : 알림 리스트 API 개발 필요
        return (
          <>
            {/** Navigation */}
            <Navigation title={"알림"} showBack={true} />
          </>
        );
      }
    }
  )
);
