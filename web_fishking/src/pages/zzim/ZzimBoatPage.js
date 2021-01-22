import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, ZzimTab },
  VIEW: { ZzimListView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
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
            <NavigationLayout title={"찜한 업체"} showBackIcon={true} />

            <ZzimTab activeIndex={0} />

            <ZzimListView fishingType={"ship"} />
          </React.Fragment>
        );
      }
    }
  )
);
