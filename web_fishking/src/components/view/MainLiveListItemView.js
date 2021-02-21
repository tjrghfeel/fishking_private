import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(
    ({
      data: {
        id,
        shipImageFileUrl,
        shipName,
        fishSpecies = [],
        sido = "",
        sigungu = "",
      },
      PageStore,
    }) => {
      return (
        <li className="item">
          <a onClick={() => PageStore.push(`/story/tv/detail/${id}`)}>
            <div className="imgWrap">
              <img src={shipImageFileUrl} className="img-fluid" alt="" />
              <span className="play">
                <img src="/assets/cust/img/svg/live-play.svg" alt="" />
              </span>
              <span className="play-live">LIVE</span>
            </div>
            <div className="InfoWrap">
              <h6>{shipName}</h6>
              <p>
                <strong className="text-primary">
                  {fishSpecies.length > 0 && fishSpecies[0]["codeName"]}
                </strong>
                {sido.concat(" ").concat(sigungu)}
              </p>
            </div>
          </a>
        </li>
      );
    }
  )
);
