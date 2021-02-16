import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { CommentListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.state = {
            title: null,
            list: [],
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        async componentDidMount() {
          const {
            match: {
              params: { eventId },
            },
            APIStore,
            PageStore,
          } = this.props;

          const resolve = await APIStore._get(`/v2/api/comment`, {
            dependentType: "event",
            linkId: eventId,
          });
          this.setState({ list: resolve });
          console.log(JSON.stringify(resolve));

          const qp = PageStore.getQueryParams();
          this.setState({ title: qp.title || null });
        }

        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          return (
            <React.Fragment>
              <NavigationLayout title={"댓글"} showBackIcon={true} />

              {/** 타이틀 */}
              {this.state.title && (
                <div className="filterWrap">
                  <h5 className="mt-2 mb-1 text-center">{this.state.title}</h5>
                </div>
              )}

              {/** 요약 */}
              <div className="container nopadding">
                <div className="row no-gutters d-flex align-items-center">
                  <div className="col-6">
                    <h5 className="">
                      댓글{" "}
                      <span className="red">
                        {Intl.NumberFormat().format(this.state.list.length)}
                      </span>
                    </h5>
                  </div>
                </div>
                <hr className="full mt-2 mb-3" />
              </div>

              {/** 리스트 */}
              {this.state.list &&
                this.state.list.map((data, index) => (
                  <React.Fragment>
                    <CommentListItemView key={index} data={data} />
                    {data.childList &&
                      data.childList.map((item, index2) => (
                        <CommentListItemView key={index2} data={item} />
                      ))}
                  </React.Fragment>
                ))}
            </React.Fragment>
          );
        }
      }
    )
  )
);
