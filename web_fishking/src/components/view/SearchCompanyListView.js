import React from "react";
import { inject, observer } from "mobx-react";
import SearchCompanyListItemView from "./SearchCompanyListItemView";

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          page: 0,
          list: [],
          isPending: false,
          isEnd: false,
          keyword: "",
          totalElements: 0,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { parent, PageStore } = this.props;
        PageStore.setScrollEvent(() => {
          this.loadPageData(this.state.page + 1);
        }, document.querySelector(`#${parent} .modal-body`));
        this.loadPageData();
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }

      loadPageData = async (page = 0, keyword = "") => {
        const { parent, APIStore, PageStore } = this.props;

        if ((page > 0 && this.state.isEnd) || this.state.isPending) return;

        if (keyword === "") keyword = null;

        const { iscompany } = PageStore.getQueryParams();
        let url = "";
        if (iscompany === "Y") {
          url = `/v2/api/fishingDiary/searchShip/company/${page}`;
        } else {
          url = `/v2/api/fishingDiary/searchShip/${page}`;
        }
        try {
          this.setState({ page, keyword, isPending: true });
          const {
            content,
            totalElements,
            pageable: { pageSize = 0 },
          } = await APIStore._get(url, {
            keyword,
          });

          if (page === 0) {
            this.setState({ list: content, totalElements });
            setTimeout(() => {
              document.querySelector(`#${parent} .modal-body`).scrollTo(0, 0);
            }, 100);
          } else {
            this.setState({ list: this.state.list.concat(content) });
          }
          if (content.length < pageSize) {
            this.setState({ isEnd: true });
          } else {
            this.setState({ isEnd: false });
          }
        } catch (err) {
          this.setState({ list: [], totalElements: 0, isEnd: true });
        } finally {
          this.setState({ isPending: false });
        }
      };

      onClick = (item) => {
        const { onClick } = this.props;
        onClick(item);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** ?????? */}
            <div className="container nopadding mt-3 mb-0">
              <form className="form-search">
                <a>
                  <img
                    src="/assets/cust/img/svg/form-search.svg"
                    alt=""
                    className="icon-search"
                  />
                </a>
                <input
                  className="form-control mr-sm-2"
                  type="text"
                  placeholder="????????? ?????? ???????????? ???????????????."
                  onKeyDown={(e) => {
                    if (e.keyCode === 13) {
                      e.preventDefault();
                      this.loadPageData(0, e.target.value);
                    }
                  }}
                />
                <a>
                  <img
                    src="/assets/cust/img/svg/navbar-search.svg"
                    alt="Search"
                  />
                </a>
              </form>
            </div>
            {/** Filter */}
            <div className="container nopadding mt-3 mb-0">
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-6">
                  <p className="mt-2 pl-2">
                    {this.state.keyword && `'${this.state.keyword}'`}
                    ????????????{" "}
                    <strong className="text-primary">
                      {Intl.NumberFormat().format(this.state.totalElements)}???
                    </strong>
                  </p>
                </div>
                {/*<div className="col-6 text-right">*/}
                {/*  <div className="custom-control custom-radio custom-control-inline">*/}
                {/*    <input*/}
                {/*      type="radio"*/}
                {/*      id="customRadioInline1"*/}
                {/*      name="customRadioInline1"*/}
                {/*      className="custom-control-input"*/}
                {/*    />*/}
                {/*    <label*/}
                {/*      className="custom-control-label"*/}
                {/*      htmlFor="customRadioInline1"*/}
                {/*    >*/}
                {/*      ?????????*/}
                {/*    </label>*/}
                {/*  </div>*/}
                {/*  <div className="custom-control custom-radio custom-control-inline">*/}
                {/*    <input*/}
                {/*      type="radio"*/}
                {/*      id="customRadioInline2"*/}
                {/*      name="customRadioInline1"*/}
                {/*      className="custom-control-input"*/}
                {/*    />*/}
                {/*    <label*/}
                {/*      className="custom-control-label"*/}
                {/*      htmlFor="customRadioInline2"*/}
                {/*    >*/}
                {/*      ?????????*/}
                {/*    </label>*/}
                {/*  </div>*/}
                {/*</div>*/}
              </div>
            </div>
            {/** ?????? */}
            <div className="container nopadding mt-3 mb-0">
              {this.state.list.map((data, index) => (
                <SearchCompanyListItemView
                  key={index}
                  data={data}
                  onClick={this.onClick}
                />
              ))}
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
