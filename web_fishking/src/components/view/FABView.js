import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ list = [{ text: null, icon: null }], onClick }) => {
    return (
      <React.Fragment>
        <div className="toggle">
          <button className="toggle_menu">
            <span></span>
            <span></span>
          </button>
        </div>
        <div className="allmenu">
          <div className="innerwrap">
            <div className="menu_wrap">
              <ul>
                {list &&
                  list.map((data, index) => (
                    <li>
                      <a
                        onClick={() => (onClick ? onClick(data["text"]) : null)}
                      >
                        <figure>
                          <img src={data["icon"]} alt="" />
                        </figure>
                        <span>{data["text"]}</span>
                      </a>
                    </li>
                  ))}
              </ul>
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  })
);
