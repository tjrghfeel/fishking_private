import React from "react";
import { inject, observer } from "mobx-react";

export default inject("ModalStore")(
  observer(({ ModalStore: { title, selectOptions, onSelect } }) => {
    return (
      <div
        className="modal fade"
        id="selectModal"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="selectModalLabel"
      >
        <div className="modal-dialog modal-dialog-centered" role="document">
          <div className="modal-content modal-sm modal-xs">
            <div className="modal-body">
              {title && (
                <React.Fragment>
                  <h5 className="text-primary mt-3 mb-3 text-center">
                    {title}
                  </h5>
                  <hr className="full mt-1 mb-1" />
                </React.Fragment>
              )}
              {selectOptions.length > 0 && (
                <div className="list-group">
                  {selectOptions.map((data, index) => {
                    if (data) {
                      return (
                        <a
                          key={index}
                          onClick={() =>
                            onSelect
                              ? onSelect({ selected: data, index })
                              : null
                          }
                          className="list-group-item"
                          data-dismiss="modal"
                        >
                          {data}
                        </a>
                      );
                    }
                  })}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  })
);
