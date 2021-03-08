import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        name,
        fishSpecies = [],
        price = 0,
        fishingDates = [],
        startFishingDates = "",
        endFishingDates = "",
      },
    }) => {
      let endDate = null;
      if (fishingDates.length > 0) endDate = new Date(fishingDates[0]);
      if (fishingDates.length > 1) endDate = new Date(fishingDates[1]);
      return (
        <React.Fragment>
          <div className="bd-callout">
            <h6>{name}</h6>
            {fishSpecies.slice(0, 5).map((data, index) => (
              <React.Fragment>{(index > 0 ? ", " : "") + data}</React.Fragment>
            ))}
            <p>
              {Intl.NumberFormat().format(price)}원 <br />
              <span className="text-secondary">
                {startFishingDates.length > 0 && (
                  <React.Fragment>
                    {startFishingDates.substr(5, 2).concat("월")}{" "}
                    {startFishingDates.substr(8, 2).concat("일")}
                  </React.Fragment>
                )}
                {endFishingDates.length > 1 && (
                  <React.Fragment>
                    {" ~ "}
                    {endFishingDates.substr(5, 2).concat("월")}{" "}
                    {endFishingDates.substr(8, 2).concat("일")}
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
