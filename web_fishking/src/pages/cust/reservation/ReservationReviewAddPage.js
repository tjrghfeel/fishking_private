import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
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
            <NavigationLayout title={"리뷰 작성"} showBackIcon={true} />

            <div className="container nopadding bg-grey-title text-center">
              <h5 className="mb-1">어복황제1호</h5>
              <h6 className="mt-0 mb-3">
                2020.10.03 <small className="grey">/</small> 우럭(오후){" "}
              </h6>
            </div>
            <hr className="mt-0" />
            <p></p>
          </React.Fragment>
        );
      }
    }
  )
);
