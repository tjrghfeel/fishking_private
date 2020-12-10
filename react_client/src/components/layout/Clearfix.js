import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ children }) => {
    return <div className="clearfix">{children}</div>;
  })
);
