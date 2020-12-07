/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

const ConfirmModal = inject("ModalStore")(
  observer(
    ({
      ModalStore: {
        data: {
          title,
          innerHtml,
          onClickOk,
          textOk,
          onClickCancel,
          textCancel,
        },
      },
    }) => {
      return (
        <div
          className="modal fade"
          id="confirmModal"
          tabIndex={-1}
          aria-labelledby="confirmModalLabel"
          aria-hidden="true"
        >
          <div className="modal-dialog modal-sm modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title text-center" id="confirmModalLabel">
                  {title}
                </h5>
              </div>
              <div className="modal-body text-center">{innerHtml}</div>
              <div className="modal-footer-btm">
                <div className="row no-gutters">
                  <div className="col-6">
                    <a
                      onClick={() => (onClickOk ? onClickOk() : null)}
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
                      {textCancel || "닫기"}
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

export default ConfirmModal;
