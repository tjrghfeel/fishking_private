import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";
import CsTabs from "../../components/layouts/CsTabs";
import CsFaqListItem from "../../components/item/CsFaqListItem";
import Http from "../../Http";

export default inject()(
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
      /************ ************ ************ ************ ************/
      /** functions */
      /************ ************ ************ ************ ************/
      async componentDidMount() {
        this.loadPageData();
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
        } = await Http._get("/v2/api/faq/" + page);

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
      /************ ************ ************ ************ ************/
      /** render */
      /************ ************ ************ ************ ************/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"고객센터"} showBack={true} />

            {/** 탭메뉴 */}
            <CsTabs />

            {/** 데이터 */}
            <div className="container nopadding">
              <div className="accordion" id="accordionFaq">
                {this.state.list.length > 0 &&
                  this.state.list.map((data, index) => (
                    <CsFaqListItem
                      key={index}
                      data={data}
                      expend={index === 0}
                    />
                  ))}
              </div>
              <p className="space mt-1"></p>
            </div>
          </>
        );
      }
    }
  )
);
