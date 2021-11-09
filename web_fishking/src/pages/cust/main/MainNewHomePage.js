/* global google, markerClusterer, daum, kakao, $ */
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
        // const imgSize = new kakao.maps.Size(18, 22),
        //   imgOption = {offset: new kakao.maps.Point(9, 9)},
        // const imgSrcShip = "/assets/cust/img/pin_boat.png",
        //   imgSrcRock = "/assets/cust/img/pin_rock.png",
        //   imgSrcHarbor = "/assets/cust/img/pin_point.png";
        const imgSrcShip = "/assets/cust/img/svg/pin_boat.svg",
          imgSrcRock = "/assets/cust/img/svg/pin_rock.svg",
          imgSrcHarbor = "/assets/cust/img/svg/pin_point.svg";

        this.setState({
          // markerImgShip: new kakao.maps.MarkerImage(imgSrcShip, imgSize, imgOption),
          // markerImgRock: new kakao.maps.MarkerImage(imgSrcRock, imgSize, imgOption),
          // markerImgHarbor: new kakao.maps.MarkerImage(imgSrcHarbor, imgSize, imgOption),
          // markerImgShip: imgSrcShip,
          markerImgShip: {
            url: imgSrcShip,
            scaledSize: new google.maps.Size(18, 22),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(9, 22)
          },
          markerImgRock: {
            url: imgSrcRock,
            scaledSize: new google.maps.Size(18, 22),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(9, 22)
          },
          markerImgHarbor: {
            url: imgSrcHarbor,
            scaledSize: new google.maps.Size(18, 22),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(9, 22)
          },
        })

        // # 지도표시
        // const options = {
        //   // center: new daum.maps.LatLng(resolve.latitude, resolve.longitude),
        //   center: new kakao.maps.LatLng(34.9267, 128.079),
        //   level: 6,
        // };
        // this.map = new kakao.maps.Map(this.container.current, options);

        this.map = new google.maps.Map(document.getElementById("map"), {
          center: { lat: 34.9267, lng: 128.079 },
          zoom: 8,
          gestureHandling: "greedy",
          disableDefaultUI: true,
          zoomControl: false,
          streetViewControl: false,
        });

        // 하단 리스트 토글
        $(function () {
          $("#content-list").click(function () {
            $(this).toggleClass("on");
            $("#scroll-list").toggleClass("on");
          });
          $("#content-list.on li").click(function (e) {
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
        //선박 리스트 세팅.
        const resolve = await APIStore._get("/v2/api/main/new");
        this.setState({resolve: resolve});
        this.sortByDistance(this)

        this.map.addListener("dragend", dragListener(this));
        this.map.addListener("zoom_changed", dragListener(this));
        // kakao.maps.event.addListener(this.map, 'dragend', dragListener(this))
        // kakao.maps.event.addListener(this.map, 'zoom_changed', this.dragendListener(this.map, this.state.resolve))

        function dragListener(component) {
          return (function() {
            const latlng = component.map.getCenter();
            let list = component.state.resolve.slice(0)
            list.sort(function (a, b) {
              const point_a = new google.maps.LatLng(a.location.latitude, a.location.longitude)
              const point_b = new google.maps.LatLng(b.location.latitude, b.location.longitude)
              const distance_a = google.maps.geometry.spherical.computeDistanceBetween(
                latlng,
                point_a,
              )
              const distance_b = google.maps.geometry.spherical.computeDistanceBetween(
                latlng,
                point_b,
              )
              return distance_a < distance_b ? -1 : distance_a > distance_b ? 1 : 0;
            })
            component.setState({resolve: list})
          })
        }

        //카메라 포인트 리스트 세팅.
        const cameraPointList = await APIStore._get("/v2/api/cameraPoint/list");
        this.setState({cameraPointList: cameraPointList});

        //마커 표시.
        this.markingOnMap();
        PageStore.reloadSwipe();
      };

      sortByDistance = (component) => {
        const latlng = component.map.getCenter();// 현재 지도 중심 위경도.
        let list = component.state.resolve.slice(0)
        // console.log(latlng.lat(), latlng.lng())
        list.sort(function (a, b) {
          //지도 중심과 선박 위치 사이 거리 계산.
          const point_a = new google.maps.LatLng(a.location.latitude, a.location.longitude)
          const point_b = new google.maps.LatLng(b.location.latitude, b.location.longitude)
          const distance_a = google.maps.geometry.spherical.computeDistanceBetween(
            latlng,
            point_a,
          )
          const distance_b = google.maps.geometry.spherical.computeDistanceBetween(
            latlng,
            point_b,
          )
          return distance_a < distance_b ? -1 : distance_a > distance_b ? 1 : 0;
        })
        component.setState({resolve: list})
      }

      // sortByDistance = (component) => {
      //   const latlng = component.map.getCenter();// 현재 지도 중심 위경도.
      //   let list = component.state.resolve.slice(0)
      //   // console.log(latlng)
      //   list.sort(function (a, b) {
      //     //지도 중심과 선박 위치 사이 거리 계산.
      //     const distance_a = new kakao.maps.Polyline({// Polyline : 지도상의 선을 나타내는 객체.
      //       path: [
      //         latlng,
      //         new kakao.maps.LatLng(a.location.latitude, a.location.longitude),
      //       ],
      //       strokeOpacity: 0
      //     }).getLength()
      //     const distance_b = new kakao.maps.Polyline({
      //       path: [
      //         latlng,
      //         new kakao.maps.LatLng(b.location.latitude, b.location.longitude),
      //       ],
      //       strokeOpacity: 0
      //     }).getLength()
      //     return distance_a < distance_b ? -1 : distance_a > distance_b ? 1 : 0;
      //   })
      //   component.setState({resolve: list})
      // }

      // markingOnMap = () => {
      //   const { PageStore } = this.props;
      //   const { resolve, markerImgShip, markerImgRock, markerImgHarbor, cameraPointList } = this.state
      //   // 지도에 마커 생성(선박)
      //   for (const point of resolve) {
      //     let markerImg, markerOverImg, type
      //     if (point.type == "갯바위") {
      //       markerImg = markerImgRock
      //       type = 'rock'
      //       // markerOverImg = markerOverImgRock
      //     } else {
      //       markerImg = markerImgShip
      //       type = 'boat'
      //       // markerOverImg = markerOverImgShip
      //     }
      //     const marker = new kakao.maps.Marker({
      //       map: this.map,
      //       image: markerImg,
      //       position: new kakao.maps.LatLng(
      //         point.location.latitude,
      //         point.location.longitude,
      //       ),
      //       title: point.shipName,
      //       clickable: true,
      //     });
      //     const content = '<div name="detailw"' +
      //       'style="border-radius: 5px;' +
      //       'border-style: none;' +
      //       'display:flex; ' +
      //       'flex-direction: column;' +
      //       'width: 200px;">' +
      //       '<div style="border-radius: 5px; background-color: rgba(0,0,0,0);">' +
      //       '<img src="https://www.fishkingapp.com'+point.shipImageFileUrl+'" style="border-radius: 5px; object-fit: contain; width: 200px;">' +
      //       '</div>' +
      //       '<div style="flex-direction: column;" >' +
      //       '<div style="color: #000; margin: 5px;">선박명: '+point.shipName+' ('+point.type+')</div>' +
      //       '<div style="color: #000; margin: 5px;">위치: '+point.address+'</div>' +
      //       '<div style="margin-top:10px; border-radius: 5px; background-color: rgba(0,0,0,0);">' +
      //       '<button ' +
      //       'style="border-radius: 5px; ' +
      //       'width: 190px; ' +
      //       'border: none; ' +
      //       'color: #fff;' +
      //       'margin-bottom: 5px;' +
      //       'margin-left: 5px;' +
      //       'margin-right: 5px;"' +
      //       'class="' + type + '"' +
      //       'onclick="goDetail(\''+type+'\',\''+point.id+'\')">상 세 보 기</button>' +
      //       '</div>' +
      //       '</div>' +
      //       '</div>'
      //     const infoWindow = new kakao.maps.InfoWindow({//지도 위 포인트 클릭시 뜨는 정보창.
      //       content: content,
      //       removable: true,
      //     })
      //     kakao.maps.event.addListener(marker, 'click', clickListener(this.map, marker, infoWindow));
      //   }
      //
      //   //지도에 마커 생성(카메라 포인트)
      //   for (const point of cameraPointList) {
      //     let markerImg = markerImgHarbor, markerOverImg, type
      //     const marker = new kakao.maps.Marker({
      //       map: this.map,
      //       image: markerImg,
      //       position: new kakao.maps.LatLng(
      //           point.latitude,
      //           point.longitude,
      //       ),
      //       title: point.name,
      //       clickable: true,
      //     });
      //     type = 'harbor';
      //     const content = '<div name="detailw"' +
      //       'style="border-radius: 5px;' +
      //       'border-style: none;' +
      //       'display:flex; ' +
      //       'flex-direction: column;' +
      //       'width: 200px;">' +
      //       '<div style="border-radius: 5px; background-color: rgba(0,0,0,0);">' +
      //       '<img src="'+point.thumbUrl+'" style="border-radius: 5px; object-fit: contain; width: 200px;">' +
      //       '</div>' +
      //       '<div style="flex-direction: column;" >' +
      //       '<div style="color: #000; margin: 5px;">포인트명: '+point.name+' ('+point.type+')</div>' +
      //       '<div style="color: #000; margin: 5px;">위치: '+point.address+'</div>' +
      //       '<div style="margin-top:10px; border-radius: 5px; background-color: rgba(0,0,0,0);">' +
      //       '<button ' +
      //       'style="border-radius: 5px; ' +
      //       'width: 190px; ' +
      //       'border: none; ' +
      //       'color: #fff;' +
      //       'margin-bottom: 5px;' +
      //       'margin-left: 5px;' +
      //       'margin-right: 5px;"' +
      //       'class="' + type + '"' +
      //       'onclick="location.href=\'/cust/company/cameraPoint/boat/detail/'+point.id+'\'">상 세 보 기</button>' +
      //       '</div>' +
      //       '</div>' +
      //       '</div>'
      //     const infoWindow = new kakao.maps.InfoWindow({//지도 위 포인트 클릭시 뜨는 정보창.
      //       content: content,
      //       removable: true,
      //     })
      //     kakao.maps.event.addListener(marker, 'click', clickListener(this.map, marker, infoWindow));
      //   }
      //
      //   // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
      //   function clickListener(map, marker, infowindow) {
      //     return function() {
      //       infowindow.open(map, marker);
      //       document.getElementsByName("detailw").forEach((item, index) => {
      //         item.parentElement.parentElement.style.border = 'none';
      //       })
      //     };
      //   }
      // }

      markingOnMap = () => {
        const { PageStore } = this.props;
        const { resolve, markerImgShip, markerImgRock, markerImgHarbor, cameraPointList } = this.state
        let markers = []

        // 지도에 마커 생성(선박)
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
          // console.log(markerImg)
          const marker = new google.maps.Marker({
            position: {
              lat: point.location.latitude,
              lng: point.location.longitude,
            },
            map: this.map,
            icon: markerImg,
            optimized: false
          });
          const content = '<div name="detailw"' +
            'style="border-radius: 5px;' +
            'border-style: none;' +
            'display:flex; ' +
            'flex-direction: column;' +
            'width: 200px;">' +
            '<div style="border-radius: 5px; background-color: rgba(0,0,0,0);">' +
            '<img src="https://www.fishkingapp.com'+point.shipImageFileUrl+'" style="border-radius: 5px; object-fit: contain; width: 200px;">' +
            '</div>' +
            '<div style="flex-direction: column;" >' +
            '<div style="color: #000; margin: 5px;">선박명: '+point.shipName+' ('+point.type+')</div>' +
            '<div style="color: #000; margin: 5px;">위치: '+point.address+'</div>' +
            '<div style="margin-top:10px; border-radius: 5px; background-color: rgba(0,0,0,0);">' +
            '<button ' +
            'style="border-radius: 5px; ' +
            'width: 190px; ' +
            'border: none; ' +
            'color: #fff;' +
            'margin-bottom: 5px;' +
            'margin-left: 5px;' +
            'margin-right: 5px;"' +
            'class="' + type + '"' +
            'onclick="goDetail(\''+type+'\',\''+point.id+'\')">상 세 보 기</button>' +
            '</div>' +
            '</div>' +
            '</div>'
          const infoWindow = new google.maps.InfoWindow({//지도 위 포인트 클릭시 뜨는 정보창.
            content: content,
          })
          // infoWindow.addListener('domready', () => {
          //   const l = $('div[name="detailw"]').parent().parent().parent();
          //   for (let i = 0; i < l.length; i++) {
          //     $(l[i]).css('padding', '0px 18px 0px 18px');
          //   }
          // })
          marker.addListener("click", () => {
            infoWindow.close();
            infoWindow.open({
              anchor: marker,
              map: this.map,
              shouldFocus: false,
            })
            // setTimeout(() => document.getElementsByName("detailw").forEach((item, index) => {
            //   item.parentElement.parentElement.parentElement.style.padding = '1px';
            // }), 1)
          })

          markers.push(marker)
        }

        //지도에 마커 생성(카메라 포인트)
        for (const point of cameraPointList) {
          let markerImg = markerImgHarbor, markerOverImg, type
          const marker = new google.maps.Marker({
            position: {
              lat: point.latitude,
              lng: point.longitude,
            },
            map: this.map,
            icon: markerImgHarbor
          });
          type = 'harbor';
          const content = '<div name="detailw"' +
            'style="border-radius: 5px;' +
            'border-style: none;' +
            'display:flex; ' +
            'flex-direction: column;' +
            'width: 200px;">' +
            '<div style="border-radius: 5px; background-color: rgba(0,0,0,0);">' +
            '<img src="'+point.thumbUrl+'" style="border-radius: 5px; object-fit: contain; width: 200px;">' +
            '</div>' +
            '<div style="flex-direction: column;" >' +
            '<div style="color: #000; margin: 5px;">지상 카메라명: '+point.name+'</div>' +
            '<div style="color: #000; margin: 5px;">위치: '+point.address+'</div>' +
            '<div style="margin-top:10px; border-radius: 5px; background-color: rgba(0,0,0,0);">' +
            '<button ' +
            'style="border-radius: 5px; ' +
            'width: 190px; ' +
            'border: none; ' +
            'color: #fff;' +
            'margin-bottom: 5px;' +
            'margin-left: 5px;' +
            'margin-right: 5px;"' +
            'class="' + type + '"' +
            'onclick="location.href=\'/cust/company/cameraPoint/boat/detail/'+point.id+'\'">상 세 보 기</button>' +
            '</div>' +
            '</div>' +
            '</div>'
          const infoWindow = new google.maps.InfoWindow({//지도 위 포인트 클릭시 뜨는 정보창.
            content: content,
          })
          // infoWindow.addListener('domready', () => {
          //   const l = $('div[name="detailw"]').parent().parent().parent();
          //   for (let i = 0; i < l.length; i++) {
          //     $(l[i]).css('padding', '0px 0px 0px 18px');
          //   }
          // })
          marker.addListener("click", () => {
            infoWindow.close();
            infoWindow.open({
              anchor: marker,
              map: this.map,
              shouldFocus: false,
            })
            // setTimeout(() => document.getElementsByName("detailw").forEach((item, index) => {
            //   item.parentElement.parentElement.parentElement.style.padding = '1px';
            // }), 1)
          })
          markers.push(marker)
        }

        // 마커 클러스터러
        const boatClusterer = new markerClusterer.MarkerClusterer({map: this.map, markers: markers})

        // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
        // function clickListener(map, marker, infowindow) {
        //   return function() {
        //     infowindow.open({
        //       anchor: marker,
        //       map,
        //       shouldFocus: false,
        //     });
        //     document.getElementsByName("detailw").forEach((item, index) => {
        //       item.parentElement.parentElement.parentElement.style.padding = '0';
        //     })
        //   };
        // }
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
            ></div>
            <div id="content">
              <NewMainShipListView list={list} onClick={this.onClick} />
            </div>
          </React.Fragment>
        )
      }
    }
  )
)