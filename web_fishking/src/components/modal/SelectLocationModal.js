import React, { useState, useRef } from "react";
import { inject, observer } from "mobx-react";
import SearchCompanyListView from "../view/SearchCompanyListView";
import SelectLocationByMapView from "../view/SelectLocationByMapView";

export default inject(
  "DataStore",
  "PageStore"
)(
  observer(({ id, onSelected, PageStore }) => {
    const [active, setActive] = useState(0);
    const dismiss = useRef(null);
    const { iscompany = null } = PageStore.getQueryParams();

    return (
      <div
        className="modal fade modal-full"
        id={id}
        tabIndex="-1"
        aria-labelledby={id.concat("Label")}
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header bg-primary d-flex justify-content-center">
              <a ref={dismiss} data-dismiss="modal" className="nav-left">
                <img
                  src="/assets/cust/img/svg/navbar-back.svg"
                  alt="뒤로가기"
                />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                선박, 위치 선택
              </h5>
            </div>
            <div className="modal-body">
              <nav className="nav nav-pills nav-menu nav-justified">
                <a
                  className={"nav-link" + (active === 0 ? " active" : "")}
                  onClick={() => setActive(0)}
                >
                  선박 선택
                </a>
                {iscompany != "Y" && (
                  <a
                    className={"nav-link" + (active === 1 ? " active" : "")}
                    onClick={() => setActive(1)}
                  >
                    위치 선택
                  </a>
                )}
              </nav>
              {active === 0 && (
                <SearchCompanyListView
                  parent={id}
                  onClick={(item) => {
                    if (onSelected) {
                      onSelected({ itemType: "Company", ...item });
                    }
                    dismiss.current?.click();
                  }}
                />
              )}
              {iscompany != "Y" && active === 1 && (
                <SelectLocationByMapView
                  onSelected={({ address, lat, lng }) => {
                    if (onSelected) {
                      onSelected({ itemType: "Location", address, lat, lng });
                    }
                    dismiss.current?.click();
                  }}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    );
  })
);
