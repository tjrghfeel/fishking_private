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
  openAlert = (title = "알림", body = "", onClose = null) => {
    this.title = title;
    this.body = body;
    this.onClose = onClose;
    $("#alertModal").modal("show");
  };
})();

export default AlertStore;
