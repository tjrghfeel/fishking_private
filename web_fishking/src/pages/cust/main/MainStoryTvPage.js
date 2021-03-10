import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { MainTab, NavigationLayout, StoryTab },
  MODAL: {
    SelectDateModal,
    SelectAreaModal,
    SelectFishModal,
    SelectCompanySortModal,
  },
  VIEW: { FilterListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          selDateActive: false,
          selAreaActive: false,
          selFishActive: false,
          selSortActive: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          page: 0,
          size: 10,
          live_list: [], // 어복 Live
          tude_list: [], // 어복 Tube
          fishingDate: null,
          sido: null,
          sigungu: null,
          species: [],
          orderBy: "popular",
          latitude: null,
          longitude: null,
        });
        this.loadPageData();
      }

      loadPageData = async () => {};
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectAreaModal
              id={"selAreaModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectCompanySortModal
              id={"selSortModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />

            <NavigationLayout title={"어복스토리"} showSearchIcon={true} />
            <StoryTab activeIndex={1} />

            {/** Filter */}
            <div className="filterWrap">
              <div className="slideList">
                <h6 className="float-left mt-2">어복 Live</h6>
                <ul className="listWrap float-right">
                  <FilterListItemView
                    data={{
                      text: "날짜",
                      isActive: this.state.selDateActive,
                      modalTarget: "selDateModal",
                      onClickClear: () => {},
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "지역",
                      isActive: this.state.selAreaActive,
                      modalTarget: "selAreaModal",
                      onClickClear: () => {},
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "어종",
                      isActive: this.state.selFishActive,
                      modalTarget: "selFishModal",
                      onClickClear: () => {},
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "정렬",
                      isActive: this.state.selSortActive,
                      modalTarget: "selSortModal",
                      onClickClear: () => {},
                    }}
                  />
                </ul>
              </div>
            </div>

            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
