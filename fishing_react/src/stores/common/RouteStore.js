import { makeAutoObservable } from "mobx";

export default new (class RouteStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** Router History */
  history = null;
  /** Set Router History */
  setHistory(history) {
    this.history = history;
  }
  /** Go History Back */
  goBack(number) {
    this.history.goBack(number || 1);
  }
  /** Go */
  go(pathname, state) {
    this.history.push(pathname, state);
  }
})();
