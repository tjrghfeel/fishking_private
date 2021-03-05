/* global kakao */
import APIStore from "./APIStore";
import ModalStore from "./ModalStore";
import { makeAutoObservable } from "mobx";

const NativeStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/

  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  postMessage(process, data) {
    window.ReactNativeWebView.postMessage(JSON.stringify({ process, data }));
  }
  linking(url) {
    if (window.isNative) {
      const json = {
        process: "Linking",
        data: url,
      };
      window.ReactNativeWebView.postMessage(JSON.stringify(json));
    } else {
      window.location.href = url;
    }
  }
  openMap({ lat, lng, address }) {
    if (window.isNative) {
      if (lat && lng) {
        this.linking(`kakaomap://route?sp=&ep=${lat},${lng}&by=CAR`);
      } else if (address) {
        const geocoder = new kakao.maps.services.Geocoder();
        APIStore.isLoading = true;
        geocoder.addressSearch(address, (result, status) => {
          if (status === kakao.maps.services.Status.OK) {
            lat = result[0].y;
            lng = result[0].x;
            this.linking(`kakaomap://route?sp=&ep=${lat},${lng}&by=CAR`);
          } else {
            ModalStore.openModal("Alert", {
              body: "주소로부터 좌표를 가져오지 못했습니다. [kakao.maps]",
            });
          }
          APIStore.isLoading = false;
        });
      }
    } else {
      console.error("지도앱 호출");
    }
  }
  clipboardCopy(text) {
    if (window.isNative) {
      const json = {
        process: "Clipboard",
        data: text,
      };
      window.ReactNativeWebView.postMessage(JSON.stringify(json));
    } else {
      navigator.clipboard.writeText(text).then(() => {
        alert("복사되었습니다.");
      });
    }
  }
})();

export default NativeStore;
