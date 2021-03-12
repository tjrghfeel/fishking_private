import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: { thumbnailUrl, name }, data, onClick }) => {
    return (
      <React.Fragment>
        <a onClick={() => (onClick ? onClick(data) : null)}>
          <div className="card card-sm">
            <div className="row no-gutters d-flex align-items-center">
              <div className="cardimgWrap">
                <img src={thumbnailUrl} className="img-fluid" alt="" />
                <span className="play">
                  <img src="/assets/cust/img/svg/live-play.svg" alt="" />
                </span>
                {/*<span className="play-time">20:17</span>*/}
              </div>
              <div className="cardInfoWrap">
                <div className="card-body">
                  <h6>{name}</h6>
                </div>
              </div>
            </div>
          </div>
        </a>
        <hr />
      </React.Fragment>
    );
  })
);
