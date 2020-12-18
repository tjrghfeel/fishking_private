import React from "react";
import { inject, observer } from "mobx-react";

import ListItem03 from "./ListItem03";
import ListItem04 from "./ListItem04";

export default inject()(
  observer(
    ({
      carouselType = "ListItem03" | "ListItem04",
      list,
      isWrapped = false,
    }) => {
      const id = carouselType.concat(new Date().getTime());
      return (
        <div id={id} className="carousel slide" data-ride="carousel">
          {isWrapped && (
            <div className="carouselwrap w-full">
              <ol className="carousel-indicators">
                {list &&
                  list.map((data, index) => (
                    <li
                      key={index}
                      data-target={"#".concat(id)}
                      data-slide-to={index}
                      className={index === 0 ? "active" : ""}
                    />
                  ))}
              </ol>
              <div className="carousel-inner">
                {list &&
                  list.map((data, index) => (
                    <div
                      key={index}
                      className={
                        "carousel-item" + (index === 0 ? " active" : "")
                      }
                    >
                      {carouselType === "ListItem03" && (
                        <ListItem03 data={data} />
                      )}
                      {carouselType === "ListItem04" && (
                        <ListItem04 data={data} />
                      )}
                    </div>
                  ))}
              </div>
            </div>
          )}
          {!isWrapped && (
            <>
              <ol className="carousel-indicators">
                {list &&
                  list.map((data, index) => (
                    <li
                      key={index}
                      data-target={"#".concat(id)}
                      data-slide-to={index}
                      className={index === 0 ? "active" : ""}
                    />
                  ))}
              </ol>
              <div className="carousel-inner">
                {list &&
                  list.map((data, index) => (
                    <div
                      key={index}
                      className={
                        "carousel-item" + (index === 0 ? " active" : "")
                      }
                    >
                      {carouselType === "ListItem03" && (
                        <ListItem03 data={data} />
                      )}
                      {carouselType === "ListItem04" && (
                        <ListItem04 data={data} />
                      )}
                    </div>
                  ))}
              </div>
            </>
          )}
        </div>
      );
    }
  )
);
