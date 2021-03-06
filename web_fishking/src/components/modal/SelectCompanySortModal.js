import React, {
  useState,
  useCallback,
  useImperativeHandle,
  forwardRef,
} from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    forwardRef(({ id, onSelected }, ref) => {
      const list = [
        {
          text: "인기순",
          value: "popular",
          imgSrc: "/assets/cust/img/svg/sort-best.svg",
        },
        {
          text: "거리순",
          value: "distance",
          imgSrc: "/assets/cust/img/svg/sort-distance.svg",
        },
        {
          text: "낮은가격순",
          value: "lowPrice",
          imgSrc: "/assets/cust/img/svg/sort-lowprice.svg",
        },
        {
          text: "높은가격순",
          value: "highPrice",
          imgSrc: "/assets/cust/img/svg/sort-highprice.svg",
        },
        {
          text: "리뷰순",
          value: "review",
          imgSrc: "/assets/cust/img/svg/sort-review.svg",
        },
        {
          text: "판매순",
          value: "sell",
          imgSrc: "/assets/cust/img/svg/sort-sell.svg",
        },
      ];
      const [selected, setSelected] = useState(0);
      const onChange = useCallback(
        (index) => {
          setSelected(index);
        },
        [setSelected]
      );
      const onInit = useCallback(() => {
        setSelected(0);
      }, [setSelected]);
      useImperativeHandle(ref, () => ({ onInit }));
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
                  정렬선택
                </h5>
                <a className="nav-right" onClick={onInit}>
                  <img
                    src="/assets/cust/img/svg/navbar-refresh.svg"
                    alt="Refresh"
                  />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <ul className="nav nav-pills nav-sel nav-col-3 mt-4">
                    {list.map((data, index) => (
                      <a
                        className={
                          "nav-link" + (selected === index ? " active" : "")
                        }
                        onClick={() => onChange(index)}
                      >
                        <figure>
                          <img src={data.imgSrc} alt={data.text} />
                        </figure>
                        <span>{data.text}</span>
                      </a>
                    ))}
                  </ul>
                </div>
              </div>
              <a
                onClick={() => (onSelected ? onSelected(list[selected]) : null)}
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
