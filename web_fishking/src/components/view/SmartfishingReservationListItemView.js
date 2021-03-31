import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        profileImage,
        username,
        shipName,
        goodsName,
        orderNumber,
        fishingDate,
        orderDate,
        reservePersonnel = 0,
        totalAmount = 0,
        status,
      },
      data,
      onClick,
      onClickApprove,
    }) => {
      return (
        <div className="container nopadding mt-2">
          <div
            className={
              status === "예약 취소" ? "card-round-red" : "card-round-grey"
            }
          >
            <div className="card card-sm">
              <div className="row no-gutters d-flex align-items-center">
                <div className="cardProfileWrap text-center">
                  <img
                    src={profileImage}
                    className="profile-thumb-md align-self-center mt-3 mb-1"
                    alt="profile"
                  />
                  <br />
                  <strong>{username}</strong>
                </div>
                <div className="cardProfileInfoWrap">
                  <div className="card-body">
                    <h6>
                      {shipName} <small className="grey">| {goodsName}</small>
                    </h6>
                    <p>
                      <a onClick={() => (onClick ? onClick(data) : null)}>
                        <strong className="text-primary">
                          예약번호: {orderNumber}
                        </strong>
                      </a>
                      <br />
                      출조일: {fishingDate}
                      <br />
                      결제일: {orderDate}
                      <br />
                      예약인원: {Intl.NumberFormat().format(reservePersonnel)}명
                      <br />
                      결제금액: {Intl.NumberFormat().format(totalAmount)}원
                      <br />
                      <strong
                        className={
                          status === "예약 취소"
                            ? "text-danger"
                            : "text-primary"
                        }
                      >
                        {status}
                      </strong>
                    </p>
                  </div>
                </div>
                {status === "예약 진행중" && (
                  <div className="cardProfileStatusWrap">
                    <a
                      onClick={() =>
                        onClickApprove ? onClickApprove(data) : null
                      }
                      className="btn btn-primary btn-sm"
                    >
                      예약승인
                    </a>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);