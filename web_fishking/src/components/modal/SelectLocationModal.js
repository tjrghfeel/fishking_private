import React, { useState, useRef } from "react";
import { inject, observer } from "mobx-react";
import SearchCompanyListView from "../view/SearchCompanyListView";
import SelectLocationByMapView from "../view/SelectLocationByMapView";

export default inject("DataStore")(
  observer(({ id, onSelected }) => {
    const [active, setActive] = useState(0);
    const dismiss = useRef(null);
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
                  src="/cust/assets/img/svg/navbar-back.svg"
                  alt="뒤로가기"
                />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                업체&위치 등록
              </h5>
            </div>
            <div className="modal-body">
              <nav className="nav nav-pills nav-menu nav-justified">
                <a
                  className={"nav-link" + (active === 0 ? " active" : "")}
                  onClick={() => setActive(0)}
                >
                  업체
                </a>
                <a
                  className={"nav-link" + (active === 1 ? " active" : "")}
                  onClick={() => setActive(1)}
                >
                  내 위치 선택
                </a>
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
              {active === 1 && (
                <SelectLocationByMapView
                  onSelected={(selected) => {
                    if (onSelected) {
                      onSelected({ itemType: "Location", address: selected });
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
