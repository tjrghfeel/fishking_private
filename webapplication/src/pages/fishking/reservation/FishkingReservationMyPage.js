import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
  },
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
            {/** Navigation */}
            <Navigation
              title={"예약내역"}
              showBackIcon={true}
              showSearchIcon={true}
            />
          </React.Fragment>
        );
      }
    }
  )
);
