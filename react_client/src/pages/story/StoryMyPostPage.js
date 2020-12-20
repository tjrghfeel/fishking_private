/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import MyStoryTabs from "../../components/layout/MyStoryTabs";
import ScrollList02 from "../../components/list/ScrollList02";

export default inject("MyMenuStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          page: 0,
          isPending: false,
          isEnd: false,
          list: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        if (page !== 0 && (this.state.isPending || this.state.isEnd)) return;
        else if (page === 0) {
          await this.setState({ isEnd: false });
        }

        await this.setState({ isPending: true });
        const {
          MyMenuStore: { REST_GET_myFishingPostList },
        } = this.props;
        const {
          content,
          pageable: { pageSize },
        } = await REST_GET_myFishingPostList(page);

        if (content.length < pageSize) {
          await this.setState({ isEnd: true });
        }

        if (page === 0) {
          await this.setState({ list: content, isPending: false });
        } else {
          await this.setState({
            list: this.state.list.concat(content),
            isPending: false,
          });
        }
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation
              title={"내 글 관리"}
              visibleBackIcon={true}
              customButton={
                <a
                  key={1}
                  onClick={() => history.push(`/story/add`)}
                  className="fixed-top-right text-white"
                >
                  글쓰기
                </a>
              }
            />

            {/** 탭메뉴 */}
            <MyStoryTabs />

            {/** List */}
            <ScrollList02 list={this.state.list} />
          </>
        );
      }
    }
  )
);
