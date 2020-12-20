import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import ZzimMyTabs from "../../components/layout/ZzimMyTabs";
import ScrollList01 from "../../components/list/ScrollList01";

export default inject("TakeStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.fishingType = "1";
        this.state = {
          page: 1,
          isPending: false,
          isEnd: false,
          list: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async (page = 1) => {
        if (page !== 1 && (this.state.isPending || this.state.isEnd)) return;
        else if (page === 1) {
          await this.setState({ isEnd: false });
        }

        await this.setState({ isPending: true });
        const {
          TakeStore: { REST_GET_take_fishingType },
        } = this.props;
        const {
          content,
          pageable: { pageSize },
        } = await REST_GET_take_fishingType(this.fishingType, page);

        console.log(JSON.stringify(content));

        if (content.length < pageSize) {
          await this.setState({ isEnd: true });
        }

        if (page === 1) {
          await this.setState({ list: content, isPending: false });
        } else {
          await this.setState({
            list: this.state.list.concat(content),
            isPending: false,
          });
        }
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"찜한 업체"} visibleBackIcon={true} />

            {/** 탭메뉴 */}
            <ZzimMyTabs />

            {/** 업체 */}
            <div className="container nopadding mt-3 mb-0">
              <ScrollList01
                title={"일반"}
                titleCls={"mb-3"}
                itemType={"ListItem07"}
                onClose={(item) => console.log(JSON.stringify(item))}
                list={[
                  {
                    imgSrc: "/assets/img/sample/boat1.jpg",
                    isLive: true,
                    fishList: ["광어", "쭈꾸미", "우럭"],
                    fishType: "f01",
                    fishingType: "선상",
                    fishCount: 13,
                    title: "어복황제1호",
                    location: "전남 진도군",
                    distance: 27,
                    isRealtime: true,
                    price: 40000,
                  },
                  {
                    imgSrc: "/assets/img/sample/boat1.jpg",
                    playTime: "20:17",
                    fishList: ["광어", "쭈꾸미", "우럭"],
                    fishType: "f02",
                    fishCount: 13,
                    title: "어복황제1호",
                    location: "전남 진도군",
                    distance: 27,
                    isRealtime: true,
                    price: 40000,
                  },
                ]}
              />
            </div>
          </>
        );
      }
    }
  )
);
