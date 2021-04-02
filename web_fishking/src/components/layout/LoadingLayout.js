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
              backgroundColor: "#FFFFFF77",
              position: "fixed",
              top: 0,
              bottom: 0,
              left: 0,
              right: 0,
              display: "none",
              justifyContent: "center",
              alignItems: "center",
              backdropFilter: "blur(1.5px)",
            }}
          >
            <img
              src={"/assets/Spin-1s-200px.svg"}
              width={"80px"}
              height={"80px"}
            />
          </div>
        )}
      </React.Fragment>
    );
  })
);
