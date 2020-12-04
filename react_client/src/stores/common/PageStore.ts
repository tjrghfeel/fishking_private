/* global $ */
import { makeAutoObservable } from "mobx";
import { ModalProps } from "../../types/StoreTypes";

export default new (class PageStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** Inject 된 외부 Script 모듈 배열 */
  scripts: Array<any> = [];
  /** 외부 Script 모듈을 Inject 한다. */
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
  /** Inject 된 외부 Script 모듈을 Remove 한다. */
  removeInjectetdScripts() {
    for (let script of this.scripts) {
      script.remove();
    }
  }
  /** Window Scroll 정보를 State 에 저장한다. */
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
  /** State 에 저장된 Window Scroll 을 복구한다. */
  restoreScroll() {
    const { state: prevState = {} } = window.history;
    const { scroll: prevScroll = { x: 0, y: 0 } } = prevState;
    window.scrollTo(prevScroll.x, prevScroll.y);
  }
  /** Modal 데이터 */
  modalData: ModalProps = { id: "" };
  /** Modal 열기 */
  openModal(data: ModalProps) {
    this.modalData = data;
    const { id } = this.modalData;
    // @ts-ignore
    $("#".concat(id)).modal("show");
  }
})();
