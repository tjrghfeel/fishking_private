/* eslint-disable jsx-a11y/anchor-is-valid */
import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useState,
} from "react";
import { inject, observer } from "mobx-react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./SelectDateModal.css";

export default inject()(
  observer(
    forwardRef(({ id, onSelected }, ref) => {
      const [date, setDate] = useState(new Date());
      const onInit = useCallback(() => {
        setDate(new Date());
      }, [setDate]);
      useImperativeHandle(ref, () => ({
        onInit,
      }));
      return (
        <div
          className="modal fade modal-full"
          id={id}
          data-modal-type={"SelectDateModal"}
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
                  날짜선택
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <Calendar value={date} onChange={setDate} />
              </div>
              <a
                onClick={() => (onSelected ? onSelected(date) : null)}
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
