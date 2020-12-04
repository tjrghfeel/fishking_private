import { makeAutoObservable } from "mobx";
import Axios from "../../Axios";

export default new (class MyStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 마이메뉴 페이지 데이터 조회 */
  getMyMenuPage() {
    Axios.get(
      "/myMenuPage",
      { memberId: 17 },
      (response) => {
        console.log(JSON.stringify(response));
      },
      (err) => {
        console.log(JSON.stringify(err));
      }
    );
  }
})();
