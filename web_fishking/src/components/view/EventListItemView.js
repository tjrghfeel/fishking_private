import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({ data: { imageUrl, eventTitle, startDay, endDay }, data, onClick }) => {
      return (
        <React.Fragment>
          <div className="container nopadding">
            <div className="row no-gutters mt-3">
              <div className="col-12">
                <a
                  onClick={() => (onClick ? onClick(data) : null)}
                  className="event"
                >
                  <img src={imageUrl} alt="" className="img-fluid rounded" />
                  <h5 className="mb-0">{eventTitle}</h5>
                  <small className="grey">
                    {startDay} ~ {endDay}
                  </small>
                </a>
              </div>
            </div>
            <p className="space mt-3 mb-3"></p>
          </div>
        </React.Fragment>
      );
    }
  )
);
