import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartfishingCsTab },
  VIEW: { NoticeListView },
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
            <NavigationLayout title={"공지사항"} showBackIcon={true} />

            <SmartfishingCsTab activeIndex={0} />

            <NoticeListView
              role={"shipowner"}
              detailPathname={`/cust/cs/notice/detail`}
            />
          </React.Fragment>
        );
      }
    }
  )
);
