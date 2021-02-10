import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
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
            <NavigationLayout
              title={
                <span className="navbar-title">
                  <img
                    src="/assets/police/img/svg/navbar-logo-smartmarine.svg"
                    alt="승선확인"
                  />
                </span>
              }
              showBackIcon={true}
            />
          </React.Fragment>
        );
      }
    }
  )
);
