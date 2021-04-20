import React, {
  useState,
  useEffect,
  useCallback,
  forwardRef,
  useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./SelectDateModal.css";

export default inject("DataStore")(
  observer(
    forwardRef(({ id, onSelected, until = null, inputDate = null }, ref) => {
      const [selected, setSelected] = useState(new Date());
      const [dateString, setDateString] = useState("");
      const formatDateString = (item) => {
        const year = item.getFullYear();
        const month =
          item.getMonth() + 1 < 10
            ? "0".concat(item.getMonth() + 1)
            : item.getMonth() + 1;
        const date =
          item.getDate() < 10 ? "0".concat(item.getDate()) : item.getDate();
        return year + "년 " + month + "월 " + date + "일";
      };
      const onInit = useCallback((init) => {
        let item = new Date();
        if (!init) {
          if (inputDate != null) {
            item = new Date(inputDate)
          }
        }
        setSelected(item);
        setDateString(formatDateString(item));
      }, [setSelected, setDateString]);
      const onChange = useCallback(
        (item) => {
          setSelected(item);
          setDateString(formatDateString(item));
        },
        [setSelected, setDateString]
      );
      useImperativeHandle(ref, () => ({ onInit }));
      useEffect(() => {
        onInit();
      }, [onInit]);
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
                  날짜선택
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img
                    src="/assets/cust/img/svg/navbar-refresh.svg"
                    alt="Refresh"
                  />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body" style={{ margin: "auto" }}>
                <Calendar
                  value={selected}
                  onChange={onChange}
                  maxDate={until}
                />
              </div>
              <div className="info-btm">
                <h5>{dateString}</h5>
                {/*<p>(음력) 06.26</p>*/}
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
