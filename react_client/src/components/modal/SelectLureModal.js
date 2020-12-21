/* eslint-disable jsx-a11y/anchor-is-valid */
import React, {
  forwardRef,
  useCallback,
  useEffect,
  useImperativeHandle,
  useState,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("CodeStore")(
  observer(
    forwardRef(
      ({ CodeStore: { REST_GET_commonCode_group }, id, onSelected }, ref) => {
        const [list, setList] = useState([]);
        /** 선택 데이터 */
        const [selected, setSelected] = useState([]);
        /** 선택 변경 */
        const onChange = useCallback(
          (data) => {
            if (selected.indexOf(data) === -1) {
              selected.push(data);
              setSelected(selected);
            } else {
              const index = selected.indexOf(data);
              setSelected(
                selected
                  .slice(0, index)
                  .concat(selected.slice(index + 1, selected.length))
              );
            }
          },
          [selected, setSelected]
        );
        /** 초기화 */
        const onInit = useCallback(() => {
          setSelected([]);
          const elements = document.querySelectorAll(
            "#".concat(id).concat(' input[type="checkbox"]')
          );
          for (let element of elements) {
            element.checked = false;
          }
        }, [id, setSelected]);
        useImperativeHandle(ref, () => ({
          onInit,
        }));
        useEffect(() => {
          (async () => {
            const resolve = await REST_GET_commonCode_group(89, 2);
            setList(resolve);
          })();
        }, [REST_GET_commonCode_group, setList]);
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
                  <a href="#none" data-dismiss="modal" className="nav-left">
                    <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
                  </a>
                  <h5 className="modal-title" id={id.concat("Label")}>
                    미끼
                  </h5>
                  <a onClick={onInit} className="nav-right">
                    <img
                      src="/assets/img/svg/navbar-refresh.svg"
                      alt="Refresh"
                    />
                    <span>초기화</span>
                  </a>
                </div>
                <div className="modal-body">
                  <div className="padding">
                    <p className="mt-3"></p>

                    {list &&
                      list.map((data, index) => (
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
                                      onChange={() => onChange(item)}
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
      }
    )
  )
);
