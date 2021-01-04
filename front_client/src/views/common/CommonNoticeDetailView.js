import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import Http from "../../Http";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          match: {
            params: { id: postId },
          },
          DataStore: { getEnumValueByIndex },
        } = this.props;
        const resolve = await Http._get("/v2/api/notice", { postId });

        const channelEnum = await getEnumValueByIndex(
          "channelType",
          resolve.channelType
        );
        await this.setState({ ...resolve, channelTypeName: channelEnum.value });
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"공지사항"} showBack={true} />

            {/** 데이터 */}
            <div className="container nopadding">
              <div className="pt-0">
                <hr className="full mt-0 mb-3" />
                <a>
                  <div className="row no-gutters align-items-center">
                    <div className="col-12 text-center">
                      <h5 className="mb-1">
                        <strong className="text-primary">
                          {this.state.channelTypeName && (
                            <React.Fragment>
                              [{this.state.channelTypeName}]
                            </React.Fragment>
                          )}{" "}
                        </strong>{" "}
                        {this.state.title}
                      </h5>
                      <small className="grey">
                        {this.state.date && (
                          <React.Fragment>
                            {this.state.date.substr(0, 10).replace(/[-]/g, ".")}
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
                    onClick={() => history.goBack()}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    목록
                  </a>
                </div>
              </div>
            </div>
          </>
        );
      }
    }
  )
);
