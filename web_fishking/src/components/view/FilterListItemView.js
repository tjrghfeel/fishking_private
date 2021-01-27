import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: { text, onClick, modalTarget } }) => {
    return (
      <li className="item">
        <a
          className="filterSel"
          data-toggle={modalTarget ? "modal" : null}
          data-target={modalTarget ? `#${modalTarget}` : null}
          onClick={() => (onClick ? onClick(text) : null)}
        >
          {text}
        </a>
      </li>
    );
  })
);
