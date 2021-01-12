import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
    View: { CommonCsApplyView },
  },
  Fishking: {
    Layout: { CsTopTab },
  },
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
            <Navigation title={"고객센터"} showBackIcon={true} />

            <CsTopTab activeIndex={1} />

            <CommonCsApplyView />
          </React.Fragment>
        );
      }
    }
  )
);
