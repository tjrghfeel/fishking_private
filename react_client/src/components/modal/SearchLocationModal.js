/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ id, onSelected }) => {
    return (
      <div
        className="modal fade modal-full"
        id={id}
        data-modal-type={"SelectDateModal"}
        tabIndex="-1"
        aria-labelledby={id.concat("Label")}
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header bg-primary d-flex justify-content-center">
              <a href="#none" data-dismiss="modal" className="nav-left">
                <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                업체 & 위치 등록
              </h5>
            </div>
            <div className="modal-body">
              {/** 탭메뉴 */}
              <nav className="nav nav-pills nav-menu nav-justified">
                <a className="nav-link active" href="story-add-boat.html">
                  업체
                </a>
                <a className="nav-link" href="story-add-map.html">
                  내 위치 선택
                </a>
              </nav>
            </div>
          </div>
        </div>
      </div>
    );
  })
);
