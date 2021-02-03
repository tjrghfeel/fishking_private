import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, StoryMyTab, MainTab },
  VIEW: { StoryMyReviewListView },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

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
                    onClick={() => PageStore.push(`/cust/story/add`)}
                    className="fixed-top-right text-white"
                  >
                    글쓰기
                  </a>
                </React.Fragment>
              }
            />

            <StoryMyTab activeIndex={3} />

            <StoryMyReviewListView addPathname={`/story/add`} />

            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
