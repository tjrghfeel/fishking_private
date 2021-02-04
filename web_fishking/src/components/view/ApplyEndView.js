import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
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
          <div className="container nopadding mt-5">
            <p className="text-center">
              업체 등록 요청이 완료되었습니다.
              <br />
              관리자 승인 후 스마트승선 앱 사용이 가능한 <br />
              아이디/비번을 발급해 드립니다.
              <br />
              <br />
              스마트승선 앱을 설치하여 사용해 주세요.
              <br />
              <br />
              <img
                src="/cust/assets/img/app-icon-smartboat.png"
                alt=""
                className="icon-xlg mt-3"
              />
              <br />
              <a className="btn btn-round-grey mt-3">스마트승선 설치하기</a>
              <br />
              <a className="btn btn-round-grey mt-2">
                스마트승선 이용안내 보러가기
              </a>
              <br />
            </p>
          </div>
        );
      }
    }
  )
);
