import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import PageStore from "../../stores/PageStore";
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
      constructor(props) {
        super(props);
        this.selAreaModal = React.createRef(null);
        this.selFishModal = React.createRef(null);
        this.state = {
          selAreaActive: false,
          selFishActive: false,
          selSortActive: false,
          textArea: "지역",
          textFish: "어종",
          textSort: "정렬",
        };
      }
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
          district: null,
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
          district: PageStore.state.district,
          fishSpeciesList: PageStore.state.fishSpeciesList,
          sort: PageStore.state.sort,
        });

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
        PageStore.push(`/story/diary/detail/${item.id}`);
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
            takeType: PageStore.state.category,
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isLikeTo: false, likeCount: item.likeCount - 1 }
            );
            PageStore.setState({ list });
          }
        } else {
          const resolve = await APIStore._post("/v2/api/loveto", {
            linkId: item.id,
            takeType: PageStore.state.category,
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isLikeTo: true, likeCount: item.likeCount + 1 }
            );
            PageStore.setState({ list });
          }
        }
      };
      onClickComment = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/story/diary/comment/${item.id}`);
      };
      onClickScrap = async (item) => {
        const { APIStore, DataStore, PageStore } = this.props;
        if (item.isScraped) {
          const resolve = await APIStore._delete("/v2/api/fishingDiary/scrap", {
            fishingDiaryId: item.id,
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isScraped: false, scrapCount: item.scrapCount - 1 }
            );
            PageStore.setState({ list });
          }
        } else {
          const resolve = await APIStore._post("/v2/api/fishingDiary/scrap", {
            fishingDiaryId: item.id,
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isScraped: true, scrapCount: item.scrapCount + 1 }
            );
            PageStore.setState({ list });
          }
        }
      };
      onSelectedArea = (selected) => {
        if (selected === null) return;

        console.log(JSON.stringify(selected));
        const { PageStore } = this.props;
        if (selected === null) {
          this.setState({ selAreaActive: false });
        } else {
          this.setState({ selAreaActive: true });
        }
        PageStore.setState({ district: selected });
        this.loadPageData(0);
      };
      onSelectedFish = (selected) => {
        const { PageStore } = this.props;
        let fishSpeciesList = null;
        if (selected.length > 0) {
          this.setState({
            selFishActive: true,
            textFish: `어종(${selected.length})`,
          });
          fishSpeciesList = "";
          for (let i = 0; i < selected.length; i++) {
            const item = selected[i];
            if (i === selected.length - 1) {
              fishSpeciesList += item.code;
            } else {
              fishSpeciesList += item.code.concat(",");
            }
          }
        } else {
          this.setState({ selFishActive: false, textFish: "어종" });
        }
        PageStore.setState({ fishSpeciesList });
        this.loadPageData(0);
      };
      onSelectedSort = (selected) => {
        const { PageStore } = this.props;
        PageStore.setState({ sort: selected.value });
        this.loadPageData(0);
      };
      onClearArea = () => {
        this.selAreaModal.current?.onInit();
        PageStore.setState({ districtList: null });
        this.setState({ selAreaActive: false, textArea: `지역` });
        this.loadPageData(0);
      };
      onClearFish = () => {
        this.selFishModal.current?.onInit();
        PageStore.setState({ fishSpeciesList: null });
        this.setState({ selFishActive: false, textFish: "어종" });
        this.loadPageData(0);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SelectAreaModal
              ref={this.selAreaModal}
              id={"selAreaModal"}
              onSelected={this.onSelectedArea}
            />
            <SelectFishModal
              ref={this.selFishModal}
              id={"selFishModal"}
              onSelected={this.onSelectedFish}
            />
            <SelectStorySortModal
              id={"selSortModal"}
              onSelected={this.onSelectedSort}
            />

            <NavigationLayout title={"어복스토리"} showSearchIcon={true} />
            <StoryTab activeIndex={0} />

            {/** Filter */}
            <FilterListView
              list={[
                {
                  text: this.state.textArea,
                  modalTarget: "selAreaModal",
                  isActive: this.state.selAreaActive,
                  onClickClear: this.onClearArea,
                },
                {
                  text: this.state.textFish,
                  modalTarget: "selFishModal",
                  isActive: this.state.selFishActive,
                  onClickClear: this.onClearFish,
                },
                {
                  text: this.state.textSort,
                  modalTarget: "selSortModal",
                  isActive: this.state.selSortActive,
                },
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
