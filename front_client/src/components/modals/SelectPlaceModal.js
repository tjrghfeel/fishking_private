import React, {
  useState,
  useCallback,
  useEffect,
  forwardRef,
  useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    forwardRef(({ id, onSelected, DataStore: { getEnums } }, ref) => {
      const [selected, setSelected] = useState(null);
      const [list] = useState([
        { key: "ship", value: "선상" },
        { key: "sealocks", value: "갯바위" },
      ]);
      const clearActive = (target) => {
        const elements = document.querySelectorAll(
          "#".concat(id) + " a.nav-link"
        );
        for (let element of elements) {
          element.classList.remove("active");
        }
        if (target) {
          target.classList.add("active");
        }
      };
      const onInit = useCallback(() => {
        setSelected(null);
        clearActive();
      }, [setSelected]);
      const onChange = useCallback(
        (e, item) => {
          setSelected(item);
          clearActive(e.target);
        },
        [setSelected]
      );
      useImperativeHandle(ref, () => ({ onInit }));
      return (
        // TODO : 공통 모달 > 낚시장소 : 선상, 갯바위에 대한 데이터가 없음. 선상 = ship, 갯바위 = sealocks 로 대입하겠음.
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
                  물때
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="container nopaddingy">
                  <ul className="col-two mt-3">
                    {list.length > 0 &&
                      list.map((data, index) => (
                        <a
                          key={index}
                          className="nav-link"
                          onClick={(e) => onChange(e, data)}
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
