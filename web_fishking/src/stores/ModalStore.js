/* global $ */
import { makeAutoObservable } from "mobx";

const ModalStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  title = "";
  body = "";
  bodyClass = "text-center";
  textOk = "";
  textClose = "";
  selectOptions = [];
  onOk = null;
  onClose = null;
  onSelect = null;

  state = {};
  setState = (state) => {
    this.state = {
      ...this.state,
      ...state,
    };
  };
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  openModal = (
    modalType = "Alert" | "Confirm" | "Select" | "Coupon" | "SNS" | "Input",
    options = {}
  ) => {
    this.title = options.title || "";
    this.body = options.body || "";
    this.bodyClass = options.bodyClass || "text-center";
    this.textOk = options.textOk || "확인";
    this.textClose = options.textClose || "닫기";
    this.selectOptions = options.selectOptions || [];
    this.onOk = options.onOk || null;
    this.onClose = options.onClose || null;
    this.onSelect = options.onSelect || null;

    if (modalType === "Alert") {
      $("#alertModal").modal("show");
    } else if (modalType === "Confirm") {
      $("#confirmModal").modal("show");
    } else if (modalType === "Select") {
      $("#selectModal").modal("show");
    } else if (modalType === "Coupon") {
      $("#couponModal").modal("show");
    } else if (modalType === "SNS") {
      $("#snsModal").modal("show");
    } else if (modalType === "Input") {
      $("#inputModal").modal("show");
    }
  };
})();

export default ModalStore;
