/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject("MemberStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.checked = React.createRef(null);
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      signout = async () => {
        if (this.checked.current?.checked) {
          const {
            MemberStore: { REST_DELETE_profileManage_delete },
            history,
          } = this.props;

          const resolve = await REST_DELETE_profileManage_delete();
          if (resolve) {
            history.push(`/main/home`);
          }
        }
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"탈퇴하기"} visibleBackIcon={true} />

            <div className="container nopadding mt-4">
              <h5 className="text-center">어복황제를 탈퇴하면</h5>
              <div className="card-round-box-grey mt-3 pt-3 pb-3">
                <ul className="list">
                  <li>
                    내 프로필, 댓글 내용, 결제한 예약 목록, 찜목록, 어복황제
                    가입정보 등 그 외 사용자가 설정한 모든 정보가 사라지고
                    복구가 불가능합니다.
                  </li>
                  <li>
                    참여중인 댓글 및 사진 정보 등 모든 정보가 즉시 삭제되므로
                    중요한 정보가 있는지 탈퇴 전에 저장해 주세요.
                  </li>
                  <li>
                    탈퇴 시 어복황제에 재가입해도 자동으로 복구되지 않습니다.
                  </li>
                </ul>
              </div>
              <label className="control radio mt-2">
                <input
                  ref={this.checked}
                  type={"checkbox"}
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  <strong>모든 정보를 삭제하는 것에 동의 합니다.</strong>
                </span>
              </label>
              <br />
              <a
                onClick={this.signout}
                className="btn btn-primary btn-lg btn-block"
              >
                탈퇴하기
              </a>
            </div>
          </>
        );
      }
    }
  )
);
