import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ id, title, options = [], onClick }) => {
    return (
      <div
        className="modal fade"
        id={id}
        tabIndex="-1"
        role="dialog"
        aria-labelledby={id.concat("Label")}
      >
        <div className="modal-dialog modal-dialog-centered" role="document">
          <div className="modal-content modal-sm modal-xs">
            <div className="modal-body">
              <h5 className="text-primary mt-3 mb-3 text-center">{title}</h5>
              <hr className="full mt-1 mb-1" />
              <div className="list-group">
                {options.map((data, index) => (
                  <a
                    key={index}
                    onClick={() => (onClick ? onClick(data) : null)}
                    className="list-group-item"
                    data-dismiss="modal"
                  >
                    {data.text}
                  </a>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  })
);
