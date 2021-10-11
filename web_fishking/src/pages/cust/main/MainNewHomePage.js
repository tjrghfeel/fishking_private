/* global daum, kakao, $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  VIEW: {
    NewMainShipListView,
    NewSideMenu,
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props)
        this.container = React.createRef();
        this.map = null;
        this.state = {
          resolve: [],
          loaded: false,
          markerImgShip: null,
          markerOverImgShip: null,
          markerImgRock: null,
          markerOverImgRock: null,
        }
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore, ModalStore } = this.props;
        localStorage.removeItem("@signup-save")

        const qp = PageStore.getQueryParams();
        if (qp.error) {
          ModalStore.openModal("Alert", { body: "비정상적인 접근입니다." });
        }

        // 마커 이미지 생성
        const imgSize = new kakao.maps.Size(30, 30),
          imgOption = {offset: new kakao.maps.Point(15, 15)},
          imgOverSize = new kakao.maps.Size(46, 46),
          imgOverOption = {offset: new kakao.maps.Point(23, 23)},
          imgSrcShip = "/assets/cust/img/pin_ship.png",
          imgOverSrcShip = "/assets/cust/img/pin_ship_over.png",
          imgSrcRock = "/assets/cust/img/pin_rock.png",
          imgOverSrcRock = "/assets/cust/img/pin_rock_over.png"

        this.setState({
          markerImgShip: new kakao.maps.MarkerImage(imgSrcShip, imgSize, imgOption),
          markerOverImgShip: new kakao.maps.MarkerImage(imgOverSrcShip, imgOverSize, imgOverOption),
          markerImgRock: new kakao.maps.MarkerImage(imgSrcRock, imgSize, imgOption),
          markerOverImgRock: new kakao.maps.MarkerImage(imgOverSrcRock, imgOverSize, imgOverOption),
        })
        // const markerImgShip = new kakao.maps.MarkerImage(imgSrcShip, imgSize, imgOption)
        // const markerOverImgShip = new kakao.maps.MarkerImage(imgOverSrcShip, imgOverSize, imgOverOption)
        // const markerImgRock = new kakao.maps.MarkerImage(imgSrcRock, imgSize, imgOption)
        // const markerOverImgRock = new kakao.maps.MarkerImage(imgOverSrcRock, imgOverSize, imgOverOption)

        // # 지도표시
        const options = {
          // center: new daum.maps.LatLng(resolve.latitude, resolve.longitude),
          center: new kakao.maps.LatLng(34.9267, 128.079),
          level: 6,
        };
        this.map = new kakao.maps.Map(this.container.current, options);

        // 하단 리스트 토글
        $(function () {
          $("#content ol").click(function () {
            $(this).toggleClass("on");
          });
          $("#content ol.on li").click(function (e) {
            e.stopPropagation();
          });
        });

        // 사이드메뉴 토글
        $(function () {
          function updateMenuButton() {
            $(".menu-icon").toggleClass("is-active");
          }
          $(".menu-button").click(function (e) {
            e.preventDefault();
            $(this).toggleClass("is-active");
            $(".side_menu").toggleClass("on");
          });
          $(".side_menu li").mouseenter(function () {
            let targetImg = $(this).find(".side_img");
            let beforeUrl = targetImg.attr("src");
            let afterUrl = beforeUrl.replace(".svg", "_on.svg");
            targetImg.attr("src", afterUrl);
          });
          $(".side_menu li").mouseleave(function () {
            let targetImg = $(this).find(".side_img");
            let beforeUrl = targetImg.attr("src");
            let afterUrl = beforeUrl.replace("_on.svg", ".svg");
            targetImg.attr("src", afterUrl);
          });
        });

        this.loadPageData()
      }

      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const resolve = await APIStore._get("/v2/api/main/new");
        this.setState({resolve: resolve});
        this.sortByDistance(this)

        kakao.maps.event.addListener(this.map, 'dragend', dragListener(this))
        // kakao.maps.event.addListener(this.map, 'zoom_changed', this.dragendListener(this.map, this.state.resolve))

        function dragListener(component) {
          return (function() {
            const latlng = component.map.getCenter();
            let list = component.state.resolve.slice(0)
            // console.log(latlng)
            list.sort(function (a, b) {
              const distance_a = new kakao.maps.Polyline({
                path: [
                  latlng,
                  new kakao.maps.LatLng(a.location.latitude, a.location.longitude),
                ],
                strokeOpacity: 0
              }).getLength()
              const distance_b = new kakao.maps.Polyline({
                path: [
                  latlng,
                  new kakao.maps.LatLng(b.location.latitude, b.location.longitude),
                ],
                strokeOpacity: 0
              }).getLength()
              return distance_a < distance_b ? -1 : distance_a > distance_b ? 1 : 0;
            })
            component.setState({resolve: list})
          })
        }

        this.markingOnMap();
        PageStore.reloadSwipe();
      };

      sortByDistance = (component) => {
        const latlng = component.map.getCenter();
        let list = component.state.resolve.slice(0)
        // console.log(latlng)
        list.sort(function (a, b) {
          const distance_a = new kakao.maps.Polyline({
            path: [
              latlng,
              new kakao.maps.LatLng(a.location.latitude, a.location.longitude),
            ],
            strokeOpacity: 0
          }).getLength()
          const distance_b = new kakao.maps.Polyline({
            path: [
              latlng,
              new kakao.maps.LatLng(b.location.latitude, b.location.longitude),
            ],
            strokeOpacity: 0
          }).getLength()
          return distance_a < distance_b ? -1 : distance_a > distance_b ? 1 : 0;
        })
        component.setState({resolve: list})
      }

      markingOnMap = () => {
        const { PageStore } = this.props;
        const { resolve, markerImgShip, markerImgRock } = this.state
        // 지도에 마커 생성
        for (const point of resolve) {
          let markerImg, markerOverImg, type
          if (point.type == "갯바위") {
            markerImg = markerImgRock
            type = 'rock'
            // markerOverImg = markerOverImgRock
          } else {
            markerImg = markerImgShip
            type = 'boat'
            // markerOverImg = markerOverImgShip
          }
          const marker = new kakao.maps.Marker({
            map: this.map,
            image: markerImg,
            position: new kakao.maps.LatLng(
              point.location.latitude,
              point.location.longitude,
            ),
            title: point.shipName,
            clickable: true,
          });
          const content = '<div style="border-radius: 5px; ' +
            'display:flex; ' +
            'flex-direction: column;' +
            'padding: 15px 5px;' +
            'width: 200px;">' +
            '<div style="width: 40%;"><img src="'+point.shipImageFileUrl+'" style="object-fit: contain; width: 190px;"></div>' +
            '<div style="flex-direction: column">' +
            '<div>선박명: '+point.shipName+' ('+point.type+')</div>' +
            '<div>위치: '+point.address+'</div>' +
            '<div style="margin-top:10px;">' +
            '<button ' +
            'style="border-radius: 10px; ' +
            'width: 100%; ' +
            'border: none; ' +
            'background: #c6c5c3;"' +
            'onclick="goDetail(\''+type+'\',\''+point.id+'\')">상 세 보 기</button>' +
            '</div>' +
            '</div>' +
            '</div>'
          const infoWindow = new kakao.maps.InfoWindow({
            content: content,
            removable: true,
          })
          kakao.maps.event.addListener(marker, 'click', clickListener(this.map, marker, infoWindow));
        }

        // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
        function clickListener(map, marker, infowindow) {
          return function() {
            infowindow.open(map, marker);
          };
        }
      }

      onClick = async (item) => {
        const { PageStore } = this.props;
        let fishingType
        if (item.type == "갯바위") {
          fishingType = 'rock'
        } else {
          fishingType = 'boat'
        }
        PageStore.storeState();
        PageStore.push(`/company/${fishingType}/detail/${item.id}`);
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore, NativeStore } = this.props;
        const list = this.state.resolve
        // console.log(list)
        return (
          <React.Fragment>
            <header>
              {/*<a href="#"><i className="fas fa-chevron-left"></i></a>*/}
              <form
                style={{ backgroundColor: "rgba(255,255,255,0.7)" }}>
                <input
                  type="text"
                  style={{ backgroundColor: "rgba(255,255,255,0.7)" }}
                  placeholder="어떤 낚시를 찾고 있나요?"
                  onChange={(e) =>
                    this.setState({ keyword: e.target.value })
                  }
                  onKeyDown={(e) => {
                    if (e.keyCode === 13) {
                      if (e.target.value == "") return;
                      PageStore.push(
                        `/search/keyword/all?keyword=${e.target.value}`
                      );
                      // this.onSubmit(e.target.value);
                      e.preventDefault();
                    }
                  }}
                />
                <a
                  onClick={() => {
                    if (this.state.keyword == "") return;
                    PageStore.push(
                      `/search/keyword/all?keyword=${this.state.keyword}`
                    );
                  }}
                >
                  <img
                    src="/assets/cust/img/svg/form-search.svg"
                    alt="Search"
                  />
                </a>
              </form>
              <div className="modal_btn">
                <a href="#" className="menu-button">
                  <svg
                    className="menu-icon"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 100 100"
                  >
                    <g fill="none" fill-rule="evenodd" stroke="#979797">
                      <path d="M13,26.5 L88,26.5"/>
                      <path d="M13,50.5 L88,50.5"/>
                      <path className="x_path" d="M13,50.5 L88,50.5"/>
                      <path d="M13,74.5 L88,74.5"/>
                    </g>
                  </svg>
                </a>
              </div>
              <NewSideMenu />
            </header>
            <div
              ref={this.container}
              id="map"
              className="map"
              style={{ width: "100%", height: "100%", position: "fixed", top: "0", left: "0" }}
            ></div>
            <div id="content">
              <NewMainShipListView list={list} onClick={this.onClick} />
              {/*<ol>*/}
              {/*  {list.map((data, index) => (*/}
              {/*    <li>*/}
              {/*      <div className="img_wrap">*/}
              {/*        <img src={data.shipImageFileUrl} alt=""/>*/}
              {/*      </div>*/}
              {/*      <div className="text_part">*/}
              {/*        <h3>{data.shipName}</h3>*/}
              {/*        {data.fishSpeciesCount > 0 && (*/}
              {/*          <p>*/}
              {/*            {data.fishSpecies.map((s, index) => {*/}
              {/*              if (index < 1) {*/}
              {/*                return (<span>{s.codeName}, </span>)*/}
              {/*              }*/}
              {/*              if (index == 1) {*/}
              {/*                return (<span>{s.codeName} 외 {data.fishSpeciesCount - 2}종 </span>)*/}
              {/*              }*/}
              {/*            })}*/}
              {/*          </p>*/}
              {/*        )}*/}
              {/*        <p>{data.address}</p>*/}
              {/*        <a onClick={() => this.onClick(data)} className="more_btn">상세보기</a>*/}
              {/*      </div>*/}
              {/*    </li>*/}
              {/*  ))}*/}
              {/*</ol>*/}
            </div>
          </React.Fragment>
        )
      }
    }
  )
)