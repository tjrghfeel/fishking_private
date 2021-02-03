import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      isLikeTo = false,
      likeCount = 0,
      commentCount = 0,
      onClickLike,
      onClickComment,
      onClickShare,
      onClickReservation,
    }) => {
      return (
        <div className="tab_barwrap fixed-bottom">
          <div className="container nopadding">
            <nav className="nav nav-pills nav-tab nav-justified">
              <a
                className={"nav-link" + (isLikeTo ? " active" : "")}
                onClick={() => (onClickLike ? onClickLike() : null)}
              >
                <span className="icon icon-good"></span>{" "}
                {Intl.NumberFormat().format(likeCount)}
              </a>
              <a
                onClick={() => (onClickComment ? onClickComment() : null)}
                className="nav-link"
              >
                <span className="icon icon-comment"></span>{" "}
                {Intl.NumberFormat().format(commentCount)}
              </a>
              <a
                onClick={() => (onClickShare ? onClickShare() : null)}
                className="nav-link"
              >
                <span className="icon icon-share"></span>
              </a>
              <a
                className="nav-link btn btn-primary btn-sm-nav-tab"
                onClick={() =>
                  onClickReservation ? onClickReservation() : null
                }
              >
                예약하기
              </a>
            </nav>
          </div>
        </div>
      );
    }
  )
);
