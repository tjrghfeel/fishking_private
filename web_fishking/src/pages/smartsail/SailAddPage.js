import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartSailMainTab },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"승선자 추가"} showBackIcon={true} />

            <div className="container nopadding mt-1">
              <h5 className="text-center">
                어복황제1호
                <br />
                <small className="red">20.10.03 (토)</small>
              </h5>
            </div>
            <div className="space mt-1 mb-4"></div>

            <div class="container nopadding mt-3">
              <p class="text-right">
                <strong class="required"></strong> 필수입력
              </p>
              <form>
                <div class="form-group">
                  <label for="">
                    승선자명 <strong class="required"></strong>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id=""
                    placeholder="승선자명을 입력하세요."
                  />
                </div>
                <div class="form-group">
                  <label for="">
                    휴대폰번호 <strong class="required"></strong>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id=""
                    placeholder="휴대폰번호를 입력해 주세요."
                  />
                </div>
                <div class="form-group">
                  <label for="">
                    생년월일 <strong class="required"></strong>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id=""
                    placeholder="생년월일을 입력해 주세요.(예: 19800707)"
                  />
                </div>
                <div class="form-group">
                  <label for="">
                    성별선택 <strong class="required"></strong>
                  </label>
                  <select class="form-control" id="">
                    <option>성별을 선택하세요</option>
                    <option>남성</option>
                    <option>여성</option>
                  </select>
                </div>

                <div class="form-group">
                  <label class="d-block">
                    거주지역 <strong class="required"></strong>
                  </label>
                  <div class="input-group mb-3">
                    <select class="form-control" id="">
                      <option>시도선택</option>
                    </select>
                    <select class="form-control" id="">
                      <option>시군구선택</option>
                    </select>
                  </div>
                </div>
                <div class="space mb-4"></div>
                <div class="form-group mt-0 mb-0">
                  <label for="InputGPrice" class="d-block">
                    <a href="policy-terms.html" class="text-primary">
                      이용약관
                    </a>{" "}
                    및{" "}
                    <a href="policy-privacy.html" class="text-primary">
                      개인정보 수집/이용
                    </a>
                    <br />
                    개인정보 제 3자 이용약관, 취소규정 동의(필수)
                  </label>
                  <label class="control checkbox">
                    <input
                      type="checkbox"
                      class="add-contrast"
                      data-role="collar"
                    />
                    <span class="control-indicator"></span>
                    <span class="control-text">
                      위 내용을 확인하였으며 동의합니다.{" "}
                    </span>
                  </label>
                </div>

                <a href="#none" class="btn btn-round btn-dark btn-block mt-3">
                  <img
                    src="/assets/smartsail/img/svg/icon-jimun.svg"
                    class="vam"
                  />
                  지문입력
                </a>
                <div class="space mb-4"></div>
              </form>
              <p class="clearfix">
                <br />
                <br />
              </p>
            </div>

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a className="btn btn-primary btn-lg btn-block">확인</a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
