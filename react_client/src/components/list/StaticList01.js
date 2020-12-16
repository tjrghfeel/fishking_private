import React from "react";
import { inject, observer } from "mobx-react";

import ListItem10 from "./ListItem10";

export default inject()(
  observer(({ list, itemType = "ListItem10" }) => {
    return (
      <div className="container nopadding mt-0">
        <div className="pt-0">
          <hr className="full mt-0 mb-3" />
          {list &&
            list.map((data, index) => <ListItem10 key={index} {...data} />)}
        </div>
      </div>
    );
  })
);
