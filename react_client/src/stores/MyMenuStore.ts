import { makeAutoObservable } from "mobx";
import Http from "../commons/Http";

export class MyMenuStore {
  constructor() {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable **/
  /********** ********** ********** ********** **********/

  /********** ********** ********** ********** **********/
  /** action **/
  /********** ********** ********** ********** **********/
  // --> [REST] 마이메뉴 > 마이메뉴 페이지
  REST_GET_myMenuPage = async () => {
    try {
      const resolve = await Http._get("/v2/api/myMenuPage", null);
      return resolve;
    } catch (err) {
      console.error(
        "[REST] 마이메뉴 > 마이메뉴 페이지 :: " + JSON.stringify(err)
      );
      return null;
    }
  };
}

export const createStore = () => {
  return new MyMenuStore();
};

export type TMyMenuStore = ReturnType<typeof createStore>;
