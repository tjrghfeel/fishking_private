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
              관리자 검토 승인 후 안내 드리겠습니다.
            </p>
          </div>
        );
      }
    }
  )
);
