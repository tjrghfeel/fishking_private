import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
    View: { CommonNoticeDetailView },
  },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onClickItem = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/fishking/common/notice/${item.id}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <Navigation title={"공지사항"} showBackIcon={true} />

            <CommonNoticeDetailView />
          </React.Fragment>
        );
      }
    }
  )
);
