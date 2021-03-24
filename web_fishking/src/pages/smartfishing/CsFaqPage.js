import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartfishingCsTab },
  VIEW: { FaqListView },
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
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"고객센터"} showBackIcon={true} />

            <SmartfishingCsTab activeIndex={1} />

            <FaqListView role={"shipowner"} />
          </React.Fragment>
        );
      }
    }
  )
);
