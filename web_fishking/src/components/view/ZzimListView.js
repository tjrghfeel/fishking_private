import React from "react";
import { inject, observer } from "mobx-react";
import ZzimListItemView from "./ZzimListItemView";
import PageStore from "../../stores/PageStore";

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
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
        const { APIStore, PageStore, fishingType = "ship" } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get(`/v2/api/take/${fishingType}/${page}`)) || {};

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

      onDelete = async (item) => {
        const { APIStore, DataStore } = this.props;
        const resolve = await APIStore._delete("/v2/api/take", {
          takeId: item.takeId,
        });
        if (resolve) {
          const list = DataStore.removeItemOfArrayByKey(
            PageStore.state.list,
            "takeId",
            item.takeId
          );
          PageStore.setState({ list });
        }
      };

      onClick = (item) => {
        const { PageStore, fishingType } = this.props;
        PageStore.push(`/company/${fishingType}/detail/${item.shipId}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <div className="container nopadding mt-3 mb-0">
              {PageStore.state.list &&
                PageStore.state.list.map((data, index) => (
                  <ZzimListItemView
                    key={index}
                    data={data}
                    onDelete={this.onDelete}
                    onClick={this.onClick}
                  />
                ))}
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
