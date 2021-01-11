import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";
import MyAlarmListItem from "../../components/item/MyAlarmListItem";
import Http from "../../Http";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          isPending: false,
          isEnd: false,
          page: 0,
          list: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        if (page > 0 && (this.state.isPending || this.state.isEnd)) return;
        else if (page === 0) await this.setState({ isEnd: false });

        await this.setState({ isPending: true, page });
        const {
          content,
          pageable: { pageSize },
        } = await Http._get("/v2/api/alert/alertList");

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
      onDelete = async (item) => {
        const resolve = await Http._delete("/v2/api/alert", {
          alertId: item.alertId,
        });
        if (resolve) {
          const {
            DataStore: { removeItemOfArray },
          } = this.props;
          const list = removeItemOfArray(
            this.state.list,
            "alertId",
            item.alertId
          );
          await this.setState({ list });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        // TODO : [PUB-NO/API-NO] 내 알림 : 알림 리스트 API 개발 필요
        return (
          <>
            {/** Navigation */}
            <Navigation title={"알림"} showBack={true} />

            {/** 데이터 */}
            <div className="container nopadding bg-grey">
              {this.state.list.map((data, index) => (
                <MyAlarmListItem
                  key={index}
                  data={data}
                  beforeData={index === 0 ? null : this.state.list[index - 1]}
                  onDelete={this.onDelete}
                />
              ))}
            </div>
          </>
        );
      }
    }
  )
);
