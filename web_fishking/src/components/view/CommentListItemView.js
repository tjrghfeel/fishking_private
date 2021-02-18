import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        profileImage,
        nickName,
        writeTime,
        content,
        likeCount = 0,
        isChildComment = false,
        isLikeTo = false,
        isMine = false,
      },
      data,
      onClickReply,
      onClickMore,
      onClickLike,
    }) => {
      return (
        <div className="container nopadding">
          <div className={"row-story-col top" + (isChildComment ? " re" : "")}>
            <div className="col">
              {isChildComment && <span className="icon-re"></span>}
              <a href="profile-user.html">
                <figure>
                  <img src={profileImage} alt="" />
                </figure>
              </a>
            </div>
            <div className="col">
              <strong>{nickName}</strong> ·{" "}
              <small className="grey">{writeTime && writeTime.latest()}</small>
              <br />
              {content}
              <br />
              <small className="grey">
                좋아요 {Intl.NumberFormat().format(likeCount)}개 ·{" "}
                <a
                  onClick={() => (onClickReply ? onClickReply(data) : null)}
                  className="grey"
                >
                  답글달기
                </a>
              </small>
            </div>
            <div className="col">
              {isMine && (
                <a onClick={() => (onClickMore ? onClickMore(data) : null)}>
                  <img
                    src="/assets/cust/img/svg/icon-ellipsis.svg"
                    alt="메뉴더보기"
                  />
                </a>
              )}
              <br />
              <a
                className={isLikeTo ? "active" : ""}
                onClick={() => (onClickLike ? onClickLike(data) : null)}
              >
                <span className="icon icon-good mt-2"></span>
              </a>
            </div>
          </div>
          <hr className="full mt-3 mb-3" />
        </div>
      );
    }
  )
);
