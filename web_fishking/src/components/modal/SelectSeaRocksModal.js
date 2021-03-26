import React, { useEffect, useCallback, useState } from "react";
import { inject, observer } from "mobx-react";

export default inject(
  "APIStore",
  "DataStore"
)(
  observer(({ id = "", APIStore, DataStore, onSelect }) => {
    const [list, setList] = useState([]);
    const [selected, setSelected] = useState([]);
    const onSelected = useCallback(
      (checked, item) => {
        if (checked) {
          setSelected(selected.concat(item["id"]));
        } else {
          setSelected(DataStore.removeItemOfArrayByItem(selected, item["id"]));
        }
      },
      [setSelected, selected]
    );
    const loadPageData = useCallback(async () => {
      const resolve = await APIStore._get(`/v2/api/searocks`);
      setList(resolve["data"] || []);
    }, [setList]);
    useEffect(() => {
      loadPageData();
    }, [loadPageData]);
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
              <a data-dismiss="modal" className="nav-left">
                <img
                  src="/assets/smartfishing/img/svg/navbar-back.svg"
                  alt="뒤로가기"
                />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                갯바위 선택
              </h5>
              <a className="nav-right">
                <img
                  src="/assets/smartfishing/img/svg/navbar-refresh.svg"
                  alt="Refresh"
                />
                <span>초기화</span>
              </a>
            </div>
            <div className="modal-body">
              <div className="padding">
                <div className="form-group">
                  <label className="d-block">
                    지역을 선택하신 후 원하시는 갯바위를 체크하세요.
                  </label>
                  <div className="input-group mb-3">
                    <select className="form-control" id="">
                      <option>시/도</option>
                    </select>
                    <select className="form-control" id="">
                      <option>시/군/구</option>
                    </select>
                    <select className="form-control" id="">
                      <option>읍/면/동</option>
                    </select>
                  </div>
                </div>
                <div className="form-group checklist">
                  {list.map((data, index) => (
                    <React.Fragment key={index}>
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                          onChange={(e) => onSelected(e.target.checked, data)}
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">{data["name"]}</span>
                      </label>
                      <br />
                    </React.Fragment>
                  ))}
                </div>
                <div className="form-group text-right">
                  <a
                    className="btn btn-third btn-sm"
                    data-toggle="modal"
                    data-target="#addRocksModal"
                  >
                    + 신규 갯바위 추가
                  </a>
                </div>
              </div>
            </div>
            <div className="modal-footer-btm">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={() => (onSelect ? onSelect(selected) : null)}
                    className="btn btn-primary btn-lg btn-block"
                    data-dismiss="modal"
                  >
                    선택
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
