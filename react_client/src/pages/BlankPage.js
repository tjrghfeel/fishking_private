import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(() => {
    return <div>Blank</div>;
  })
);
