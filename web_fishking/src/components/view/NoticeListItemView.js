import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ index, data: { date, title, channelType }, data, onClick }) => {
    return (
      <>
        <hr className={"full" + (index === 0 ? " mt-0" : " mt-3") + " mb-3"} />
        <a onClick={() => (onClick ? onClick(data) : null)}>
          <div className="row no-gutters align-items-center">
            <div className="col-11 pl-2">
              <strong className="text-primary">[{channelType}] </strong> {title}
              <br />
              <small className="grey">
                {date !== null && (
                  <React.Fragment>
                    {date.substr(0, 10).replace(/[-]/g, ".")}
                  </React.Fragment>
                )}
              </small>
            </div>
            <div className="col-1 text-right pl-1">
              <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
            </div>
          </div>
        </a>
      </>
    );
  })
);
