/* global daum, kakao, $, Hls */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  VIEW: { CompanyGoodListItemView, GoodsBlogListItemView },
  MODAL: { CompanyGoodsDetailModal },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "NativeStore",
  "ModalStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.container = React.createRef(null);
          this.video = React.createRef(null);
          this.map = null;
          this.state = {};
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          this.loadPageData();
        }

        loadPageData = async () => {
          const {
            match: {
              params: { id },
            },
            APIStore,
            PageStore,
          } = this.props;
          let resolve = await APIStore._get(`/v2/api/ship/${id}`);
          this.setState(resolve);

          // # 비디오 표시
          if (resolve.liveVideo && resolve.liveVideo !== "") {
            const video = document.querySelector("#video");
            if (Hls.isSupported()) {
              const hls = new Hls();
              hls.loadSource(resolve.liveVideo);
              hls.attachMedia(video);
            } else if (video.canPlayType("application/vnd.apple.mpegurl")) {
              video.src = resolve.liveVideo;
            }
          }

          // # 별점 스크립트 로드
          PageStore.injectScript("/assets/cust/js/jquery.rateit.min.js", {
            global: true,
          });

          // # 지도표시
          const options = {
            center: new daum.maps.LatLng(resolve.latitude, resolve.longitude),
            level: 7,
          };
          this.map = new daum.maps.Map(this.container.current, options);
          const marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(
              resolve.latitude,
              resolve.longitude
            ),
          });
          marker.setMap(this.map);
        };
        requestLike = async () => {
          const { APIStore } = this.props;
          if (this.state.liked) {
            await APIStore._delete("/v2/api/loveto", {
              takeType: "ship",
              linkId: this.state.id,
            });
            this.setState({ liked: false });
          } else {
            await APIStore._post("/v2/api/loveto", {
              takeType: "ship",
              linkId: this.state.id,
            });
            this.setState({ liked: true });
          }
        };
        modalSNS = () => {
          const { ModalStore } = this.props;
          ModalStore.openModal("SNS", {
            onSelect: (selected) => {
              console.log(selected);
            },
          });
        };
        copyAddress = () => {
          const { NativeStore } = this.props;
          NativeStore.clipboardCopy(this.state.address);
        };
        makeCall = () => {
          const { ModalStore, NativeStore } = this.props;
          ModalStore.openModal("Confirm", {
            title: "전화걸기",
            body: (
              <p>
                [안심번호로 안심하고 통화하세요]
                <br />
                어복황제에서 전화연결 시 녹음에 동의되며
                <br />
                안심번호로 연결됩니다.
              </p>
            ),
            onOk: () => {
              NativeStore.linking(`tel://${this.state.tel}`);
            },
          });
        };
        findWay = () => {
          const { NativeStore } = this.props;
          NativeStore.linking(
            `kakaomap://route?sp=&ep=${this.state.latitude},${this.state.longitude}&by=CAR`
          );
        };
        advice = () => {
          const { PageStore } = this.props;
          PageStore.push(
            `/cs/qna/add?q=${encodeURI(
              JSON.stringify({
                contents: `선박id:${this.state.id}\n선박명:${this.state.name}\n\n내용:`,
              })
            )}`
          );
        };
        goToStoryDiary = (item) => {
          const { PageStore } = this.props;
          PageStore.push(`/story/diary/detail/${item.id}`);
        };
        goToStoryUser = (item) => {
          const { PageStore } = this.props;
          PageStore.push(`/story/story/detail/${item.id}`);
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const {
            PageStore,
            match: {
              params: { id },
            },
          } = this.props;
          return (
            <React.Fragment>
              <CompanyGoodsDetailModal
                id={"goodsModal"}
                data={this.state.goodsDetail || {}}
              />

              {/** 상품이미지 */}
              <div
                id="carousel-visual-detail"
                className="carousel slide"
                data-ride="carousel"
              >
                <div className="float-top-left">
                  <a onClick={() => PageStore.goBack()}>
                    <img
                      src="/assets/cust/img/svg/navbar-back.svg"
                      alt="뒤로가기"
                    />
                  </a>
                </div>
                <div className="carousel-inner">
                  <div className="carousel-item active">
                    {this.state.liveVideo === "" &&
                      this.state.profileImage === "" && (
                        <React.Fragment>
                          <img
                            src="/assets/cust/img/sample/boat1.jpg"
                            className="d-block w-100"
                            alt=""
                          />
                          <span className="play">
                            <img
                              src="/assets/cust/img/svg/live-play-big.svg"
                              alt=""
                            />
                          </span>
                          <div className="play-progress">
                            <div className="play-progress-time">01:21</div>
                            <div className="play-bar">
                              <div
                                className="play-on"
                                style={{ width: "15%" }}
                              ></div>
                            </div>
                            <span
                              className="play-control"
                              style={{ left: "20%" }}
                            ></span>
                            <div className="play-progress-time-all">02.57</div>
                          </div>
                          <div className="float-btm-right">
                            <a>
                              <img
                                src="/assets/cust/img/svg/play-sound-on.svg"
                                alt="사운드켜기"
                              />
                            </a>
                            <a>
                              <img
                                src="/assets/cust/img/svg/play-expand.svg"
                                alt="전체보기"
                              />
                            </a>
                          </div>
                        </React.Fragment>
                      )}
                    {this.state.liveVideo === "" &&
                      this.state.profileImage !== "" && (
                        <React.Fragment>
                          <img
                            src={this.state.profileImage}
                            className="d-block w-100"
                            alt=""
                          />
                        </React.Fragment>
                      )}
                    {this.state.liveVideo !== "" && (
                      <React.Fragment>
                        <video id="video" style={{ width: "100%" }}></video>
                      </React.Fragment>
                    )}
                  </div>
                </div>
              </div>

              {/** 상품타이틀 */}
              <div className="container nopadding">
                <div className="card mt-3">
                  <h4>{this.state.name}</h4>
                  <div className="rateit-wrap">
                    <span className="float-left">
                      {this.state.fishingType == "ship" && "선상"}
                      {this.state.fishingType == "seaRocks" && "갯바위"}
                    </span>{" "}
                    &nbsp;&nbsp;
                    <div
                      className="rateit float-left"
                      data-rateit-value={(this.state.avgReview || 0).toFixed(2)}
                      data-rateit-ispreset="true"
                      data-rateit-readonly="true"
                      data-rateit-starwidth="16"
                      data-rateit-starheight="16"
                    ></div>
                    <span>
                      {" "}
                      <strong>{(this.state.avgReview || 0).toFixed(2)} </strong>
                      ({Intl.NumberFormat().format(this.state.reviewCount || 0)}
                      ){" "}
                    </span>{" "}
                    &nbsp;&nbsp;
                  </div>
                  <div className="float-top-right">
                    <a onClick={this.requestLike}>
                      <span
                        className={
                          "icon-heart" +
                          (this.state.liked ? " active" : " float-left")
                        }
                      ></span>
                    </a>
                    <a onClick={this.modalSNS}>
                      <img src="/assets/cust/img/svg/icon-share.svg" alt="" />
                    </a>
                  </div>
                </div>
              </div>

              {/** Coupon */}
              <div className="container nopadding">
                <div className="couponWrap mt-3">
                  <a onClick={() => PageStore.push(`/coupon/available`)}>
                    <div className="couponDown">
                      <div className="row no-gutters align-items-center">
                        <div className="col-9">
                          <img
                            src="/assets/cust/img/svg/icon-coupon.svg"
                            alt=""
                            className="align-middle"
                          />{" "}
                          어복황제는 지금 할인중!
                        </div>
                        <div className="col-3 left-dline">쿠폰받기</div>
                      </div>
                    </div>
                  </a>
                </div>
              </div>

              {/** 판매상품 */}
              <div className="container nopadding">
                <h5>판매상품</h5>
                {this.state.goods &&
                  this.state.goods.map((data, index) => (
                    <CompanyGoodListItemView key={index} data={data} />
                  ))}
              </div>

              {/** 조황일지 */}
              <div className="container nopadding">
                <h5>
                  <a
                    onClick={() =>
                      PageStore.push(`/main/story/diary?shipId=${id}`)
                    }
                    className="float-right-more"
                  >
                    전체보기
                  </a>
                  조황일지{" "}
                  <span className="text-primary">
                    {Intl.NumberFormat().format(
                      this.state.fishingDiaryCount || 0
                    )}
                  </span>
                </h5>
                {this.state.fishingDiary &&
                  this.state.fishingDiary.map((data, index) => (
                    <GoodsBlogListItemView
                      key={index}
                      data={data}
                      onClick={this.goToStoryDiary}
                    />
                  ))}
                <div className="mt-5"></div>
              </div>

              {/** 유저조행기 */}
              <div className="container nopadding">
                <h5>
                  <a
                    onClick={() =>
                      PageStore.push(`/main/story/user?shipId=${id}`)
                    }
                    className="float-right-more"
                  >
                    전체보기
                  </a>
                  유저조행기{" "}
                  <span className="text-primary">
                    {this.state.fishingBlogCount || 0}
                  </span>
                </h5>
                {this.state.fishingBlog &&
                  this.state.fishingBlog.map((data, index) => (
                    <GoodsBlogListItemView
                      key={index}
                      data={data}
                      onClick={this.goToStoryUser}
                    />
                  ))}
                <div className="mt-5"></div>
              </div>

              {/** 리뷰 */}
              <div className="container nopadding">
                <h5>
                  <a
                    onClick={() =>
                      PageStore.push(`/company/review/${this.state.id}`)
                    }
                    className="float-right-more"
                  >
                    전체보기
                  </a>
                  리뷰{" "}
                  <span className="text-primary">
                    {Intl.NumberFormat().format(this.state.reviewCount || 0)}
                  </span>
                </h5>
                <div className="row no-gutters align-items-center">
                  <div className="col-4 text-center align-self-center">
                    <h2 className="rateit-text">
                      <img src="/assets/cust/img/star-big.png" alt="profile" />
                      {(this.state.avgReview || 0).toFixed(2)}
                    </h2>
                    <small>전체 평균 평점</small>
                  </div>
                  <div className="col-8 left-line">
                    <div className="rateit-wrap rateit-list-wrap">
                      <span className="float-left">
                        <strong className="title">손맛</strong>
                      </span>{" "}
                      &nbsp;&nbsp;
                      <div
                        className="rateit float-left"
                        data-rateit-value={(
                          this.state.tasteByReview || 0
                        ).toFixed(1)}
                        data-rateit-ispreset="true"
                        data-rateit-readonly="true"
                        data-rateit-starwidth="16"
                        data-rateit-starheight="16"
                      ></div>
                    </div>
                    <div className="rateit-wrap rateit-list-wrap">
                      <span className="float-left">
                        <strong className="title">서비스</strong>
                      </span>{" "}
                      &nbsp;&nbsp;
                      <div
                        className="rateit float-left"
                        data-rateit-value={(
                          this.state.serviceByReview || 0
                        ).toFixed(1)}
                        data-rateit-ispreset="true"
                        data-rateit-readonly="true"
                        data-rateit-starwidth="16"
                        data-rateit-starheight="16"
                      ></div>
                    </div>
                    <div className="rateit-wrap rateit-list-wrap">
                      <span className="float-left">
                        <strong className="title">청결도</strong>
                      </span>{" "}
                      &nbsp;&nbsp;
                      <div
                        className="rateit float-left"
                        data-rateit-value={(
                          this.state.cleanByReview || 0
                        ).toFixed(1)}
                        data-rateit-ispreset="true"
                        data-rateit-readonly="true"
                        data-rateit-starwidth="16"
                        data-rateit-starheight="16"
                      ></div>
                    </div>
                  </div>
                </div>
                <p className="space"></p>
              </div>

              {/** 위치정보 */}
              <div className="container nopadding">
                <h5>위치정보</h5>
                <ul className="notice">
                  <li>
                    <a
                      onClick={() =>
                        PageStore.push(
                          `/common/mapview?name=${this.state.name}&lat=${this.state.latitude}&lon=${this.state.longitude}`
                        )
                      }
                      className="float-right-more"
                    >
                      <img
                        src="/assets/cust/img/svg/icon-location.svg"
                        alt=""
                        className="vam"
                      />
                      지도보기
                    </a>
                    {this.state.address}
                  </li>
                </ul>
                <input
                  type={"hidden"}
                  id={"map-address"}
                  value={this.state.address}
                />
                <div
                  ref={this.container}
                  id="map"
                  className="map-sm"
                  style={{ width: "100%", height: "170px" }}
                ></div>
                <div className="row no-gutters align-items-center border-round-btm">
                  <div className="col-4 text-center align-self-center border-right">
                    <a onClick={() => this.findWay()}>
                      <div className="padding">
                        <img
                          src="/assets/cust/img/svg/icon-map.svg"
                          alt=""
                          className="vam"
                        />{" "}
                        길찾기
                      </div>
                    </a>
                  </div>
                  <div className="col-4 text-center align-self-center border-right">
                    <a onClick={this.copyAddress}>
                      <div className="padding">
                        <img
                          src="/assets/cust/img/svg/icon-copy.svg"
                          alt=""
                          className="vam"
                        />{" "}
                        주소복사
                      </div>
                    </a>
                  </div>
                  <div className="col-4 text-center align-self-center">
                    <a data-toggle="modal" data-target="#snsModal">
                      <div className="padding">
                        <img
                          src="/assets/cust/img/svg/icon-share.svg"
                          alt=""
                          className="vam icon-xs"
                        />{" "}
                        공유하기
                      </div>
                    </a>
                  </div>
                </div>
                <div className="space"></div>
              </div>

              {/** 사장님 한마디 */}
              {(this.state.ownerWordingTitle || this.state.ownerWording) && (
                <div className="container nopadding">
                  <h5>사장님 한마디</h5>
                  <ul className="notice">
                    <li className="icon-notice">
                      {this.state.ownerWordingTitle && (
                        <React.Fragment>
                          <strong>{this.state.ownerWordingTitle}</strong>
                          <br />
                        </React.Fragment>
                      )}
                      {this.state.ownerWording && (
                        <small>{this.state.ownerWording}</small>
                      )}
                    </li>
                  </ul>
                  <div className="space"></div>
                </div>
              )}

              {/** 이벤트 */}
              {this.state.events && (
                <div className="container nopadding">
                  <h5>
                    <a
                      onClick={() => PageStore.push(`/event/list`)}
                      className="float-right-more"
                    >
                      전체보기
                    </a>
                    이벤트
                  </h5>
                  <ul className="notice">
                    {this.state.events.map((data, index) => (
                      <li className="icon-event" key={index}>
                        {data}
                      </li>
                    ))}
                  </ul>
                  <div className="space"></div>
                </div>
              )}

              {/** 공지사항 */}
              {(this.state.noticeTitle || this.state.notice) && (
                <div className="container nopadding">
                  <h5>공지사항</h5>
                  <ul className="notice">
                    <li className="icon-notice">
                      {this.state.noticeTitle && (
                        <React.Fragment>
                          <strong>{this.state.noticeTitle}</strong>
                          <br />
                        </React.Fragment>
                      )}
                      {this.state.notice && <small>{this.state.notice}</small>}
                    </li>
                  </ul>
                  <div className="space"></div>
                </div>
              )}

              {/** 선박정보 */}
              <div className="container nopadding">
                <h5>선박정보</h5>
                <ul className="notice">
                  <li>
                    {this.state.weight === 3 && "3톤(t) | 8인승"}
                    {this.state.weight === 5 && "5톤(t) | 18인승"}
                    {this.state.weight === 9 && "9톤(t) | 22인승"}
                  </li>
                </ul>
                <div className="space"></div>
              </div>

              {/** 서비스 */}
              <div className="container nopadding">
                <h5>눈 길이 가는 특별한 서비스!</h5>
                <ul className="service">
                  {this.state.services &&
                    this.state.services.map((data, index) => (
                      <li key={index}>{data}</li>
                    ))}
                </ul>
                <div className="clearfix-sm"></div>
                <hr />
                <ul className="notice">
                  <li>
                    <strong>서비스</strong>
                    <br />
                    <small className="text-secondary">
                      {this.state.services && (
                        <React.Fragment>
                          {"· "}
                          {this.state.services.map((data, index) => {
                            if (index === 0) {
                              return <React.Fragment>{data}</React.Fragment>;
                            } else {
                              return (
                                <React.Fragment>
                                  {", ".concat(data)}
                                </React.Fragment>
                              );
                            }
                          })}
                        </React.Fragment>
                      )}
                    </small>
                  </li>
                  <li>
                    <strong>편의시설</strong>
                    <br />
                    <small className="text-secondary">
                      {this.state.facilities &&
                        this.state.facilities.map((data, index) => {
                          if (index === 0) {
                            return (
                              <React.Fragment>
                                {"· ".concat(data)}
                              </React.Fragment>
                            );
                          } else {
                            return (
                              <React.Fragment>
                                {", ".concat(data)}
                              </React.Fragment>
                            );
                          }
                        })}
                    </small>
                  </li>
                  {this.state.devices && this.state.devices.length > 0 && (
                    <li>
                      <strong>보유장비</strong>
                      <br />
                      <small className="text-secondary">
                        {this.state.devices.map((data, index) => {
                          if (index === 0) {
                            return (
                              <React.Fragment>
                                {"· ".concat(data)}
                              </React.Fragment>
                            );
                          } else {
                            return (
                              <React.Fragment>
                                {", ".concat(data)}
                              </React.Fragment>
                            );
                          }
                        })}
                      </small>
                    </li>
                  )}
                </ul>
                <p className="clearfix"></p>
              </div>

              {/** 오류신고 */}
              <div className="container nopadding">
                <div className="warningWrap mt-3 text-center">
                  <h5 className="align-items-center">
                    <a onClick={this.advice}>
                      <img
                        src="/assets/cust/img/svg/icon-info.svg"
                        alt=""
                        className="vam"
                      />{" "}
                      <span className="grey">잘못된 정보 알리기</span>
                    </a>
                  </h5>
                  <p>
                    <small className="grey">
                      ㈜투비는 통신판매중계자로서 통신판매의 당사자가 아니며,
                      <br />
                      상품의 예약, 이용 및 환불 등과 관련한 의무와 책임은 각
                      판매자에게 있습니다.
                    </small>
                  </p>
                </div>
              </div>

              {/** 하단버튼 */}
              <div className="fixed-bottom">
                <div className="row no-gutters">
                  <div className="col-3">
                    <a
                      onClick={this.makeCall}
                      className="btn btn-secondary btn-lg btn-block"
                    >
                      <img
                        src="/assets/cust/img/svg/icon-call-w.svg"
                        alt=""
                        className="vam"
                      />
                    </a>
                  </div>
                  <div className="col-9">
                    <a
                      onClick={() => PageStore.push(`/reservation/goods/${id}`)}
                      className="btn btn-primary btn-lg btn-block"
                    >
                      예약하기
                    </a>
                  </div>
                </div>
              </div>
            </React.Fragment>
          );
        }
      }
    )
  )
);
