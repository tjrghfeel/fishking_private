/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import AddCouponModal from "../../components/modal/AddCouponModal";
import Clearfix from "../../components/layout/Clearfix";
import ListItem13 from "../../components/list/ListItem13";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          sortDirection: "1",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        // --> 예제 데이터
        const list = [
          {
            title: "시즌 얼리버드 쿠폰",
            discount: 2000,
            useStartDate: "2020.08.01",
            useEndDate: "2020.08.31",
            description: "익월 상품 및 50,000원 이상 구매시 사용가능",
            isUsed: true,
          },
          {
            title: "시즌 얼리버드 쿠폰",
            discount: 2000,
            useStartDate: "2020.08.01",
            useEndDate: "2020.08.31",
            description: "익월 상품 및 50,000원 이상 구매시 사용가능",
            isUsed: false,
          },
        ];
        return (
          <>
            {/** Navigation */}
            <Navigation
              title={"쿠폰함"}
              visibleBackIcon={true}
              customButton={
                <React.Fragment key={1}>
                  <a
                    className="fixed-top-right text-white"
                    data-toggle="modal"
                    data-target="#addModal"
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
                  <strong className="red large">{list.length}</strong> 장
                </div>
                <div className="col-8 text-right">
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline1"
                      name="customRadioInline1"
                      className="custom-control-input"
                      checked={this.state.sortDirection === "1"}
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ sortDirection: "1" });
                      }}
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
                      checked={this.state.sortDirection === "2"}
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ sortDirection: "2" });
                      }}
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
            <Clearfix cls={"mt-3"} />

            {/** 리스트 */}
            {list &&
              list.map((data, index) => <ListItem13 key={index} {...data} />)}

            {/** 모달 팝업 */}
            <AddCouponModal
              id={"addModal"}
              onAdded={(code) => console.log(code)}
            />
          </>
        );
      }
    }
  )
);
