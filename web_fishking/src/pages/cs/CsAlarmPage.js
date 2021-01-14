import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import PageStore from "../../stores/PageStore";
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
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get("/v2/api/alert/alertList");

        console.log("hi");
        console.log(JSON.stringify(content));
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
                {PageStore.state.list.map((data, index) => (
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
