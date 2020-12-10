import React from "react";
import { inject, observer } from "mobx-react";

import ListItem01 from "./ListItem01";
import ListItem05 from "./ListItem05";
import ListItem08 from "./ListItem08";
import ListItemMore from "./ListItemMore";

export default inject()(
  observer(
    ({
      title,
      itemType = "ListItem01" | "ListItem05" | "ListItem08",
      list,
      hasMore,
      onClickMore,
      cls,
    }) => {
      return (
        <>
          {title && <h5>{title}</h5>}
          <div className={"slideList" + (cls ? " ".concat(cls) : "")}>
            <ul className="listWrap">
              {list &&
                list.map((data, index) => (
                  <li key={index} className="item">
                    {itemType === "ListItem01" && <ListItem01 {...data} />}
                    {itemType === "ListItem05" && <ListItem05 {...data} />}
                    {itemType === "ListItem08" && <ListItem08 {...data} />}
                  </li>
                ))}
              {hasMore && (
                <ListItemMore
                  onClick={onClickMore}
                  cls={itemType === "ListItem08" ? "inner-md" : ""}
                />
              )}
            </ul>
          </div>
        </>
      );
    }
  )
);
