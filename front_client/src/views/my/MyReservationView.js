import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import MainBottomTabs from "../../components/layouts/MainBottomTabs";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          options: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          DataStore: { getEnums },
        } = this.props;
        const options = await getEnums("orderStatus");
        this.setState({ options });
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        // TODO : [PUB-NO/API-NO] 예약내역 : 예약내역 리스트 API 개발 필요
        return (
          <>
            {/** Navigation */}
            <Navigation title={"예약내역"} showBack={true} showSearch={true} />

            <div className="filterWrap">
              <div className="slideList">
                <form className="form-inline">
                  <select className="form-control">
                    <option value={""}>전체</option>
                    {this.state.options.map((data, index) => (
                      <option key={index} value={data.key}>
                        {data.value}
                      </option>
                    ))}
                  </select>
                </form>
              </div>
            </div>

            {/** 메인탭 */}
            <MainBottomTabs activedIndex={4} />
          </>
        );
      }
    }
  )
);
