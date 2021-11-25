import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout, CsTab, QnaTab, MainTab },
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

            <CsTab activeIndex={2} />

            <QnaTab activeIndex={1} />

            <QnaListView detailPathname={`/cs/qna/detail`} />
            <div className="container nopadding" style={{height: '50px'}}>
            </div>
            <MainTab activeIndex={4} />
          </React.Fragment>
        );
      }
    }
  )
);
