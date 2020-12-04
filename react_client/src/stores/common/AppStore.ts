import { makeAutoObservable } from "mobx";

export default new (class AppStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 로그인 데이터 */
  member: any = null;
})();
