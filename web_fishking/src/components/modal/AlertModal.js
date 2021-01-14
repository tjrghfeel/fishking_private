import React from "react";
import { inject, observer } from "mobx-react";

export default inject("ModalStore")(
  observer(({ ModalStore: { title, body, onOk } }) => {
    return (
      <div
        className="modal fade"
        id="alertModal"
        tabIndex="-1"
        aria-labelledby="alertModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-sm modal-dialog-centered">
          <div className="modal-content">
            {title && (
              <div className="modal-header">
                <h5 className="modal-title text-center" id="alertModalLabel">
                  {title}
                </h5>
              </div>
            )}
            <div className="modal-body text-center">{body}</div>
            <div className="modal-footer-btm">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={() => (onOk ? onOk() : null)}
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
