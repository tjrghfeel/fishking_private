/* global $ */
import { makeAutoObservable } from "mobx";

const AlertStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  id = "alertModal";
  title = "";
  body = "";
  onClose = null;
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  openAlert = (body, title = "알림", onClose = null) => {
    this.title = title || "알림";
    this.body = body;
    this.onClose = onClose;
    $("#alertModal").modal("show");
  };
})();

export default AlertStore;
