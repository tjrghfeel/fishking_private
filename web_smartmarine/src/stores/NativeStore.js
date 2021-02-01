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
})();

export default NativeStore;
