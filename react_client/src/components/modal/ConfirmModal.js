/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      id = "",
      title,
      children,
      textOk,
      onClickOK,
      textCancel = "닫기",
      onClickCancel,
    }) => {
      return (
        <div
          className="modal fade"
          id={id}
          tabIndex="-1"
          aria-labelledby={id.concat("Label")}
          aria-hidden="true"
        >
          <div className="modal-dialog modal-sm modal-dialog-centered">
            <div className="modal-content">
              {title && (
                <div className="modal-header">
                  <h5 className="modal-title text-center">{title}</h5>
                </div>
              )}
              <div className="modal-body text-center">{children}</div>
              <div className="modal-footer-btm">
                <div className="row no-gutters">
                  <div className="col-6">
                    <a
                      onClick={() => (onClickOK ? onClickOK() : null)}
                      className="btn btn-primary btn-lg btn-block"
                      data-dismiss="modal"
                    >
                      {textOk}
                    </a>
                  </div>
                  <div className="col-6">
                    <a
                      onClick={() => (onClickCancel ? onClickCancel() : null)}
                      className="btn btn-third btn-lg btn-block"
                      data-dismiss="modal"
                    >
                      {textCancel}
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);
