import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import PageStore from "../../../stores/PageStore";
const {
  LAYOUT: { MainTab, NavigationLayout, StoryTab },
  MODAL: { SelectMultiAreaModal, SelectFishModal, SelectStorySortModal },
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
          loaded: false
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          list: [],
          category: "fishingBlog",
          page: 0,
          district1: null,
          district2List: null,
          fishSpeciesList: null,
          sort: "createdDate",
          shipId: qp.shipId || null,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        // if (!restored) this.loadPageData();//뒤로가기를 통해 조행기목록페이지로 들어와도 데이터들을 다시 로드하여 댓글수가 바뀐게 보일수있도록.
        this.loadPageData();
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.setState({});
        PageStore.removeScrollEvent();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if ((page > 0 && PageStore.state.isEnd) || APIStore.isLoading) return;

        PageStore.setState({ page, isPending: true });

        let district2List = null;
        if (
          PageStore.state.district2List &&
          PageStore.state.district2List.length > 0
        ) {
          district2List = PageStore.state.district2List.join(",");
        }
        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get("/v2/api/fishingDiary/list/" + page, {
            category: PageStore.state.category,
            district1: PageStore.state.district1,
            district2List,
            fishSpeciesList: PageStore.state.fishSpeciesList,
            sort: PageStore.state.sort,
            shipId: PageStore.state.shipId,
          })) || {};

        if (page === 0) {
          PageStore.setState({ list: content });
          setTimeout(() => {
            // window.scrollTo(0, 0);
          }, 100);
        } else {
          PageStore.setState({ list: PageStore.state.list.concat(content) });
        }
        if (content.length < pageSize) {
          PageStore.setState({ isEnd: true });
        } else {
          PageStore.setState({ isEnd: false });
        }
        PageStore.reloadSwipe();
        this.setState({loaded: true});
      };

      onClick = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/story/user/detail/${item.id}`);
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
        const { PageStore } = this.props;
        if (selected.length === 0) {
          this.setState({ selAreaActive: false });
          PageStore.setState({ district1: null, district2List: null });
        } else {
          this.setState({ selAreaActive: true });
          let district1 = null;
          let district2List = null;
          if (selected[0]["extraValue1"] !== null) {
            district1 = selected[0]["code"];
            district2List = [];
            for(let i=1; i<selected.length; i++){
                district2List.push(selected[i].code);
            }
            // for (let item of selected) {
            //   district2List.push(item.code);
            // }
          } else district1 = selected[0]["code"];

          PageStore.setState({ district1, district2List });
        }
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
        const { PageStore } = this.props;
        this.selAreaModal.current?.onInit();
        PageStore.setState({ district1: null, district2List: null });
        this.setState({ selAreaActive: false, textArea: `지역` });
        this.loadPageData(0);
      };
      onClearFish = () => {
        const { PageStore } = this.props;
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
            <SelectMultiAreaModal
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

            <NavigationLayout title={"어복스토리"} showSearchIcon={false} />
            <StoryTab activeIndex={2} />

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

            {((!PageStore.state.list || PageStore.state.list.length < 1) && this.state.loaded) && (
              <React.Fragment>
                <p className="clearfix"></p>
                <div className="text-center">
                    <span className="mb-3"
                          style={{color: 'rgba(116,124,132,0.9)', fontWeight: 'normal'}}>
                      조건에 맞는 항목이 없습니다.
                    </span>
                </div>
              </React.Fragment>
            )}

            <a
              onClick={() => PageStore.push(`/story/add`)}
              className="add-circle"
            >
              <img
                src="/assets/cust/img/svg/icon-write-white.svg"
                alt=""
                className="add-icon"
              />
            </a>

            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
