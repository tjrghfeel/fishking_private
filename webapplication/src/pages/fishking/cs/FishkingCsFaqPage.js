import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
    View: { CommonCsFaqListView },
  },
  Fishking: {
    Layout: { CsTopTab },
  },
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
            <Navigation title={"고객센터"} showBackIcon={true} />

            <CsTopTab activeIndex={0} />

            <CommonCsFaqListView role={"member"} />
          </React.Fragment>
        );
      }
    }
  )
);
