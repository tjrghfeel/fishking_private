import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import CsTabs from "../../components/layouts/CsTabs";
import CsQnaTabs from "../../components/layouts/CsQnaTabs";
import CsQnaListItem from "../../components/item/CsQnaListItem";
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
        } = await Http._get("/v2/api/qna/" + page);

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

      onClickItem = async (item) => {
        const { page, sort, list, totalElements } = this.state;
        const {
          history,
          ViewStore: { saveState },
        } = this.props;
        await saveState({ page, sort, list, totalElements });

        history.push(`/common/cs/qna/detail/` + item.id);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"고객센터"} showBack={true} />
            {/** 탭메뉴 */}
            <CsTabs />
            {/** 탭메뉴 */}
            <CsQnaTabs />

            {/** 데이터 */}
            {this.state.list.map((data, index) => (
              <CsQnaListItem
                key={index}
                data={data}
                onClick={this.onClickItem}
              />
            ))}
          </>
        );
      }
    }
  )
);