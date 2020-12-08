import React from "react";
import { inject, observer } from "mobx-react";

import ListItem01 from "./ListItem01";
import ListItem05 from "./ListItem05";
import ListItemMore from "./ListItemMore";

export default inject()(
  observer(
    ({ title, itemType = "ListItem01", list, hasMore, onClickMore, cls }) => {
      return (
        <>
          <h5>{title}</h5>
          <div className={"slideList" + (cls ? " ".concat(cls) : "")}>
            <ul className="listWrap">
              {list &&
                list.map((data, index) => {
                  if (itemType === "ListItem01") {
                    return (
                      <li key={index} className="item">
                        <ListItem01 key={index} {...data} />
                      </li>
                    );
                  } else if (itemType === "ListItem05") {
                    return (
                      <li key={index} className="item">
                        <ListItem05 key={index} {...data} />
                      </li>
                    );
                  }
                })}
              {hasMore && <ListItemMore onClick={onClickMore} />}
            </ul>
          </div>
        </>
      );
    }
  )
);
