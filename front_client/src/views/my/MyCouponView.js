import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";
import MyCouponListItem from "../../components/item/MyCouponListItem";
import Http from "../../Http";
import AddCouponModal from "../../components/modals/AddCouponModal";

export default inject("ViewStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          isPending: false,
          isEnd: false,
          page: 0,
          sort: "createDate",
          list: [],
          totalElements: 0,
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          saved = false,
          scroll = null,
          data = null,
        } = window.history.state;
        if (saved) {
          if (data !== null) {
            const { page, sort, list, totalElements } = data;
            await this.setState({ page, sort, list, totalElements });
          }
          if (scroll !== null) {
            const { x, y } = scroll;
            window.scrollTo(x, y);
          }
          this.props.ViewStore.clearState();
        } else {
          this.loadPageData();
        }
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

      loadPageData = async (page = 0, sort = "createDate") => {
        if (page > 0 && (this.state.isPending || this.state.isEnd)) return;
        else if (page === 0) await this.setState({ isEnd: false });

        await this.setState({ isPending: true, page, sort });
        const {
          content,
          totalElements,
          pageable: { pageSize },
        } = await Http._get("/v2/api/usableCouponList/" + page, { sort });

        await this.setState({ totalElements });
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

      onClickCoupon = async (item) => {
        const { page, sort, list, totalElements } = this.state;
        const {
          history,
          ViewStore: { saveState },
        } = this.props;
        await saveState({ page, sort, list, totalElements });

        // TODO : [NO-FILE] 내 쿠폰 : 바로예약 네비게이션 연결
        console.log(JSON.stringify(item));
      };

      addCoupon = async (code) => {
        // TODO : [PUB-OK/API-NO] 내 쿠폰 : 쿠폰 등록 요청
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation
              title={"쿠폰함"}
              showBack={true}
              customButton={
                <React.Fragment key={1}>
                  <a
                    href="#none"
                    className="fixed-top-right text-white"
                    data-toggle="modal"
                    data-target="#addCouponModal"
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
                  <strong className="red large">
                    {Intl.NumberFormat().format(this.state.totalElements)}
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
                      defaultChecked={this.state.sort === "createDate"}
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
                      defaultChecked={this.state.sort === "popular"}
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

            {/** 리스트 */}
            {this.state.list.map((data, index) => (
              <MyCouponListItem
                key={index}
                data={data}
                onClick={this.onClickCoupon}
              />
            ))}

            {/** 모달 팝업 */}
            <AddCouponModal id={"addCouponModal"} onOk={this.addCoupon} />
          </>
        );
      }
    }
  )
);
