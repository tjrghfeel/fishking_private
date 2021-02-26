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
      const [selectedLv1, setSelectedLv1] = useState(null);
      const [selectedLv2, setSelectedLv2] = useState(null);
      useEffect(() => {
        (async () => {
          let resolve = await DataStore.getCodes("152", 2);
          setList(resolve);
          resolve = await DataStore.getCodes("156", 2);
          setList2(resolve);
        })();
      }, [DataStore, setList, setList2]);
      const onSelectLv1 = useCallback(
        (e, data) => {
          // >>>>> 다른 옵션 체크 해제
          let elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"][data-level="lv1"]')
          );
          for (let element of elements) {
            if (element !== e.target) element.checked = false;
          }
          elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"][data-level="lv2"]')
          );
          for (let element of elements) {
            element.checked = false;
          }
          // >>>>> 행정구역 있으면 보여줌
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
          setSelectedLv1(data);
          setSelectedLv2(null);
        },
        [DataStore, list2, setAreaList, setSelectedLv1, setSelectedLv2]
      );
      const onSelectLv2 = useCallback(
        (e, data) => {
          const elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"][data-level="lv2"]')
          );
          for (let element of elements) {
            if (element !== e.target) element.checked = false;
          }
          setSelectedLv2(data);
        },
        [setSelectedLv2]
      );
      const onInit = useCallback(() => {
        setSelectedLv1(null);
        setSelectedLv2(null);
        const elements = document.querySelectorAll(
          "#".concat(id).concat(' input[type="checkbox"]')
        );
        for (let element of elements) {
          element.checked = false;
        }
      }, [setSelectedLv1, setSelectedLv2]);
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
                  {list &&
                    list.map((item, index1) => (
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
                onClick={() =>
                  onSelected ? onSelected({ selectedLv1, selectedLv2 }) : null
                }
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
