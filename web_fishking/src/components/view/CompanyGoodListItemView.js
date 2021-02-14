import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { fishSpecies = [], price = 0, fishingDates = [] },
      data,
      onClick,
    }) => {
      let endDate = null;
      if (fishingDates.length > 0) endDate = new Date(fishingDates[0]);
      if (fishingDates.length > 1) endDate = new Date(fishingDates[1]);
      return (
        <React.Fragment>
          <div className="bd-callout">
            <a
              onClick={() => (onClick ? onClick(data) : null)}
              className="btn btn-icon float-top-right mt-2"
              data-toggle="modal"
              data-target="#goodsModal"
            >
              <img src="/assets/cust/img/svg/form-search.svg" alt="" />
            </a>
            <h6>{fishSpecies.length > 0 && fishSpecies[0]}</h6>
            <p>
              {Intl.NumberFormat().format(price)}원 <br />
              <span className="text-secondary">
                {fishingDates.length > 0 && (
                  <React.Fragment>
                    {fishingDates[0].substr(5, 2).concat("월")}{" "}
                    {fishingDates[0].substr(8, 2).concat("일")}
                  </React.Fragment>
                )}
                {fishingDates.length > 1 && (
                  <React.Fragment>
                    {" ~ "}
                    {fishingDates[1].substr(5, 2).concat("월")}{" "}
                    {fishingDates[1].substr(8, 2).concat("일")}
                  </React.Fragment>
                )}{" "}
                {endDate !== null && endDate > new Date() && "지금 예약 가능"}
              </span>
            </p>
          </div>
          <hr />
        </React.Fragment>
      );
    }
  )
);
