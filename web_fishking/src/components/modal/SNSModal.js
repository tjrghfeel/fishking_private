import React from "react";
import { inject, observer } from "mobx-react";

export default inject("ModalStore")(
  observer(({ ModalStore: { onSelect } }) => {
    return (
      <div
        className="modal fade modal-full modal-sns"
        id="snsModal"
        tabIndex="-1"
        aria-labelledby="snsModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-body text-center">
              <a className="nav-expander" data-dismiss="modal">
                <img src="/assets/img/svg/icon_close_grey.svg" alt="팝업닫기" />
              </a>
              <h4>공유하기</h4>
              <p className="clearfix"></p>
              <ul className="sns">
                <li>
                  <a onClick={() => (onSelect ? onSelect("KAKAO") : null)}>
                    <figure>
                      <img
                        src="/assets/img/svg/sns_kakaotalk.svg"
                        alt="카카오톡"
                      />
                    </figure>
                    <span>카카오톡</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => (onSelect ? onSelect("FACEBOOK") : null)}>
                    <figure>
                      <img
                        src="/assets/img/svg/sns_facebook.svg"
                        alt="페이스북"
                      />
                    </figure>
                    <span>페이스북</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => (onSelect ? onSelect("KAKAOSTORY") : null)}>
                    <figure>
                      <img
                        src="/assets/img/svg/sns_kakaostory.svg"
                        alt="카카오스토리"
                      />
                    </figure>
                    <span>카카오스토리</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => (onSelect ? onSelect("INSTAGRAM") : null)}>
                    <figure>
                      <img
                        src="/assets/img/svg/sns_instagram.svg"
                        alt="인스타그램"
                      />
                    </figure>
                    <span>인스타그램</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => (onSelect ? onSelect("MORE") : null)}>
                    <figure>
                      <img src="/assets/img/svg/sns_more.svg" alt="더보기" />
                    </figure>
                    <span>더보기</span>
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    );
  })
);
