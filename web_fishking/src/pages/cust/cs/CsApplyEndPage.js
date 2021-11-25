import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout, CsTab, MainTab },
  VIEW: { ApplyEndView },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
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

            <CsTab activeIndex={1} />

            <ApplyEndView />
            <div className="container nopadding" style={{height: '50px'}}>
            </div>
            <MainTab activeIndex={4} />
          </React.Fragment>
        );
      }
    }
  )
);
