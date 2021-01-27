import React from "react";
import { inject, observer } from "mobx-react";
import FilterListItemView from "./FilterListItemView";

export default inject()(
  observer(({ list }) => {
    return (
      <div className="filterWrap">
        <div className="slideList">
          <ul className="listWrap">
            {list.map((data, index) => (
              <FilterListItemView key={index} data={data} />
            ))}
          </ul>
        </div>
      </div>
    );
  })
);
