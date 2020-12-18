import React from "react";
import { inject, observer } from "mobx-react";

import ListItem12 from "./ListItem12";
import ListItem14 from "./ListItem14";

export default inject()(
  observer(({ list, itemType = "ListItem12" | "ListItem14", onClick }) => {
    return (
      <div className="container nopadding bg-grey">
        {list &&
          list.map((data, index) => {
            if (itemType === "ListItem12") {
              if (index === 0) {
                return (
                  <ListItem12
                    key={index}
                    data={{ ...data, onClick }}
                    cls={"mt-3"}
                  />
                );
              } else {
                return <ListItem12 key={index} data={{ ...data, onClick }} />;
              }
            } else if (itemType === "ListItem14") {
              return <ListItem14 key={index} data={data} />;
            }
          })}
      </div>
    );
  })
);
