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
                  <nav className="nav nav-pills nav-sel nav-col-3 mt-4">
                    <a
                      className={
                        "nav-link" +
                        (selected === "인기순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("인기순")}
                    >
                      <figure>
                        <img src="/assets/img/svg/sort-best.svg" alt="인기순" />
                      </figure>
                      <span>인기순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "거리순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("거리순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-distance.svg"
                          alt="거리순"
                        />
                      </figure>
                      <span>거리순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "낮은가격순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("낮은가격순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-lowprice.svg"
                          alt="낮은가격순"
                        />
                      </figure>
                      <span>낮은가격순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "높은가격순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("높은가격순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-highprice.svg"
                          alt="높은가격순"
                        />
                      </figure>
                      <span>높은가격순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "리뷰순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("리뷰순")}
                    >
                      <figure>
                        <img
                          src="/assets/img/svg/sort-review.svg"
                          alt="리뷰순"
                        />
                      </figure>
                      <span>리뷰순</span>
                    </a>
                    <a
                      className={
                        "nav-link" +
                        (selected === "판매순" ? " ".concat("active") : "")
                      }
                      onClick={() => setSelected("판매순")}
                    >
                      <figure>
                        <img src="/assets/img/svg/sort-sell.svg" alt="판매순" />
                      </figure>
                      <span>판매순</span>
                    </a>
                  </nav>
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
