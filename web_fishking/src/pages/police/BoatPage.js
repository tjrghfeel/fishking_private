import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
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
        this.init();
      }
      init = () => {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          page: 0,
          list: [],
          isPending: false,
          isEnd: false,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData(0);
      };
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;
        const { isPending, isEnd } = PageStore.state;

        if (page > 0 && (isPending || isEnd)) return;

        PageStore.setState({ page });
        const resolve =
          (await APIStore._get(`/v2/api/police/ships/${page}`)) || {};
        const { content = [], pageable: { pageSize = 0 } = {} } = resolve;

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
        PageStore.setState({ isPending: false });
      };
      onClickCCTV = (item) => {
        console.log(JSON.stringify(item));
      };
      onClickRide = (item) => {
        console.log(JSON.stringify(item));
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/aboard?goodsId=${item["goodsId"]}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"선상정보"} showBackIcon={true} />

            <table className="table text-center">
              <thead className="thead-light">
                <tr>
                  <th scope="col">선상명</th>
                  <th scope="col">승선인원/탑승정원</th>
                  <th scope="col">현재상태</th>
                  <th scope="col">CCTV</th>
                  <th scope="col">승선명부</th>
                </tr>
              </thead>
              <tbody>
                {PageStore.state?.list?.map((data, index) => (
                  <tr key={index}>
                    <th scope="row">{data["shipName"]}</th>
                    <td>
                      <strong className="large text-primary">
                        {Intl.NumberFormat().format(data["ridePersonnel"] || 0)}
                      </strong>
                      /
                      <strong className="large">
                        {Intl.NumberFormat().format(data["maxPersonnel"] || 0)}
                      </strong>
                    </td>
                    <td>
                      <strong className="large text-primary">
                        {data["status"]}
                      </strong>
                    </td>
                    <td>
                      <a
                        className="btn btn btn-round-grey btn-xs-round"
                        onClick={() => this.onClickCCTV(data)}
                      >
                        보기
                      </a>
                    </td>
                    <td>
                      <a
                        className="btn btn btn-round-grey btn-xs-round"
                        onClick={() => this.onClickRide(data)}
                      >
                        확인
                      </a>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            <PoliceMainTab activeIndex={2} />
          </React.Fragment>
        );
      }
    }
  )
);
