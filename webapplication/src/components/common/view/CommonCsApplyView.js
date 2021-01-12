import React from "react";
import { inject, observer } from "mobx-react";

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      requestSubmit = async () => {
        const { PageStore, APIStore, navigateTo } = this.props;
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수입력
              </p>

              <form>
                <div className="row">
                  <div className="col-xs-12 col-sm-12 apply ">
                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputCompany"
                        >
                          업체명<strong className="required"></strong>
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="inputCompany"
                          placeholder="업체명을 입력하세요"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputPhone"
                        >
                          휴대폰번호<strong className="required"></strong>
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="inputPhone"
                          placeholder="휴대폰번호를 입력하세요"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputRocal"
                        >
                          지역명<strong className="required"></strong>
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="inputRocal"
                          placeholder="지역명을 입력하세요"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputPort"
                        >
                          항구명
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="inputPort"
                          placeholder="항구명을 입력하세요"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputCname"
                        >
                          대표자명
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="inputCname"
                          placeholder="대표자명을 입력하세요"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label className="input-group-addon" htmlFor="inputTel">
                          전화번호
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="inputTel"
                          placeholder="전화번호를 입력하세요"
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputBiznum"
                        >
                          사업자등록증 <strong className="required"></strong>
                        </label>
                        <input
                          type="file"
                          className="form-control"
                          id="inputBiznum"
                          placeholder="사업자등록증을 첨부하세요."
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputBizName"
                        >
                          대표자 신분증<strong className="required"></strong>
                        </label>
                        <input
                          type="file"
                          className="form-control"
                          id="inputBizName"
                          placeholder="대표자 신분증을 첨부하세요."
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <div className="input-group">
                        <label
                          className="input-group-addon"
                          htmlFor="inputPhone"
                        >
                          정산 통장사본<strong className="required"></strong>
                        </label>
                        <input
                          type="file"
                          className="form-control"
                          id="inputPhone"
                          placeholder="정산 통장사본을 첨부하세요 "
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </form>
            </div>

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.requestSubmit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    요청하기
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
