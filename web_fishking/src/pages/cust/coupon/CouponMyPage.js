import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { CouponMyListItemView },
  MODAL: { AddCouponModal },
} = Components;

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
        PageStore.restoreState({
          list: [],
          page: 0,
          sort: "createDate",
        });
        this.loadPageData();
      }

      loadPageData = async (page = 0, sort = "createDate") => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page, sort });
        const {
          content,
          totalElements,
          pageable: { pageSize = 0 },
        } = await APIStore._get("/v2/api/usableCouponList/" + page, { sort });

        console.log(JSON.stringify(content));
        PageStore.setState({ totalElements });
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

      onClick = async (item) => {
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
            <AddCouponModal
              id={"addCouponModal"}
              onOk={(code) => console.log(code)}
            />

            <NavigationLayout
              title={"쿠폰함"}
              showBackIcon={true}
              customButton={
                <React.Fragment key={1}>
                  <a
                    className="fixed-top-right text-white"
                    data-toggle="modal"
                    data-target="#addCouponModal"
                  >
                    쿠폰등록
                  </a>
                </React.Fragment>
              }
            />

            <div className="filterWrap mb-4">
              <div className="row no-gutters">
                <div className="col-4 pt-1 pl-2">
                  <small className="grey">보유쿠폰</small>
                  <strong className="red large">
                    {Intl.NumberFormat().format(PageStore.state.totalElements)}
                  </strong>{" "}
                  장
                </div>
                <div className="col-8 text-right">
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline1"
                      name="customRadioInline1"
                      className="custom-control-input"
                      defaultChecked={true}
                      onClick={() => this.loadPageData(0, "createDate")}
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline1"
                    >
                      최신순
                    </label>
                  </div>
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline2"
                      name="customRadioInline1"
                      className="custom-control-input"
                      defaultChecked={PageStore.state.sort === "popular"}
                      onClick={() => this.loadPageData(0, "popular")}
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline2"
                    >
                      인기순
                    </label>
                  </div>
                </div>
              </div>
            </div>

            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <React.Fragment>
                  <CouponMyListItemView
                    key={index}
                    data={data}
                    onClick={this.onClick}
                  />
                </React.Fragment>
              ))}
          </React.Fragment>
        );
      }
    }
  )
);
