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
