import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { ApplyAddView },
} = Components;

export default inject(
  "PageStore",
  "DataStore",
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
            <NavigationLayout title={"업체등록"} showBackIcon={true} />

            <ApplyAddView successPathname={"/login"} />
          </React.Fragment>
        );
      }
    }
  )
);
