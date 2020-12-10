/* eslint-disable jsx-a11y/anchor-is-valid */
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
      /** 선택 데이터 */
      const [selected, setSelected] = useState(null);
      const onInit = useCallback(() => {
        setSelected(null);
      }, [setSelected]);
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
                  정렬선택
                </h5>
                <a onClick={onInit} className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <ul className="nav nav-pills nav-sel nav-col-3 mt-4">
                    <a
                      className={
                        "nav-link" +
                        (selected === "최신순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("최신순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-latest.svg"
                          alt="최신순"
                        />
                      </figure>
                      <span>최신순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "추천순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("추천순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-recommand.svg"
                          alt="추천순"
                        />
                      </figure>
                      <span>추천순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "댓글순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("댓글순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-comment.svg"
                          alt="댓글순"
                        />
                      </figure>
                      <span>댓글순</span>
                    </a>
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
