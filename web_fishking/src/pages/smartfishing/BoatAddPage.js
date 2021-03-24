import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          name: "", // 선박명
          fishingType: "S", // 구분 : 선상 = S , 갯바위 = R
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"선박등록"} showBackIcon={true} />

            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수입력
              </p>
              <form>
                <div className="form-group">
                  <label htmlFor="InputGName">
                    선박명 <strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="InputGName"
                    placeholder="선박명을 입력하세요"
                  />
                </div>
                <div className="form-group mb-1">
                  <label htmlFor="InputGPrice" className="d-block">
                    구분 <strong className="required"></strong>
                  </label>
                  <label className="control radio">
                    <input
                      name={"checkFishingType"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      defaultChecked={this.state.fishingType}
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ fishingType: "S" });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">선상</span>
                  </label>{" "}
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <label className="control radio">
                    <input
                      name={"checkFishingType"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ fishingType: "R" });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">갯바위</span>
                  </label>
                </div>
                <div className="space mt-1 mb-4"></div>
                <div className="form-group mt-0 mb-2">
                  <label htmlFor="InputGPrice" className="d-block">
                    메인화면 노출 <strong className="required"></strong>
                  </label>
                  <label className="control checkbox">
                    <input
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">실시간 조항 영상 노출</span>
                  </label>
                </div>
              </form>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
