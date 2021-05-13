/* global daum, kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";

export default inject("PageStore","ModalStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.container = React.createRef(null);
        this.map = null;
        this.markers = [];
        this.geocoder = new kakao.maps.services.Geocoder();
        this.places = new kakao.maps.services.Places();
        this.state = { address: "", lat: 0, lng: 0, searchKey:'' };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const options = {
          center: new daum.maps.LatLng(36.252932, 127.724734),
          level: 7,
        };
        this.map = new daum.maps.Map(this.container.current, options);
        kakao.maps.event.addListener(this.map, "click", ({latLng})=>{this.setPosition(latLng.getLat(),latLng.getLng())});
        setTimeout(() => {
          this.map.relayout();
        }, 0);
      }

      // 좌표값받아서, 기존마커제거, 해당좌표에 해당하는 주소값과 좌표를 state에 저장, 마커생성.
      setPosition = (inputLat, inputLng) => {
          const lat = inputLat;
          const lng = inputLng;

          if (this.markers.length > 0) {//기존 마커목록에 마커가 존재하면 제거해줌.
              for (let marker of this.markers) {
                  marker.setMap(null);//마커객체의 setMap(null)로 마커를 지도상에서 제거.
              }
              this.markers = [];
          }

          // const image = new kakao.maps.MarkerImage(
          //   "/assets/cust/img/svg/marker-boat-white.svg",
          //   new kakao.maps.Size(64, 69),
          //   { offset: new kakao.maps.Point(27, 69) }
          // );

          this.geocoder.coord2Address(lng, lat, (result, status) => {//클릭된 lat,lng를 ceocoder.coord2Address()를 통해 도로명, 구 주소를 받아옴.
              if (status === kakao.maps.services.Status.OK) {
                  const address = result[0]["address"]["address_name"];
                  this.setState({ address, lat, lng });//주소를 state에 저장.
              }
          });

          const position = new kakao.maps.LatLng(lat, lng);//클릭된 위치로부터 받아온 lat,lng로 위치좌표객체 LatLng생성.
          const marker = new kakao.maps.Marker({ position });//LatLng객체를 마커객체에 넣어 마커생성.
          marker.setMap(this.map);//마커객체의 setMap()으로 지도에 마커를 올림.
          this.markers.push(marker);//리액트클래스의 markers 배열에 마커를 추가.
      }

      onSelected = () => {
        const { onSelected } = this.props;
        if (onSelected && this.state.address.length > 0) {
          onSelected({
            address: this.state.address,
            lat: this.state.lat,
            lng: this.state.lng,
          });
        }
      };

      searchAddress = (e)=>{
        const {ModalStore} = this.props;
        e.preventDefault();

        this.places.setMap(this.map);
        this.places.keywordSearch( this.state.searchKey, (result, status)=>{
            if(status === kakao.maps.services.Status.OK){
                const resultAddress = result[0];
                let x = resultAddress.x;
                let y = resultAddress.y;
                this.setPosition(y,x);
                this.map.setCenter(new kakao.maps.LatLng(y,x));
            }
            else if(status === kakao.maps.services.Status.ZERO_RESULT){
                this.geocoder.addressSearch( this.state.searchKey, (result, status)=>{
                    if(status === kakao.maps.services.Status.OK){
                        const resultAddress = result[0];
                        let x = resultAddress.x;
                        let y = resultAddress.y;
                        this.setPosition(y,x);
                        this.map.setCenter(new kakao.maps.LatLng(y,x));
                    }
                    else if(status === kakao.maps.services.Status.ZERO_RESULT){
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

      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <div className="container nopadding">
              <div className="mapwrap-sm">
                <div>
                  <form style={{width:'100%'}} onSubmit={(e)=>{this.searchAddress(e)}} >
                      <label style={{width:'20%',textAlign:'center',margin:8, fontSize:15}}>지도이동</label>
                      <input style={{border:'1px solid #2b79c8',borderRadius:5,width:'50%'}} type="text"
                            onChange={(e)=>{this.setState({searchKey:e.target.value})}} />
                      <input style={{width:'10%', border:'1px solid #2b79c8', borderRadius:5, backgroundColor:'#2b79c8', color:'white',
                          margin:8}} type="submit" value="검색"/>
                  </form>
                </div>
                <div className="info-btm-round pt-2">
                  <div className="row no-gutters d-flex align-items-center pt-0">
                    <div className="col-8 text-center">
                      {this.state.address}
                    </div>
                    <div className="col-4 text-center">
                      <a
                        className="btn btn-primary btn-sm-nav-tab"
                        onClick={this.onSelected}
                      >
                        위치적용
                      </a>
                    </div>
                  </div>
                </div>
                <div ref={this.container} id="map" style={{height:'calc(100vh-120px)'}}>
                  {/*<a*/}
                  {/*  href="#none"*/}
                  {/*  className="pointer img-sm"*/}
                  {/*  style={{ top: "310px", left: "225px" }}*/}
                  {/*>*/}
                  {/*  <img src="/assets/cust/img/svg/marker-boat-white.svg" />*/}
                  {/*</a>*/}
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
