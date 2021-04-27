/* global $ */
import { makeAutoObservable } from "mobx";
import NativeStore from "./NativeStore";

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
  history = [];
  closeModal = (length = 1) => {
    if (length === 0) {
      // -> 모두 닫기
      for (let modalType of this.history) {
        $(`#${modalType}`).modal("hide");
      }
    } else {
      for (let i = 0; i < length; i++) {
        const modalType = this.history.pop();
        $(`#${modalType}`).modal("hide");
      }
    }
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
    this.address = options.address || null;

    if (modalType !== "SNS") this.history.push(modalType);

    if (modalType === "Alert") {
      $("#alertModal").modal("show");
    } else if (modalType === "Confirm") {
      $("#confirmModal").modal("show");
    } else if (modalType === "Select") {
      $("#selectModal").modal("show");
    } else if (modalType === "Coupon") {
      $("#couponModal").modal("show");
    } else if (modalType === "SNS") {
      if (this.address != null) {
        NativeStore.postMessage("Share", {
          message: this.address,
        });
      } else {
        NativeStore.postMessage("Share", {
          message: 'https://tiny.one/25r3whkw?action=' + window.location.href.split('cust')[1],
        });
      }
      // $("#snsModal").modal("show");
    } else if (modalType === "Input") {
      $("#inputModal").modal("show");
    }
  };
})();

export default ModalStore;
