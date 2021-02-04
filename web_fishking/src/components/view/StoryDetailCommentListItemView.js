import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      index,
      data: {
        profileImage,
        nickName,
        writeTime,
        content,
        likeCount = 0,
        isLikeTo,
        isChildComment = false,
      },
      data,
      onClick,
    }) => {
      return (
        <React.Fragment>
          <div className="container nopadding">
            <div
              className={"row-story-col top" + (isChildComment ? " re" : "")}
            >
              <div className="col">
                {isChildComment && <span className="icon-re"></span>}
                <a
                  onClick={() =>
                    onClick ? onClick("profile", data, index) : null
                  }
                >
                  <figure>
                    <img src={profileImage} alt="" />
                  </figure>
                </a>
              </div>
              <div className="col">
                <strong>{nickName}</strong> ·{" "}
                <small className="grey">{writeTime.latest()}</small>
                <br />
                {content}
                <br />
                <small className="grey">
                  좋아요 {Intl.NumberFormat().format(likeCount)}개 ·{" "}
                  <a
                    onClick={() =>
                      onClick ? onClick("comment", data, index) : null
                    }
                    className="grey"
                  >
                    답글달기
                  </a>
                </small>
              </div>
              <div className="col">
                <a
                  onClick={() =>
                    onClick ? onClick("more", data, index) : null
                  }
                >
                  <img
                    src="/assets/cust/img/svg/icon-ellipsis.svg"
                    alt="메뉴더보기"
                  />
                </a>
                <br />
                <a
                  className={isLikeTo ? "active" : ""}
                  onClick={() =>
                    onClick ? onClick("like", data, index) : null
                  }
                >
                  <span className="icon icon-good mt-2"></span>
                </a>
              </div>
            </div>
            <hr className="full mt-3 mb-3" />
          </div>
        </React.Fragment>
      );
    }
  )
);
