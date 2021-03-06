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
      (
        {
          data: { total, used } = { total: [], used: [] },
          editable = true,
          count = 0,
          onChange,
        },
        ref
      ) => {
        const [selected, setSelected] = useState([]);
        const onSelect = useCallback(
          (num) => {
            if (!editable) return;

            if (used.indexOf(num) !== -1) return;

            if (selected.indexOf(num) === -1 && selected.length < count) {
              const arr = selected.concat(num);
              setSelected(arr);
              if (onChange) onChange(arr);
            } else if (selected.indexOf(num) !== -1) {
              const index = selected.indexOf(num);
              const bef = selected.slice(0, index);
              const aft = selected.slice(index + 1, selected.length);
              setSelected(bef.concat(aft));
              if (onChange) onChange(bef.concat(aft));
            }
          },
          [used, selected, setSelected, onChange]
        );
        const onSetSelected = useCallback(
          (selectedData) => {
            setSelected(selectedData);
          },
          [setSelected]
        );
        useImperativeHandle(ref, () => ({ selected, onSetSelected }));
        return (
          <React.Fragment>
            <div className="container nopadding bg-grey bg-grey-sm">
              <div className="card-round-box-grey bg-blue">
                <div className="text-center">
                  <div className="boatWap">
                    <img src="/assets/cust/img/svg/boat01.svg" alt="" />
                    <a onClick={() => onSelect("1")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("1") !== -1 && !editable
                            ? " active"
                            : used.indexOf("1") !== -1
                            ? " disabled"
                            : selected.indexOf("1") !== -1
                            ? " active"
                                : total.indexOf("1") === -1
                                  ? " disabled"
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
                          (used.indexOf("2") !== -1 && !editable
                            ? " active"
                            : used.indexOf("2") !== -1
                            ? " disabled"
                            : selected.indexOf("2") !== -1
                            ? " active"
                                : total.indexOf("2") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "145px", left: "50px" }}
                      >
                        2
                      </span>
                    </a>
                    <a onClick={() => onSelect("3")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("3") !== -1 && !editable
                            ? " active"
                            : used.indexOf("3") !== -1
                            ? " disabled"
                            : selected.indexOf("3") !== -1
                            ? " active"
                                : total.indexOf("3") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "190px", left: "50px" }}
                      >
                        3
                      </span>
                    </a>
                    <a onClick={() => onSelect("4")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("4") !== -1 && !editable
                            ? " active"
                            : used.indexOf("4") !== -1
                            ? " disabled"
                            : selected.indexOf("4") !== -1
                            ? " active"
                                : total.indexOf("4") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "235px", left: "50px" }}
                      >
                        4
                      </span>
                    </a>
                    <a onClick={() => onSelect("5")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("5") !== -1 && !editable
                            ? " active"
                            : used.indexOf("5") !== -1
                            ? " disabled"
                            : selected.indexOf("5") !== -1
                            ? " active"
                                : total.indexOf("5") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "280px", left: "50px" }}
                      >
                        5
                      </span>
                    </a>
                    <a onClick={() => onSelect("6")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("6") !== -1 && !editable
                            ? " active"
                            : used.indexOf("6") !== -1
                            ? " disabled"
                            : selected.indexOf("6") !== -1
                            ? " active"
                                : total.indexOf("6") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "325px", left: "50px" }}
                      >
                        6
                      </span>
                    </a>
                    <a onClick={() => onSelect("7")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("7") !== -1 && !editable
                            ? " active"
                            : used.indexOf("7") !== -1
                            ? " disabled"
                            : selected.indexOf("7") !== -1
                            ? " active"
                                : total.indexOf("7") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "370px", left: "50px" }}
                      >
                        7
                      </span>
                    </a>
                    <a onClick={() => onSelect("8")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("8") !== -1 && !editable
                            ? " active"
                            : used.indexOf("8") !== -1
                            ? " disabled"
                            : selected.indexOf("8") !== -1
                            ? " active"
                                : total.indexOf("8") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "415px", left: "50px" }}
                      >
                        8
                      </span>
                    </a>
                    <a onClick={() => onSelect("9")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("9") !== -1 && !editable
                            ? " active"
                            : used.indexOf("9") !== -1
                            ? " disabled"
                            : selected.indexOf("9") !== -1
                            ? " active"
                                : total.indexOf("9") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "460px", left: "50px" }}
                      >
                        9
                      </span>
                    </a>
                    <a onClick={() => onSelect("10")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("10") !== -1 && !editable
                            ? " active"
                            : used.indexOf("10") !== -1
                            ? " disabled"
                            : selected.indexOf("10") !== -1
                            ? " active"
                                : total.indexOf("10") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "505px", left: "50px" }}
                      >
                        10
                      </span>
                    </a>
                    <a onClick={() => onSelect("11")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("11") !== -1 && !editable
                            ? " active"
                            : used.indexOf("11") !== -1
                            ? " disabled"
                            : selected.indexOf("11") !== -1
                            ? " active"
                                : total.indexOf("11") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "550px", left: "50px" }}
                      >
                        11
                      </span>
                    </a>

                    <a onClick={() => onSelect("12")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("12") !== -1 && !editable
                            ? " active"
                            : used.indexOf("12") !== -1
                            ? " disabled"
                            : selected.indexOf("12") !== -1
                            ? " active"
                                : total.indexOf("12") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "100px", left: "180px" }}
                      >
                        12
                      </span>
                    </a>
                    <a onClick={() => onSelect("13")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("13") !== -1 && !editable
                            ? " active"
                            : used.indexOf("13") !== -1
                            ? " disabled"
                            : selected.indexOf("13") !== -1
                            ? " active"
                                : total.indexOf("13") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "145px", left: "190px" }}
                      >
                        13
                      </span>
                    </a>
                    <a onClick={() => onSelect("14")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("14") !== -1 && !editable
                            ? " active"
                            : used.indexOf("14") !== -1
                            ? " disabled"
                            : selected.indexOf("14") !== -1
                            ? " active"
                                : total.indexOf("14") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "190px", left: "190px" }}
                      >
                        14
                      </span>
                    </a>
                    <a onClick={() => onSelect("15")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("15") !== -1 && !editable
                            ? " active"
                            : used.indexOf("15") !== -1
                            ? " disabled"
                            : selected.indexOf("15") !== -1
                            ? " active"
                                : total.indexOf("15") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "235px", left: "190px" }}
                      >
                        15
                      </span>
                    </a>
                    <a onClick={() => onSelect("16")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("16") !== -1 && !editable
                            ? " active"
                            : used.indexOf("16") !== -1
                            ? " disabled"
                            : selected.indexOf("16") !== -1
                            ? " active"
                                : total.indexOf("16") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "280px", left: "190px" }}
                      >
                        16
                      </span>
                    </a>
                    <a onClick={() => onSelect("17")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("17") !== -1 && !editable
                            ? " active"
                            : used.indexOf("17") !== -1
                            ? " disabled"
                            : selected.indexOf("17") !== -1
                            ? " active"
                                : total.indexOf("17") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "325px", left: "190px" }}
                      >
                        17
                      </span>
                    </a>
                    <a onClick={() => onSelect("18")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("18") !== -1 && !editable
                            ? " active"
                            : used.indexOf("18") !== -1
                            ? " disabled"
                            : selected.indexOf("18") !== -1
                            ? " active"
                                : total.indexOf("18") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "370px", left: "190px" }}
                      >
                        18
                      </span>
                    </a>
                    <a onClick={() => onSelect("19")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("19") !== -1 && !editable
                            ? " active"
                            : used.indexOf("19") !== -1
                            ? " disabled"
                            : selected.indexOf("19") !== -1
                            ? " active"
                                : total.indexOf("19") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "415px", left: "190px" }}
                      >
                        19
                      </span>
                    </a>
                    <a onClick={() => onSelect("20")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("20") !== -1 && !editable
                            ? " active"
                            : used.indexOf("20") !== -1
                            ? " disabled"
                            : selected.indexOf("20") !== -1
                            ? " active"
                                : total.indexOf("20") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "460px", left: "190px" }}
                      >
                        20
                      </span>
                    </a>
                    <a onClick={() => onSelect("21")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("21") !== -1 && !editable
                            ? " active"
                            : used.indexOf("21") !== -1
                            ? " disabled"
                            : selected.indexOf("21") !== -1
                            ? " active"
                                : total.indexOf("21") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "505px", left: "190px" }}
                      >
                        21
                      </span>
                    </a>
                    <a onClick={() => onSelect("22")}>
                      <span
                        className={
                          "boat-position" +
                          (used.indexOf("22") !== -1 && !editable
                            ? " active"
                            : used.indexOf("22") !== -1
                            ? " disabled"
                            : selected.indexOf("22") !== -1
                            ? " active"
                                : total.indexOf("22") === -1
                                  ? " disabled"
                                  : "")
                        }
                        style={{ top: "550px", left: "190px" }}
                      >
                        22
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
