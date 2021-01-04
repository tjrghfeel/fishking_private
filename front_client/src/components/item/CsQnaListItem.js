import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { date, questionType = "0", replied = false },
      data,
      onClick,
    }) => {
      let questionTypeName = "";
      switch (questionType) {
        case "0":
          questionTypeName = "예약결제";
          break;
        case "1":
          questionTypeName = "취소";
          break;
      }
      return (
        <>
          <div className="container nopadding mt-3">
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <div className="row no-gutters align-items-center">
                <div className="col-6 text-left">
                  <strong>{questionTypeName}</strong>
                  <br />
                  <small className="grey">
                    {date && (
                      <React.Fragment>
                        {date.substr(0, 10).replace(/[-]/g, ".")}
                      </React.Fragment>
                    )}
                  </small>
                </div>
                <div className="col-6 text-right">
                  <span
                    className={
                      "status-icon" + (replied ? " status3" : " status2")
                    }
                  >
                    {replied && "답변완료"}
                    {!replied && "답변대기"}
                  </span>
                </div>
              </div>
            </a>
          </div>
          <hr />
        </>
      );
    }
  )
);
