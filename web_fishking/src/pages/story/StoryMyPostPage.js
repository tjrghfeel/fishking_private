import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, StoryMyTab, MainTab },
  VIEW: { StoryMyPostListView },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onClick = (item) => {};
      onClickProfile = (item) => {
        const { PageStore } = this.props;
        PageStore.push(`/member/profile/${item.memberId}`);
      };
      onClickComment = (item) => {};
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"내 글 관리"}
              showBackIcon={true}
              customButton={
                <React.Fragment>
                  <a
                    onClick={() => PageStore.push(`/story/add`)}
                    className="fixed-top-right text-white"
                  >
                    글쓰기
                  </a>
                </React.Fragment>
              }
            />

            <StoryMyTab activeIndex={0} />

            <StoryMyPostListView
              onClick={this.onClick}
              onClickProfile={this.onClickProfile}
              onClickComment={this.onClickComment}
            />

            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
