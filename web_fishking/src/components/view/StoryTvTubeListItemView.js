import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        snippet: {
          thumbnails: {
            high: { url },
          },
          title,
          description="",
          publishedAt = "",
        },
        statistics: { viewCount = 0 },
      },
      data,
      onClick,
    }) => {
      return (
        <React.Fragment>
          <div className="container nopadding mt-2">
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <div className="card card-sm">
                <div className="row no-gutters">
                  <div className="cardimgWrap">
                    <img src={url} className="img-fluid" alt="" />
                  </div>
                  <div className="cardInfoWrap">
                    <div className="card-body">
                      <h6>{title}</h6>
                      <p>
                        <span className="grey">
                          조회수 {viewCount === 0 && "없음 "}
                          {viewCount > 0 &&
                            Intl.NumberFormat().format(viewCount).concat(" ")}회
                          | {publishedAt.substr(0, 10).replace(/[-]/g, ".")}
                        </span>
                      </p>
                      <p>{(description.length > 25)?description.substring(0,25)+"..." : description}</p>
                    </div>
                  </div>
                </div>
              </div>
            </a>
          </div>
          <hr />
        </React.Fragment>
      );
    }
  )
);
