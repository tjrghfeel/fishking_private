import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { text, onClick, modalTarget, isActive = false, onClickClear },
    }) => {
      return (
        <li className="item">
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
        </li>
      );
    }
  )
);
