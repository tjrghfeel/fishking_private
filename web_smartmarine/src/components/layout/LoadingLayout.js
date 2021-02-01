import React from "react";
import { inject, observer } from "mobx-react";

export default inject("APIStore")(
  observer(({ APIStore: { isLoading } }) => {
    return (
      <React.Fragment>
        {isLoading && (
          <div
            style={{
              zIndex: 9999,
              backgroundColor: "#00000044",
              position: "fixed",
              top: 0,
              bottom: 0,
              left: 0,
              right: 0,
            }}
          ></div>
        )}
      </React.Fragment>
    );
  })
);
