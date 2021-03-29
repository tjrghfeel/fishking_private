import React from "react";
import { inject, observer } from "mobx-react";
import NoticeListItemView from "./NoticeListItemView";

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
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore, role } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get("/v2/api/notice/" + page, { role })) || {};

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
        const { PageStore, detailPathname = null } = this.props;
        if (detailPathname !== null) {
          PageStore.storeState();
          PageStore.push(`${detailPathname}/${item.id}`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {PageStore.state.list && (
              <div className="container nopadding">
                <div className="pt-0">
                  {PageStore.state.list.map((data, index) => (
                    <NoticeListItemView
                      key={index}
                      data={data}
                      index={index}
                      onClick={this.onClickItem}
                    />
                  ))}
                </div>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
