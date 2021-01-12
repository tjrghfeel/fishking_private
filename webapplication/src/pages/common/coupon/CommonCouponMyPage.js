import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Item: { CouponMyItem },
    Layout: { Navigation },
  },
  Fishking: {
    Layout: { BottomTab },
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        PageStore.restoreState({
          page: 0,
          sort: "createDate",
          list: [],
          totalCount: 0,
          isEnd: false,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1, PageStore.state.sort);
        });
        this.loadPageData();
      }

      loadPageData = async (page = 0, sort = "createDate") => {
        const { APIStore, PageStore } = this.props;

        if (APIStore.isLoading || (page > 0 && PageStore.state.isEnd)) return;

        const {
          content,
          totalElements,
          pageable: { pageSize },
        } = await APIStore._get("/v2/api/usableCouponList/" + page, { sort });

        console.log(JSON.stringify(content));
        PageStore.setState({ totalCount: totalElements, page });
        if (page === 0) {
          PageStore.setState({ list: content });
        } else {
          PageStore.setState({ list: PageStore.state.list.concat(content) });
        }
        if (content.length < pageSize) {
          PageStore.setState({ isEnd: true });
        } else {
          PageStore.setState({ isEnd: false });
        }
      };

      onClickItem = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState(PageStore.state);

        console.log(JSON.stringify(item));
      };

      onCouponRegist = () => {
        const { ModalStore } = this.props;
        ModalStore.openModal("Coupon", {
          onOk: (code) => {
            console.log("coupon -> " + code);
          },
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <Navigation
              title={"쿠폰함"}
              showBackIcon={true}
              customButton={
                <React.Fragment>
                  <a
                    onClick={this.onCouponRegist}
                    className="fixed-top-right text-white"
                  >
                    쿠폰등록
                  </a>
                </React.Fragment>
              }
            />

            {/** Filter */}
            <div className="filterWrap mb-4">
              <div className="row no-gutters">
                <div className="col-4 pt-1 pl-2">
                  <small className="grey">보유쿠폰</small>
                  <strong className="red large">4</strong> 장
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
            <p className="clearfix mt-3"></p>

            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <CouponMyItem
                  key={index}
                  data={data}
                  onClick={this.onClickItem}
                />
              ))}

            <BottomTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
