import React from "react";
import { inject, observer } from "mobx-react";
import StoryPostListItemView from "./StoryPostListItemView";

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
        } = await APIStore._get("/v2/api/myFishingPostList/" + page);

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

      onClick = (item) => {
        console.log(JSON.stringify(item));
      };
      onClickProfile = (item) => {
        console.log(JSON.stringify(item));
      };
      onClickComment = (item) => {
        console.log(JSON.stringify(item));
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <StoryPostListItemView
                  key={index}
                  data={data}
                  showCommentIcon={true}
                  onClick={this.onClick}
                  onClickProfile={this.onClickProfile}
                  onClickComment={this.onClickComment}
                />
              ))}
          </React.Fragment>
        );
      }
    }
  )
);
