import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        ordersStatus,
        shipImageUrl,
        shipName,
        fishingType,
        sigungu,
        distance = 0,
        fishingDate,
        ordersNum,
      },
      data,
      onClick,
      onClickFind,
      onClickAgain,
      onClickReview,
    }) => {
      let ordersStatusClassName = "status";
      if (ordersStatus === "예약 대기") ordersStatusClassName += " status2";
      else if (ordersStatus === "대기자 예약")
        ordersStatusClassName += " status1";
      else if (ordersStatus === "예약 확정")
        ordersStatusClassName += " status4";
      else if (ordersStatus === "취소 완료")
        ordersStatusClassName += " status6";
      else if (ordersStatus === "출조 완료")
        ordersStatusClassName += " status5";
      else if (ordersStatus === "예약 완료")
        ordersStatusClassName += " status3";
      return (
        <div className="container nopadding mt-2">
          <div className="card-round-grey">
            <span className={ordersStatusClassName}>{ordersStatus}</span>
            {(ordersStatus === "예약 확정" || ordersStatus === "예약 완료") && (
              <span className="dday">D{fishingDate.dday()}</span>
            )}
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <div className="card card-sm">
                <div className="row no-gutters">
                  <div className="cardimgWrap">
                    <img src={shipImageUrl} className="img-fluid" alt="" />
                  </div>
                  <div className="cardInfoWrap">
                    <div className="card-body">
                      <h6>{shipName}</h6>
                      <p>
                        <strong>{fishingType}</strong>
                        <br />
                        <span className="grey">
                          {sigungu} {Intl.NumberFormat().format(distance)}km
                        </span>
                      </p>
                    </div>
                  </div>
                </div>
                <hr className="mt-1 mb-1" />
                <div className="row no-gutters">
                  <div className="col-6 padding-sm">
                    <small className="grey">
                      이용일
                      <br />
                      예약번호
                    </small>
                  </div>
                  <div className="col-6 padding-sm">
                    <small>
                      2020년 08월 25일(화) 09:00 ~<br />
                      {ordersNum}
                    </small>
                  </div>
                </div>
              </div>
            </a>
            {ordersStatus === "예약 확정" && (
              <a
                onClick={() => (onClickFind ? onClickFind(data) : null)}
                className="btn btn-third btn-block btn-sm mt-1 mb-1"
              >
                길찾기
              </a>
            )}
            {ordersStatus === "취소 완료" && (
              <a
                onClick={() => (onClickAgain ? onClickAgain(data) : null)}
                className="btn btn-third btn-block btn-sm mt-1 mb-1"
              >
                다시예약하기
              </a>
            )}
            {ordersStatus === "출조 완료" && (
              <div className="row no-gutters">
                <div className="col-6 padding-sm">
                  <a
                    onClick={() => (onClickAgain ? onClickAgain(data) : null)}
                    className="btn btn-third btn-block btn-sm mt-1 mb-1"
                  >
                    다시예약하기
                  </a>
                </div>
                <div className="col-6 padding-sm">
                  <a
                    onClick={() => (onClickReview ? onClickReview(data) : null)}
                    className="btn btn-info btn-block btn-sm mt-1 mb-1"
                  >
                    리뷰작성
                  </a>
                </div>
              </div>
            )}
          </div>
        </div>
      );
    }
  )
);
