import { makeAutoObservable } from "mobx";
import Http from "../commons/Http";

export class CouponStore {
  constructor() {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable **/
  /********** ********** ********** ********** **********/

  /********** ********** ********** ********** **********/
  /** action **/
  /********** ********** ********** ********** **********/
  // --> [GET] 쿠폰 > 다운 가능한 쿠폰리스트 출력
  REST_GET_downloadableCouponList = async (page: number) => {
    try {
      const resolve = await Http._get("/v2/api/downloadableCouponList/" + page);
      return resolve;
    } catch (err) {
      console.error(
        "[REST] 쿠폰 > 다운 가능한 쿠폰리스트 출력 :: " + JSON.stringify(err)
      );
      return null;
    }
  };
}

export const createStore = () => {
  return new CouponStore();
};

export type TCouponStore = ReturnType<typeof createStore>;
