/* global $ */
import React, { useEffect, useCallback, useState, useRef } from "react";
import { inject, observer } from "mobx-react";
import AddSeaRocksModal from "./AddSeaRocksModal";

export default inject(
  "APIStore",
  "DataStore"
)(
  observer(({ id = "", APIStore, DataStore, onSelect, positions }) => {
    const [list, setList] = useState([]); // 갯바위 목록
    const selSido = useRef(null);
    const selSigungu = useRef(null);
    const [arrSido, setArrSido] = useState([]); // 시/도 리스트
    const [arrSigungu, setArrSigungu] = useState([]); // 시/군/구 리스트
    const [arrDong, setArrDong] = useState([]); // 동/읍/면 리스트
    const [selected, setSelected] = useState([]);
    const [openAddModal, setOpenAddModal] = useState(false);
    const addRef = useRef(null);
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
    const selectSido = useCallback(async () => {
      let resolve = await APIStore._get(`/v2/api/searocks`, {
        sido: selSido.current.selectedOptions[0].value,
      });
      setList(resolve?.data || []);
      // 시군구 리스트
      resolve = await APIStore._get(`/v2/api/commonCode/area`, {
        groupId: 156,
        parCode: selSido.current.selectedOptions[0].value,
      });
      setArrSigungu(resolve);
      selSigungu.current.value = "";
    }, [setArrSigungu, selSigungu]);
    const selectSigungu = useCallback(async () => {
      let resolve = await APIStore._get(`/v2/api/searocks`, {
        sido: selSido.current.selectedOptions[0].value,
        sigungu: selSigungu.current.selectedOptions[0].value,
      });
      setList(resolve?.data || []);
    }, [setList]);
    const loadPageData = useCallback(async () => {
      // 갯바위 목록
      let resolve = await APIStore._get(`/v2/api/searocks`);
      setList(resolve["data"] || []);
      // 시도 리스트
      resolve = await APIStore._get(`/v2/api/commonCode/area`, {
        groupId: 152,
      });
      setArrSido(resolve);
      if (positions) {
          let newSelected = selected.slice();
        for (let p of positions) {
          newSelected = newSelected.concat(parseInt(p));
          document.querySelector(`#id-${p}`).checked = true;
        }
          setSelected(newSelected);
      }
    }, [setList, setArrSido]);
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
              {/*<a className="nav-right">*/}
              {/*  <img*/}
              {/*    src="/assets/smartfishing/img/svg/navbar-refresh.svg"*/}
              {/*    alt="Refresh"*/}
              {/*  />*/}
              {/*  <span>초기화</span>*/}
              {/*</a>*/}
            </div>
            <div className="modal-body">
              <div className="padding">
                <div className="form-group">
                  <label className="d-block">
                    지역을 선택하신 후 원하시는 갯바위를 체크하세요.
                  </label>
                  <div className="input-group mb-3">
                    <select
                      ref={selSido}
                      className="form-control"
                      onChange={(e) => {
                        selectSido();
                      }}
                    >
                      <option value={""}>시/도</option>
                      {arrSido.map((data, index) => (
                        <option key={index} value={data["code"]}>
                          {data["codeName"]}
                        </option>
                      ))}
                    </select>
                    <select
                      className="form-control"
                      ref={selSigungu}
                      onChange={(e) => {
                        selectSigungu();
                      }}
                    >
                      <option value={""}>시/군/구</option>
                      {arrSigungu.map((data, index) => (
                        <option key={index} value={data["code"]}>
                          {data["codeName"]}
                        </option>
                      ))}
                    </select>

                  </div>
                </div>
                <div className="form-group checklist">
                  {list.map((data, index) => (
                    <React.Fragment key={index}>
                      <label className="control checkbox">
                        <input
                          id={`id-${data["id"]}`}
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
                    onClick={() => {
                      setOpenAddModal(true);
                      setTimeout(() => {
                        $("#addRocksModal").modal("show");
                        setTimeout(() => {
                          addRef.current.relayout();
                        }, 500);
                      }, 100);
                    }}
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
        {openAddModal && (
          <AddSeaRocksModal
            ref={addRef}
            id={"addRocksModal"}
            onClose={loadPageData}
          />
        )}
      </div>
    );
  })
);
