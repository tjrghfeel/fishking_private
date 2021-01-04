import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import CsTabs from "../../components/layouts/CsTabs";
import CsQnaTabs from "../../components/layouts/CsQnaTabs";
import Http from "../../Http";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const {
          match: {
            params: { id: postId },
          },
        } = this.props;

        const resolve = await Http._get("/v2/api/qna/detail", { postId });
        this.setState(resolve);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"고객센터"} showBack={true} />

            {/** 탭메뉴 */}
            <CsTabs />

            {/** 탭메뉴 */}
            <CsQnaTabs />

            {/** 데이터 */}
            {this.state.id && (
              <div className="container nopadding mt-3">
                <dl className="dl-horizontal-round">
                  <dt>문의 유형</dt>
                  <dd>
                    {this.state.questionType === "0" ? "예약결제" : "취소"}
                  </dd>
                  <dt>답변 여부</dt>
                  <dd>
                    <span
                      className={
                        "status-icon" +
                        (this.state.replied ? " status3" : " status2")
                      }
                    >
                      {this.state.replied ? "답변완료" : "답변대기"}
                    </span>{" "}
                    &nbsp;{" "}
                    {this.state.date &&
                      this.state.date.substr(0, 10).replace(/[-]/g, ".")}
                  </dd>
                  <dt>문의 내용</dt>
                  <dd>{this.state.contents}</dd>
                </dl>
                <hr className="space mt-4 mb-4" />
                {this.state.replied && (
                  <React.Fragment>
                    <dl className="dl-horizontal-round">
                      <dt>답변 내용</dt>
                      <dd>{this.state.replyContents}</dd>
                    </dl>
                    <hr className="space mt-4 mb-4" />
                  </React.Fragment>
                )}
              </div>
            )}

            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={() => history.goBack()}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    목록으로
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
