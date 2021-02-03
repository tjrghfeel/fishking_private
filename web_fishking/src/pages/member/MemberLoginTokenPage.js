import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import qs from "qs";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject("PageStore")(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.state = { text: "" };
        }

        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          const { location, PageStore } = this.props;
          const { accesstoken = null, error = null } =
            qs.parse(location.search.replace(/[?]/g, "")) || {};

          if (error !== null) {
            this.setState({ text: error });
          } else if (accesstoken !== null) {
            PageStore.setLogin(accesstoken);
            PageStore.push(`/cust/main/my`);
          } else {
            this.setState({ text: "잘못된 접근입니다." });
          }
        }
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          return (
            <React.Fragment>
              <NavigationLayout
                title={"로그인"}
                showBackIcon={true}
                backPathname={`/main/home`}
              />
              {this.state.text}
            </React.Fragment>
          );
        }
      }
    )
  )
);
