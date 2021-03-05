import React, {
  useState,
  useCallback,
  useImperativeHandle,
  forwardRef,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("APIStore")(
  observer(
    forwardRef(({ APIStore, id = "", onSelected }, ref) => {
      const [list, setList] = useState([]);
      const [selected, setSelected] = useState(null);
      const load = useCallback(
        async (type) => {
          setList([]);
          const resolve = await APIStore._get(`/v2/api/searchPointList`, {
            type,
          });
          setList(resolve);
        },
        [setList]
      );
      useImperativeHandle(ref, () => ({ load }));
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
                  지역선택
                </h5>
              </div>
              <div className="modal-body">
                <div className="row-region-col-two">
                  <div className="col">
                    <ul className="region col2">
                      {list.map((data, index) => (
                        <li
                          key={index}
                          onClick={(e) => {
                            for (let li of document.querySelectorAll(
                              `#${id} li`
                            )) {
                              if (e.target.parentElement !== li)
                                li.classList.remove("active");
                            }
                            e.target.parentElement.classList.add("active");
                            setSelected(data);
                          }}
                        >
                          {data["isAlerted"] && (
                            <a>
                              <span className="icon icon-alarm on"></span>
                            </a>
                          )}
                          <a className="link">{data["observerName"]}</a>
                        </li>
                      ))}
                    </ul>
                  </div>
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
