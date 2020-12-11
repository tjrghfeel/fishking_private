import { makeAutoObservable } from "mobx";

declare global {
  interface Window {
    isNative: boolean | undefined;
    ReactNativeWebView: any;
  }
}

export class ExternalStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 전화걸기 */
  link_call(phone: string) {
    if (window.isNative) {
      const json = {
        process: "LINK",
        data: phone,
      };
      window.ReactNativeWebView.postMessage(JSON.stringify(json));
    } else {
      window.location.href = "tel:".concat(phone);
    }
  }
}

export const createStore = () => {
  return new ExternalStore();
};

export type TExternalStore = ReturnType<typeof createStore>;
