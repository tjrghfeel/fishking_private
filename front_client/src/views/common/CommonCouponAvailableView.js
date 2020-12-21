import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import CommonCouponAvailableListItem from "../../components/item/CommonCouponAvailableListItem";
import Http from "../../Http";

export default inject()(
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
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        this.loadPageData();
        window.addEventListener("scroll", this.onScrollForPage);
      }

      componentWillUnmount() {
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
        const {
          content,
          pageable: { pageSize },
        } = await Http._get("/v2/api/downloadableCouponList/" + page);

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
      };

      downloadCoupon = async (item) => {
        const resolve = await Http._post("/v2/api/downloadCoupon", {
          couponId: item.id,
        });

        if (resolve) {
          this.loadPageData();
        }
      };

      downloadAllCoupon = async () => {
        for (let item of this.state.list) {
          await Http._post("/v2/api/downloadCoupon", { couponId: item.id });
        }
        this.loadPageData();
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"어복황제는 지금 할인중!"} showBack={true} />

            {/** 리스트 */}
            <div className="container nopadding bg-grey">
              {this.state.list.map((data, index) => (
                <CommonCouponAvailableListItem
                  key={index}
                  data={data}
                  index={index}
                  onClick={this.downloadCoupon}
                />
              ))}
            </div>

            {/** 하단버튼 */}
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
          </>
        );
      }
    }
  )
);
