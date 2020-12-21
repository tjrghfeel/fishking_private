import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        document.querySelector("body").classList.add("pofile");
      }

      componentWillUnmount() {
        document.querySelector("body").classList.remove("pofile");
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return <div>profile</div>;
      }
    }
  )
);
