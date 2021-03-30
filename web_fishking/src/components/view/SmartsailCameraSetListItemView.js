import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: { image, name }, data, onClick }) => {
    return (
      <div className="container nopadding mt-3 card card-sm">
        <div className="row no-gutters mt-3 mb-2">
          <div className="col-4 pl-3">
            <div className="cardimgWrap">
              <img src={image} className="img-fluid" alt="" />
            </div>
          </div>
          <div className="col-5">
            <h6>{name}</h6>
            {/*<p>*/}
            {/*  <span className="grey">*/}
            {/*    조회수 4,321*/}
            {/*    <br />*/}
            {/*    27명 시청중*/}
            {/*  </span>*/}
            {/*</p>*/}
          </div>
          <div className="col-3 text-center">
            <nav>
              <div
                className="nav nav-tabs btn-set mt-3 mr-3 vam"
                id="nav-tab"
                role="tablist"
              >
                <a
                  className="nav-link active btn btn-on"
                  id="nav-home-tab"
                  data-toggle="tab"
                  role="tab"
                  aria-controls="nav-on"
                  aria-selected="true"
                  onClick={() => (onClick ? onClick(data, true) : null)}
                >
                  ON
                </a>
                <a
                  className="nav-link btn btn-off"
                  id="nav-profile-tab"
                  data-toggle="tab"
                  role="tab"
                  aria-controls="nav-off"
                  aria-selected="false"
                  onClick={() => (onClick ? onClick(data, false) : null)}
                >
                  OFF
                </a>
              </div>
            </nav>
          </div>
        </div>
        <hr className="full mt-2 mb-3" />
      </div>
    );
  })
);
