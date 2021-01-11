import { makeAutoObservable } from "mobx";

const ViewStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  history = null;
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  setHistory = (history) => {
    this.history = history;
  };
  push = (pathname) => {
    this.history.push(pathname);
    // window.location.href = pathname;
  };
  goBack = () => {
    this.history.goBack();
  };
  saveState = async (data = {}) => {
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
        data,
      },
      "",
      location.pathname
    );
    return;
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
})();

export default ViewStore;
