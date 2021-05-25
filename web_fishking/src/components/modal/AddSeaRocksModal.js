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

export default inject(
  "APIStore",
  "PageStore",
    "ModalStore"
)(
  observer(
    forwardRef(({ APIStore, PageStore, ModalStore, id = "", onClose }, ref) => {
      const [arrSido, setArrSido] = useState([]); // 시/도 리스트
      const [arrSigungu, setArrSigungu] = useState([]); // 시/군/구 리스트
      const [arrDong, setArrDong] = useState([]); // 동/읍/면 리스트
        const [searchKey, setSearchKey] = useState('');//위치 검색 키워드
      const selSido = useRef(null);
      const selSigungu = useRef(null);
      const selDong = useRef(null);
      const container = useRef(null);
      const geocoder = new kakao.maps.services.Geocoder();
        // const options = {
        //     center: new daum.maps.LatLng(36.252932, 127.724734),
        //     level: 7,
        // };
       // let map = new daum.maps.Map(container.current, options);
      let map = null;
      const [arrMarker, setArrMarker] = useState([]);
      let markers = [];
      let addressSearchKey = '';

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

        //select값에 따른 지도 중앙점 이동.
          geocoder.addressSearch( selSido.current.selectedOptions[0].value, (result, status)=>{
              if(status === kakao.maps.services.Status.OK){
                  const resultAddress = result[0];
                  let x = resultAddress.x;
                  let y = resultAddress.y;

                  map.setCenter(new kakao.maps.LatLng(y,x));
              }
              else if(status === kakao.maps.services.Status.ZERO_RESULT){
                  ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
              }
              else{
                  ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
              }
          })
          setMapCenter(selSido.current.selectedOptions[0].value);
      }, [setArrSigungu, selSigungu]);
      // 읍면동 리스트 조회
      const selectSigungu = useCallback(async () => {
          setMapCenter(selSido.current.selectedOptions[0].value+' '+selSigungu.current.selectedOptions[0].value);
      }, []);

      const searchAddress = useCallback( async (addressSearchKey) =>{//정확한 이유는 모르겠으나, map을 외부컴포넌트에서 relayout()를 통해 초기화하고있어 useState()를 통해
                                                //리렌더링을 하면 생성한 map이 사라지게 되어있다. 해서 검색어를 useState()를 사용한변수에 할당하지않고 일반 변수에만 저장한뒤 메소드인자로
                                                //넘겨주는 방식으로 위치키워드검색 메소드를 만들었다.
          let places = new kakao.maps.services.Places();
          places.keywordSearch( addressSearchKey, (result, status)=>{
              if(status === kakao.maps.services.Status.OK){
                  const resultAddress = result[0];
                  let x = resultAddress.x;
                  let y = resultAddress.y;

                  map.setCenter(new kakao.maps.LatLng(y,x));
              }
              else if(status === kakao.maps.services.Status.ZERO_RESULT){
                  geocoder.addressSearch( addressSearchKey, (result2, status2)=>{
                      if(status2 === kakao.maps.services.Status.OK){
                          const resultAddress = result2[0];
                          let x = resultAddress.x;
                          let y = resultAddress.y;

                          map.setCenter(new kakao.maps.LatLng(y,x));
                      }
                      else if(status2 === kakao.maps.services.Status.ZERO_RESULT){
                          ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
                      }
                      else{
                          ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
                      }
                  })
              }
              else{
                  ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
              }
          })
      }, []);

      //키워드로 지도검색하여 중앙점이동.
      const setMapCenter = (searchKey)=>{
          geocoder.addressSearch( searchKey, (result, status)=>{
              if(status === kakao.maps.services.Status.OK){
                  const resultAddress = result[0];
                  let x = resultAddress.x;
                  let y = resultAddress.y;

                  map.setCenter(new kakao.maps.LatLng(y,x));
              }
              else if(status === kakao.maps.services.Status.ZERO_RESULT){
                  ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
              }
              else{
                  ModalStore.openModal("Alert", { body: "일치하는 결과가 없습니다" });
              }
          })
      }

      // 지도 리로드
      const relayout = useCallback(() => {
        const options = {
          center: new daum.maps.LatLng(36.252932, 127.724734),
          level: 7,
        };
        map = new daum.maps.Map(container.current, options);
        setTimeout(() => {
          map.relayout();
        }, 0);
        kakao.maps.event.addListener(map, "click", addMarker);
      }, [arrMarker, setArrMarker]);

      const addMarker = useCallback(
        (e) => {
          const latlng = e.latLng;
          const lat = latlng.getLat();
          const lng = latlng.getLng();

          const position = new kakao.maps.LatLng(lat, lng);
          const marker = new kakao.maps.Marker({ position });
          marker.setMap(map);
          arrMarker.push(marker);
          setArrMarker([...arrMarker]);
        },
        [arrMarker, setArrMarker]
      );

      // 지도 마커 삭제
      const removeMarkers = useCallback(() => {
        for (let marker of arrMarker) {
          marker.setMap(null);
        }
        arrMarker.splice(0);
      }, []);

      // 신규 갯바위 등록
      const onSubmit = useCallback(async () => {
        const sido = selSido.current.selectedOptions[0].value;
        const sigungu = selSigungu.current.selectedOptions[0].value;
        const dong = selDong.current.selectedOptions[0].value;
        const points = [];
        for (let marker of arrMarker) {
          const position = marker.getPosition();
          const latitude = position.getLat();
          const longitude = position.getLng();
          points.push({ latitude, longitude });
        }
        const latitude = points[0]?.latitude;
        const longitude = points[0]?.longitude;
        let address = null;
        const addr = await PageStore.getAddressInfo(latitude, longitude);
        address = addr[0]["address_name"];

        const resolve = await APIStore._post(`/v2/api/searocks/add`, {
          sido,
          sigungu,
          dong,
          address,
          points,
          latitude,
          longitude,
          name,
          averageDepth,
          floorMaterial,
          tideTime,
          introduce,
          isOpen: true,
        });
        if (resolve["result"] === "success") {
          $(`#${id}`).modal("hide");
          onClose();
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
                  <div className="form-group" style={{marginBottom:0}}>
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
                        {/*<select className="form-control" ref={selDong}>*/}
                          {/*<option value={""}>읍/면/동</option>*/}
                          {/*</select>}*/}
                    </div>
                  </div>

                    <div style={{width:'100%'}}>
                            <label style={{width:'20%',textAlign:'center',margin:8, fontSize:15}}>지도이동</label>
                            <input style={{border:'1px solid #2b79c8',borderRadius:5,width:'50%'}} type="text"
                                   onChange={(e)=>{ addressSearchKey = e.target.value;     }} />
                            <button style={{ border:'1px solid #2b79c8', borderRadius:5, backgroundColor:'#2b79c8', color:'white',
                                margin:8}} type="button" onClick={()=>{searchAddress(addressSearchKey)}}>검색</button>
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
                  {/*<div className="form-group">*/}
                  {/*  <label htmlFor="">*/}
                  {/*    저질<strong className="required"></strong>*/}
                  {/*  </label>*/}
                  {/*  <input*/}
                  {/*    type="text"*/}
                  {/*    className="form-control"*/}
                  {/*    placeholder="저질을 입력하세요"*/}
                  {/*    value={floorMaterial}*/}
                  {/*    onChange={(e) => setFloorMaterial(e.target.value)}*/}
                  {/*  />*/}
                  {/*</div>*/}
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
