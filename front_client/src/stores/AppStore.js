import { makeAutoObservable } from "mobx";

const AppStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  version = "0.1.1"; // 앱 버전
  loggedIn = false; // 로그인 여부
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  loadAppData = () => {
    const accessToken = localStorage.getItem("@accessToken") || null;
    if (accessToken !== null) {
      this.loggedIn = true;
    } else {
      this.loggedIn = false;
    }
  };
})();

export default AppStore;
