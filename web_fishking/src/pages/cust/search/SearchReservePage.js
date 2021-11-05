import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout, MainTab },
  MODAL: { SelectDateModal },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = { fishingType: "ship", fishingDate: new Date() };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        let fishingType = qp.fishingType || "ship"
        this.setState({fishingType});
      }

      search = () => {
        const { PageStore } = this.props;
        const fishingType = this.state.fishingType === "ship" ? "boat" : "rock";
        PageStore.push(
          `/main/company/${fishingType}?fishingDate=${this.state.fishingDate.format(
            "-"
          )}`
        );
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) =>
                this.setState({ fishingDate: selected })
              }
            />

            <NavigationLayout title={"검색"} showBackIcon={true} />

            <nav className="nav nav-pills nav-menu nav-justified">
              <a
                className="nav-link"
                onClick={() => PageStore.push(`/search/all`)}
              >
                전체
              </a>
              <a
                className="nav-link active"
                onClick={() => PageStore.push(`/search/reserve`)}
              >
                예약
              </a>
            </nav>

            {/** 예약 */}
            <div className="container nopadding mt-3">
              <div className="card-round-grey">
                <h6 className="text-center">
                  <img
                    src="/assets/cust/img/svg/form-search.svg"
                    alt=""
                    className="vam"
                  />{" "}
                  낚시 예약 검색
                </h6>
                <p className="mt-3">낚시 어디로 가세요?</p>
                <div className="row no-gutters">
                  <div className="col-6 padding-sm">
                    <a
                      onClick={() => this.setState({ fishingType: "ship" })}
                      className={
                        "btn " +
                        (this.state.fishingType === "ship"
                          ? "btn-info"
                          : "btn-third") +
                        " btn-block btn-sm mt-1 mb-1"
                      }
                    >
                      선상 예약
                    </a>
                  </div>
                  <div className="col-6 padding-sm">
                    <a
                      onClick={() => this.setState({ fishingType: "seaRocks" })}
                      className={
                        "btn " +
                        (this.state.fishingType === "seaRocks"
                          ? "btn-info"
                          : "btn-third") +
                        " btn-block btn-sm mt-1 mb-1"
                      }
                    >
                      갯바위 예약
                    </a>
                  </div>
                </div>
                <p className="mt-3">언제 가시나요?</p>
                <div className="row no-gutters">
                  <div className="col-12 padding-sm">
                    <a
                      className="btn btn-third btn-block btn-sm mt-1 mb-1"
                      data-toggle="modal"
                      data-target="#selDateModal"
                    >
                      <img
                        src="/assets/cust/img/svg/icon-cal.svg"
                        alt=""
                        className="float-left"
                      />
                      {this.state.fishingDate &&
                        this.state.fishingDate.toString()}
                    </a>
                  </div>
                </div>
                <hr className="mt-2 mb-3" />
                <a
                  onClick={this.search}
                  className="btn btn-primary btn-block btn-sm mt-1 mb-3"
                >
                  예약 검색
                </a>
              </div>
            </div>
            <MainTab activeIndex={5} />
          </React.Fragment>
        );
      }
    }
  )
);
