import React from "react";
import { inject, observer } from "mobx-react";
import QnaItem from "../item/QnaItem";
import CsQnaTopTab from "../../fishking/layout/CsQnaTopTab";

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          page: 0,
          list: [],
          isEnd: false,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get("/v2/api/qna/" + page);

        if (page === 0) {
          PageStore.setState({ list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);
        } else {
          PageStore.setState({ list: PageStore.state.list.concat(content) });
        }
        if (content.length < pageSize) {
          PageStore.setState({ isEnd: true });
        } else {
          PageStore.setState({ isEnd: false });
        }
      };

      onClickItem = (item) => {
        const { onClick } = this.props;
        onClick(item);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          PageStore,
          navigateTo: { tab1, tab2 },
        } = this.props;
        return (
          <React.Fragment>
            <CsQnaTopTab
              activeIndex={1}
              navigate1To={tab1}
              navigate2To={tab2}
            />
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <QnaItem key={index} data={data} onClick={this.onClickItem} />
              ))}
          </React.Fragment>
        );
      }
    }
  )
);
