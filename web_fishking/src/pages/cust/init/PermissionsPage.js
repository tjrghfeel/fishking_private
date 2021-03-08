import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";

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
        return <React.Fragment></React.Fragment>;
      }
    }
  )
);
