import { makeAutoObservable } from "mobx";

export default new (class RouteStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** Router History */
  history: History | null = null;
  /** Set Router History */
  setHistory(history: History) {
    this.history = history;
  }
  /** Go History Back */
  back() {
    this.history!.back();
  }
  /** Go */
  go(pathname: string, state: any) {
    // @ts-ignore
    this.history.push(pathname, state);
  }
})();
