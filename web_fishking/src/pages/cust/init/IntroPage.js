import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";

export default inject(
  "PageStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        document.querySelector("body").classList.add("full");
        const { PageStore } = this.props;
        PageStore.reloadSwipe();
      }
      onInitiate = async () => {
        if (window.wversion === 2) {
          const { PageStore } = this.props;
          PageStore.push(`/init/permissions`);
        } else {
          if (window.ReactNativeWebView) {
            window.ReactNativeWebView.postMessage(
              JSON.stringify({ process: "Initiate", data: null })
            );
          } else {
            const { PageStore } = this.props;
            PageStore.push(`/main/home`);
          }
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <div
              id="carousel-about"
              className="carousel slide"
              data-ride="carousel"
            >
              <div className="carouselwrap">
                <ol className="carousel-indicators">
                  <li
                    data-target="#carousel-about"
                    data-slide-to="0"
                    className="active"
                  ></li>
                  <li data-target="#carousel-about" data-slide-to="1"></li>
                  <li data-target="#carousel-about" data-slide-to="2"></li>
                  <li data-target="#carousel-about" data-slide-to="3"></li>
                  {/*<li data-target="#carousel-about" data-slide-to="4"></li>*/}
                </ol>
                <div className="carousel-inner">
                  <div className="carousel-item active">
                    <img
                      src="/assets/cust/img/about/fishking_about1.jpg"
                      alt="????????? ??????"
                      className="d-block w-100"
                    />
                  </div>
                  <div className="carousel-item">
                    <img
                      src="/assets/cust/img/about/fishking_about2.jpg"
                      alt="????????? ??????"
                      className="d-block w-100"
                    />
                  </div>
                  <div className="carousel-item">
                    <img
                      src="/assets/cust/img/about/fishking_about3.jpg"
                      alt="?????? ?????? ??????"
                      className="d-block w-100"
                    />
                  </div>
                  <div className="carousel-item">
                    <img
                      src="/assets/cust/img/about/fishking_about4.jpg"
                      alt="????????? ?????????"
                      className="d-block w-100"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    className="btn btn-primary btn-lg btn-block"
                    onClick={this.onInitiate}
                  >
                    ????????????
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
