import React from "react";
import { inject, observer } from "mobx-react";

import ListItem06 from "./ListItem06";
import ListItem07 from "./ListItem07";

export default inject()(
  observer(
    ({ title, titleCls, itemType = "ListItem06" | "ListItem07", list }) => {
      return (
        <>
          {title && (
            <h6
              className={
                "text-secondary" + (titleCls ? " ".concat(titleCls) : "")
              }
            >
              {title}
            </h6>
          )}
          {list &&
            list.map((data, index) => {
              if (itemType === "ListItem06") {
                return <ListItem06 key={index} data={data} />;
              } else {
                return <ListItem07 key={index} data={data} />;
              }
            })}
        </>
      );
    }
  )
);
