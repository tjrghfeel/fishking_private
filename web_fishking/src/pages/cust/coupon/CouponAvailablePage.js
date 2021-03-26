import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import PageStore from "../../../stores/PageStore";
import DataStore from "../../../stores/DataStore";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { CouponAvailableListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
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
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
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
        } = await APIStore._get("/v2/api/downloadableCouponList/" + page);

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

      downloadCoupon = async (item) => {
        const { APIStore, PageStore, DataStore } = this.props;
        const resolve = await APIStore._post("/v2/api/downloadCoupon", {
          couponId: item.id,
        });

        if (resolve) {
          const list = DataStore.updateItemOfArrayByKey(
            PageStore.state.list,
            "id",
            item.id,
            {
              isUsable: false,
            }
          );
          PageStore.setState({ list });
        }
      };

      downloadAllCoupon = async () => {
        const { APIStore } = this.props;
        const resolve = await APIStore._post("/v2/api/downloadAllCoupon");

        if (resolve) {
          const list = [];
          for (let item of PageStore.state.list) {
            item.isUsable = false;
            list.push(item);
          }
          PageStore.setState({ list });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"어복황제는 지금 할인중!"}
              showBackIcon={true}
            />

            <div className="container nopadding bg-grey">
              {PageStore.state.list &&
                PageStore.state.list.map((data, index) => (
                  <React.Fragment>
                    <CouponAvailableListItemView
                      key={index}
                      data={data}
                      onClick={this.downloadCoupon}
                    />
                  </React.Fragment>
                ))}
            </div>

            {PageStore.state.list && PageStore.state.list.length > 0 && (
              <div className="fixed-bottom">
                <div className="row no-gutters">
                  <div className="col-12">
                    <a
                      onClick={this.downloadAllCoupon}
                      className="btn btn-primary btn-lg btn-block"
                    >
                      전체 다운받기
                    </a>
                  </div>
                </div>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
