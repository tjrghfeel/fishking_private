import React from "react";
import { inject, observer } from "mobx-react";
import Http from "../../Http";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file = React.createRef(null);
        this.state = {
          isShip: false,
          isMe: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        document.querySelector("body").classList.add("pofile");
        this.loadPageData();
      }

      componentWillUnmount() {
        document.querySelector("body").classList.remove("pofile");
      }

      loadPageData = async () => {
        const {
          match: {
            params: { memberId: userId },
          },
        } = this.props;

        const resolve = await Http._get("/v2/api/profile", { userId });
        this.setState(resolve);
        console.log(JSON.stringify(resolve));
      };

      onChangeProfile = async () => {
        if (this.file.current?.files.length > 0) {
          const file = this.file.current?.files[0];

          const form = new FormData();
          form.append("profileImage", file);

          const resolve = await Http._put_upload(
            "/v2/api/profileManage/profileImage",
            form
          );
          console.log(resolve);
          if (resolve) {
            this.loadPageData();
          }
        }
      };

      requestLike = async () => {
        // TODO [PUB-OK/API-NO] 프로필 보기 : 좋아요 요청 API 개발 필요
      };

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        // TODO : [PUB-OK/API-NO] 프로필보기 : 좋아요 여부 데이터 항목이 필요합니다.
        // TODO : [PUB-OK/API-NO] 프로필보기 : '선상 실시간 예약업체' 문구는 어떤 데이터 항목을 대입하나요?
        // TODO : [PUB-OK/API-NO] 프로필보기 : 업체찜수 데이터 항목이 필요합니다.
        // TODO : [PUB-OK/API-NO] 프로필보기 : 실시간 예약 업체 여부 데이터 항목이 필요합니다.
        const { history } = this.props;
        return (
          <>
            <input
              ref={this.file}
              type="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={this.onChangeProfile}
            />

            <div>
              {/** profilewrap */}
              <div className="profilewrap">
                <div className="float-top-left">
                  <a onClick={() => history.goBack()}>
                    <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
                  </a>
                </div>
                <div className="imgWrap">
                  <img
                    src="/assets/img/bg-profile02.jpg"
                    className="d-block w-100"
                    alt=""
                  />
                  {this.state.isShip && (
                    <a onClick={this.requestLike}>
                      <span className="icon-goods active"></span>
                    </a>
                  )}
                </div>
                <figure>
                  <span>
                    <img
                      className="media-object profile"
                      src={this.state.profileImage}
                      alt=""
                    />
                    {this.state.isMe && (
                      <a
                        onClick={() => this.file.current?.click()}
                        className="img-upload btn btn-circle btn-circle-sm btn-white float_btn"
                      >
                        <img
                          src="/assets/img/svg/icon-photo.svg"
                          alt=""
                          className="icon-photo"
                        />
                      </a>
                    )}
                  </span>
                </figure>
              </div>
            </div>

            <div className="profiletit">
              {/** 프로필 정보 */}
              {this.state.isShip && (
                <React.Fragment>
                  <span className="tag-orange">선상 실시간 예약업체</span>
                  <h5>{this.state.shipName}</h5>
                  <p>
                    {this.state.fishSpecies &&
                      this.state.fishSpecies.length > 0 && (
                        <React.Fragment>
                          {this.state.fishSpecies[0]}&nbsp;
                          <small className="grey">|&nbsp;</small>
                        </React.Fragment>
                      )}
                    <small className="grey">{this.state.sido} </small>
                  </p>
                  {this.state.fishSpecies && this.state.fishSpecies.length > 0 && (
                    <ul className="tag center">
                      {this.state.fishSpecies.map((data, index) => (
                        <li key={index}>{data}</li>
                      ))}
                    </ul>
                  )}
                </React.Fragment>
              )}
              {!this.state.isShip && (
                <React.Fragment>
                  <h5>{this.state.nickName}</h5>
                  <p className="clearfix mt-4"></p>
                </React.Fragment>
              )}
              {/** 활동 정보 */}
              <nav className="nav nav-pills nav-sel nav-my nav-justified mt-5 pt-0">
                {!this.state.isMe && (
                  <a className="nav-link border-right" href="#none">
                    <h4 className="text-primary">{this.state.takeCount}</h4>
                    <span>업체 찜 수</span>
                  </a>
                )}
                <a className="nav-link border-right" href="#none">
                  <h4 className="text-primary">
                    {Intl.NumberFormat().format(this.state.postCount || 0)}
                  </h4>
                  <span>작성 글 수</span>
                </a>
                <a className="nav-link" href="#none">
                  <h4 className="text-primary">
                    {Intl.NumberFormat().format(this.state.likeCount || 0)}
                  </h4>
                  <span>좋아요 수</span>
                </a>
              </nav>

              {/** 기능 */}
              <nav className="nav nav-pills nav-sel nav-circle nav-justified mt-5">
                {this.state.isShip && (
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/img/svg/icon-home.svg" alt="" />
                    </figure>
                    <span>업체 바로가기</span>
                  </a>
                )}
                <a className="nav-link" href="#none">
                  <figure>
                    <img src="/assets/img/svg/icon-search-b.svg" alt="" />
                  </figure>
                  <span>검색하기</span>
                </a>
                <a className="nav-link" href="#none">
                  <figure>
                    <img src="/assets/img/svg/icon-post.svg" alt="" />
                  </figure>
                  <span>작성 글 보기</span>
                </a>
                <a className="nav-link" href="#none">
                  <figure>
                    <img src="/assets/img/svg/icon-reserv.svg" alt="" />
                  </figure>
                  <span>예약하기</span>
                </a>
              </nav>
            </div>
          </>
        );
      }
    }
  )
);
