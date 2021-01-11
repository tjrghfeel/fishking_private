import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";
import CsTabs from "../../components/layouts/CsTabs";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file1 = React.createRef(null);
        this.file2 = React.createRef(null);
        this.file3 = React.createRef(null);
        this.companyName = React.createRef(null);
        this.mobile = React.createRef(null);
        this.location = React.createRef(null);
        this.state = {
          companyName: "",
          mobile: "", // 휴대폰번호
          location: "", // 지역명
          portName: "", // 항구명
          memberName: "", // 대표자명
          tel: "", // 전화번호
          isRequested: false, // 요청완료 여부
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      onSubmit = async () => {
        const {
          DataStore: { isValidMobile },
        } = this.props;
        const { companyName, mobile, location } = this.state;
        const file1 = this.file1.current?.files[0] || null;
        const file2 = this.file2.current?.files[0] || null;
        const file3 = this.file3.current?.files[0] || null;

        if (companyName === "") {
          this.companyName.current?.classList.add("is-invalid");
          return;
        } else {
          this.companyName.current?.classList.remove("is-invalid");
        }
        if (mobile === "" || !isValidMobile(mobile)) {
          this.mobile.current?.classList.add("is-invalid");
          return;
        } else {
          this.mobile.current?.classList.remove("is-invalid");
        }
        if (location === "") {
          this.location.current?.classList.add("is-invalid");
          return;
        } else {
          this.location.current?.classList.remove("is-invalid");
        }
        if (file1 === null) {
          this.file1.current?.classList.add("is-invalid");
          return;
        } else {
          this.file1.current?.classList.remove("is-invalid");
        }
        if (file2 === null) {
          this.file2.current?.classList.add("is-invalid");
          return;
        } else {
          this.file2.current?.classList.remove("is-invalid");
        }
        if (file3 === null) {
          this.file3.current?.classList.add("is-invalid");
          return;
        } else {
          this.file3.current?.classList.remove("is-invalid");
        }

        // TODO : 고객센터 > 업체등록요청 : 등록 API 필요
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

            {!this.state.isRequested && (
              <React.Fragment>
                {/** 데이터 */}
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
                              ref={this.companyName}
                              type="text"
                              className="form-control"
                              placeholder="업체명을 입력하세요"
                              value={this.state.companyName}
                              onChange={(e) =>
                                this.setState({ companyName: e.target.value })
                              }
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
                              ref={this.mobile}
                              type="number"
                              className="form-control"
                              placeholder="휴대폰번호를 입력하세요"
                              value={this.state.mobile}
                              onChange={(e) =>
                                this.setState({ mobile: e.target.value })
                              }
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
                              ref={this.location}
                              type="text"
                              className="form-control"
                              placeholder="지역명을 입력하세요"
                              value={this.state.location}
                              onChange={(e) =>
                                this.setState({ location: e.target.value })
                              }
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
                              type="text"
                              className="form-control"
                              placeholder="항구명을 입력하세요"
                              value={this.state.portName}
                              onChange={(e) =>
                                this.setState({ portName: e.target.value })
                              }
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
                              type="text"
                              className="form-control"
                              placeholder="대표자명을 입력하세요"
                              value={this.state.memberName}
                              onChange={(e) =>
                                this.setState({ memberName: e.target.value })
                              }
                            />
                          </div>
                        </div>

                        <div className="form-group">
                          <div className="input-group">
                            <label
                              className="input-group-addon"
                              htmlFor="inputTel"
                            >
                              전화번호
                            </label>
                            <input
                              type="number"
                              className="form-control"
                              placeholder="전화번호를 입력하세요"
                              value={this.state.tel}
                              onChange={(e) =>
                                this.setState({ tel: e.target.value })
                              }
                            />
                          </div>
                        </div>

                        <div className="form-group">
                          <div className="input-group">
                            <label
                              className="input-group-addon"
                              htmlFor="inputBiznum"
                            >
                              사업자등록증{" "}
                              <strong className="required"></strong>
                            </label>
                            <input
                              ref={this.file1}
                              type="file"
                              className="form-control"
                              placeholder="사업자등록증을 첨부하세요."
                              accept="image/*"
                            />
                          </div>
                        </div>

                        <div className="form-group">
                          <div className="input-group">
                            <label
                              className="input-group-addon"
                              htmlFor="inputBizName"
                            >
                              대표자 신분증
                              <strong className="required"></strong>
                            </label>
                            <input
                              ref={this.file2}
                              type="file"
                              className="form-control"
                              placeholder="대표자 신분증을 첨부하세요."
                              accept="image/*"
                            />
                          </div>
                        </div>

                        <div className="form-group">
                          <div className="input-group">
                            <label
                              className="input-group-addon"
                              htmlFor="inputPhone"
                            >
                              정산 통장사본
                              <strong className="required"></strong>
                            </label>
                            <input
                              ref={this.file3}
                              type="file"
                              className="form-control"
                              placeholder="정산 통장사본을 첨부하세요 "
                              accept="image/*"
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>

                {/** 하단버튼 */}
                <div className="fixed-bottom">
                  <div className="row no-gutters">
                    <div className="col-12">
                      <a
                        onClick={this.onSubmit}
                        className="btn btn-primary btn-lg btn-block"
                      >
                        요청하기
                      </a>
                    </div>
                  </div>
                </div>
              </React.Fragment>
            )}

            {this.state.isRequested && (
              <div className="container nopadding mt-5">
                <p className="text-center">
                  업체 등록 요청이 완료되었습니다.
                  <br />
                  관리자 승인 후 스마트승선 앱 사용이 가능한 <br />
                  아이디/비번을 발급해 드립니다.
                  <br />
                  <br />
                  스마트승선 앱을 설치하여 사용해 주세요.
                  <br />
                  <br />
                  <img
                    src="/assets/img/app-icon-smartboat.png"
                    alt=""
                    className="icon-xlg mt-3"
                  />
                  <br />
                  <a className="btn btn-round-grey mt-3">스마트승선 설치하기</a>
                  <br />
                  <a className="btn btn-round-grey mt-2">
                    스마트승선 이용안내 보러가기
                  </a>
                  <br />
                </p>
              </div>
            )}
          </>
        );
      }
    }
  )
);
