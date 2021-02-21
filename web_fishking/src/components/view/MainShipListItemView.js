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
        lowPrice = 0,
      },
      PageStore,
    }) => {
      return (
        <li className="item">
          <a onClick={() => PageStore.push(`/company/boat/detail/${id}`)}>
            <div className="imgWrap">
              <img src={shipImageFileUrl} className="img-fluid" alt="" />
              {/*<span className="play">*/}
              {/*  <img src="/assets/cust/img/svg/live-play.svg" alt="" />*/}
              {/*</span>*/}
              {/*<span className="play-time">20:17</span>*/}
            </div>
            <div className="InfoWrap">
              <h6>{shipName}</h6>
              <p>
                <strong className="text-primary">
                  {fishSpecies.length > 0 && fishSpecies[0]["codeName"]}
                </strong>
                {sido.concat(" ").concat(sigungu)}
              </p>
              <h6>
                {Intl.NumberFormat().format(lowPrice)}
                <small>원</small>
              </h6>
            </div>
          </a>
        </li>
      );
    }
  )
);
