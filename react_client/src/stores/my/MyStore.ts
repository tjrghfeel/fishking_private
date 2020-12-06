import { makeAutoObservable, runInAction } from "mobx";
import http from "../../Http";
import { MyMenuPageDataProps } from "./MyProps";

export class MyStore {
  constructor() {
    makeAutoObservable(this);
  }

  /** 마이메뉴페이지 응답 데이터 */
  myMenuPageData: MyMenuPageDataProps | null = null;
  /** 마이메뉴페이지 API 호출 */
  loadMyMenuPageData(memberId: number) {
    http.request("GET", "/myMenuPage", null, { memberId }, (response: any) => {
      runInAction(() => {
        this.myMenuPageData = response;
      });
    });
  }
  /** 마이메뉴페이지 응답 데이터 초기화 */
  clearMyMenuPageData() {
    this.myMenuPageData = null;
  }
}

export const createStore = () => {
  return new MyStore();
};

export type TMyStore = ReturnType<typeof createStore>;
