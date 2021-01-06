import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import MyZzimTabs from "../../components/layouts/MyZzimTabs";
import MyZzimListItem from "../../components/item/MyZzimListItem";
import Http from "../../Http";

export default inject("ViewStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          isPending: false,
          isEnd: false,
          page: 0,
          list: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          saved = false,
          scroll = null,
          data = null,
        } = window.history.state;
        if (saved) {
          if (data !== null) {
            const { page, sort, list, totalElements } = data;
            await this.setState({ page, sort, list, totalElements });
          }
          if (scroll !== null) {
            const { x, y } = scroll;
            window.scrollTo(x, y);
          }
          this.props.ViewStore.clearState();
        } else {
          this.loadPageData();
        }
        window.addEventListener("scroll", this.onScrollForPage);
      }

      componentWillUnmount() {
        window.removeEventListener("scroll", this.onScrollForPage);
      }

      onScrollForPage = async () => {
        const scrollHeight =
          document.scrollingElement.scrollHeight - window.outerHeight;
        const itemHeight = 200;
        const scrollPosition = window.pageYOffset;

        if (scrollPosition + itemHeight >= scrollHeight) {
          await this.loadPageData(this.state.page + 1, this.state.sort);
        }
      };

      loadPageData = async (page = 0) => {
        if (page > 0 && (this.state.isPending || this.state.isEnd)) return;
        else if (page === 0) await this.setState({ isEnd: false });

        await this.setState({ isPending: true, page });
        const {
          content,
          pageable: { pageSize },
        } = await Http._get("/v2/api/take/0/" + page);

        console.log(JSON.stringify(content));

        if (page === 0) {
          await this.setState({ list: content });
        } else {
          await this.setState({ list: this.state.list.concat(content) });
        }

        if (content.length < pageSize) {
          await this.setState({ isEnd: true });
        } else {
          await this.setState({ isEnd: false });
        }

        await this.setState({ isPending: false });
      };

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        // TODO : 마이찜 > 선상/갯바위 : 응답 데이터와 퍼블 데이터 항목 맞지 않음
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"찜한 업체"} showBack={true} />

            {/** 탭메뉴 */}
            <MyZzimTabs />

            {/** 업체 */}
            <div className="container nopadding mt-3 mb-0">
              {this.state.list.length > 0 &&
                this.state.list.map((data, index) => (
                  <MyZzimListItem key={index} data={data} />
                ))}
            </div>
          </>
        );
      }
    }
  )
);
