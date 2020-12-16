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
  /** Native Linking :: 전화걸기 */
  linking(url: string) {
    if (window.isNative) {
      const json = {
        process: "Linking",
        data: url,
      };
      window.ReactNativeWebView.postMessage(JSON.stringify(json));
    } else {
      window.location.href = url;
    }
  }
}

export const createStore = () => {
  return new ExternalStore();
};

export type TExternalStore = ReturnType<typeof createStore>;
