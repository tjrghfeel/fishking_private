import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.state = {};
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          this.loadPageData();
        }
        loadPageData = async () => {
          const {
            match: {
              params: { id: postId },
            },
            APIStore,
          } = this.props;

          const resolve = await APIStore._get("/v2/api/notice", { postId });
          this.setState(resolve);
        };

        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const { PageStore } = this.props;
          return (
            <React.Fragment>
              <div className="container nopadding">
                <div className="pt-0">
                  <hr className="full mt-0 mb-3" />
                  <a>
                    <div className="row no-gutters align-items-center">
                      <div className="col-12 text-center">
                        <h5 className="mb-1">
                          <strong className="text-primary">
                            [{this.state.channelType}]{" "}
                          </strong>{" "}
                          {this.state.title}
                        </h5>
                        <small className="grey">
                          {this.state.date && (
                            <React.Fragment>
                              {this.state.date
                                .substr(0, 10)
                                .replace(/[-]/g, ".")}
                            </React.Fragment>
                          )}
                        </small>
                      </div>
                    </div>
                  </a>
                  <hr className="full mt-3 mb-3" />
                  <a>
                    <div className="row no-gutters align-items-center">
                      <div className="col-12 pl-2">{this.state.contents}</div>
                    </div>
                  </a>
                  <hr className="full mt-3 mb-3" />
                </div>
              </div>

              {/** 하단버튼 */}
              <div className="fixed-bottom">
                <div className="row no-gutters">
                  <div className="col-12">
                    <a
                      onClick={() => PageStore.goBack()}
                      className="btn btn-primary btn-lg btn-block"
                    >
                      목록
                    </a>
                  </div>
                </div>
              </div>
            </React.Fragment>
          );
        }
      }
    )
  )
);
