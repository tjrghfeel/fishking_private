import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject("TakeStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          boatCount: 0,
          rockCount: 0,
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          TakeStore: { REST_GET_take_count },
        } = this.props;
        const resolve = await REST_GET_take_count();
        if (resolve && resolve.length > 0) {
          this.setState({
            boatCount: resolve[0],
            rockCount: resolve[1],
          });
        }
      }

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"찜한 업체"} visibleBackIcon={true} />

            {/** 내역 */}
            <div className="container nopadding">
              <div className="row-zzim">
                <div
                  className="rows"
                  style={{
                    background:
                      "url(/assets/img/bg_zzim1.jpg) no-repeat top left",
                    backgroundSize: "cover",
                  }}
                >
                  <a onClick={() => history.push(`/common/zzim/boat`)}>
                    <h4>
                      <span className="icon-heart-white">
                        {Intl.NumberFormat().format(this.state.boatCount)}
                      </span>
                      <br />
                      선상 낚시
                    </h4>
                    <p>생생한 라이브 바다 낚시</p>
                  </a>
                </div>
              </div>
              <div className="row-zzim">
                <div
                  className="rows"
                  style={{
                    background:
                      "url(/assets/img/bg_zzim2.jpg) no-repeat top left",
                    backgroundSize: "cover",
                  }}
                >
                  <a onClick={() => history.push(`/common/zzim/rock`)}>
                    <h4>
                      <span className="icon-heart-white">
                        {Intl.NumberFormat().format(this.state.rockCount)}
                      </span>
                      <br />
                      갯바위 낚시
                    </h4>
                    <p>전국 인기 포인트를 한눈에!</p>
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
