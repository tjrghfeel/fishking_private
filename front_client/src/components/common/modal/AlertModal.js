import React from "react";
import { inject, observer } from "mobx-react";

export default inject("DialogStore")(
  observer(({ DialogStore: { id = "alertModal", title, body, onClose } }) => {
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
            <div className="modal-body text-center">{body}</div>
            <div className="modal-footer-btm">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={() => (onClose ? onClose() : null)}
                    className="btn btn-primary btn-lg btn-block"
                    data-dismiss="modal"
                  >
                    확인
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  })
);
