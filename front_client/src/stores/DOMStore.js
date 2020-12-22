/* global $ */
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
  applyCarouselSwipe = async () => {
    await this.loadScript("/assets/js/swiper.min.js");
    await this.loadScript("/assets/js/jquery.touchSwipe.min.js");
    await this.loadScript("/assets/js/default.js");

    $(".carousel").swipe({
      swipe: function (
        event,
        direction,
        distance,
        duration,
        fingerCount,
        fingerData
      ) {
        if (direction == "left") $(this).carousel("next");
        if (direction == "right") $(this).carousel("prev");
      },
      tap: function (event, target) {
        // navigateTo(url)
      },

      allowPageScroll: "vertical",
      excludedElements: "label, button, input, select, textarea, .noSwipe",
      threshold: 1,
    });

    $(document).swipe({
      swipe: function (event, direction, distance, duration, fingerCount) {},
      click: function (event, target) {
        $(target).click();
      },
      threshold: 75,
    });
  };
})();

export default DOMStore;
