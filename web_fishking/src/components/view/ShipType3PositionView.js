import React, {
  useState,
  useCallback,
  forwardRef,
  useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    forwardRef(
      ({ data: { total, used } = { total: [], used: [] }, count = 0 }, ref) => {
        const [selected, setSelected] = useState([]);
        const onSelect = useCallback(
          (num) => {
            if (used.indexOf(num) !== -1) return;

            if (selected.indexOf(num) === -1 && selected.length < count) {
              const arr = selected.concat(num);
              setSelected(arr);
            } else if (selected.indexOf(num) !== -1) {
              const index = selected.indexOf(num);
              const bef = selected.slice(0, index);
              const aft = selected.slice(index + 1, selected.length);
              setSelected(bef.concat(aft));
            }
          },
          [used, selected, setSelected]
        );
        useImperativeHandle(ref, () => ({ selected }));
        return (
          <React.Fragment>
            <div className="container nopadding bg-grey bg-grey-sm">
              <h5 className="bullet">위치선정</h5>
              <p>
                <small>원하시는 선상의 위치를 선택하세요.</small>
              </p>
              <br />
              <div className="card-round-box-grey bg-blue">
                <div className="text-center">
                  <div className="boatWap">
                    <img src="/assets/cust/img/svg/boat01.svg" alt="" />
                    <a onClick={() => onSelect("1")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("1") !== -1
                            ? " disabled"
                            : selected.indexOf("1") !== -1
                            ? " active"
                            : "")
                        }
                        style={{ top: "100px", left: "60px" }}
                      >
                        1
                      </span>
                    </a>
                    <a onClick={() => onSelect("2")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("2") ? " active" : "")
                        }
                        style={{ top: "235px", left: "50px" }}
                      >
                        2
                      </span>
                    </a>
                    <a onClick={() => onSelect("3")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("3") ? " active" : "")
                        }
                        style={{ top: "370px", left: "50px" }}
                      >
                        3
                      </span>
                    </a>
                    <a onClick={() => onSelect("4")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("4") ? " active" : "")
                        }
                        style={{ top: "505px", left: "50px" }}
                      >
                        4
                      </span>
                    </a>
                    <a onClick={() => onSelect("5")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("5") ? " active" : "")
                        }
                        style={{ top: "100px", left: "180px" }}
                      >
                        5
                      </span>
                    </a>
                    <a onClick={() => onSelect("6")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("6") ? " active" : "")
                        }
                        style={{ top: "235px", left: "190px" }}
                      >
                        6
                      </span>
                    </a>
                    <a onClick={() => onSelect("7")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("7") ? " active" : "")
                        }
                        style={{ top: "370px", left: "190px" }}
                      >
                        7
                      </span>
                    </a>
                    <a onClick={() => onSelect("8")}>
                      <span
                        className={
                          "boat-position" +
                          (selected.includes("8") ? " active" : "")
                        }
                        style={{ top: "505px", left: "190px" }}
                      >
                        8
                      </span>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    )
  )
);
