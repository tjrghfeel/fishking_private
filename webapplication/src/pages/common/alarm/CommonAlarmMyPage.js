import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import PageStore from "../../../stores/PageStore";
const {
  Common: {
    Item: { AlarmItem },
    Layout: { Navigation },
  },
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
          page: 0,
          list: [],
        });
        this.loadPageData();
      }
      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const { content } = await APIStore._get("/v2/api/alert/alertList");

        PageStore.setState({ list: content });
      };
      requestDelete = async (item) => {
        const { APIStore, DataStore } = this.props;
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
            <Navigation title={"알림"} showBackIcon={true} />

            <div className="container nopadding bg-grey">
              {PageStore.state.list &&
                PageStore.state.list.map((data, index) => (
                  <AlarmItem
                    key={index}
                    data={data}
                    beforeData={
                      index === 0 ? null : PageStore.state.list[index - 1]
                    }
                    onDelete={this.requestDelete}
                  />
                ))}
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
