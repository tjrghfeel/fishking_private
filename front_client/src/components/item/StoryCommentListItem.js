import React, { useState, useEffect } from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    ({
      DataStore: { getEnumValue, latestTimeFormat },
      data: { title, contents, dependentType, time },
      data,
      onClick,
    }) => {
      const [dependentTypeNm, setDependentTypeNm] = useState("");
      const [timeString, setTimeString] = useState("");
      useEffect(() => {
        (async () => {
          const enm = await getEnumValue("dependentType", dependentType);
          setDependentTypeNm(enm.value);
          setTimeString(latestTimeFormat(time));
        })();
      }, [
        getEnumValue,
        latestTimeFormat,
        dependentType,
        setDependentTypeNm,
        setTimeString,
      ]);
      return (
        <>
          <a onClick={() => (onClick ? onClick(data) : null)}>
            <div className="row no-gutters ">
              <div className="col-9 pl-2">
                {dependentType && (
                  <span className="tag">{dependentTypeNm}</span>
                )}
                <br />
                <small className="grey">{title}</small>
                <br />
                {contents}
              </div>
              <div className="col-3 text-right pl-1 pt-1">
                <small className="grey">{timeString}</small>
              </div>
            </div>
          </a>
          <div className="space mt-3 mb-3"></div>
        </>
      );
    }
  )
);
