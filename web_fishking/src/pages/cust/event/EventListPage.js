import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { EventListItemView },
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
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          list: [],
          page: 0,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if ((page > 0 && PageStore.state.isEnd) || APIStore.isLoading) return;

        PageStore.setState({ page, isPending: true });

        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get(`/v2/api/event/list/${page}`);

        console.log(JSON.stringify(content));

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

      onClick = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/event/detail/${item.eventId}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"이벤트"} showBackIcon={true} />

            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <EventListItemView
                  key={index}
                  data={data}
                  onClick={this.onClick}
                />
              ))}
          </React.Fragment>
        );
      }
    }
  )
);
