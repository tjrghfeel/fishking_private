import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
    View: { CommonCsQnaListView },
  },
  Fishking: {
    Layout: { CsTopTab },
  },
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
      onClickItem = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState(PageStore.state);
        PageStore.push(`/fishking/cs/qna/${item.id}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <Navigation title={"고객센터"} showBackIcon={true} />

            <CsTopTab activeIndex={2} />

            <CommonCsQnaListView
              onClick={this.onClickItem}
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
