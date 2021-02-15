import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        name = "",
        fishSpecies = [],
        minPersonnel = 0,
        maxPersonnel = 0,
        reservationPersonal = 0,
        startTime = "",
        endTime = "",
        price = 0,
      },
      data,
      onChange,
    }) => {
      return (
        <div className="container nopadding mt-2">
          <div className="row no-gutters">
            <div className="col-2 text-center">
              <label className="control checkbox">
                <input
                  type="radio"
                  name="reservation-goods-radio"
                  className="add-contrast"
                  data-role="collar"
                  onChange={(e) =>
                    onChange ? onChange(e.target.checked, data) : null
                  }
                />
                <span className="control-indicator"></span> <small></small>
              </label>
            </div>
            <div className="col-7">
              <h6>{name}</h6>
              <span className="tag">
                남은수{" "}
                {Intl.NumberFormat().format(maxPersonnel - reservationPersonal)}
                명
              </span>
              <ul className="list">
                <li>
                  최소인원 {Intl.NumberFormat().format(minPersonnel)}명 /
                  최대인원 {Intl.NumberFormat().format(maxPersonnel)}명
                </li>
                <li>
                  {startTime.formatTime01()} ~ {endTime.formatTime01()} (
                  {endTime.substr(0, 2) - startTime.substr(0, 2)}시간)
                </li>
                <li>
                  어종:{" "}
                  {fishSpecies.map((data, index) => {
                    if (index === 0) {
                      return (
                        <React.Fragment key={index}>{data}</React.Fragment>
                      );
                    } else {
                      return (
                        <React.Fragment key={index}>
                          {", ".concat(data)}
                        </React.Fragment>
                      );
                    }
                  })}
                </li>
              </ul>
            </div>
            <div className="col-3 text-right">
              <strong className="red large">
                {Intl.NumberFormat().format(price)}
                <span>원</span>
              </strong>
            </div>
          </div>
          <p className="space mt-3 mb-1"></p>
        </div>
      );
    }
  )
);
