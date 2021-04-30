import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      beforeData = null,
      data: { alertType, content, createdDate = "", iconDownloadUrl },
      data,
      onDelete,
    }) => {
      let showDateHeader = false;
      if (
        beforeData === null
      ) {
        showDateHeader = true;
      } else {
        if (!beforeData.createdDate) {
          showDateHeader = true;
        } else if (beforeData.createdDate.substr(0, 10) !== createdDate.substr(0, 10)) {
          showDateHeader = true;
        }
      }
      return (
        <>
          {showDateHeader && (
            <h5 className="text-center">
              {createdDate.substr(0, 10).replace(/[-]/g, ".")}
            </h5>
          )}
          <div className="card-round-box-grey mt-3 pt-3 pb-3">
            <div className="row no-gutters">
              <div className="col-2 text-center">
                <a className="btn btn-circle pt-0">
                  <img src={iconDownloadUrl} alt="" className="icon-md" />
                </a>
              </div>
              <div className="col-9">
                <strong className="blue">[{alertType}]</strong> {content}
                <br />
                <small className="grey">
                  {createdDate && createdDate.substr(11, 5)}
                </small>
              </div>
              <div className="col-1 text-right">
                <a onClick={() => (onDelete ? onDelete(data) : null)}>
                  <img src="/assets/cust/img/svg/icon_close_grey.svg" alt="" />
                </a>
              </div>
            </div>
          </div>
        </>
      );
    }
  )
);
