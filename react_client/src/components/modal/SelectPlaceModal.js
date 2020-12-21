/* eslint-disable jsx-a11y/anchor-is-valid */
import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useState,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("CodeStore")(
  observer(
    forwardRef(({ id, onSelected }, ref) => {
      const [list] = useState([
        { key: "boat", value: "선상" },
        { key: "rock", value: "갯바위" },
      ]);
      /** 선택 데이터 */
      const [selected, setSelected] = useState([]);
      /** 선택 변경 */
      const onChange = useCallback(
        (data) => {
          const key = data.key;
          let index = -1;

          for (let i = 0; i < selected.length; i++) {
            if (key === selected[i].key) {
              index = i;
              break;
            }
          }

          if (index === -1) {
            selected.push(data);
            setSelected(selected);
          } else {
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
          "#".concat(id).concat(" a.nav-link")
        );
        for (let element of elements) {
          element.classList.remove("active");
        }
      }, [id, setSelected]);
      useImperativeHandle(ref, () => ({
        onInit,
      }));
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
                  낚시 장소
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="container nopaddingy">
                  <ul className="col-two mt-3">
                    {list &&
                      list.map((data, index) => (
                        <a
                          key={index}
                          className={
                            "nav-link" +
                            (selected.indexOf(data.key) !== -1
                              ? " ".concat("active")
                              : "")
                          }
                          onClick={(e) => {
                            const classList = e.target.classList;
                            if (classList.contains("active")) {
                              e.target.classList.remove("active");
                            } else {
                              e.target.classList.add("active");
                            }
                            onChange(data);
                          }}
                        >
                          {data.value}
                        </a>
                      ))}
                  </ul>
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
