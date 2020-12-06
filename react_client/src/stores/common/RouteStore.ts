import { makeAutoObservable } from "mobx";
import qs from "query-string";

export class RouteStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 라우트 히스토리 객체 */
  history: History | null = null;
  /** 라우트 로케이션 객체 */
  location: Location | null = null;

  /** 라우트 객체 설정 */
  setRouteObject(history: History, location: Location) {
    this.history = history;
    this.location = location;
  }

  /** 뒤로가기 */
  back() {
    this.history!.go(-1);
  }

  /** 이동 */
  go(pathname: string, state?: any) {
    // @ts-ignore
    this.history.push(pathname, state);
  }

  /** Query Parameter 가져오기 */
  getQueryParam(key: string) {
    return qs.parse(this.location?.search || "")[key] || null;
  }
}

export const createStore = () => {
  return new RouteStore();
};

export type TRouteStore = ReturnType<typeof createStore>;
