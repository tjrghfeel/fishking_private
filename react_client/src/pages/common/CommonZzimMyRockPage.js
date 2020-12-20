import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return <div>hello</div>;
      }
    }
  )
);
