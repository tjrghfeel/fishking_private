import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartfishingCsTab, QnaTab },
  VIEW: { QnaListView },
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
            <NavigationLayout title={"고객센터"} showBackIcon={true} />

            <SmartfishingCsTab activeIndex={2} />

            <QnaTab activeIndex={1} />

            <QnaListView detailPathname={`/cs/qna/detail`} />
          </React.Fragment>
        );
      }
    }
  )
);
