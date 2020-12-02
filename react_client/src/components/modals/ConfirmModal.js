/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

const ConfirmModal = inject("PageStore")(
  observer(({ PageStore }) => {
    const { modalData } = PageStore;
    let { title, innerHtml, textOk, textCancel, onOk, onCancel } = modalData;
    textCancel = textCancel || "닫기";
    return (
      <div
        className="modal fade"
        id="confirmModal"
        tabIndex="-1"
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
                    onClick={() => (onOk ? onOk() : null)}
                    className="btn btn-primary btn-lg btn-block"
                    data-dismiss="modal"
                  >
                    {textOk}
                  </a>
                </div>
                <div className="col-6">
                  <a
                    onClick={() => (onCancel ? onCancel() : null)}
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
  })
);

export default ConfirmModal;
