import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        isLiveButton = false,
        text,
        onClick,
        modalTarget,
        isActive = false,
        onClickClear,
      },
    }) => {
      return (
        <li className="item">
          {isLiveButton && (
            <a
              onClick={() => (onClick ? onClick(isActive) : null)}
              className={"filterLive" + (isActive ? " active" : "")}
            >
              <span className="sr-only">라이브</span>
            </a>
          )}
          {!isLiveButton && (
            <React.Fragment>
              <a
                className={"filterSel" + (isActive ? " active" : "")}
                data-toggle={modalTarget ? "modal" : null}
                data-target={modalTarget ? `#${modalTarget}` : null}
                onClick={() => (onClick ? onClick(text) : null)}
              >
                {text}
              </a>
              {isActive && (
                <a onClick={() => (onClickClear ? onClickClear() : null)}>
                  <span className="close"></span>
                </a>
              )}
            </React.Fragment>
          )}
        </li>
      );
    }
  )
);
