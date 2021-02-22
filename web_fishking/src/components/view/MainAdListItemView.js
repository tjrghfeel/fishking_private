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
      active = false,
      PageStore,
    }) => {
      return (
        <div className={"carousel-item " + (active ? " active" : "")}>
          <div
            className="card"
            onClick={() => PageStore.push(`/company/boat/detail/${id}`)}
          >
            <div className="row no-gutters">
              <div className="cardimgWrap">
                <img src={shipImageFileUrl} className="img-fluid" alt="" />
              </div>
              <div className="cardInfoWrap">
                <div className="card-body">
                  <h6>{shipName}</h6>
                  <p>
                    <strong className="text-primary">
                      {fishSpecies.length > 2 &&
                        fishSpecies
                          .splice(0, 2)
                          .map((data, index) => (
                            <React.Fragment key={index}>
                              {data.codeName}
                            </React.Fragment>
                          ))}
                    </strong>
                    <br />
                    {sido.concat(" ").concat(sigungu)}
                  </p>
                  <h6 className="btm-right">
                    {Intl.NumberFormat().format(lowPrice)}
                    <small>원</small>
                  </h6>
                </div>
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);
