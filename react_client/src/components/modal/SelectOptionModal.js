import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useState,
} from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    forwardRef(({ id, onSelected }, ref) => {
      const [selected1, setSelected1] = useState([]); // 장르
      const [selected2, setSelected2] = useState([]); // 서비스 제공
      const [selected3, setSelected3] = useState(null); // 가격
      const [selected4, setSelected4] = useState([]); // 편의시설
      const [select4Str, setSelect4Str] = useState(""); // 편의시설 문자열
      /** 선택 변경 */
      const onChange = useCallback(
        (optionType, item) => {
          if (optionType === "1") {
            if (selected1.indexOf(item) === -1) {
              selected1.push(item);
              setSelected1(selected1);
            } else {
              const index = selected1.indexOf(item);
              setSelected1(
                selected1
                  .slice(0, index)
                  .concat(selected1.slice(index + 1, selected1.length))
              );
            }
          } else if (optionType === "2") {
            if (selected2.indexOf(item) === -1) {
              selected2.push(item);
              setSelected2(selected2);
            } else {
              const index = selected2.indexOf(item);
              setSelected2(
                selected2
                  .slice(0, index)
                  .concat(selected2.slice(index + 1, selected2.length))
              );
            }
          } else if (optionType === "3") {
            setSelected3(item);
          } else if (optionType === "4") {
            if (selected4.indexOf(item) === -1) {
              selected4.push(item);
              setSelected4(selected4);
              setSelect4Str(selected4);
            } else {
              const index = selected4.indexOf(item);
              setSelected4(
                selected4
                  .slice(0, index)
                  .concat(selected4.slice(index + 1, selected4.length))
              );
              setSelect4Str(
                selected4
                  .slice(0, index)
                  .concat(selected4.slice(index + 1, selected4.length))
              );
            }
          }
        },
        [
          selected1,
          setSelected1,
          selected2,
          setSelected2,
          selected3,
          setSelected3,
          selected4,
          setSelected4,
          setSelect4Str,
        ]
      );
      /** 초기화 */
      const onInit = useCallback(() => {
        setSelected1([]);
        setSelected2([]);
        setSelected3("");
        setSelected4([]);

        let elements = document.querySelectorAll(
          "#".concat(id).concat(' input[type="checkbox"]')
        );
        for (let element of elements) {
          element.checked = false;
        }

        elements = document.querySelectorAll(
          "#".concat(id).concat(" a.nav-link")
        );
        for (let element of elements) {
          element.className = "nav-link";
        }
      }, [id, setSelected1, setSelected2, setSelected3, setSelected4]);
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
                  옵션선택
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <h6 className="modal-title-sub">장르</h6>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                          onChange={() => onChange("1", "에깅")}
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">에깅</span>
                      </label>
                    </div>
                  </div>

                  <h6 className="modal-title-sub">서비스 제공</h6>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                          onChange={() => onChange("2", "1인 추가")}
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">1인 추가</span>
                      </label>
                    </div>
                  </div>

                  <h6 className="modal-title-sub">가격</h6>
                  <ul className="nav nav-pills nav-fill nav-sel nav-box mt-4">
                    <a
                      className={
                        "nav-link" +
                        (selected3 === null ? " ".concat("active") : "")
                      }
                      onClick={() => onChange("3", null)}
                    >
                      전체
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected3 === 5 ? " ".concat("active") : "")
                      }
                      onClick={() => onChange("3", 5)}
                    >
                      ~ 5만
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected3 === 10 ? " ".concat("active") : "")
                      }
                      onClick={() => onChange("3", 10)}
                    >
                      5 ~ 10만
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected3 === 30 ? " ".concat("active") : "")
                      }
                      onClick={() => onChange("3", 30)}
                    >
                      10 ~ 30만
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected3 === 999 ? " ".concat("active") : "")
                      }
                      onClick={() => onChange("3", 999)}
                    >
                      30만 ~
                    </a>
                  </ul>

                  <h6 className="modal-title-sub">편의시설 {select4Str}</h6>
                  <ul className="nav nav-pills nav-sel nav-col-4 mt-4">
                    <a
                      className={
                        "nav-link" +
                        (select4Str.indexOf("WIFI") !== -1
                          ? " ".concat("active")
                          : "")
                      }
                      onClick={() => onChange("4", "WIFI")}
                    >
                      <figure>
                        <img
                          src="/assets/img/facility/facility-wifi.svg"
                          alt="WIFI"
                        />
                      </figure>
                      <span>WIFI</span>
                    </a>
                  </ul>
                </div>
              </div>
              <a
                onClick={() =>
                  onSelected
                    ? onSelected({ selected1, selected2, selected3, selected4 })
                    : null
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
