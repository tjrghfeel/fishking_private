import React from "react";
import { inject, observer } from "mobx-react";

export default inject("DialogStore")(
  observer(
    ({
      DialogStore: {
        id = "confirmModal",
        title,
        body,
        onOk,
        onClose,
        textOk = "확인",
        textClose = "닫기",
      },
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
              <div className="modal-body text-center">{body}</div>
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
                      onClick={() => (onClose ? onClose() : null)}
                      className="btn btn-third btn-lg btn-block"
                      data-dismiss="modal"
                    >
                      {textClose}
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
