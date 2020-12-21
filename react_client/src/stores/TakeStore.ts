import { makeAutoObservable } from "mobx";
import Http from "../commons/Http";

export class TakeStore {
  constructor() {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable **/
  /********** ********** ********** ********** **********/

  /********** ********** ********** ********** **********/
  /** action **/
  /********** ********** ********** ********** **********/
  // --> [GET] 찜 > 선상/갯바위 상품 찜 개수 조회
  REST_GET_take_count = async (page: number) => {
    try {
      const resolve = await Http._get("/v2/api/take/count");
      return resolve;
    } catch (err) {
      console.error(
        "[REST] 찜 > 선상/갯바위 상품 찜 개수 조회 :: " + JSON.stringify(err)
      );
      return null;
    }
  };
  // --> [GET] 찜 > 선상/갯바위 상품 찜 목록 조회
  REST_GET_take_fishingType = async (fishingType: number, page: number) => {
    try {
      const resolve = await Http._get(
        "/v2/api/take/" + fishingType + "/" + page
      );
      return resolve;
    } catch (err) {
      console.error(
        "[REST] 찜 > 선상/갯바위 상품 찜 목록 조회 :: " + JSON.stringify(err)
      );
      return null;
    }
  };
}

export const createStore = () => {
  return new TakeStore();
};

export type TTakeStore = ReturnType<typeof createStore>;
