import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
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
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"개인정보 제 3자 제공 동의"} showBackIcon={true} />

            {/** 데이터 */}
            <div className="container nopadding mt-0">
              <div className="padding policy">
                <p>
                  "어복황제"에서는 "회원"의 예약/결제 서비스 제공 등을 위해
                  최소한의 개인정보를 수집/제공하고 있습니다.
                </p>
                <p className="clearfix"></p>

                <p>
                  (1)"어복황제" 개인정보 수집 및 이용 목적 및 항목, 보유기간은
                  아래와 같습니다.
                </p>
                <p>
                  "회원"은 정보 수집/이용 약관에 동의하지 않을 수 있으며,
                  동의하지 않는 경우 예약 서비스 이용에 제한이 있을 수 있습니다.
                </p>
                <p className="clearfix"></p>

                <table className="table table-bordered">
                  <colgroup>
                    <col style={{ width: "20%" }} />
                    <col style={{ width: "40%" }} />
                    <col style={{ width: "40%" }} />
                  </colgroup>
                  <thead>
                    <tr>
                      <th>수집/이용 목적</th>
                      <th>위탁 업무 내용</th>
                      <th>개인 정보의 보유 및 이용 기간</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <th>예약 및 구매 서비스 이용</th>
                      <td>예약자명, 휴대폰 번호, 결제정보</td>
                      <td>
                        전자상거래 상 소비자보호에 관한 법률에 따라 5년 간 보관
                      </td>
                    </tr>
                    <tr>
                      <th>결제 서비스 이용</th>
                      <td>
                        - 신용카드 결제 : 카드사명, 카드번호, 유효기간, 이메일
                        등<br />
                        - 휴대폰 결제 : 휴대폰 번호, 통신사, 결제 승인번호 등
                        <br />
                        - 계좌 이체 시 : 은행명, 계좌번호, 예금주
                        <br />- 간편 결제 시: 계좌번호, 결제(통합) 비밀번호
                      </td>
                      <td>
                        전자상거래 상 소비자보호에 관한 법률에 따라 5년 간 보관
                      </td>
                    </tr>
                    <tr>
                      <th>서비스 이용 및 부정거래 기록 확인</th>
                      <td>
                        서비스 이용 시간/이용기록, 접속로그, 이용콘텐츠, 쿠키,
                        접속IP정보, 주소, 사용된 신용카드 정보, 기기 모델명,
                        브라우저 정보
                      </td>
                      <td>통신비밀보호법에 따라 3개월간 보관</td>
                    </tr>
                  </tbody>
                </table>
                <p className="clearfix"></p>

                <p>
                  (2) 회원 정보를 제공받는자, 제공목적, 제공하는 정보,
                  보유/이용기간은 아래와 같습니다.
                </p>
                <p className="clearfix"></p>

                <table className="table table-bordered">
                  <colgroup>
                    <col style={{ width: "15%" }} />
                    <col style={{ width: "35%" }} />
                    <col style={{ width: "35%" }} />
                    <col style={{ width: "15%" }} />
                  </colgroup>
                  <thead>
                    <tr>
                      <th>제공 받는 자</th>
                      <th>제공목적</th>
                      <th>제공정보</th>
                      <th>보유 및 이용기간</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td style={{ textAlign: "center" }}>이벤트 제공 업체</td>
                      <td style={{ textAlign: "center" }}>
                        이벤트 당첨자 해피콜
                      </td>
                      <td style={{ textAlign: "center" }}>
                        이름, 전화번호, 주소
                      </td>
                      <td style={{ textAlign: "center" }}>
                        재화 또는 서비스의
                        <br /> 제공 목적이 달성 된 후 파기
                      </td>
                    </tr>
                    <tr>
                      <td style={{ textAlign: "center" }}>
                        어복황제 상품예약 및 구매 서비스 제공 업체(업주)
                      </td>
                      <td style={{ textAlign: "center" }}>
                        어복황제 상품예약 및 구매 서비스 이용계약 이행
                        <br />
                        (서비스 제공, 확인, 이용자 정보 확인)
                      </td>
                      <td style={{ textAlign: "center" }}>
                        예약 또는 구매한 서비스의 이용자 정보
                        <br />
                        (예약자 이름, 휴대폰번호, 예약자 안심번호, 예약번호,
                        예약한 업체명, 예약한 상품명, 결제금액)
                      </td>
                      <td style={{ textAlign: "center" }}>
                        상품예약 및 구매 서비스 제공 완료 후 6개월
                      </td>
                    </tr>
                    <tr>
                      <td style={{ textAlign: "center" }}>
                        승선 업체
                      </td>
                      <td style={{ textAlign: "center" }}>
                        낚시 관리 및 육성법에 따른 승선자 명부 제출
                      </td>
                      <td style={{ textAlign: "center" }}>
                        이름, 생년월일, 성별, 주소, 전화번호, 비상연락처
                      </td>
                      <td style={{ textAlign: "center" }}>
                        승선한 날로부터 3개월
                      </td>
                    </tr>
                    <tr>
                      <td style={{ textAlign: "center" }}>
                        해양수산부
                      </td>
                      <td style={{ textAlign: "center" }}>
                        낚시어선 출입항 신고 및 승선자명부 제출에관한 업무 및
                        긴급상황 발생 시 신속한 구조활동에 관한 업무
                      </td>
                      <td style={{ textAlign: "center" }}>
                        이름, 생년월일, 성별, 주소, 전화번호, 비상연락처
                      </td>
                      <td style={{ textAlign: "center" }}>
                        국민의 신분증명 및 의무행정 안전부 고시 제2017-1호, 2017.7.26 타법개정,
                        표준 개인정보 보호지침, 개인정보 파일 보호기간 책정 기준표에 의한
                        "낚시海 시스템 연계" 에 관한 기록보관 : 준영구
                      </td>
                    </tr>
                  </tbody>
                </table>
                <p className="clearfix"></p>

                <p>
                  (3) 상위 (1), (2) 외 사항은 어복황제 이용약관,
                  개인정보처리방침 운영에 따릅니다.
                </p>
                <br/>
                <p>부칙</p>
                <p>
                  1. 이 약관은 2021년 4월 1일부터 시행됩니다.
                </p>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
