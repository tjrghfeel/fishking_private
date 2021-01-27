import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { MainTab, NavigationLayout, StoryTab },
  MODAL: { SelectAreaModal, SelectFishModal, SelectStorySortModal },
  VIEW: { FilterListView, StoryPostListItemView },
} = Components;

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
          isPending: false,
          isEnd: false,
          list: [],
          category: "fishingDiary",
          page: 0,
          districtList: null,
          fishSpeciesList: null,
          sort: "createdDate",
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
        } = await APIStore._get("/v2/api/fishingDiary/list/" + page, {
          category: PageStore.state.category,
          districtList: PageStore.state.districtList,
          fishSpeciesList: PageStore.state.fishSpeciesList,
          sort: PageStore.state.sort,
        });

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
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/story/detail/${item.id}`);
      };
      onClickProfile = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/member/profile/${item.memberId}`);
      };
      onClickLike = async (item) => {
        const { APIStore, DataStore, PageStore } = this.props;
        if (item.isLikeTo) {
          const resolve = await APIStore._delete("/v2/api/loveto", {
            linkId: item.id,
            takeType: "fishingDiary",
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isLikeTo: false }
            );
            PageStore.setState({ list });
          }
        } else {
          const resolve = await APIStore._post("/v2/api/loveto", {
            linkId: item.id,
            takeType: "fishingDiary",
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isLikeTo: true }
            );
            PageStore.setState({ list });
          }
        }
      };
      onClickComment = async (item) => {};
      onClickScrap = async (item) => {};
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SelectAreaModal
              id={"selAreaModal"}
              onSelected={(selected) => {
                let districtList = null;
                if (selected.length > 0) {
                  districtList = "";
                  for (let i = 0; i < selected.length; i++) {
                    const item = selected[i];
                    if (i === selected.length - 1) {
                      districtList += item;
                    } else {
                      districtList += item.concat(",");
                    }
                  }
                }
                PageStore.setState({ districtList });
                this.loadPageData(0);
              }}
            />
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => {
                let fishSpeciesList = null;
                if (selected.length > 0) {
                  fishSpeciesList = "";
                  for (let i = 0; i < selected.length; i++) {
                    const item = selected[i];
                    if (i === selected.length - 1) {
                      fishSpeciesList += item.code;
                    } else {
                      fishSpeciesList += item.code.concat(",");
                    }
                  }
                }
                PageStore.setState({ fishSpeciesList });
                this.loadPageData(0);
              }}
            />
            <SelectStorySortModal
              id={"selSortModal"}
              onSelected={(selected) => {
                PageStore.setState({ sort: selected.value });
                this.loadPageData(0);
              }}
            />

            <NavigationLayout title={"어복스토리"} showSearchIcon={true} />
            <StoryTab activeIndex={0} />

            {/** Filter */}
            <FilterListView
              list={[
                { text: "지역", modalTarget: "selAreaModal" },
                { text: "어종", modalTarget: "selFishModal" },
                { text: "정렬", modalTarget: "selSortModal" },
              ]}
            />

            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <StoryPostListItemView
                  key={index}
                  data={data}
                  showLikeIcon={true}
                  showCommentIcon={true}
                  showScrapIcon={true}
                  onClick={this.onClick}
                  onClickProfile={this.onClickProfile}
                  onClickLike={this.onClickLike}
                  onClickComment={this.onClickComment}
                  onClickScrap={this.onClickScrap}
                />
              ))}
            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
