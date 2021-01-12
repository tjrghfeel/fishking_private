import React from "react";
import { inject, observer } from "mobx-react";
import FaqItem from "../item/FaqItem";

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
        const { APIStore, PageStore, role } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get("/v2/api/faq/" + page, { role });

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

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {PageStore.state.list && (
              <div className="container nopadding">
                <div className="accordion" id="accordionFaq">
                  {PageStore.state.list.map((data, index) => (
                    <FaqItem key={index} data={data} expend={index === 0} />
                  ))}
                </div>
                <p className="space mt-1"></p>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
