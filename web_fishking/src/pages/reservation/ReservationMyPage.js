import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import DataStore from "../../stores/DataStore";
const {
  LAYOUT: { NavigationLayout, MainTab },
  VIEW: { ReservationMyListItemView },
} = Components;

export default inject(
  "PageStore",
  "DataStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          page: 0,
          list: [],
          isEnd: false,
          sort: "",
          options: [],
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1, PageStore.state.sort);
        });
        const options = await DataStore.getEnums("orderStatus");
        PageStore.setState({ options });
        if (!restored) this.loadPageData();
      }

      loadPageData = async (page = 0, sort = "") => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page, sort });
        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get("/v2/api/myOrdersList/" + page, { sort });

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
        PageStore.push(`/reservation/my/detail/${item.id}`);
      };
      onClickFind = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        console.log(JSON.stringify(item));
      };
      onClickAgain = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        console.log(JSON.stringify(item));
      };
      onClickReview = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        console.log(JSON.stringify(item));
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"예약내역"}
              showBackIcon={true}
              showSearchIcon={true}
            />

            {/** Filter */}
            <div className="filterWrap">
              <div className="slideList">
                <form className="form-inline">
                  <select
                    className="form-control"
                    onChange={(e) => this.loadPageData(0, e.target.value)}
                  >
                    <option value="" selected>
                      전체
                    </option>
                    {PageStore.state.options &&
                      PageStore.state.options.map((data, index) => (
                        <option key={index} value={data.key}>
                          {data.value}
                        </option>
                      ))}
                  </select>
                </form>
              </div>
            </div>

            {/** 안내 */}
            <div className="container nopadding mt-2">
              <div className="card-round-box-grey">
                <nav className="nav nav-pills nav-sel nav-my nav-arrow nav-col-3">
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/img/svg/reserv1.svg" alt="" />
                    </figure>
                    <span>결제완료</span>
                  </a>
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/img/svg/reserv2.svg" alt="" />
                    </figure>
                    <span>알림발송</span>
                  </a>
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/img/svg/reserv3.svg" alt="" />
                    </figure>
                    <span>예약확정</span>
                  </a>
                </nav>
                <hr className="mt-0 mb-3" />
                <p className="text-center">
                  <small className="grey">
                    예약대기 중에 예약 취소 시{" "}
                    <span className="red">취소 및 환불규정</span>에 따라
                    적용됩니다.
                  </small>
                </p>
              </div>
            </div>

            {/** list */}
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <ReservationMyListItemView
                  key={index}
                  data={data}
                  onClick={this.onClick}
                  onClickFind={this.onClickFind}
                  onClickAgain={this.onClickAgain}
                  onClickReview={this.onClickReview}
                />
              ))}
            <p className="clearfix">
              <br />
            </p>

            <MainTab activeIndex={4} />
          </React.Fragment>
        );
      }
    }
  )
);