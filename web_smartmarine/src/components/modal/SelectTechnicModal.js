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
    forwardRef(({ DataStore: { getEnums }, id, onSelected }, ref) => {
      const [list, setList] = useState([]); // 어종 목록
      const [selected, setSelected] = useState([]); // 선택 목록
      const onInit = useCallback(() => {
        setSelected([]);
        const elements = document.querySelectorAll(
          "#".concat(id).concat(' input[type="checkbox"]')
        );
        for (let element of elements) {
          element.checked = false;
        }
      }, [setSelected]);
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
      useEffect(() => {
        (async () => {
          const codes = await getEnums("fishingTechnic", 2);
          setList(codes);
        })();
      }, [getEnums, setList]);
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
                <h5 className="modal-title" id={id.concat("Label")}>
                  낚시기법
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
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
                              {item.key !== null && (
                                <label className="control checkbox">
                                  <input
                                    type="checkbox"
                                    className="add-contrast"
                                    data-role="collar"
                                    onChange={(e) =>
                                      onChange(e.target.checked, item)
                                    }
                                  />
                                  <span className="control-indicator"></span>
                                  <span className="control-text">
                                    {item.value}
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
