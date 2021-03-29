import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          year: null,
          month: null,
          data: null,
          sum1: 0,
          sum2: 0,
          sum3: 0,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { APIStore, PageStore } = this.props;
        const { year, month, shipId } = PageStore.getQueryParams();
        const resolve = await APIStore._get(`/v2/api/calculate/detail`, {
          year,
          month,
          shipId,
        });
        this.setState({ year, month, data: resolve });
        if (resolve && resolve["content"]) {
          let sum1 = 0;
          let sum2 = 0;
          for (let item of resolve["content"] || []) {
            const payAmount = item["payAmount"];
            sum1 = sum1 + Math.abs(payAmount);
            if (payAmount < 0) {
              sum2 = sum2 + payAmount;
            }
          }
          this.setState({ sum1, sum2, sum3: sum1 - Math.abs(sum2) });
        }
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"정산관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={5} />

            <div className="container nopadding">
              <div className="row no-gutters">
                <div className="col-12 text-center">
                  {/*<p className="mt-3">*/}
                  {/*  <span className="status relative status2">정산대기</span>*/}
                  {/*</p>*/}
                  <h5 className="mb-2">
                    정산월: {this.state.year}-{this.state.month}
                  </h5>
                  <h3 className="mt-2">{this.state.data?.shipName}</h3>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <div className="row no-gutters">
                <div className="col-12 text-center">
                  <table className="table">
                    <thead className="thead-light">
                      <tr>
                        <th scope="col">결제일</th>
                        <th scope="col">주문자</th>
                        <th scope="col">주문상품</th>
                        <th scope="col">인원</th>
                        <th scope="col">결제금액 (원)</th>
                      </tr>
                    </thead>
                    {this.state.data?.content && (
                      <tbody>
                        {(this.state.data?.content || []).map((data, index) => (
                          <tr key={index}>
                            <th scope="row">{data["payDate"]}</th>
                            <td>{data["orderName"]}</td>
                            <td>{data["goodsName"]}</td>
                            <td>{data["personnel"]}</td>
                            <td
                              className={
                                "text-right" +
                                ((data["payAmount"] || 0) < 0 ? " red" : "")
                              }
                            >
                              {Intl.NumberFormat().format(
                                data["payAmount"] || 0
                              )}
                            </td>
                          </tr>
                        ))}
                        <tr className="table-secondary">
                          <th scope="row">예약금액</th>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td className="text-right large">
                            {Intl.NumberFormat().format(this.state.sum1)}
                          </td>
                        </tr>
                        <tr className="table-danger">
                          <th scope="row">취소금액</th>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td className="text-right large red">
                            {Intl.NumberFormat().format(this.state.sum2)}
                          </td>
                        </tr>
                        <tr className="table-primary">
                          <th scope="row">정산금액</th>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td className="text-right large">
                            <strong>
                              {Intl.NumberFormat().format(this.state.sum3)}
                            </strong>
                          </td>
                        </tr>
                      </tbody>
                    )}
                  </table>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
