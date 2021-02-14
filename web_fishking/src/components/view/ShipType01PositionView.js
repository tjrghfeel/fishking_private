import React, {
  useState,
  useCallback,
  forwardRef,
  useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    forwardRef(({ name, personCount = 0 }, ref) => {
      const [selected, setSelected] = useState([]);
      const onChange = useCallback(
        (slot) => {
          if (selected.includes(slot)) {
            const index = selected.indexOf(slot);
            const bef = selected.splice(0, index);
            const aft = selected.splice(index + 1, selected.length);
            setSelected(bef.concat(aft));
            document.querySelector(`.slot-${slot}`).classList.remove("active");
          } else {
            selected.push(slot);
            setSelected(selected);
            document.querySelector(`.slot-${slot}`).classList.add("active");
          }
        },
        [selected, setSelected]
      );
      useImperativeHandle(ref, () => ({ selected }));
      return (
        <div className="card-round-box-grey bg-blue">
          <h6 className="card-header-white text-center">
            {name} {Intl.NumberFormat().format(personCount)}명
          </h6>
          <div className="text-center">
            <div className="boatWap">
              <img src="/assets/cust/img/svg/boat01.svg" alt="" />
              <a onClick={() => onChange(1)}>
                <span
                  className={"slot-1 boat-position"}
                  style={{ top: "100px", left: "60px" }}
                >
                  1
                </span>
              </a>
              <a onClick={() => onChange(2)}>
                <span
                  className={"slot-2 boat-position"}
                  style={{ top: "150px", left: "50px" }}
                >
                  2
                </span>
              </a>
              <a onClick={() => onChange(3)}>
                <span
                  className={"slot-3 boat-position"}
                  style={{ top: "200px", left: "50px" }}
                >
                  3
                </span>
              </a>
              <a onClick={() => onChange(4)}>
                <span
                  className={"slot-4 boat-position"}
                  style={{ top: "250px", left: "50px" }}
                >
                  4
                </span>
              </a>
              <a onClick={() => onChange(5)}>
                <span
                  className={"slot-5 boat-position"}
                  style={{ top: "300px", left: "50px" }}
                >
                  5
                </span>
              </a>
              <a onClick={() => onChange(6)}>
                <span
                  className={"slot-6 boat-position"}
                  style={{ top: "350px", left: "50px" }}
                >
                  6
                </span>
              </a>
              <a onClick={() => onChange(7)}>
                <span
                  className={"slot-7 boat-position"}
                  style={{ top: "400px", left: "50px" }}
                >
                  7
                </span>
              </a>
              <a onClick={() => onChange(8)}>
                <span
                  className={"slot-8 boat-position"}
                  style={{ top: "450px", left: "50px" }}
                >
                  8
                </span>
              </a>
            </div>
          </div>
        </div>
      );
    })
  )
);
