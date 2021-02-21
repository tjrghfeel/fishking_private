import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        const keyword = qp.keyword || "";

        const resolve = await APIStore._get(`/v2/api/search/all`, { keyword });
        this.setState(resolve);
        console.log(JSON.stringify(resolve));
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return <React.Fragment></React.Fragment>;
      }
    }
  )
);
