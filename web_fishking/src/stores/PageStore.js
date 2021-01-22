import { makeAutoObservable } from "mobx";

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
    // this.history.push(pathname);
    window.location.href = pathname;
  };
  reload = () => {
    window.location.reload();
  };
  goBack = () => {
    // this.history.goBack();
    window.history.back();
  };
  setLogin = (accessToken = null) => {
    if (accessToken !== null) {
      localStorage.setItem("@accessToken", accessToken);
      this.loggedIn = true;
    } else {
      localStorage.removeItem("@accessToken");
      this.loggedIn = false;
    }
  };
  loadLogin = () => {
    const accessToken = localStorage.getItem("@accessToken") || null;
    if (accessToken !== null) this.loggedIn = true;
  };
  setScrollEvent = (onScroll) => {
    window.addEventListener("scroll", () => {
      const scrollHeight =
        document.scrollingElement.scrollHeight - window.outerHeight;
      const itemHeight = 80;
      const scrollPosition = window.pageYOffset;

      if (scrollPosition + itemHeight >= scrollHeight) {
        onScroll();
      }
    });
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
      location.pathname
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
    } else if (defaultState) this.state = defaultState;
    if (scroll !== null) {
      window.scrollTo(scroll.x || 0, scroll.y || 0);
    }
    this.clearState();
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
      location.pathname
    );
  };
  injectScript = (
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
})();

export default PageStore;
