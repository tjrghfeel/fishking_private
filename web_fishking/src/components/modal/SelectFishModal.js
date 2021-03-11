import React, {
  useState,
  useEffect,
  useCallback,
  forwardRef,
  useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    forwardRef(({ DataStore: { getCodes }, id, onSelected }, ref) => {
      const [list80, setList80] = useState([]); // 대표어종
      const [list161, setList161] = useState([]); // 두족류
      const [list162, setList162] = useState([]); // 기타어종

      const [selected, setSelected] = useState([]); // 선택 목록
      const onInit = useCallback(
        async (defaultSelected = []) => {
          setSelected(defaultSelected);
          const elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"]')
          );
          for (let element of elements) {
            if (
              defaultSelected.includes(
                element.getAttribute("data-code") || null
              )
            ) {
              element.checked = true;
            } else {
              element.checked = false;
            }
          }
        },
        [setSelected]
      );
      const onChange = useCallback(
        (checked, item) => {
          if (checked) {
            // 선택 추가
            selected.push(item);
            setSelected(selected);
          } else {
            // 선택 삭제
            let index = -1;
            for (let i = 0; i < selected.length; i++) {
              if (selected[i].id === item.id) {
                index = i;
                break;
              }
            }
            if (index !== -1) {
              setSelected(
                selected
                  .slice(0, index)
                  .concat(selected.slice(index + 1, selected.length))
              );
            }
          }
        },
        [setSelected, selected]
      );
      useImperativeHandle(ref, () => ({ onInit }));
      const load = useCallback(async () => {
        const arr80 = await getCodes("80", 3);
        const arr161 = await getCodes("161", 3);
        const arr162 = await getCodes("162", 3);
        setList80(arr80);
        setList161(arr161);
        setList162(arr162);
      }, [getCodes, setList80, setList161, setList162]);
      useEffect(() => {
        (async () => {
          await load();
        })();
      }, [load]);
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
                <h5 className="modal-title" id={id.concat("Label")}>
                  어종선택
                </h5>
                <a onClick={() => onInit([])} className="nav-right">
                  <img
                    src="/assets/cust/img/svg/navbar-refresh.svg"
                    alt="Refresh"
                  />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <h6 className="modal-title-sub">대표어종</h6>
                  {list80 &&
                    list80.map((data, index) => (
                      <div key={index} className="row">
                        {data &&
                          data.map((item, index2) => (
                            <div key={index2} className="col">
                              {item.id !== null && (
                                <label className="control checkbox">
                                  <input
                                    type="checkbox"
                                    className="add-contrast"
                                    data-role="collar"
                                    data-code={item.code}
                                    onChange={(e) =>
                                      onChange(e.target.checked, item)
                                    }
                                  />
                                  <span className="control-indicator"></span>
                                  <span className="control-text">
                                    {item.codeName}
                                  </span>
                                </label>
                              )}
                            </div>
                          ))}
                      </div>
                    ))}
                  <h6 className="modal-title-sub">두족류</h6>
                  {list161 &&
                    list161.map((data, index) => (
                      <div key={index} className="row">
                        {data &&
                          data.map((item, index2) => (
                            <div key={index2} className="col">
                              {item.id !== null && (
                                <label className="control checkbox">
                                  <input
                                    type="checkbox"
                                    className="add-contrast"
                                    data-role="collar"
                                    data-code={item.code}
                                    onChange={(e) =>
                                      onChange(e.target.checked, item)
                                    }
                                  />
                                  <span className="control-indicator"></span>
                                  <span className="control-text">
                                    {item.codeName}
                                  </span>
                                </label>
                              )}
                            </div>
                          ))}
                      </div>
                    ))}
                  <h6 className="modal-title-sub">기타어종</h6>
                  {list162 &&
                    list162.map((data, index) => (
                      <div key={index} className="row">
                        {data &&
                          data.map((item, index2) => (
                            <div key={index2} className="col">
                              {item.id !== null && (
                                <label className="control checkbox">
                                  <input
                                    type="checkbox"
                                    className="add-contrast"
                                    data-role="collar"
                                    data-code={item.code}
                                    onChange={(e) =>
                                      onChange(e.target.checked, item)
                                    }
                                  />
                                  <span className="control-indicator"></span>
                                  <span className="control-text">
                                    {item.codeName}
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
