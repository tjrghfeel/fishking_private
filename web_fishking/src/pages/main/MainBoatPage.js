import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { MainTab, NavigationLayout },
  VIEW: { FilterListView },
  MODAL: {
    SelectDateModal,
    SelectAreaModal,
    SelectFishModal,
    SelectCompanySortModal,
  },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          filterLiveActive: false,
          filterDateText: "날짜",
          filterAreaText: "지역",
          filterFishText: "어종",
          filterSortText: "정렬",
          filterOptionText: "옵션",
          filterDateActive: false,
          filterAreaActive: false,
          filterFishActive: false,
          filterSortActive: false,
          filterOptionActive: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) => console.log(selected)}
            />
            <SelectAreaModal
              id={"selAreaModal"}
              onSelected={(selected) => console.log(selected)}
            />
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => console.log(selected)}
            />
            <SelectCompanySortModal
              id={"selSortModal"}
              onSelected={(selected) => console.log(selected)}
            />

            <NavigationLayout title={"바다낚시"} showSearchIcon={true} />

            <FilterListView
              list={[
                {
                  isLiveButton: true,
                  isActive: this.state.filterLiveActive,
                  onClick: (selected) => console.log(selected),
                },
                {
                  text: this.state.filterDateText,
                  isActive: this.state.filterDateActive,
                  modalTarget: "selDateModal",
                },
                {
                  text: this.state.filterAreaText,
                  isActive: this.state.filterAreaActive,
                  modalTarget: "selAreaModal",
                },
                {
                  text: this.state.filterFishText,
                  isActive: this.state.filterFishActive,
                  modalTarget: "selFishModal",
                },
                {
                  text: this.state.filterSortText,
                  isActive: this.state.filterSortActive,
                  modalTarget: "selSortModal",
                },
                {
                  text: this.state.filterOptionText,
                  isActive: this.state.filterOptionActive,
                },
              ]}
            />

            <MainTab activeIndex={1} />
          </React.Fragment>
        );
      }
    }
  )
);
