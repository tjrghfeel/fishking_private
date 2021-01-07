import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import MyStoryTabs from "../../components/layouts/MyStoryTabs";
import Http from "../../Http";
import MainBottomTabs from "../../components/layouts/MainBottomTabs";
import StoryScrapListItem from "../../components/item/StoryScrapListItem";

export default inject(
  "ViewStore",
  "DOMStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          isPending: false,
          isEnd: false,
          page: 0,
          list: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          saved = false,
          scroll = null,
          data = null,
        } = window.history.state;
        if (saved) {
          if (data !== null) {
            const { page, list } = data;
            await this.setState({ page, list });
          }
          if (scroll !== null) {
            const { x, y } = scroll;
            window.scrollTo(x, y);
          }
          this.props.ViewStore.clearState();
        } else {
          this.loadPageData();
        }
        window.addEventListener("scroll", this.onScrollForPage);
      }

      componentWillUnmount() {
        const {
          DOMStore: { clearScripts },
        } = this.props;
        clearScripts();
        window.removeEventListener("scroll", this.onScrollForPage);
      }

      onScrollForPage = async () => {
        const scrollHeight =
          document.scrollingElement.scrollHeight - window.outerHeight;
        const itemHeight = 200;
        const scrollPosition = window.pageYOffset;

        if (scrollPosition + itemHeight >= scrollHeight) {
          await this.loadPageData(this.state.page + 1, this.state.sort);
        }
      };

      loadPageData = async (page = 0) => {
        if (page > 0 && (this.state.isPending || this.state.isEnd)) return;
        else if (page === 0) await this.setState({ isEnd: false });

        await this.setState({ isPending: true, page });
        let {
          content,
          pageable: { pageSize },
        } = await Http._get("/v2/api/myFishingDiaryScrap/" + page);

        if (page === 0) {
          await this.setState({ list: content });
        } else {
          await this.setState({ list: this.state.list.concat(content) });
        }

        if (content.length < pageSize) {
          await this.setState({ isEnd: true });
        } else {
          await this.setState({ isEnd: false });
        }

        await this.setState({ isPending: false });

        this.props.DOMStore.clearScripts();
        this.props.DOMStore.applyCarouselSwipe();
      };

      onClick = async (item) => {
        const { page, list } = this.state;
        const {
          history,
          ViewStore: { saveState },
        } = this.props;
        await saveState({ page, list });

        history.push(`/story/detail/` + item.id);
      };
      onClickProfile = async (item) => {
        const { page, list } = this.state;
        const {
          history,
          ViewStore: { saveState },
        } = this.props;
        await saveState({ page, list });

        history.push(`/common/profile/` + item.memberId);
      };
      onClickComment = async (item) => {
        const { page, list } = this.state;
        const {
          history,
          ViewStore: { saveState },
        } = this.props;
        await saveState({ page, list });

        history.push(`/story/comment/` + item.id);
      };
      onClickLike = async () => {
        // TODO : 내 글 관리 > 스크랩 : 좋아요 토글 요청
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation
              title={"내 글 관리"}
              showBack={true}
              customButton={
                <React.Fragment key={1}>
                  <a
                    onClick={() => history.push(`/story/add`)}
                    className="fixed-top-right text-white"
                  >
                    글쓰기
                  </a>
                </React.Fragment>
              }
            />

            {/** 탭메뉴 */}
            <MyStoryTabs />

            {/** 리스트 > 데이터 없음 */}
            {this.state.list.length === 0 && (
              <div className="container nopadding mt-3 mb-0 text-center">
                <p className="mt-5 mb-3">
                  <img
                    src="/assets/img/svg/icon-scrap-no.svg"
                    alt=""
                    className="icon-lg"
                  />
                </p>
                <h6>내가 스크랩한 게시글이 없습니다.</h6>
                <p className="mt-3">
                  <small className="grey">
                    낚시인들의 소중한 경험을 저장해 보세요.
                  </small>
                </p>
                <p className="mt-5">
                  <a
                    onClick={() => history.push(`/main/story`)}
                    className="btn btn-primary btn-round"
                  >
                    어복스토리 보러 가기
                  </a>
                </p>
              </div>
            )}

            {/** 리스트 > 데이터 있음 */}
            {this.state.list.length > 0 && (
              <div className="container nopadding">
                {this.state.list.map((data, index) => (
                  <StoryScrapListItem
                    key={index}
                    data={data}
                    onClick={this.onClick}
                    onClickProfile={this.onClickProfile}
                    onClickLike={this.onClickLike}
                    onClickComment={this.onClickComment}
                  />
                ))}
              </div>
            )}

            {/** 하단탭 */}
            <MainBottomTabs activedIndex={3} />
          </>
        );
      }
    }
  )
);
