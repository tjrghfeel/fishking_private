import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
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
                <tr>
                  <th scope="row">어복호</th>
                  <td>
                    <strong className="large text-primary">21</strong>/
                    <strong className="large">24</strong>
                  </td>
                  <td>
                    <strong className="large text-primary">출항 중</strong>
                  </td>
                  <td>
                    <a className="btn btn btn-round-grey btn-xs-round">보기</a>
                  </td>
                  <td>
                    <a className="btn btn btn-round-grey btn-xs-round">확인</a>
                  </td>
                </tr>
                <tr>
                  <th scope="row">어복호</th>
                  <td>
                    <strong className="large text-primary">21</strong>/
                    <strong className="large">24</strong>
                  </td>
                  <td>
                    <strong className="large text-primary">출항 중</strong>
                  </td>
                  <td>
                    <a className="btn btn btn-round-grey btn-xs-round">보기</a>
                  </td>
                  <td>
                    <a className="btn btn btn-round-grey btn-xs-round">확인</a>
                  </td>
                </tr>
                <tr>
                  <th scope="row">어복호</th>
                  <td>
                    <strong className="large text-primary">21</strong>/
                    <strong className="large">24</strong>
                  </td>
                  <td>
                    <strong className="large">출항 전</strong>
                  </td>
                  <td>
                    <a className="btn btn btn-round-grey btn-xs-round">보기</a>
                  </td>
                  <td>
                    <a className="btn btn btn-round-grey btn-xs-round">확인</a>
                  </td>
                </tr>
              </tbody>
            </table>

            <PoliceMainTab activeIndex={2} />
          </React.Fragment>
        );
      }
    }
  )
);
