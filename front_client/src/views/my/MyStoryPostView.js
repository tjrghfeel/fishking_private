import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import MyStoryTabs from "../../components/layouts/MyStoryTabs";
import StoryPostListItem from "../../components/item/StoryPostListItem";
import Http from "../../Http";
import MainBottomTabs from "../../components/layouts/MainBottomTabs";

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
        } = await Http._get("/v2/api/myFishingPostList/" + page);

        // TODO : [PUB-OK/API-NO] 내글관리 > 게시글 목록 조회 : 데이터 항목이 필요합니다. (등록시간:n분전)

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
      onClickComment = () => {
        // TODO : [NO-FILE] 댓글 쓰기 이동
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

            {/** List */}
            {this.state.list.map((data, index) => (
              <StoryPostListItem
                key={index}
                data={data}
                onClick={(data) => this.onClick(data)}
                onClickProfile={(data) => this.onClickProfile(data)}
                // onClickLike={(data) => this.onClickLike(data)}
                onClickComment={(data) => this.onClickComment(data)}
                // onClickScrap={(data) => this.onClickScrap(data)}
              />
            ))}

            {/** 하단탭 */}
            <MainBottomTabs activedIndex={3} />
          </>
        );
      }
    }
  )
);
