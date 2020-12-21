import { makeAutoObservable } from "mobx";

const DOMStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  arr_injected_scripts = [];
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  loadScript = (
    src,
    options = { defer: false, crossOrigin: false, global: false }
  ) => {
    return new Promise((resolve) => {
      const script = document.createElement("script");
      script.src = src;
      script.addEventListener("load", () => {
        resolve(true);
      });
      script.addEventListener("error", () => {
        resolve(false);
      });

      if (options.defer) {
        script.setAttribute("defer", "true");
      }
      if (options.crossOrigin) {
        script.setAttribute("crossOrigin", "true");
      }
      if (!options.global) {
        this.arr_injected_scripts.push(script);
      }
      document.body.appendChild(script);
    });
  };
  clearScripts = () => {
    return new Promise((resolve) => {
      for (let script of this.arr_injected_scripts) {
        script.remove();
      }
    });
  };
})();

export default DOMStore;
