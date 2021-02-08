import React, {
  useEffect,
  useState,
  useCallback,
  useImperativeHandle,
  forwardRef,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    forwardRef(({ id, DataStore, onSelected }, ref) => {
      const [list, setList] = useState([]);
      const [list2, setList2] = useState([]);
      const [areaList, setAreaList] = useState([]);
      const [selected, setSelected] = useState(null);
      useEffect(() => {
        (async () => {
          let resolve = await DataStore.getCodes("152", 2);
          setList(resolve);
          resolve = await DataStore.getCodes("154", 2);
          setList2(resolve);
        })();
      }, [DataStore, setList, setList2]);
      const onSelectLv1 = useCallback(
        (e, data) => {
          const elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"][data-level="lv1"]')
          );
          for (let element of elements) {
            if (element !== e.target) element.checked = false;
          }
          let resolve = [];
          for (let inner of list2) {
            for (let item of inner) {
              if (item.extraValue1 === data.code) {
                resolve.push(item);
              }
            }
          }

          if (resolve.length > 0) {
            const areas = DataStore.makeArrayToColumns(resolve, 2);
            setAreaList(areas);
          } else {
            setAreaList([]);
          }
          setSelected(data);
        },
        [DataStore, list2, setAreaList]
      );
      const onSelectLv2 = useCallback(
        (e, data) => {
          const elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"][data-level="lv2"]')
          );
          for (let element of elements) {
            if (element !== e.target) element.checked = false;
          }
          setSelected(data);
        },
        [setSelected]
      );
      const onInit = useCallback(() => {
        setSelected(null);
        const elements = document.querySelectorAll(
          "#".concat(id).concat(' input[type="checkbox"]')
        );
        for (let element of elements) {
          element.checked = false;
        }
      }, [setSelected]);
      useImperativeHandle(ref, () => ({ onInit }));
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
                    src="/assets/cust/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
                <h5 className="modal-title" id="selAreaModalLabel">
                  지역선택
                </h5>
                <a className="nav-right" onClick={onInit}>
                  <img
                    src="/assets/cust/img/svg/navbar-refresh.svg"
                    alt="Refresh"
                  />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <h6 className="modal-title-sub">지역별 선택</h6>
                  {list.map((item, index1) => (
                    <div className="row" key={index1}>
                      {item.map((data, index2) => (
                        <div className="col" key={index2}>
                          {data.id && (
                            <label className="control checkbox">
                              <input
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                data-level="lv1"
                                onChange={(e) => onSelectLv1(e, data)}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {data.codeName}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                  {areaList.length > 0 && (
                    <React.Fragment>
                      <h6 className="modal-title-sub">행정구역 선택</h6>
                      {areaList.map((item, index1) => (
                        <div className="row" key={index1}>
                          {item.map((data, index2) => (
                            <div className="col" key={index2}>
                              {data.id && (
                                <label className="control checkbox">
                                  <input
                                    type="checkbox"
                                    className="add-contrast"
                                    data-role="collar"
                                    data-level="lv2"
                                    onChange={(e) => onSelectLv2(e, data)}
                                  />
                                  <span className="control-indicator"></span>
                                  <span className="control-text">
                                    {data.codeName}
                                  </span>
                                </label>
                              )}
                            </div>
                          ))}
                        </div>
                      ))}
                    </React.Fragment>
                  )}
                </div>
              </div>
              <a
                onClick={() => (onSelected ? onSelected(selected) : null)}
                className="btn btn-primary btn-lg btn-block btn-btm"
                data-dismiss="modal"
              >
                적용하기
              </a>
            </div>
          </div>
        </div>
      );
    })
  )
);
