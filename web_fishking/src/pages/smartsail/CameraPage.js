import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartSailMainTab },
  VIEW: { SmartsailCameraListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.keyword = React.createRef(null);
        this.hasVideo = React.createRef(null);
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          keyword: null,
          hasVideo: null,
          list: [],
        });
        if (!restored) this.loadPageData();
      }
      initParams = () => {
        const { PageStore } = this.props;
        PageStore.setState({ keyword: null, hasVideo: null });
        this.keyword.current.value = "";
        this.hasVideo.current.value = 0;
      };
      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const { keyword, hasVideo } = PageStore.state;
        const resolve = await APIStore._get(`/v2/api/sail/ships`, {
          keyword: keyword === "" ? null : keyword,
          hasVideo,
        });
        PageStore.setState({ list: resolve });
        console.log(JSON.stringify(resolve));
      };
      onClick = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/camera/add?shipId=${item.shipId}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"상품관리"}
              customButton={
                <a className="fixed-top-right new">
                  <strong>N</strong>
                  <img
                    src="/assets/smartsail/img/svg/icon-alarm.svg"
                    alt="알림내역"
                  />
                  <span className="sr-only">알림내역</span>
                </a>
              }
              showBackIcon={true}
            />
            <SmartSailMainTab activeIndex={2} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option value={1}>선상명</option>
                    </select>
                    <input
                      ref={this.keyword}
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value={PageStore.state.keyword}
                      onChange={(e) =>
                        PageStore.setState({ keyword: e.target.value })
                      }
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    녹화영상
                  </label>
                  <select
                    ref={this.hasVideo}
                    className="form-control"
                    onChange={(e) => {
                      const selectedValue = e.target.selectedOptions[0].value;
                      if (selectedValue === 0)
                        PageStore.setState({ hasVideo: null });
                      else if (selectedValue === 1)
                        PageStore.setState({ hasVideo: true });
                      else PageStore.setState({ hasVideo: false });
                    }}
                  >
                    <option value={0}>녹화영상전체</option>
                    <option value={1}>유</option>
                    <option value={2}>무</option>
                  </select>
                </li>
                <li className="full">
                  <p>
                    <a
                      className="btn btn-primary btn-sm"
                      onClick={this.loadPageData}
                    >
                      검색
                    </a>
                    <a
                      className="btn btn-grey btn-sm"
                      onClick={this.initParams}
                    >
                      초기화
                    </a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            {PageStore.state.list?.map((data, index) => (
              <SmartsailCameraListItemView
                key={index}
                data={data}
                onClick={this.onClick}
              />
            ))}
          </React.Fragment>
        );
      }
    }
  )
);
