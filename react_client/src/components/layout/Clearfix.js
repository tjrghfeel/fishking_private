import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ children, cls }) => {
    return (
      <div className={"clearfix" + (cls ? " ".concat(cls) : "")}>
        {children}
      </div>
    );
  })
);
