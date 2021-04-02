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
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const { goodsId = null } = PageStore.getQueryParams();

        const resolve = await APIStore._get(`/v2/api/police/ride/${goodsId}`);
        this.setState(resolve);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout
              title={"승선명부"}
              showBackIcon={true}
              customButton={
                <React.Fragment>
                  <a className="fixed-top-right new">
                    <img
                      src="/assets/police/img/svg/icon-download.svg"
                      alt="다운로드"
                    />
                    <span className="sr-only">다운로드</span>
                  </a>
                </React.Fragment>
              }
            />

            <div className="container nopadding">
              <div className="card mt-3">
                <h4 className="text-center">
                  {this.state.shipName} <small className="grey">|</small>{" "}
                  <small>
                    현재{" "}
                    <strong className="large orange">
                      {Intl.NumberFormat().format(this.state.ridersCount || 0)}
                    </strong>
                    명
                  </small>
                  <br />
                  <small className="grey">{this.state.date}</small>
                </h4>
              </div>
            </div>

            <table className="table text-center">
              <thead className="thead-light">
                <tr>
                  <th scope="col">이름</th>
                  <th scope="col">성별</th>
                  <th scope="col">생년월일</th>
                  <th scope="col">지역</th>
                  <th scope="col">연락처</th>
                  <th scope="col">지문인식</th>
                </tr>
              </thead>
              <tbody>
                {this.state.riders?.map((data, index) => (
                  <tr key={index}>
                    <th scope="row">{data["name"]}</th>
                    <td></td>
                    <td>
                      {data["birthdate"].substr(2, 8).replace(/[-]/g, ".")}
                    </td>
                    <td></td>
                    <td>{data["phone"]}</td>
                    <td>{data["fingerPrint"]}</td>
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
