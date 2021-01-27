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
      const [selected, setSelected] = useState([]);
      useEffect(() => {
        (async () => {
          const resolve = await DataStore.getCodes("152", 2);
          setList(resolve);
        })();
      }, [DataStore, setList]);
      const onChange = useCallback(
        (code) => {
          if (selected.indexOf(code) !== -1) {
            const array = DataStore.removeItemOfArrayByItem(selected, code);
            setSelected(array);
          } else {
            setSelected(selected.concat(code));
          }
        },
        [DataStore, selected, setSelected]
      );
      const onInit = useCallback(() => {
        setSelected([]);
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
                  <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
                </a>
                <h5 className="modal-title" id="selAreaModalLabel">
                  지역선택
                </h5>
                <a className="nav-right" onClick={onInit}>
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
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
                                onChange={(e) => onChange(data.code)}
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
