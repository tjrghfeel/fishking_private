/* global $, kakao */
import { makeAutoObservable } from "mobx";
import qs from "qs";
import ModalStore from "./ModalStore";

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
  saved = {};
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  setSaved = (saved) => {
    this.saved = {
      ...this.saved,
      ...saved,
    };
    localStorage.setItem("@saved", JSON.stringify(this.saved));
  };
  loadSaved = () => {
    let saved = localStorage.getItem("@saved");
    if (saved !== null) {
      saved = JSON.parse(saved);
      this.saved = saved;
    }
  };
  setHistory = (history) => {
    this.history = history;
  };
  push = (pathname) => {
    this.removeScrollEvent();
    let service = this.history.location.pathname.split("/")[1];
    if (
      pathname.indexOf("/cust") !== -1 ||
      pathname.indexOf("/police") !== -1 ||
      pathname.indexOf("/common") !== -1 ||
      pathname.indexOf("/smartfishing") !== -1
    )
      service = "";
    else service = "/" + service;
    // window.location.href = `${service}${pathname}`;
    this.history.push(`${service}${pathname}`);
  };
  reload = () => {
    window.location.reload();
  };
  goBack = () => {
    if (
      window.isNative &&
      (window.location.pathname.indexOf("/cust/main/home") !== -1 ||
        window.location.pathname.indexOf("/smartfishing/login") !== -1 ||
        window.location.pathname.indexOf("/smartfishing/dashboard") !== -1 ||
        window.location.pathname == "/police/" ||
        window.location.pathname.indexOf("/police/login") !== -1 ||
        window.location.pathname.indexOf("/police/dashboard") !== -1)
    ) {
      window.ReactNativeWebView.postMessage(
        JSON.stringify({
          process: "Exit",
          data: "",
        })
      );
    } else {
      if ($(".modal.show").length > 0) {
        $(".modal.show").modal("hide");
      } else {
        const redirectUrl = sessionStorage.getItem("@redirect-url");
        sessionStorage.removeItem("@redirect-url");
        sessionStorage.setItem("@goBack", "Y");
        if (redirectUrl === null) {
          window.history.back();
        } else {
          window.history.go(-2);
        }
      }
    }
  };
  getQueryParams = () => {
    const params = qs.parse(window.location.search, {
      ignoreQueryPrefix: true,
    });
    return params;
  };
  setAccessToken = (
    accessToken = null,
    service = "cust" | "police" | "smartfishing",
    auto = "Y" | "N"
  ) => {
    if (accessToken === null) {
      localStorage.clear();
      sessionStorage.clear();
      // localStorage.removeItem("@accessToken");
      // localStorage.removeItem(`@accessToken_${service}`);
      // sessionStorage.removeItem("@accessToken");
      // sessionStorage.removeItem(`@accessToken_${service}`);
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
      // this.loggedIn = true;
    }

    const cust =
      localStorage.getItem(`@accessToken_cust`) ||
      sessionStorage.getItem(`@accessToken_cust`) ||
      null;
    const smartfishing =
      localStorage.getItem(`@accessToken_smartfishing`) ||
      sessionStorage.getItem(`@accessToken_smartfishing`) ||
      null;
    const police =
      localStorage.getItem(`@accessToken_police`) ||
      sessionStorage.getItem(`@accessToken_police`) ||
      null;
    if (cust === null && smartfishing === null && police === null)
      this.loggedIn = false;
    else this.loggedIn = true;
  };
  windowEventHandler = [];
  elementEventHandler = [];
  setScrollEvent = (onScroll, element) => {
    if (element) {
      const scrollEventHandler = () => {
        const scrollHeight = element.scrollHeight - element.offsetHeight;
        const itemHeight = 80;
        const scrollPosition = element.scrollTop;

        if (scrollPosition + itemHeight >= scrollHeight) {
          onScroll();
        }
      };
      element.addEventListener("scroll", scrollEventHandler);
      this.elementEventHandler.push({ element, scrollEventHandler });
    } else {
      const scrollEventHandler = () => {
        const scrollHeight =
          document.scrollingElement.scrollHeight - window.outerHeight;
        const itemHeight = 80;
        const scrollPosition = window.pageYOffset;

        if (scrollPosition + itemHeight >= scrollHeight) {
          onScroll();
        }
      };
      window.addEventListener("scroll", scrollEventHandler);
      this.windowEventHandler.push(scrollEventHandler);
    }
  };
  removeScrollEvent = () => {
    for (let windowEvent of this.windowEventHandler) {
      window.removeEventListener("scroll", windowEvent);
    }
    for (let elementEvent of this.elementEventHandler) {
      const { element, scrollEventHandler } = elementEvent;
      element.removeEventListener("scroll", scrollEventHandler);
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

    // # 토글 버튼
    $(".toggle_menu").on("click", function () {
      $(".toggle_menu>span").stop().toggleClass("on");
      $(this).stop().toggleClass("on");
    });

    let chkNum = 0;
    $(".toggle_menu").click(function () {
      if (chkNum == 0) {
        $(".toggle_menu>span").stop().addClass("on");
        $("nav").stop().addClass("view");
        $(".navbar").stop().addClass("on");
        $(this).stop().addClass("on");
        $(".allmenu").fadeIn();
        chkNum = 1;
      } else {
        $(".toggle_menu>span").stop().removeClass("on");
        $("nav").stop().removeClass("view");
        $(".navbar").stop().removeClass("on");
        $(this).stop().removeClass("on");
        $(".allmenu").fadeOut();
        chkNum = 0;
      }
    });
  };
  getAddressInfo = (lat, lng) => {
    return new Promise((resolve) => {
      const geocoder = new kakao.maps.services.Geocoder();
      geocoder.coord2RegionCode(lng, lat, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          resolve(result);
        } else {
          resolve(false);
        }
      });
    });
  };
})();

export default PageStore;
