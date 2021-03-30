import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartSailMainTab },
  VIEW: { SmartsailCameraSetListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
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
        const { APIStore, PageStore } = this.props;
        const { shipId } = PageStore.getQueryParams();
        const resolve = await APIStore._get(`/v2/api/sail/camera`, { shipId });
        this.setState(resolve);
      };
      onClick = async (item, isUse) => {
        const { APIStore } = this.props;
        const resolve = await APIStore._post(`/v2/api/sail/camera/update`, {
          cameraId: item.cameraId,
          isUse,
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"카메라설정"} showBackIcon={true} />

            <div className="container nopadding">
              <div className="card mt-3">
                <h4 className="text-center">
                  {this.state.shipName} <br />{" "}
                  <strong className="large orange">
                    {Intl.NumberFormat().format(this.state.count || 0)}
                  </strong>{" "}
                  <small className="grey">대의 카메라 제어중</small>
                </h4>
              </div>
            </div>
            <p className="space mt-2"></p>

            {this.state.content?.map((data, index) => (
              <SmartsailCameraSetListItemView
                key={index}
                data={data}
                onClick={this.onClick}
              />
            ))}

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    className="btn btn-primary btn-lg btn-block"
                    onClick={() => PageStore.goBack()}
                  >
                    확인
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
