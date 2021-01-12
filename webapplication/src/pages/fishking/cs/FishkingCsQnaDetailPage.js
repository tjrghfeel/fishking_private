import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
    View: { CommonCsQnaDetailView },
  },
  Fishking: {
    Layout: { CsTopTab, CsQnaTopTab },
  },
} = Components;
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
        const {
          match: {
            params: { id: postId },
          },
          APIStore,
        } = this.props;
        const resolve = await APIStore._get("/v2/api/qna/detail", { postId });
        this.setState(resolve);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <Navigation title={"고객센터"} showBackIcon={true} />

            <CsTopTab activeIndex={2} />

            <CommonCsQnaDetailView
              navigateTo={{
                tab1: `/fishking/cs/qna`,
                tab2: `/fishking/cs/qna/list`,
              }}
            />
          </React.Fragment>
        );
      }
    }
  )
);
