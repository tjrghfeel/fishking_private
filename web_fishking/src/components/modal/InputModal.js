import React, { useState } from "react";
import { inject, observer } from "mobx-react";

export default inject("ModalStore")(
  observer(
    ({ ModalStore: { body = "", textOk, textClose, onOk, onClose } }) => {
      const [input, setInput] = useState(body);
      return (
        <div
          className="modal fade"
          id="inputModal"
          tabIndex="-1"
          aria-labelledby="inputModalLabel"
          aria-hidden="true"
        >
          <div className="modal-dialog modal-sm modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-body text-center">
                <input
                  type="text"
                  className="form-control no-line"
                  value={input}
                  onChange={(e) => setInput(e.target.value)}
                />
              </div>
              <div className="modal-footer-btm">
                <div className="row no-gutters">
                  <div className="col-6">
                    <a
                      onClick={() => {
                        if (onOk) {
                          onOk(input);
                          setInput("");
                        }
                      }}
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
