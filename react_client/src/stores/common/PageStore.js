/* global $ */
import { makeAutoObservable } from "mobx";

export default new (class PageStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** Inject 된 외부 Script 모듈 배열 */
  scripts = [];
  /** 외부 Script 모듈을 Inject 한다. */
  injectScript(src, onLoad, onError) {
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
  /** Inject 된 외부 Script 모듈을 Remove 한다. */
  removeInjectetdScripts() {
    for (let script of this.scripts) {
      script.remove();
    }
  }
  /** Window Scroll 정보를 State 에 저장한다. */
  storeScroll(pageX, pageY) {
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
  /** State 에 저장된 Window Scroll 을 복구한다. */
  restoreScroll() {
    const { state: prevState = {} } = window.history;
    const { scroll: prevScroll = { x: 0, y: 0 } } = prevState;
    window.scrollTo(prevScroll.x, prevScroll.y);
  }
  /** Modal 데이터 */
  modalData = { type: null };
  /** Modal 열기 */
  openModal(data) {
    this.modalData = data;
    const { id } = this.modalData;
    $("#".concat(id)).modal("show");
  }
})();
