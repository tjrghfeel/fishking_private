/* global $ */
import { makeAutoObservable } from "mobx";
import qs from "qs";
import * as path from "path";

const PageStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  history = null;
  loggedIn = false;
  state = {};
  injectedScripts = [];
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  setHistory = (history) => {
    this.history = history;
  };
  push = (pathname) => {
    let service = this.history.location.pathname.split("/")[1];
    if (
      pathname.indexOf("/cust") !== -1 ||
      pathname.indexOf("/police") !== -1 ||
      pathname.indexOf("/common") !== -1 ||
      pathname.indexOf("/smartfishing") !== -1
    )
      service = "";
    else service = "/" + service;
    window.location.href = `${service}${pathname}`;
  };
  reload = () => {
    window.location.reload();
  };
  goBack = () => {
    const redirectUrl = sessionStorage.getItem("@redirect-url");
    sessionStorage.removeItem("@redirect-url");
    sessionStorage.setItem("@goBack", "Y");
    if (redirectUrl === null) window.history.back();
    else window.history.go(-2);
  };
  getQueryParams = () => {
    const params = qs.parse(window.location.search, {
      ignoreQueryPrefix: true,
    });
    return params;
  };
  setAccessToken = (
    accessToken = null,
    service = "cust" | "police",
    auto = "Y" | "N"
  ) => {
    if (accessToken === null) {
      localStorage.removeItem("@accessToken");
      localStorage.removeItem(`@accessToken_${service}`);
      sessionStorage.removeItem("@accessToken");
      sessionStorage.removeItem(`@accessToken_${service}`);
    } else {
      if (auto === "Y")
        localStorage.setItem(`@accessToken_${service}`, accessToken);
      else sessionStorage.setItem(`@accessToken_${service}`, accessToken);
    }
  };
  loadAccessToken = (service = "cust" | "police" | "smartfishing") => {
    const accessToken =
      localStorage.getItem(`@accessToken_${service}`) ||
      sessionStorage.getItem(`@accessToken_${service}`) ||
      null;
    if (accessToken !== null) {
      localStorage.setItem(`@accessToken`, accessToken);
      this.loggedIn = true;
    }
  };
  setScrollEvent = (onScroll, element) => {
    if (element) {
      element.addEventListener("scroll", () => {
        const scrollHeight = element.scrollHeight - element.offsetHeight;
        const itemHeight = 80;
        const scrollPosition = element.scrollTop;

        if (scrollPosition + itemHeight >= scrollHeight) {
          onScroll();
        }
      });
    } else {
      window.addEventListener("scroll", () => {
        const scrollHeight =
          document.scrollingElement.scrollHeight - window.outerHeight;
        const itemHeight = 80;
        const scrollPosition = window.pageYOffset;

        if (scrollPosition + itemHeight >= scrollHeight) {
          onScroll();
        }
      });
    }
  };
  setState = (state) => {
    this.state = {
      ...this.state,
      ...state,
    };
  };
  storeState = async (data) => {
    const { pageXOffset, pageYOffset, location } = window;
    const { state: prevState = {} } = window.history;

    window.history.replaceState(
      {
        ...prevState,
        saved: true,
        scroll: {
          x: pageXOffset,
          y: pageYOffset,
        },
        data: JSON.parse(JSON.stringify(data || this.state)),
      },
      "",
      location.pathname + (location.search || "")
    );
    return;
  };
  restoreState = (defaultState) => {
    const { saved = false, scroll = null, data = null } =
      window.history.state || {};
    if (saved) {
      if (data !== null) {
        this.state = data;
      }
      this.clearState();
    } else if (defaultState) this.state = defaultState;
    if (scroll !== null) {
      window.scrollTo(scroll.x || 0, scroll.y || 0);
    }
    return saved;
  };
  clearState = () => {
    const { location } = window;
    const { state: prevState = {} } = window.history;

    window.history.replaceState(
      {
        ...prevState,
        saved: false,
        scroll: {
          x: 0,
          y: 0,
        },
        data: null,
      },
      "",
      location.pathname + (location.search || "")
    );
  };
  injectScript = (
    src,
    options = { defer: false, crossOrigin: false, global: false, id: null }
  ) => {
    return new Promise((resolve) => {
      const script = document.createElement("script");
      script.src = src;
      if (options["id"] !== null) script.id = options["id"];
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
        this.injectedScripts.push(script);
      }
      document.body.appendChild(script);
    });
  };
  clearInjectedScripts = () => {
    for (let script of this.arr_injected_scripts) {
      script.remove();
    }
  };
  reloadSwipe = () => {
    document.querySelector("#js-touch-swipe").remove();
    document.querySelector("#js-swiper").remove();
    document.querySelector("#js-default").remove();
    this.injectScript("/assets/cust/js/jquery.touchSwipe.min.js", {
      id: "js-touch-swipe",
    });
    this.injectScript("/assets/cust/js/swiper.min.js", { id: "js-swiper" });
    this.injectScript("/assets/cust/js/default.js", { id: "js-default" });
    this.applySwipe();
  };
  applySwipe = (id) => {
    $(`${id || ".carousel"}`).swipe({
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

export default PageStore;
