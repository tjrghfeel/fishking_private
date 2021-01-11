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
  getCurrentPosition = () => {
    // TODO : 내 위치 조회
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          console.log(JSON.stringify(position));
        },
        (error) => {
          console.error(JSON.stringify(error));
        },
        {
          // enableHighAccuracy: true,
          // maximumAge: 30000,
          // timeout: 27000,
        }
      );
    }
  };
})();

export default NativeStore;
