import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ imgSrc }) => {
    return <img src={imgSrc} className="d-block w-100" alt="" />;
  })
);
