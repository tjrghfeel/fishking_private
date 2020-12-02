import React from "react";
import { inject, observer } from "mobx-react";

const ListItem02 = inject("RouteStore")(
  observer(
    ({
      RouteStore,
      navigateTo,
      imgSrc,
      title,
      textPrimary,
      location,
      price,
    }) => {
      const go = (pathname) => {
        RouteStore.go(pathname);
      };
      return (
        <div className="card" onClick={() => go(navigateTo)}>
          <div className="row no-gutters">
            <div className="cardimgWrap">
              <img src={imgSrc} className="img-fluid" alt="" />
            </div>
            <div className="cardInfoWrap">
              <div className="card-body">
                <h6>{title}</h6>
                <p>
                  <strong className="text-primary">{textPrimary}</strong>
                  <br />
                  {location}
                </p>
                {price && (
                  <h6 className="btm-right">
                    70,000<small>원</small>
                  </h6>
                )}
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);

export default ListItem02;
