import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
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
        return (
          <React.Fragment>
            <NavigationLayout title={"공지사항"} showBackIcon={true} />

            <NoticeListView
              role={"member"}
              detailPathname={`/cs/notice/detail`}
            />
          </React.Fragment>
        );
      }
    }
  )
);
