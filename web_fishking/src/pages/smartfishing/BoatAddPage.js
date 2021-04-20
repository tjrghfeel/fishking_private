/* global daum, kakao, $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { ShipType3PositionView, ShipType5PositionView, ShipType9PositionView },
  MODAL: { SelectSeaRocksModal, AddSeaRocksModal },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.profileImage = React.createRef(null);
        this.videoId = React.createRef(null);
        this.ifrmAddress = React.createRef(null);
        this.zonecode = React.createRef(null);
        this.textAddr = React.createRef(null);
        this.textAddrDet = React.createRef(null);
        this.shipType3 = React.createRef(null);
        this.shipType5 = React.createRef(null);
        this.shipType9 = React.createRef(null);
        this.state = {
          name: "", // 선박명
          fishingType: "ship", // 구분 : 선상 = ship , 갯바위 = seaRocks
          fishSpecies: [],
          services: [],
          facilities: [],
          events: [],
          noticeTitle: null,
          notice: null,
          profileImage: null, // 선박사진
          videoId: null, // 녹화영상
          address: null,
          sido: null,
          sigungu: null,
          latitude: null,
          longitude: null,
          router: null,
          adtCameras: [],
          nhnCameras: [],
          weight: null, // 선박크기
          boardingPerson: 0, // 탑승인원 - 슬롯개수
          positions: [], // 사용위치목록

          arr_fishSpecies: [],
          arr_services: [],
          arr_facilities: [],
          arr_adtCameras: [],
          arr_nhnCameras: [],
          isUpdate: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { DataStore, APIStore, PageStore } = this.props;
        // 주요어종
        const arr80 = await DataStore.getCodes("80", 3);
        const arr161 = await DataStore.getCodes("161", 3);
        const arr162 = await DataStore.getCodes("162", 3);

        // const arr_fishSpecies = await DataStore.getCodes("80", 3);
        this.setState({ arr_fishSpecies: [...arr80, ...arr161, ...arr162] });
        // 서비스제공
        const arr_services = await DataStore.getCodes("85", 3);
        this.setState({ arr_services });
        // 편의시설
        const arr_facilities = await DataStore.getCodes("87", 3);
        this.setState({ arr_facilities });
        // 카메라 리스트
        const cameras = await APIStore._get(`/v2/api/ship/cameras`);
        await this.setState({
          arr_adtCameras: cameras["adt"] || [],
          adtCameras: cameras["adt"] || [],
          arr_nhnCameras: cameras["nhn"] || [],
          nhnCameras: cameras["nhn"] || [],
        });
        // 수정인경우 데이터 불러오기
        const { id } = PageStore.getQueryParams();
        if (id) {
          const resolve = await APIStore._get(`/v2/api/ship/detail/${id}`);
          await this.setState({ ...resolve });
          // 주소 설정
          this.textAddr.current.value = resolve["address"];
          // 선상위치 선택
          if (this.state.weight === 3) {
            this.shipType3.current.onSetSelected(this.state.positions);
          } else if (this.state.weight === 5) {
            this.shipType5.current.onSetSelected(this.state.positions);
          } else if (this.state.weight === 9) {
            this.shipType9.current.onSetSelected(this.state.positions);
          }
          // 구분 선택
          document
            .querySelector(
              `[name="checkFishingType"][value="${this.state.fishingType}"]`
            )
            .click();
          // 갯바위시 지도 그리기
          if (this.state.fishingType === "seaRocks") {
            for (let item of this.state.positions) {
              const rockData = await APIStore._get(`/v2/api/searocks/id`, {
                seaRockId: [item],
              });
              if (rockData && rockData["data"]) {
                for (let index = 0; index < rockData["data"].length; index++) {
                  // 지도 그리기
                  const data = rockData["data"][index];
                  const latitude = data["points"][0]["latitude"];
                  const longitude = data["points"][0]["longitude"];
                  const container = document.querySelector(`#map-${index}`);
                  const tmpMap = new daum.maps.Map(container, {
                    center: new daum.maps.LatLng(latitude, longitude),
                    level: 7,
                  });
                  // 마커 그리기
                  for (let point of data["points"]) {
                    const marker = new kakao.maps.Marker({
                      position: new kakao.maps.LatLng(
                        point["latitude"],
                        point["longitude"]
                      ),
                    });
                    marker.setMap(tmpMap);
                  }
                  setTimeout(() => {
                    tmpMap.relayout();
                  }, 100);
                }
              }
            }
          }
          // 주요어종 선택
          const {
            fishSpecies,
            services,
            facilities,
            adtCameras,
            nhnCameras,
          } = resolve;
          for (let item of fishSpecies) {
            document.querySelector(
              `[name="checkbox-fishSpecies"][value="${item}"]`
            ).checked = true;
          }
          // 서비스제공 선택
          for (let item of services) {
            document.querySelector(
              `[name="checkbox-services"][value="${item}"]`
            ).checked = true;
          }
          // 편의시설 선택
          for (let item of facilities) {
            document.querySelector(
              `[name="checkbox-facilities"][value="${item}"]`
            ).checked = true;
          }
          // ADT카메라 선택
          for (let item of this.state.arr_adtCameras) {
            const serial = item["serial"];
            if (adtCameras && adtCameras.length > 0) {
              for (let camera of adtCameras) {
                const cserial = camera["serial"];
                if (serial == cserial) {
                  document.querySelector(
                    `[name="adt-camera-${serial}"]`
                  ).value = "Y";
                } else {
                  document.querySelector(
                    `[name="adt-camera-${serial}"]`
                  ).value = "N";
                }
              }
            } else {
              document.querySelector(`[name="adt-camera-${serial}"]`).value =
                "N";
            }
          }
          // NHN카메라 선택
          for (let item of this.state.arr_nhnCameras) {
            const serial = item["serial"];
            if (nhnCameras && nhnCameras.length > 0) {
              for (let camera of nhnCameras) {
                const cserial = camera["serial"];
                if (serial == cserial) {
                  document.querySelector(
                    `[name="nhn-camera-${serial}"]`
                  ).value = "Y";
                } else {
                  document.querySelector(
                    `[name="nhn-camera-${serial}"]`
                  ).value = "N";
                }
              }
            } else {
              document.querySelector(`[name="nhn-camera-${serial}"]`).value =
                "N";
            }
          }
        }
      };
      uploadFile = async (uploadType) => {
        const { APIStore } = this.props;
        if (uploadType === "profileImage") {
          if (this.profileImage.current?.files.length > 0) {
            const file = this.profileImage.current?.files[0];
            const form = new FormData();
            form.append("file", file);
            form.append("filePublish", "ship");
            const upload = await APIStore._post_upload(
              `/v2/api/filePreUpload`,
              form
            );
            if (upload && upload["downloadUrl"])
              this.setState({ profileImage: upload["downloadUrl"] });
          }
        } else if (uploadType === "videoId") {
          if (this.videoId.current?.files.length > 0) {
            const file = this.videoId.current?.files[0];
            const form = new FormData();
            form.append("file", file);
            form.append("filePublish", "ship");
            const upload = await APIStore._post_upload(
              `/v2/api/filePreUpload`,
              form
            );
            if (upload && upload["fileId"])
              this.setState({ videoId: upload["fileId"] });
          }
        }
      };
      openFindAddress = () => {
        if (this.ifrmAddress.current.style.display === "block") return;

        const currentScroll = Math.max(
          document.body.scrollTop,
          document.documentElement.scrollTop
        );
        new daum.Postcode({
          width: "100%",
          height: "100%",
          oncomplete: (data) => {
            let addr = "";
            if (data.userSelectedType === "R") {
              addr = data.roadAddress;
            } else {
              addr = data.jibunAddress;
            }
            this.setState({
              sido: addr.split(" ")[0],
              sigungu: addr.split(" ")[1],
            });
            this.zonecode.current.value = data["zonecode"];
            this.textAddr.current.value = addr;
            this.ifrmAddress.current.style.display = "none";
            document.body.scrollTop = currentScroll;
            // 좌표변환
            const geocoder = new kakao.maps.services.Geocoder();
            geocoder.addressSearch(addr, (result, status) => {
              if (status === kakao.maps.services.Status.OK) {
                const x = result[0].x;
                const y = result[0].y;
                this.setState({ latitude: y, longitude: x });
              }
            });
          },
        }).embed(this.ifrmAddress.current);
        this.ifrmAddress.current.style.display = "block";
      };
      submit = async () => {
        const {
          id = null,
          name,
          fishingType,
          fishSpecies,
          services,
          facilities,
          events,
          noticeTitle,
          notice,
          profileImage,
          videoId,
          sido,
          sigungu,
          latitude,
          longitude,
          router,
          adtCameras,
          nhnCameras,
          weight,
          boardingPerson,
          positions,
        } = this.state;

        const params = {
          name,
          fishingType,
          fishSpecies,
          services,
          facilities,
          events,
          noticeTitle,
          notice,
          profileImage,
          videoId,
          addr: this.textAddr.current.value.concat(
            this.textAddrDet.current.value
          ),
          sido,
          sigungu,
          latitude,
          longitude,
          router,
          adtCameras,
          nhnCameras,
          weight,
          boardingPerson,
          positions,
        };
        const { APIStore, ModalStore, PageStore } = this.props;
        ModalStore.openModal("Confirm", {
          body: "저장하시겠습니까?",
          onOk: async () => {
            let resolve = null;
            if (id) {
              resolve = APIStore._put(`/v2/api/ship/update/${id}`, params);
            } else {
              resolve = APIStore._post(`/v2/api/ship/add`, params);
            }
            if (resolve) {
              ModalStore.openModal("Alert", {
                body: "저장되었습니다.",
                onOk: () => {
                  PageStore.push(`/boat`);
                },
              });
            }
          },
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore, DataStore, APIStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"선박등록"} showBackIcon={true} />

            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수입력
              </p>
              <form>
                <div className="form-group">
                  <label htmlFor="InputGName">
                    선박명 <strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="선박명을 입력하세요"
                    value={this.state.name}
                    onChange={(e) => this.setState({ name: e.target.value })}
                  />
                </div>
                <div className="form-group mb-1">
                  <label htmlFor="InputGPrice" className="d-block">
                    구분 <strong className="required"></strong>
                  </label>
                  <label className="control radio">
                    <input
                      name={"checkFishingType"}
                      type="radio"
                      value={"ship"}
                      className="add-contrast"
                      data-role="collar"
                      defaultChecked={this.state.fishingType}
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({
                            fishingType: "ship",
                            positions: [],
                            boardingPerson: 0,
                            weight: null,
                          });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">선상</span>
                  </label>{" "}
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <label className="control radio">
                    <input
                      name={"checkFishingType"}
                      type="radio"
                      value={"seaRocks"}
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({
                            fishingType: "seaRocks",
                            positions: [],
                            boardingPerson: 0,
                            weight: null,
                          });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">갯바위</span>
                  </label>
                </div>
                <div className="space mt-1 mb-4"></div>
                {/** 선상 / 갯바위 선택 */}
                {this.state.fishingType === "ship" && (
                  <React.Fragment>
                    <div className="form-group">
                      <label className="d-block">
                        선상 예약 위치 설정{" "}
                        <strong className="required"></strong>
                      </label>
                      <p className="text-primary pl-0">
                        선상 예약 위치를 설정하시면 고객이 어복황제 예약시 선상
                        위치를 선택가능합니다.
                      </p>
                    </div>
                    <div className="form-group">
                      <div className="input-group">
                        <select
                          value={this.state.weight}
                          className="form-control"
                          onChange={(e) => {
                            const selectedValue =
                              e.target.selectedOptions[0].value;
                            if (selectedValue == 0) {
                              this.setState({
                                weight: null,
                                boardingPerson: 0,
                                positions: [],
                              });
                            } else {
                              this.setState({
                                weight: selectedValue,
                                boardingPerson: 0,
                                positions: [],
                              });
                            }
                          }}
                        >
                          <option value={0}>선박크기 선택</option>
                          <option value={3}>3톤 (8명)</option>
                          <option value={5}>5톤 (18명)</option>
                          <option value={9}>9톤 (22명)</option>
                        </select>
                        <input
                          type="text"
                          className="form-control"
                          placeholder="슬롯 개수 입력"
                          readOnly
                          value={this.state.boardingPerson}
                          onChange={(e) =>
                            this.setState({ boardingPerson: e.target.value })
                          }
                        />
                        <div className="input-group-append">
                          <span className="input-group-text">개</span>
                        </div>
                      </div>
                      {this.state.weight == 3 && (
                        <React.Fragment>
                          <ShipType3PositionView
                            ref={this.shipType3}
                            count={8}
                            onChange={(selected) =>
                              this.setState({
                                boardingPerson: selected.length,
                                positions: selected,
                              })
                            }
                          />
                        </React.Fragment>
                      )}
                      {this.state.weight == 5 && (
                        <React.Fragment>
                          <ShipType5PositionView
                            ref={this.shipType5}
                            count={18}
                            onChange={(selected) =>
                              this.setState({
                                boardingPerson: selected.length,
                                positions: selected,
                              })
                            }
                          />
                        </React.Fragment>
                      )}
                      {this.state.weight == 9 && (
                        <React.Fragment>
                          <ShipType9PositionView
                            ref={this.shipType9}
                            count={22}
                            onChange={(selected) =>
                              this.setState({
                                boardingPerson: selected.length,
                                positions: selected,
                              })
                            }
                          />
                        </React.Fragment>
                      )}
                    </div>
                  </React.Fragment>
                )}
                {this.state.fishingType === "seaRocks" && (
                  <React.Fragment>
                    <SelectSeaRocksModal
                      id={"selRocksModal"}
                      onSelect={async (selected) => {
                        await this.setState({ positions: selected });
                        const resolve = await APIStore._get(
                          `/v2/api/searocks/id`,
                          { seaRockId: selected }
                        );
                        if (resolve && resolve["data"]) {
                          for (
                            let index = 0;
                            index < resolve["data"].length;
                            index++
                          ) {
                            // 지도 그리기
                            const data = resolve["data"][index];
                            const latitude = data["points"][0]["latitude"];
                            const longitude = data["points"][0]["longitude"];
                            const container = document.querySelector(
                              `#map-${index}`
                            );
                            const tmpMap = new daum.maps.Map(container, {
                              center: new daum.maps.LatLng(latitude, longitude),
                              level: 7,
                            });
                            // 마커 그리기
                            for (let point of data["points"]) {
                              const marker = new kakao.maps.Marker({
                                position: new kakao.maps.LatLng(
                                  point["latitude"],
                                  point["longitude"]
                                ),
                              });
                              marker.setMap(tmpMap);
                            }
                            setTimeout(() => {
                              tmpMap.relayout();
                            }, 100);
                          }
                        }
                      }}
                    />
                    {/*<AddSeaRocksModal id={"addRocksModal"} />*/}
                    <div className="form-group text-center">
                      <a
                        className="btn btn-secondary btn-block btn-sm"
                        data-toggle="modal"
                        data-target="#selRocksModal"
                      >
                        갯바위 선택
                      </a>
                    </div>
                    {this.state.positions?.map((data, index) => (
                      <React.Fragment>
                        <div className="mapwrap">
                          <div
                            id={`map-${index}`}
                            style={{ height: "270px" }}
                          ></div>
                        </div>
                        <div className="space mt-0 mb-4"></div>
                      </React.Fragment>
                    ))}
                  </React.Fragment>
                )}
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={() => {
                      const arr_ele = document.querySelectorAll(
                        '[name="checkbox-fishSpecies"]'
                      );
                      for (let ele of arr_ele) {
                        ele.checked = true;
                      }
                      const fishSpecies = [];
                      for (let row of this.state.arr_fishSpecies) {
                        for (let item of row) {
                          if (item["code"] === null) continue;
                          fishSpecies.push(item["code"]);
                        }
                      }
                      this.setState({ fishSpecies });
                    }}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    주요어종 <strong className="required"></strong>
                  </label>
                  {(this.state.arr_fishSpecies || []).map((data, index) => (
                    <div key={index} className="row">
                      {data.map((item, index2) => (
                        <div key={index2} className="col">
                          {item["id"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"checkbox-fishSpecies"}
                                value={item["code"]}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  if (e.target.checked) {
                                    this.setState({
                                      fishSpecies: this.state.fishSpecies.concat(
                                        item["code"]
                                      ),
                                    });
                                  } else {
                                    this.setState({
                                      fishSpecies: DataStore.removeItemOfArrayByItem(
                                        this.state.fishSpecies,
                                        item["code"]
                                      ),
                                    });
                                  }
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mt-1 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={() => {
                      const arr_ele = document.querySelectorAll(
                        '[name="checkbox-services"]'
                      );
                      for (let ele of arr_ele) {
                        ele.checked = true;
                      }
                      const services = [];
                      for (let item of this.state.arr_services) {
                        services.push(item["code"]);
                      }
                      this.setState({ services });
                    }}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    서비스제공 <strong className="required"></strong>
                  </label>
                  {(this.state.arr_services || []).map((data, index) => (
                    <div key={index} className="row">
                      {data.map((item, index2) => (
                        <div key={index2} className="col">
                          {item["id"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"checkbox-services"}
                                value={item["code"]}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  if (e.target.checked) {
                                    this.setState({
                                      services: this.state.services.concat(
                                        item["code"]
                                      ),
                                    });
                                  } else {
                                    this.setState({
                                      services: DataStore.removeItemOfArrayByItem(
                                        this.state.services,
                                        item["code"]
                                      ),
                                    });
                                  }
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mt-1 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={() => {
                      const arr_ele = document.querySelectorAll(
                        '[name="checkbox-facilities"]'
                      );
                      for (let ele of arr_ele) {
                        ele.checked = true;
                      }
                      const facilities = [];
                      for (let item of this.state.arr_facilities) {
                        facilities.push(item["code"]);
                      }
                      this.setState({ facilities });
                    }}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    편의시설 <strong className="required"></strong>
                  </label>
                  {(this.state.arr_facilities || []).map((data, index) => (
                    <div key={index} className="row">
                      {data.map((item, index2) => (
                        <div key={index2} className="col">
                          {item["id"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"checkbox-facilities"}
                                value={item["code"]}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  if (e.target.checked) {
                                    this.setState({
                                      facilities: this.state.facilities.concat(
                                        item["code"]
                                      ),
                                    });
                                  } else {
                                    this.setState({
                                      facilities: DataStore.removeItemOfArrayByItem(
                                        this.state.facilities,
                                        item["code"]
                                      ),
                                    });
                                  }
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <span className="float-right">
                    <a
                      className="btn btn btn-round-grey btn-xs-round"
                      onClick={() =>
                        this.setState({
                          events: this.state.events.concat({
                            eventId: null,
                            title: null,
                            contents: null,
                          }),
                        })
                      }
                    >
                      <span className="large">+</span> 추가
                    </a>
                    &nbsp;
                    <a
                      className="btn btn btn-round-grey btn-xs-round"
                      onClick={() =>
                        this.setState({
                          events: this.state.events.slice(
                            0,
                            this.state.events.length - 1
                          ),
                        })
                      }
                    >
                      ― 삭제
                    </a>
                  </span>
                  <label htmlFor="InputGPrice" className="d-block">
                    이벤트 <strong className="required"></strong>
                  </label>
                  {this.state.events.map((data, index) => (
                    <React.Fragment key={index}>
                      <input
                        type="text"
                        className="form-control mt-3"
                        placeholder="제목을 입력하세요"
                        value={data["title"]}
                        onChange={(e) => (data["title"] = e.target.value)}
                      />
                      <textarea
                        className="form-control mt-2"
                        rows="7"
                        placeholder="내용을 작성하세요"
                        value={data["contents"]}
                        onChange={(e) => (data["contents"] = e.target.value)}
                      ></textarea>
                    </React.Fragment>
                  ))}
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputGPrice" className="d-block">
                    공지사항 <strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control mt-3"
                    placeholder="제목을 입력하세요"
                    value={this.state.noticeTitle}
                    onChange={(e) =>
                      this.setState({ noticeTitle: e.target.value })
                    }
                  />
                  <textarea
                    className="form-control mt-2"
                    rows="7"
                    placeholder="내용을 작성하세요"
                    value={this.state.notice}
                    onChange={(e) => this.setState({ notice: e.target.value })}
                  ></textarea>
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputVodFile">
                    선박사진 등록 <strong className="required"></strong>
                  </label>
                  <input
                    ref={this.profileImage}
                    type="file"
                    className="form-control"
                    placeholder="선박사진을 등록하세요."
                    onChange={() => this.uploadFile("profileImage")}
                  />
                  <br />
                  {this.state.profileImage}
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputVodFile">
                    녹화영상 파일등록 <strong className="required"></strong>
                  </label>
                  <input
                    ref={this.videoId}
                    type="file"
                    className="form-control"
                    placeholder="녹화영상을 등록하세요."
                    onChange={() => this.uploadFile("videoId")}
                  />
                  <br />
                  {this.state.video}
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label>
                    승선위치 <strong className="required"></strong>
                  </label>
                  <div className="input-group">
                    <input
                      ref={this.zonecode}
                      type="text"
                      className="form-control col-4"
                      placeholder="우편번호"
                      readOnly
                    />
                    <div className="input-group-append">
                      <button
                        className="btn btn-third btn-sm"
                        type="button"
                        onClick={this.openFindAddress}
                      >
                        우편번호검색
                      </button>
                    </div>
                  </div>
                  <div
                    ref={this.ifrmAddress}
                    style={{
                      display: "none",
                      border: "1px solid",
                      height: "300px",
                      margin: "5px 0",
                      position: "relative",
                    }}
                  >
                    <img
                      src="//t1.daumcdn.net/postcode/resource/images/close.png"
                      id="btnFoldWrap"
                      style={{
                        cursor: "pointer",
                        position: "absolute",
                        right: "0px",
                        top: "-1px",
                        zIndex: "1",
                      }}
                      onClick={() =>
                        (this.ifrmAddress.current.style.display = "none")
                      }
                      alt="접기 버튼"
                    />
                  </div>
                  <input
                    ref={this.textAddr}
                    type="text"
                    className="form-control mt-2"
                    placeholder="기본주소"
                    readOnly
                  />
                  <input
                    ref={this.textAddrDet}
                    type="text"
                    className="form-control mt-2"
                    placeholder="상세주소"
                  />
                </div>
                <div className="form-group">
                  <label className="d-block">
                    좌표 <strong className="required"></strong>
                  </label>
                  <div className="input-group mb-3">
                    <div className="input-group-prepend">
                      <span className="input-group-text">X</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder=""
                      readOnly
                      value={this.state.latitude}
                    />
                    <div className="input-group-append">
                      <span className="input-group-text">Y</span>
                    </div>
                    <input
                      type="text"
                      className="form-control"
                      placeholder=""
                      readOnly
                      value={this.state.longitude}
                    />
                  </div>
                </div>
                <div className="space mt-0 mb-4"></div>
                <h6 className="mb-3 mt-3">SKB캡스 카메라등록</h6>

                <div className="form-group">
                  <label htmlFor="InputLTE">LTE 라우터 IMEI </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="LTE 라우터 IMEI를 입력하세요."
                    value={this.state.router}
                    onChange={(e) => this.setState({ router: e.target.value })}
                  />
                </div>
                {this.state.arr_adtCameras.length > 0 && (
                  <div className="form-group">
                    <label className="d-block">카메라 </label>

                    {this.state.arr_adtCameras?.map((data, index) => (
                      <div className="input-group mb-2" key={index}>
                        <select
                          name={`adt-camera-${data["serial"]}`}
                          className="form-control"
                          onChange={(e) => {
                            if (e.target.selectedOptions[0].value === "N") {
                              this.setState({
                                adtCameras: DataStore.removeItemOfArrayByKey(
                                  this.state.adtCameras,
                                  "serial",
                                  data["serial"]
                                ),
                              });
                            } else {
                              this.setState({
                                adtCameras: this.state.adtCameras.concat(data),
                              });
                            }
                          }}
                        >
                          <option value={"Y"}>카메라 선택</option>
                          <option value={"N"}>카메라 미선택</option>
                        </select>
                        <input
                          type="text"
                          className="form-control"
                          placeholder="카메라 이름을 입력하세요"
                          readOnly
                          value={data["name"].concat(`[${data["serial"]}]`)}
                        />
                      </div>
                    ))}
                  </div>
                )}

                <div className="space mt-0 mb-4"></div>
                <h6 className="mb-3 mt-3">NHN토스트캠 카메라등록</h6>
                <div className="form-group">
                  {this.state.arr_nhnCameras?.map((data, index) => (
                    <div className="input-group mb-2" key={index}>
                      <select
                        name={`nhn-camera-${data["serial"]}`}
                        className="form-control"
                        onChange={(e) => {
                          if (e.target.selectedOptions[0].value === "N") {
                            this.setState({
                              nhnCameras: DataStore.removeItemOfArrayByKey(
                                this.state.nhnCameras,
                                "serial",
                                data["serial"]
                              ),
                            });
                          } else {
                            this.setState({
                              nhnCameras: this.state.nhnCameras.concat(data),
                            });
                          }
                        }}
                      >
                        <option value={"Y"}>카메라 선택</option>
                        <option value={"N"}>카메라 미선택</option>
                      </select>
                      <input
                        type="text"
                        className="form-control"
                        placeholder="카메라 이름을 입력하세요"
                        readOnly
                        value={data["name"].concat(`[${data["serial"]}]`)}
                      />
                    </div>
                  ))}
                </div>
                <div className="space mb-4"></div>
              </form>
              <p className="clearfix">
                <br />
                <br />
              </p>
            </div>
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.submit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    확인
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
