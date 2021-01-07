import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { createdDate, questionType, replied = false },
      data,
      onClick,
    }) => {
      return (
        <>
          <div className="container nopadding mt-3">
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <div className="row no-gutters align-items-center">
                <div className="col-6 text-left">
                  <strong>{questionType}</strong>
                  <br />
                  <small className="grey">
                    {createdDate && (
                      <React.Fragment>
                        {createdDate.substr(0, 10).replace(/[-]/g, ".")}
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
