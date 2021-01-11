/* global $ */
import { makeAutoObservable } from "mobx";

const DialogStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  title = "";
  body = "";
  onClose = null;
  onOk = null;
  textOk = "";
  textClose = "";
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  openAlert = (body = "", title = "", onClose = null) => {
    this.title = title;
    this.body = body;
    this.onClose = onClose;
    $("#alertModal".concat(this.id)).modal("show");
  };
  openConfirm = (
    body = "",
    title = "",
    onOk = null,
    onClose = null,
    textOk = "확인",
    textClose = "닫기"
  ) => {
    console.log("b");
    this.title = title;
    this.body = body;
    this.onOk = onOk;
    this.onClose = onClose;
    this.textOk = textOk;
    this.textClose = textClose;
    $("#confirmModal").modal("show");
  };
})();

export default DialogStore;
