import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ list }) => {
    return (
      <>
        {list &&
          list.map((data, index) => {
            const { imgSrc, title, contents, isLive, playtime } = data;
            return (
              <React.Fragment key={index}>
                <div className="container nopadding mt-2">
                  <a href="story-tv-detail.html">
                    <div className="card card-sm">
                      <div className="row no-gutters">
                        <div className="cardimgWrap">
                          <img src={imgSrc} className="img-fluid" alt="" />
                          {(isLive || playtime) && (
                            <span className="play">
                              <img src="/assets/img/svg/live-play.svg" alt="" />
                            </span>
                          )}
                          {isLive && <span className="play-live">LIVE</span>}
                          {playtime && (
                            <span className="play-time">{playtime}</span>
                          )}
                        </div>
                        <div className="cardInfoWrap">
                          <div className="card-body">
                            <h6>{title}</h6>
                            <p>{contents}…</p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </a>
                </div>
                <hr />
              </React.Fragment>
            );
          })}
      </>
    );
  })
);
