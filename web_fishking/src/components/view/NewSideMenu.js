import React from "react";
import { inject, observer } from "mobx-react";

export default inject(
  "PageStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          keyword: "",
        }
      }
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <div className="side_menu">
              <ol>
                <li>
                  <a onClick={() => PageStore.push(`/main/home`)}>
                    <img
                      src="/assets/cust/img/svg/ico_1_home.svg"
                      alt="홈 메뉴"
                      className="side_img"
                    />
                    <span>홈</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => PageStore.push(`/main/company/boat`)}>
                    <img
                      src="/assets/cust/img/svg/ico_2_boat.svg"
                      alt="선상 메뉴"
                      className="side_img"
                    />
                    <span>선상</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => PageStore.push(`/main/company/rock`)}>
                    <img
                      src="/assets/cust/img/svg/ico_3_rock.svg"
                      alt="갯바위 메뉴"
                      className="side_img"
                    />
                    <span>갯바위</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => PageStore.push(`/main/story/diary`)}>
                    <img
                      src="/assets/cust/img/svg/ico_4_chat.svg"
                      alt="어복스토리 메뉴"
                      className="side_img"
                    />
                    <span>어복스토리</span>
                  </a>
                </li>
                <li>
                  <a onClick={() => PageStore.push(`/main/my`)}>
                    <img
                      src="/assets/cust/img/svg/ico_5_my.svg"
                      alt="마이메뉴"
                      className="side_img"
                    />
                    <span>마이메뉴</span>
                  </a>
                </li>
              </ol>
            </div>
          </React.Fragment>
        )
      }
    }
  )
)