/* global daum, kakao, $ */
import React, {
  useState,
  useEffect,
  useCallback,
  useRef,
  forwardRef,
  useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("APIStore")(
  observer(
    forwardRef(({ APIStore, id = "" }, ref) => {
      const [arrSido, setArrSido] = useState([]); // 시/도 리스트
      const [arrSigungu, setArrSigungu] = useState([]); // 시/군/구 리스트
      const [arrDong, setArrDong] = useState([]); // 동/읍/면 리스트
      const selSido = useRef(null);
      const selSigungu = useRef(null);
      const selDong = useRef(null);
      const container = useRef(null);
      let map = null;
      let markers = [];

      // 시도 리스트 조회
      const loadSido = useCallback(async () => {
        let resolve = await APIStore._get(`/v2/api/commonCode/area`, {
          groupId: 152,
        });
        setArrSido(resolve);
      }, [setArrSido]);
      // 시군구 리스트 조회
      const selectSido = useCallback(async () => {
        const resolve = await APIStore._get(`/v2/api/commonCode/area`, {
          groupId: 156,
          parCode: selSido.current.selectedOptions[0].value,
        });
        setArrSigungu(resolve);
        selSigungu.current.value = "";
      }, [setArrSigungu, selSigungu]);
      // 읍면동 리스트 조회
      const selectSigungu = useCallback(async () => {}, []);
      // 지도 리로드
      const relayout = useCallback(() => {
        const options = {
          center: new daum.maps.LatLng(36.252932, 127.724734),
          level: 7,
        };
        map = new daum.maps.Map(container.current, options);
        kakao.maps.event.addListener(map, "click", (e) => {
          const latlng = e.latLng;
          const lat = latlng.getLat();
          const lng = latlng.getLng();

          const position = new kakao.maps.LatLng(lat, lng);
          const marker = new kakao.maps.Marker({ position });
          marker.setMap(map);
          markers.push(marker);
        });
        setTimeout(() => {
          map.relayout();
        }, 0);
      }, []);
      // 지도 마커 삭제
      const removeMarkers = useCallback(() => {
        for (let marker of markers) {
          marker.setMap(null);
        }
        markers = [];
      }, []);
      // 신규 갯바위 등록
      const onSubmit = useCallback(async () => {
        const sido = selSido.current.selectedOptions[0].value;
        const sigungu = selSigungu.current.selectedOptions[0].value;
        const dong = selDong.current.selectedOptions[0].value;
        const points = [];
        for (let marker of markers) {
          const position = marker.getPosition();
          const latitude = position.getLat();
          const longitude = position.getLng();
          points.push({ latitude, longitude });
        }
        const latitude = points[0]?.latitude;
        const longitude = points[0]?.longitude;

        console.log(
          JSON.stringify({
            sido,
            sigungu,
            dong,
            points,
            latitude,
            longitude,
            name,
            averageDepth,
            floorMaterial,
            tideTime,
            introduce,
          })
        );
        const resolve = await APIStore._post(`/v2/api/searocks/add`, {
          sido,
          sigungu,
          dong,
          points,
          latitude,
          longitude,
          name,
          averageDepth,
          floorMaterial,
          tideTime,
          introduce,
        });
        console.log(JSON.stringify(resolve));
        if (resolve["result"] === "success") {
          $(`#${id}`).modal("hide");
        }
      });
      useImperativeHandle(ref, () => ({ relayout }));
      useEffect(() => {
        loadSido();
      }, [loadSido]);

      const [name, setName] = useState("");
      const [averageDepth, setAverageDepth] = useState("");
      const [floorMaterial, setFloorMaterial] = useState("");
      const [tideTime, setTideTime] = useState("");
      const [introduce, setIntroduce] = useState("");
      return (
        <div
          className="modal fade modal-full"
          id={id}
          tabIndex="-1"
          aria-labelledby={id.concat("Label")}
          aria-hidden="true"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header bg-primary d-flex justify-content-center">
                <a data-dismiss="modal" className="nav-left">
                  <img
                    src="/assets/smartfishing/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
                <h5 className="modal-title" id={id.concat("Label")}>
                  신규 갯바위 추가
                </h5>
                {/*<a className="nav-right">*/}
                {/*  <img*/}
                {/*    src="/assets/smartfishing/img/svg/navbar-refresh.svg"*/}
                {/*    alt="Refresh"*/}
                {/*  />*/}
                {/*  <span>초기화</span>*/}
                {/*</a>*/}
              </div>
              <div className="modal-body">
                <div className="padding">
                  <div className="form-group">
                    <label className="d-block">
                      지역을 선택하신 후 원하시는 갯바위를 체크하세요.
                    </label>
                    <div className="input-group mb-3">
                      <select
                        className="form-control"
                        ref={selSido}
                        onChange={(e) => {
                          if (e.target.selectedOptions[0].value !== "") {
                            selectSido();
                          } else {
                            setArrSigungu([]);
                          }
                        }}
                      >
                        <option value={""}>시/도</option>
                        {arrSido.map((data, index) => (
                          <option key={index} value={data["code"]}>
                            {data["codeName"]}
                          </option>
                        ))}
                      </select>
                      <select
                        className="form-control"
                        ref={selSigungu}
                        onChange={(e) => {
                          if (e.target.selectedOptions[0].value !== "") {
                            selectSigungu();
                          } else {
                            setArrDong([]);
                          }
                        }}
                      >
                        <option value={""}>시/군/구</option>
                        {arrSigungu.map((data, index) => (
                          <option key={index} value={data["code"]}>
                            {data["codeName"]}
                          </option>
                        ))}
                      </select>
                      <select className="form-control" ref={selDong}>
                        <option value={""}>읍/면/동</option>
                      </select>
                    </div>
                  </div>

                  <div className="mapwrap">
                    <div
                      ref={container}
                      id="container"
                      style={{ height: "270px" }}
                    ></div>
                  </div>

                  <div className="form-group text-right mt-3">
                    <a className="btn btn-third btn-sm" onClick={removeMarkers}>
                      ↑ 포인트 제거
                    </a>
                  </div>

                  <div className="form-group">
                    <label htmlFor="">
                      갯바위 명칭<strong className="required"></strong>
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="갯바위 명칭을 입력하세요"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="">
                      평균수심(m)<strong className="required"></strong>
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="평균수심(m)를 입력하세요"
                      value={averageDepth}
                      onChange={(e) => setAverageDepth(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="">
                      저질<strong className="required"></strong>
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="저질을 입력하세요"
                      value={floorMaterial}
                      onChange={(e) => setFloorMaterial(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="">
                      적정물 때<strong className="required"></strong>
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="적정물 때를 입력하세요"
                      value={tideTime}
                      onChange={(e) => setTideTime(e.target.value)}
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="">
                      소개 (수온, 주요대상어, 특징 등){" "}
                      <strong className="required"></strong>
                    </label>
                    <textarea
                      className="form-control"
                      placeholder=""
                      rows="7"
                      value={introduce}
                      onChange={(e) => setIntroduce(e.target.value)}
                    ></textarea>
                  </div>
                </div>
              </div>
              <a
                onClick={onSubmit}
                className="btn btn-primary btn-lg btn-block btn-btm"
              >
                신규 갯바위 등록
              </a>
            </div>
          </div>
        </div>
      );
    })
  )
);
