import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

import ScrollList04 from "../../components/list/ScrollList04";

export default inject("CouponStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          isLoading: false,
          isEnd: false,
          page: 1,
          list: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async (iPage = 1) => {
        const { isLoading, isEnd, page, list } = this.state;

        if (isLoading || (iPage !== 1 && isEnd)) return;

        const {
          CouponStore: { REST_GET_downloadableCouponList },
        } = this.props;

        await this.setState({ page: iPage });
        const { content } = await REST_GET_downloadableCouponList(iPage);
        if (page === 1) {
          await this.setState({ list: content });
        } else {
          await this.setState({ list: list.concat(content) });
        }

        if (content.length === 0) await this.setState({ isEnd: true });
      };

      downloadCoupon = async (item) => {
        // TODO : 쿠폰 다운로드 요청
        console.log(JSON.stringify(item));
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation
              title={"어복황제는 지금 할인중!"}
              visibleBackIcon={true}
            />

            {/** 리스트 */}
            <ScrollList04
              itemType={"ListItem12"}
              onClick={this.downloadCoupon}
              list={[
                {
                  couponType: "discount",
                  couponName: "낚시갈 땐 그냥드림",
                  discount: 2000,
                  couponDescription: "40,000원 이상 구매시 사용가능",
                },
                {
                  couponType: "discount",
                  couponName: "낚시갈 땐 그냥드림",
                  discount: 2000,
                  couponDescription: "40,000원 이상 구매시 사용가능",
                  disabled: true,
                },
              ]}
            />
          </>
        );
      }
    }
  )
);
