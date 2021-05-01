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
        personnel,
      },
      data,
      onClick, // 상세
      onClickCancel, // 취소하기
      onClickReview, // 리뷰작성
      onClickReservation, // 다시예약
      onClickMap, // 길찾기
    }) => {
      // 취소가능일자
      let cancelable = true;
      if (fishingDate) {
        const minDate = new Date();
        minDate.setDate(minDate.getDate() + 2);
        const goDate = new Date(
          fishingDate.substr(0, 4),
          new Number(fishingDate.substr(4, 2)) - 1,
          fishingDate.substr(6, 2)
        );
        if (minDate.getTime() <= goDate.getTime()) {
          cancelable = true;
        }
      }

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
              <React.Fragment>
                <span className="dday">D{fishingDate.dday()}</span>
                <span className="dday" style={{top: '54px'}}>{personnel} 명</span>
              </React.Fragment>
            )}
            {!(ordersStatus === "예약 확정" || ordersStatus === "예약 완료") && (
              <span className="dday">{personnel} 명</span>
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
                        <br />
                        <span className="grey">{sigungu}</span>
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
                      {fishingDate && (
                        <React.Fragment>
                          {fishingDate.substr(0, 4)}년{" "}
                          {fishingDate.substr(4, 2)}월{" "}
                          {fishingDate.substr(6, 2)}일{" "}
                          {fishingDate.substr(9, 2)}:{fishingDate.substr(11, 2)}{" "}
                          ~<br />
                        </React.Fragment>
                      )}
                      {ordersNum}
                    </small>
                  </div>
                </div>
              </div>
            </a>
            {cancelable &&
              (ordersStatus === "대기자 예약" ||
                ordersStatus === "예약 대기" ||
                ordersStatus === "예약 진행중" ||
                ordersStatus === "예약 완료") && (
                <a
                  onClick={() => (onClickCancel ? onClickCancel(data) : null)}
                  className="btn btn-third btn-block btn-sm mt-1 mb-1"
                >
                  취소하기
                </a>
              )}
            {ordersStatus === "취소 완료" && (
              <a
                onClick={() =>
                  onClickReservation ? onClickReservation(data) : null
                }
                className="btn btn-third btn-block btn-sm mt-1 mb-1"
              >
                다시예약하기
              </a>
            )}
            {ordersStatus === "출조 완료" && (
              <div className="row no-gutters">
                <div className="col-6 padding-sm">
                  <a
                    onClick={() =>
                      onClickReservation ? onClickReservation(data) : null
                    }
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
            {ordersStatus === "예약 확정" && (
              <a
                onClick={() => (onClickMap ? onClickMap(data) : null)}
                className="btn btn-third btn-block btn-sm mt-1 mb-1"
              >
                길찾기
              </a>
            )}
          </div>
        </div>
      );
    }
  )
);
