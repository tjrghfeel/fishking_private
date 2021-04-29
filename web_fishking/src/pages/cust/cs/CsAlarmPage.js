import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import PageStore from "../../../stores/PageStore";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { AlarmListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          alertType: null
        }
      }

      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const qp = PageStore.getQueryParams();
        this.setState({ alertType: qp.alertType })
        this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;
        console.log(this.state.alertType)
        PageStore.setState({ page });
        let url = '/v2/api/alert/alertList'
        if (this.state.type !== null) {
          url = '/v2/api/alert/alertList?type=f'
        }
        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get(url);

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

      onDelete = async (item) => {
        const { APIStore, DataStore, PageStore } = this.props;
        const resolve = await APIStore._delete("/v2/api/alert", {
          alertId: item.alertId,
        });
        if (resolve) {
          const list = DataStore.removeItemOfArrayByKey(
            PageStore.state.list,
            "alertId",
            item.alertId
          );
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
            <NavigationLayout title={"알림"} showBackIcon={true} />

            {PageStore.state.list && (
              <div className="container nopadding bg-grey">
                {(PageStore.state.list.length===0)?
                    <div className="container nopadding mt-3 mb-0 text-center">
                        <p className="mt-5 mb-3">
                            <img
                                src="/assets/cust/img/svg/icon-scrap-no.svg"
                                alt=""
                                className="icon-lg"
                            />
                        </p>
                        <h6>확인하실 알림이 없습니다.</h6>
                    </div>
                    :
                    PageStore.state.list.map((data, index) => (
                  <AlarmListItemView
                    key={index}
                    data={data}
                    beforeData={
                      index === 0 ? null : PageStore.state.list[index - 1]
                    }
                    onDelete={this.onDelete}
                  />
                ))}
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
