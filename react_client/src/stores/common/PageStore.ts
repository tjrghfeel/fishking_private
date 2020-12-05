import { makeAutoObservable } from "mobx";

export class PageStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 동적 로드된 스크립트 엘리먼트 배열 */
  scripts: Array<HTMLElement> = [];

  /** 스크립트 동적 로드하기 */
  injectScript(src: string, onLoad?: Function, onError?: Function) {
    return new Promise((resolve) => {
      const script = document.createElement("script");
      script.src = src;
      script.addEventListener("load", () => {
        if (onLoad) onLoad();
        resolve(true);
      });
      script.addEventListener("error", () => {
        if (onError) onError();
        resolve(false);
      });
      document.body.appendChild(script);
      this.scripts.push(script);
    });
  }

  /** 동적 로드된 스크립트 제거 */
  removeInjectetdScripts() {
    return new Promise((resolve) => {
      for (let script of this.scripts) {
        script.remove();
      }
      resolve(true);
    });
  }

  /** window scroll 정보를 state 에 저장 */
  storeScroll(pageX: number, pageY: number) {
    const { pageXOffset, pageYOffset, location } = window;
    const { state: prevState = {} } = window.history;
    window.history.replaceState(
      {
        ...prevState,
        scroll: {
          x: pageX || pageXOffset,
          y: pageY || pageYOffset,
        },
      },
      "",
      location.pathname
    );
  }

  /** state 에 저장된 스크롤 정보를 복구 */
  restoreScroll() {
    const { state: prevState = {} } = window.history;
    const { scroll: prevScroll = { x: 0, y: 0 } } = prevState;
    window.scrollTo(prevScroll.x, prevScroll.y);
  }
}

export const createStore = () => {
  return new PageStore();
};

export type TPageStore = ReturnType<typeof createStore>;
