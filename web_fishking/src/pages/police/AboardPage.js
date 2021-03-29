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
                  어복황제1호 <small className="grey">|</small>{" "}
                  <small>
                    현재 <strong className="large orange">10</strong>명
                  </small>
                  <br />
                  <small className="grey">20.10.03 (토)</small>
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
                <tr>
                  <th scope="row">홍길동</th>
                  <td>남</td>
                  <td>70.07.07</td>
                  <td>서울시 강남구</td>
                  <td>010-1234-5678</td>
                  <td>지문완료</td>
                </tr>
                <tr>
                  <th scope="row">박길동</th>
                  <td>남</td>
                  <td>70.07.07</td>
                  <td>서울시 강남구</td>
                  <td>010-1234-5678</td>
                  <td>지문완료</td>
                </tr>
                <tr>
                  <th scope="row">김길동</th>
                  <td>남</td>
                  <td>70.07.07</td>
                  <td>서울시 강남구</td>
                  <td>010-1234-5678</td>
                  <td>지문완료</td>
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
