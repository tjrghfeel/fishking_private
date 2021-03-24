import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
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
            <NavigationLayout title={"정산 계좌 설정"} showBackIcon={true} />

            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-12 pl-2">정산 계좌 정보를 입력합니다.</div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <select className="form-control">
                      <option>은행선택</option>
                      <option>국민은행</option>
                      <option>하나은행</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      id="inputPassword"
                      placeholder="계좌번호를 입력해 주세요."
                      value=""
                    />
                  </div>
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      id="inputPasswordC"
                      placeholder="예금주 명을 입력해 주세요."
                      value=""
                    />
                  </div>
                </form>
              </div>
              <a className="btn btn-primary btn-lg btn-block">확인</a>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
