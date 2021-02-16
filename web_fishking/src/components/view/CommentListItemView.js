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
      },
    }) => {
      return (
        <div className="container nopadding">
          <div className="row-story-col top">
            <div className="col">
              <a href="profile-user.html">
                <figure>
                  <img src="/assets/cust/img/sample/profile2.jpg" alt="" />
                </figure>
              </a>
            </div>
            <div className="col">
              <strong>바다공주</strong> · <small className="grey">11분전</small>
              <br />
              멋지네요 낚시 가고 싶어요.
              <br />
              <small className="grey">
                좋아요 0개 ·{" "}
                <a href="#none" className="grey">
                  답글달기
                </a>
              </small>
            </div>
            <div className="col">
              <a href="#none" data-toggle="modal" data-target="#ellipsisModal">
                <img
                  src="/assets/cust/img/svg/icon-ellipsis.svg"
                  alt="메뉴더보기"
                />
              </a>
              <br />
              <a className="" href="#none">
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
