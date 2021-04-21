import React from "react";
import { inject, observer } from "mobx-react";
import FaqListItemView from "./FaqListItemView";

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
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore, role } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get("/v2/api/faq/" + page, { role })) || {};

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
                    <FaqListItemView
                      key={index}
                      data={data}
                      expend={index === 0}
                    />
                  ))}
                </div>
              </div>
            )}
            <div style={{marginTop: 30}}>
              <p style={{textAlign:"center", height:130}} className="space mt-1">
                <br/>(주)투비<br/>
                대표 조옥수 | 대표번호 1566-2996<br/>
                전남 목포시 연산백련로 1번길 8-11<br/>
                사업자 등록번호 : 337-87-02139<br/>
                통신판매업 신고번호 : 제2021-전남목포-0022호<br/>
                개인정보담당자 : help@to-be.kr
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
