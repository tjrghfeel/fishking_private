import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject()(
  observer(() => {
    return (
      <>
        {/** Navigation */}
        <Navigation title={"이용약관"} visibleBackIcon={true} />

        {/** 데이터 */}
        <div className="container nopadding mt-0">
          <div className="padding policy">
            <h4>제 1 조 (목적)</h4>
            <p>
              이 약관은 주식회사 투비 (이하 "회사”라 합니다)가 제공하는 어복황제
              서비스(이하 "서비스”라 합니다)와 관련하여, 회사와 이용 고객간에
              서비스의 이용조건 및 절차, 회사와 회원간의 권리, 의무 및 기타
              필요한 사항을 규정함을 목적으로 합니다. 본 약관은 PC통신,
              스마트폰(안드로이드폰, 아이폰 등) 앱 등을 이용하는 전자상거래에
              대해서도 그 성질에 반하지 않는 한 준용됩니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 2 조 (용어의 정의)</h4>
            <p>
              1. “어복황제”란 “업주”가 재화 또는 서비스 상품(이하 “재화 등”이라
              합니다)을 “이용자”에게 제공하기 위하여, “회사”가 컴퓨터 등
              정보통신설비를 이용하여 재화 등을 거래할 수 있도록 설정하여
              제공하는 가상의 영업장을 말하며, 아울러 어복황제를 운영하는 회사의
              의미로도 사용합니다.
              <br />
              2. “이용자” 란 “어복황제”에 접속하여 이 약관에 따라 “어복황제”가
              제공하는 서비스를 받는 회원 및 비회원을 말합니다.
              <br />
              3. “회원”이라 함은 “어복황제”에 개인정보를 제공하여 회원등록을 한
              자로서, “어복황제”의 정보를 지속적으로 제공받으며, “어복황제”이
              제공하는 서비스를 계속적으로 이용할 수 있는 자를 의미하고,
              “어복황제” 광고업소는 포함되지 않습니다.
              <br />
              4. “비회원”이라 함은 “회원”으로 가입하지 않고 “회사”가 제공하는
              서비스를 이용하는 자를 말합니다.
              <br />
              5. “상품예약서비스”라 함은 “회원”이 “업주”의 “재화 등”을 예약 할
              수 있도록 “회사”가 제공하는 서비스를 말하며, 낚시배 및 낚시좌대
              제공 등으로 나누어 집니다.
              <br />
              6. “업주”란 “회사”가 제공하는 “서비스”를 이용해 “재화 등”에 관한
              정보를 게재하고, 제공(낚시배 및 낚시좌대 제공)하는 주체를
              말합니다.
              <br />
              7. “오픈마켓”이란 “회사”에서 제공하는 “결제 서비스”를 말하며
              일반결제 등으로 나누어집니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 3 조 (약관의 명시와 개정)</h4>
            <p>
              1. “회사”는 이 약관의 내용과 상호, 영업소 소재지 주소(소비자의
              불만을 처리할 수 있는 곳의 주소를 포함), 대표자의 성명,
              사업자등록번호, 통신판매업 신고번호, 연락처(전화, 전자우편 주소
              등) 등을 “이용자”가 알 수 있도록 “어복황제”의 고객센터화면에
              게시합니다. 다만, 약관의 내용은 “이용자”가 연결화면을 통하여
              보도록 할 수 있습니다.
              <br />
              2. “회사”는 『전자상거래 등에서의 소비자보호에 관한 법률』,
              『약관의 규제등에 관한 법률』, 『전자문서 및 전자거래기본법』,
              『전자서명법』, 『정보통신망 이용촉진 등에 관한 법률』,
              『소비자기본법』 등 관련법령을 위배하지 않는 범위에서 이 약관을
              개정할 수 있습니다.
              <br />
              3. “회사”는 약관을 개정할 경우에는 적용일자 및 개정사유를 명시하여
              현행약관과 함께 “사이트”의 초기화면에 그 적용일자 7일 이전부터
              적용일자 전일까지 공지합니다. 다만, “이용자”에게 불리하게
              약관내용을 변경하는 경우에는 최소한 30일 이상의 사전 유예기간을
              두고 공지합니다. 이 경우 “회사”는 개정 전과 개정 후 내용을
              “이용자”가 알기 쉽도록 표시합니다.
              <br />
              4. “회원”은 변경된 약관에 동의하지 않을 권리가 있으며, 변경된
              약관에 동의하지 않을 경우에는 서비스 이용을 중단하고 탈퇴를 요청할
              수 있습니다. 다만, “이용자”가 제3항의 방법 등으로 “회사”가 별도
              고지한 약관 개정 공지 기간 내에 “회사”에 개정 약관에 동의하지
              않는다는 명시적인 의사표시를 하지 않는 경우 변경된 약관에 동의한
              것으로 간주합니다.
              <br />
              5. 이 약관에서 정하지 아니한 사항과 이 약관의 해석에 관하여는
              『전자상거래 등에서의 소비자보호에 관한 법률』, 『약관의 규제 등에
              관한 법률』, 공정거래위원회가 정하는 『전자상거래 등에서의
              소비자보호지침』 및 관계 법령 또는 상관례에 따릅니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 4 조 (관련법령과의 관계)</h4>
            <p>
              이 약관 또는 개별약관에서 정하지 않은 사항은 전기통신사업법,
              전자거래기본법, 정보통신망법, 전자상거래 등에서의 소비자보호에
              관한 법률, 개인정보보호법 등 관련 법령의 규정과 일반적인 상관례에
              의합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 5 조 (서비스의 제공 및 변경)</h4>
            <p>
              1. “회사”는 다음과 같은 서비스를 제공합니다.
              <br />
              <div className="indent">
                1) “재화 등”에 대한 광고플랫폼 서비스
                <br />
                2) “재화 등”에 대한 예약중계 등 통신판매중개서비스
                <br />
                3) 위치기반 서비스
                <br />
                4) 기타 “회사”가 정하는 서비스
                <br />
              </div>
              2. “회사”는 서비스 제공과 관련한 회사 정책의 변경 등 기타 상당한
              이유가 있는 경우 등 운영상, 기술상의 필요에 따라 제공하고 있는
              “서비스”의 전부 또는 일부를 변경 또는 중단할 수 있습니다.
              <br />
              3. “서비스”의 내용, 이용방법, 이용시간에 대하여 변경 또는 “서비스”
              중단이 있는 경우에는 변경 또는 중단될 “서비스”의 내용 및 사유와
              일자 등은 그 변경 또는 중단 전에 “서비스” 내 "공지사항" 화면 등
              “회원”이 충분이 인지할 수 있는 방법으로 사전에 공지합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 6 조 (서비스 이용시간 및 중단)</h4>
            <p>
              1. “서비스”의 이용은 “회사”의 업무상 또는 기술상 특별한 지장이
              없는 한 연중무휴 1일 24시간을 원칙으로 합니다. 다만, 정기 점검
              등의 필요로 “회사”가 정한 날이나 시간은 제외됩니다. 정기점검시간은
              서비스제공화면에 공지한 바에 따릅니다.
              <br />
              2. “회사”는 “서비스”의 원활한 수행을 위하여 필요한 기간을 정하여
              사전에 공지하고 서비스를 중지할 수 있습니다. 단, 불가피하게 긴급한
              조치를 필요로 하는 경우 사후에 통지할 수 있습니다.
              <br />
              3. “회사”는 컴퓨터 등 정보통신설비의 보수점검•교체 및 고장, 통신의
              두절 등의 사유가 발생한 경우에는 “서비스”의 제공을 일시적으로
              중단할 수 있습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 7 조 (이용계약의 성립)</h4>
            <p>
              1. 이용계약은 “회원”이 되고자 하는 자(이하 “가입신청자”)가 약관의
              내용에 동의를 하고, “회사”가 정한 가입 양식에 따라 회원정보를
              기입하여 회원가입신청을 하고 “회사”가 이러한 신청에 대하여
              승인함으로써 체결됩니다.
              <br />
              2. “회사”는 다음 각 호에 해당하는 신청에 대하여는 승인을 하지
              않거나 사후에 이용계약을 해지할 수 있습니다.
              <br />
              <div className="indent">
                1) 가입신청자가 이 약관에 의하여 이전에 회원자격을 상실한 적이
                있는 경우
                <br />
                2) 실명이 아니거나 타인의 명의를 이용한 경우
                <br />
                3) 회사가 실명확인절차를 실시할 경우에 이용자의 실명가입신청이
                사실 아님이 확인된 경우
                <br />
                4) 등록내용에 허위의 정보를 기재하거나, 기재 누락, 오기가 있는
                경우
                <br />
                5) 이미 가입된 회원과 전자우편주소가 동일한 경우
                <br />
                6) 부정한 용도 또는 영리를 추구할 목적으로 본 서비스를
                이용하고자 하는 경우
                <br />
                7) 기타 이 약관에 위배되거나 위법 또는 부당한 이용신청임이
                확인된 경우 및 회사가 합리적인 판단에 의하여 필요하다고 인정하는
                경우
                <br />
              </div>
              3. 제1항에 따른 신청에 있어 “회사”는 “회원”에게 전문기관을 통한
              실명확인 및 본인인증을 요청할 수 있습니다.
              <br />
              4. “회사”는 서비스관련설비의 여유가 없거나, 기술상 또는 업무상
              문제가 있는 경우에는 승낙을 유보할 수 있습니다.
              <br />
              5. “회원”은 회원가입 시 등록한 사항에 변경이 있는 경우, 상당한
              기간 이내에 “회사”에 대하여 회원정보 수정 등의 방법으로 그
              변경사항을 알려야 합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 8 조 (이용계약의 종료)</h4>
            <p>
              1. “회원”의 해지
              <div className="indent">
                1) “회원”은 언제든지 “회사”에게 해지의사를 통지함으로써
                이용계약을 해지할 수 있습니다.
                <br />
                2) “회사”는 전항에 따른 “회원”의 해지 요청에 대해 특별한 사정이
                없는 한 이를 즉시 처리합니다.
                <br />
                3) “회원”이 계약을 해지하는 경우 “회원”이 작성한 게시물은
                삭제되지 않습니다.
                <br />
              </div>
              2. “회사”의 해지
              <br />
              <div className="indent">
                1) “회사”는 다음과 같은 사유가 있는 경우, 이용계약을 해지할 수
                있습니다. 이 경우 “회사”는 “회원”에게 전자우편, 전화, 팩스
                기타의 방법을 통하여 해지 사유를 밝혀 해지의사를 통지합니다.
                다만, “회사”는 해당“회원”에게 해지 사유에 대한 의견진술의 기회를
                부여 할 수 있습니다.
                <br />
                가. 제7조 제2항에서 정하고 있는 이용계약의 승낙거부사유가 있음이
                확인 된 경우
                <br />
                나. “회원”이 “회사”나 다른 회원 기타 타인의 권리나 명예, 신용
                기타 정당한 이익을 침해하는 행위를 한 경우
                <br />
                다. 기타 “회원”이 이 약관 및 “회사”의 정책에 위배되는 행위를
                하거나 이 약관에서 정한 해지 사유가 발생한 경우
                <br />
                2) 이용계약은 “회사”가 해지의사를 “회원”에게 통지함으로써
                종료됩니다. 이 경우 “회사”가 해지의사를 “회원”이 등록한
                전자우편주소로 발송하거나 “회사” 게시판에 게시함으로써 통지에
                갈음합니다.
              </div>
            </p>
            <p className="clearfix"></p>

            <h4>제 9 조 (회원의 ID 및 비밀번호에 대한 의무)</h4>
            <p>
              1. ID와 비밀번호에 관한 관리책임은 “회원”에게 있습니다.
              <br />
              2. “회원”은 자신의 ID 및 비밀번호를 제3자에게 이용하게 해서는
              안됩니다.
              <br />
              3. “회원”이 자신의 ID 및 비밀번호를 도난 당하거나 제3자가 사용하고
              있음을 인지한 경우에는 즉시 “회사”에 통보하고 “회사”의 조치가 있는
              경우에는 그에 따라야 합니다.
              <br />
              4. “회원”이 제3항에 따른 통지를 하지 않거나 “회사”의 조치에 응하지
              아니하여 발생하는 모든 불이익에 대한 책임은 “회원”에게 있습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제10조 (저작권)</h4>
            <p>
              본 서비스에 대한 모든 권리는 저작권자인 “회사”가 보유하며
              저작권자만이 사용 허락을 할 권리가 있습니다. 회원은 아래와 같은
              제한사항을 준수하여야 합니다.
              <br />
              1. 본 서비스는 저작권법, 국제 저작권 협약 및 기타 지적재산권에
              관한 법률 및 협정에 의해 보호됩니다. 서비스와 관련된 이미지,
              동영상, 텍스트, 디자인 등 모든 복사본에 대한 저작권 및 지적
              재산권은 회사가 보유합니다. 서비스를 이용함으로써 엑세스 할 수
              있는 내용물에 대한 모든 소유권과 지적 재산권은 각 내용물의 해당
              소유주의 자산이며 저작권법이나 기타 지적 재산권 법률 및 협약에
              의해 보호됩니다.
              <br />
              2. 회원은 약관에서 정한 사항을 제외하고는 허락 받은 권리를
              제3자에게 양도ㆍ임대하거나 재사용 허락하거나 담보로 제공할 수
              없습니다.
              <br />
              3. 서비스에 사용된 모든 텍스트 및 이미지, 로고 등의 상표권 및
              저작권은 모두 회사에게 있습니다. 이 밖에 명시적으로 제공되지 않은
              모든 권리는 당사가 보유합니다.
              <br />
              4. 회사는 회원이 본 서비스를 저작권법 및 약관의 조건에 합치된
              범위에서 사용하고 있는지 여부를 확인하기 위하여 회원에게 필요한
              사항을 서면으로 확인 요청할 수 있고, 이에 대해 회원은 적극
              협조하여야 합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 11 조 (회원 및 이용자의 의무)</h4>
            <p>
              1. “회원”은 관계법령 및 이 약관의 규정, “회사”의 정책, 이용안내 등
              “회사”가 통지 또는 공지하는 사항을 준수하여야 하며, 기타 “회사”
              업무에 방해되는 행위를 하여서는 안됩니다.
              <br />
              2. “회원”은 서비스 이용과 관련하여 다음 각 호의 행위를 하여서는
              안됩니다.
              <br />
              <div className="indent">
                1) 서비스 신청 또는 변경 시 허위내용의 등록
                <br />
                2) “회사”에 게시된 정보의 허가 받지 않은 변경
                <br />
                3) “회사”가 정한 정보 이외의 정보(컴퓨터 프로그램 등)의 송신
                또는 게시
                <br />
                4) “회사” 또는 제3자의 저작권 등 지적 재산권에 대한 침해
                <br />
                5) “회사” 또는 제3자의 명예를 손상시키거나 업무를 방해하는 행위
                <br />
                6) 외설 또는 폭력적인 메시지, 화상, 음성 기타 공공질서
                미풍양속에 반하는 정보를 “서비스”에 공개 또는 게시하는 행위
                <br />
                7) 고객센터 상담 내용이 욕설, 폭언, 성희롱 등에 해당하는 행위
                <br />
                8) 포인트를 부정하게 적립하거나 사용하는 등의 행위
                <br />
                9) 허위 예약, 허위 리뷰 작성 등을 통해 서비스를 부정한 목적으로
                이용하는 행위
                <br />
                10) 자신의 ID, PW를 제3자에게 양도하거나 대여하는 등의 행위
                <br />
                11) 회원이 아이디, 닉네임, 기타 서비스 내에서 사용되는 명칭 등의
                선정 시에는 다음 각 호에 해당하는 내용을 사용하여서는 안됩니다.
                <br />
                <div className="indent">
                  ① 회사가 제공하는 서비스의 공식 운영자를 사칭하거나 유사한
                  명칭을 사용하여 다른 이용고객에게 혼란을 초래하는 행위
                  <br />
                  ② 선정적이고 음란한 내용이 포함된 명칭의 사용
                  <br />
                  ③ 제3자의 상표권, 저작권 등의 권리를 침해할 가능성이 있는
                  명칭의 사용
                  <br />
                  ④ 비어, 속어라고 판단되거나 반사회적이고 관계법령에 저촉되는
                  내용이 포함된 명칭의 사용
                  <br />
                </div>
                12) 정당한 사유 없이 당사의 영업을 방해하는 내용을 기재하는 행위
                <br />
                13) 리버스엔지니어링, 디컴파일, 디스어셈블 및 기타 일체의 가공
                행위를 통하여 서비스를 복제, 분해 또 모방 기타 변형하는 행위
                <br />
                14) 자동 접속 프로그램 등을 사용하는 등 정상적인 용법과 다른
                방법으로 서비스를 이용하여 회사의 서버에 부하를 일으켜 회사의
                정상적인 서비스를 방해하는 행위
                <br />
                15) “회원”이 의도적으로 “회사”의 정상적 영업 및 서비스 제공
                활동을 방해하는 행위
                <br />
                16) 기타 관계법령에 위반된다고 판단되는 행위
                <br />
              </div>
              3. “회사”는 회원 및 이용자가 본 조 제2항의 금지행위를 한 경우 본
              약관 제14조에 따라 서비스 이용 제한 조치를 취할 수 있습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 12 조 (회원의 게시물)</h4>
            <p>
              “회원”이 작성한 게시물에 대한 저작권 및 모든 책임은 이를 게시한
              “회원”에게 있습니다. 단, “회사”는 “회원”이 게시하거나 등록하는
              게시물의 내용이 다음 각 호에 해당한다고 판단되는 경우 해당
              게시물을 사전통지 없이 삭제 또는 임시조치(블라인드)할 수 있습니다.
              <br />
              <div className="indent">
                1) 다른 회원 또는 제3자를 비방하거나 명예를 손상시키는 내용인
                경우
                <br />
                2) 공공질서 및 미풍양속에 위반되는 내용일 경우
                <br />
                3) 범죄적 행위에 결부된다고 인정되는 경우
                <br />
                4) 회사의 저작권, 제3자의 저작권 등 기타 권리를 침해하는 내용인
                경우
                <br />
                5) 회원이 사이트와 게시판에 음란물을 게재하거나 음란사이트를
                링크하는 경우
                <br />
                6) 회사로부터 사전 승인 받지 아니한 상업광고, 판촉 내용을
                게시하는 경우
                <br />
                7) 해당 상품과 관련 없는 내용인 경우
                <br />
                8) 정당한 사유 없이 “회사” 또는 “업주”의 영업을 방해하는 내용을
                기재하는 경우
                <br />
                9) 자신의 업소를 홍보할 목적으로 전화번호 노출, 계좌번호노출,
                중복게시물 및 홈페이지 노출, 허위 또는 과장된 게시글을 게재하는
                경우
                <br />
                10) 의미 없는 문자 및 부호에 해당하는 경우
                <br />
                11) 제3자 등으로부터 권리침해신고가 접수된 경우
                <br />
                12) 닉네임 및 게시물 등에 비어, 속어라고 판단되거나 반사회적이고
                관계법령에 저촉되는 내용이 포함된 경우
                <br />
                13) 악의적인 목적, 의미없는 내용 또는 도배성 및 중복게시글을
                게재하는 경우
                <br />
                14) 기타 관계법령에 위반된다고 판단되는 경우
              </div>
            </p>
            <p className="clearfix"></p>

            <h4>제 13 조 (회원게시물의 관리)</h4>
            <p>
              1. “회원”의 "게시물"이 정보통신망법 및 저작권법 등 관련법에
              위반되는 내용을 포함하는 경우, 권리자는 관련법이 정한 절차에 따라
              해당 "게시물"의 게시중단 및 삭제 등을 요청할 수 있으며, 회사는
              관련법에 따라 조치를 취하여야 합니다.
              <br />
              2. 회사는 전항에 따른 권리자의 요청이 없는 경우라도 권리침해가
              인정될 만한 사유가 있거나 기타 회사 정책 및 관련법에 위반되는
              경우에는 관련법에 따라 해당 "게시물"에 대해 임시조치 등을 취할 수
              있습니다.
              <br />
              3. 본 조에 따른 세부 절차는 정보통신망법 및 저작권법이 규정한 범위
              내에서 회사가 정한 게시중단요청서비스에 따릅니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 14 조 (이용제한 등)</h4>
            <p>
              1. “회사”는 “회원”이 이 약관의 의무를 위반하거나 “서비스”의
              정상적인 운영을 방해한 경우, 경고, 일시정지, 영구이용정지 등의
              서비스 이용제한 조치를 취할 수 있습니다.
              <br />
              2. “회사”는 "주민등록법"을 위반한 명의도용 및 결제도용, 전화번호
              도용, "저작권법" 및 "컴퓨터프로그램보호법"을 위반한 불법프로그램의
              제공 및 운영방해, "정보통신망법"을 위반한 불법통신 및 해킹,
              악성프로그램의 배포, 접속권한 초과행위 등과 같이 관련법을 위반한
              경우에는 즉시 영구이용정지를 할 수 있습니다.
              <br />
              3. 회사는 회원이 계속해서 1년 이상 로그인하지 않는 경우,
              회원정보의 보호 및 운영의 효율성을 위해 이용을 제한할 수 있습니다.
              <br />
              4. 본 조의 이용제한 범위 내에서 제한의 조건 및 세부내용은 회사의
              이용제한정책에서 정하는 바에 의합니다.
              <br />
              5. 본 조에 따라 서비스 이용을 제한하거나 계약을 해지하는 경우에는
              회사는 제15조[회원에 대한 통지]에 따라 통지합니다.
              <br />
              6. “회원”은 본 조에 따른 이용제한 등에 대해 “회사”가 정한 절차에
              따라 이의신청을 할 수 있습니다. 이 때 이의가 정당하다고 회사가
              인정하는 경우 “회사”는 즉시 서비스의 이용을 재개합니다.
              <br />
              7. 본 조에 따라 이용제한이 되는 경우 서비스 이용을 통해 획득한
              혜택 등도 모두 이용중단, 또는 소멸되며, “회사”는 이에 대해 별도로
              보상하지 않습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 15 조 (권리의 귀속)</h4>
            <p>
              1. “서비스”에 대한 저작권 및 지적재산권은 “회사”에 귀속됩니다. 단,
              “회원”의 "게시물" 및 제휴계약에 따라 제공된 저작물 등은
              제외합니다.
              <br />
              2. “회사”가 제공하는 “서비스”의 디자인, “회사”가 만든 텍스트,
              스크립트(script), 그래픽, “회원” 상호간 전송 기능 등 “회사”가
              제공하는 “서비스”에 관련된 모든 상표, 서비스 마크, 로고 등에 관한
              저작권 기타 지적재산권은 대한민국 및 외국의 법령에 기하여 “회사”가
              보유하고 있거나 “회사”에게 소유권 또는 사용권이 있습니다.
              <br />
              3. “회원”은 이 이용약관으로 인하여 서비스를 소유하거나 “서비스”에
              관한 저작권을 보유하게 되는 것이 아니라, “회사로부터 서비스의
              이용을 허락 받게 되는바, 정보 취득 또는 개인용도로만 “서비스”를
              이용할 수 있습니다.
              <br />
              4. “회원”은 명시적으로 허락된 내용을 제외하고는 “서비스”를 통해
              얻어지는 정보를 영리 목적으로 사용, 복사, 유통하는 것을 포함하여,
              “회사”가 만든 텍스트, 스크립트, 그래픽의 “회원” 상호간 전송 기능
              등을 복사하거나 유통할 수 없습니다.
              <br />
              5. “회사”는 서비스와 관련하여 “회원”에게 “회사”가 정한 이용조건에
              따라 계정, ID, 콘텐츠 등을 이용할 수 있는 이용권만을 부여하며,
              이용자는 회사를 이용함으로써 얻은 정보를 회사의 사전 승낙 없이
              복제, 송신, 출판, 배포, 방송 등 기타 방법에 의하여 영리 목적으로
              이용하거나 제3자에게 이용, 양도, 판매, 담보목적으로 제공하여서는
              안됩니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 16 조 (회원에 대한 통지)</h4>
            <p>
              1. “회사”가 “회원”에 대한 통지를 하는 경우, “회원”이 가입신청 시
              “회사”에 제출한 전자우편 주소나 휴대전화번호 등으로 할 수
              있습니다.
              <br />
              2. “회사”는 불특정다수 “회원”에 대한 통지의 경우, 1주일 이상
              사이트에 게시함으로써 개별 통지에 갈음할 수 있습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 17 조 (회사의 의무)</h4>
            <p>
              1. “회사”는 관련법과 이 약관이 금지하거나 미풍양속에 반하는 행위를
              하지 않으며, 계속적이고 안정적으로 “서비스”를 제공하기 위하여
              최선을 다하여 노력합니다.
              <br />
              2. “회사”는 “회원”이 안전하게 “서비스”를 이용할 수 있도록
              개인정보(신용정보 포함)보호를 위해 개인정보취급방침을 수립하여
              공시하고 준수합니다.
              <br />
              3. 회사는 관계 법령이 정한 의무사항을 준수합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 18 조 (개별 서비스에 대한 약관 및 이용조건)</h4>
            <p>
              1. “회사”는 개별 서비스와 관련한 별도의 약관 및 이용 정책을 둘 수
              있으며, 개별서비스에서 별도로 적용되는 약관에 대한 동의는 “회원”이
              개별서비스를 최초로 이용할 경우 별도의 동의절차를 거치게 됩니다.
              이 경우 개별 서비스에 대한 이용약관 등이 본 약관에 우선합니다.
              <br />
              2. 전항에도 불구하고 “회사”는 개별 서비스에 대한 이용 정책에
              대해서는 “서비스”를 통해 공지할 수 있으며, “이용자”는 이용 정책을
              숙지하고 준수하여야 합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 19 조 (오픈마켓 서비스 이용)</h4>
            <p>
              1. “회사”는 “오픈마켓 서비스”에 대하여, 통신판매중개자로서
              구매자와 “업주” 간의 자유로운 상품의 거래를 위한 시스템을 운영 및
              관리, 제공할 뿐이므로, 구매자는 상품을 구매하기 전에 반드시
              “업주”가 사이트 내에 작성한 상품의 상세 내용과 거래의 조건을
              정확하게 확인해야 합니다. 구매하려는 상품의 내용과 거래의 조건을
              확인하지 않고 구매하여 발생한 모든 손실과 손해는 구매자 본인이
              부담합니다.
              <br />
              2. 구매자는 이 약관 및 “회사”가 서비스 화면에서 공지하는 내용을
              지켜야 하며, 이용약관 및 공지 내용을 위반하거나 이행하지 않아서
              발생하는 모든 손실과 손해에 대하여 책임을 집니다.
              <br />
              3. 구매자는 “업주”와 상품 매매 절차에서 분쟁이 발생하면 분쟁의
              해결을 위하여 성실히 임해야 하며, 구매자의 분쟁해결 과정 시 불성실
              등 구매자의 귀책사유로 “업주”와 “회사”에 손실과 손해가 발생하면
              그에 대한 모든 책임을 부담하여야 합니다.
              <br />
              4. 구매자는 매매대금의 결제와 관련하여 구매자가 입력한 정보 및 그
              정보와 관련하여 발생하는 제반 문제에 대한 모든 책임을 부담하여야
              합니다.
              <br />
              5. “회사”는 구매자에게 서비스가 안전하게 제공될 수 있도록 각종
              설비와 자료를 관리하고, 서비스가 제공 목적에 맞게 이용되고 있는지
              확인합니다. 만일 구매자에게 서비스 이용 목적에 위반되는 부분이
              있는 것으로 확인되면 사유의 소명을 요청할 수 있습니다.
              <br />
              6. 구매자는 “회사”가 구매자의 서비스 이용 편의를 높이기 위하여
              “판매자(업주)” 등으로부터 상품 관련 정보를 제공받아 게재하거나
              제3자가 제공하는 방식으로 사이트를 통하여 참조용 상품 정보나 관련
              콘텐츠를 제공하는 경우에도 상품 구매와 관련하여 자신의 판단과
              책임으로 결정하여야 합니다. 이 경우 “회사”는 어떠한 경우에도
              구매자의 구매결정에 대하여 책임을 부담하지 아니 합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 20 조 (포인트)</h4>
            <p>
              1. 포인트는 “서비스”를 통해 “재화 등”을 구매하는 경우 대금 결제
              수단으로 사용할 수 있는 현금 등가 등의 결제수단을 의미합니다.
              <br />
              2. 포인트는 “회원”의 구매활동, 이벤트 참여 등에 따라 “회사”가
              적립, 부여하는 무료 포인트와 “회원”이 유료로 구매하는 유료포인트로
              구분됩니다.
              <br />
              3. 무료포인트의 유효기간은 적립일로부터 1년이며, 유료 포인트는
              충전일로부터 5년이 경과하는 날까지 이용하지 않을 경우 상법상
              소멸시효에 따라 소멸됩니다. 단, “회사”는 무료포인트의 유효기간을
              변경할 수 있으며 이 경우 발급 시점에 “회원”에게 고지합니다.
              <br />
              4. “회사”가 무상으로 적립 또는 부여하는 무료포인트는 현금환급
              신청이 불가합니다.
              <br />
              5. “회사”는 “회원”이 유료포인트에 대한 환급을 요구할 경우,
              환급수수료를 공제하고 환급할 수 있으며, 환급조건 및 환급수수료에
              대한 구체적인 내용은 서비스 페이지를 통해 안내합니다.
              <br />
              6. “회원” 탈퇴 시 미 사용한 무료포인트는 소멸되며, “회사”는
              소멸되는 무료 포인트에 대해서 별도의 보상을 하지 않습니다.
              <br />
              7. “회사”는 “회원”이 포인트를 적립, 구매, 사용하는 경우
              휴대폰인증, I-PIN 등 “회사”가 정한 인증절차를 거치도록 할 수
              있습니다.
              <br />
              8. “회사”는 포인트 적립기준, 사용조건 및 제한 등에 관한 사항을
              서비스 화면에 별도로 게시하거나 통지합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 21 조 (할인쿠폰)</h4>
            <p>
              1. 할인쿠폰은 “회사”의 이벤트 프로모션 참여, “업주” 발급, “회사”의
              정책에 따른 “회원” 등급별 부여 등을 통하여 “회원”에게 지급되며,
              “할인쿠폰”별 유효기간, 할인금액 및 사용방법 등은 개별 안내사항을
              통하여 확인 가능합니다.
              <br />
              2. 할인쿠폰은 현금으로 환급될 수 없으며, 할인쿠폰에 표시된
              유효기간이 만료되거나 이용계약이 종료되면 소멸합니다.
              <br />
              3. “회사”는 “회원”이 부정한 목적과 방법으로 할인쿠폰 등을
              획득하거나 사용하는 사실이 확인될 경우, 해당 이용자에 대한
              할인쿠폰을 회수 또는 소멸시키거나 회원자격을 제한할 수 있습니다.
              <br />
              4. 할인쿠폰의 제공내용 및 운영방침은 “회사”의 정책에 따라 달라질
              수 있습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 22조 (티켓)</h4>
            <p>
              1. 티켓은 “회사” 에서 제공하는 “제휴사” 상품으로 구매 시 현장에서
              결제수단으로 사용할 수 있는 디지털 증권을 말합니다.
              <br />
              2. "회사"는 "회원"이 구매한 "티켓"의 상세 내용을 카카오톡 또는 LMS
              등으로 발송합니다.
              <br />
              3. “회원”이 구매한 “티켓”의 상세 내용은 서비스 내 예약 내역
              페이지에서 확인 가능합니다.
              <br />
              3. 티켓의 1일 사용수량은 “제휴사”가 정한 기준에 따라 상이합니다.
              <br />
              4. 티켓의 유효기간은 제휴사별로 상이하며, 유효기간 만료 시 연장은
              불가합니다. 유효기간 만료에 따른 환불 요청은 “회사”에 요청 시
              가능합니다.
              <br />
              5. 티켓은 개별 판매 조건에 명시된 유효기간 및 조건에 한하여
              서비스가 제공되며, 별도의 취소/환불 규정을 따릅니다. "회원"은
              "사이트"에 명시된 내용 및 개별 주의사항(사용 정보 등)을 확인하여야
              할 책임이 있습니다.
              <br />
              6. 필요 시 본인확인절차를 통해 구매자와 사용자의 일치 여부 등을
              확인 할 수 있습니다. 또한, "회원"이 이를 위반하여 "회사"의
              제휴업체 또는 제휴 업체가 지정한 장소에서 서비스를 제공받지 못하는
              등의 손해에 대하여 "회사"에게 이의를 제기할 수 없습니다.
              <br />
              7. "회원"의 티켓 구매 처리를 증명하기 위하여 발송된 카카오톡, LMS
              등을 소지하는 것만으로 본인 확인절차를 갈음할 수 없습니다.
              <br />
              8. "티켓"을 구매한 "회원"은 수신확인의 통지를 받은 날(카카오톡
              또는 LMS 통지 등을 받은 날)로부터 7일(청약 철회 기간) 이내에
              "회사"에서 제공하는 서비스를 통해 티켓 구매 청약을 철회할 수
              있습니다.
              <br />
              9. "회사"는 "회원"이 다음 각 호의 사유에 해당하는 경우 경고,
              일시정지, 영구이용정지 등으로 "서비스"의 이용을 제한할 수
              있습니다.
              <br />
              <div className="indent">
                1) "회원"이 "사이트"를 통하여 "티켓"을 구입한 후 이를 다시
                "회사"에 환불 처리를 요청한 후 이를 마치 환불하지 않은 듯한
                태도를 보이며 제휴업체에서 "상품 등"을 받는 행위 또는 "티켓"을
                사용하고도 마치 사용하지 않은 듯한 태도를 보이며 "회사"에 환불을
                요구하는 행위 등을 하는 경우
                <br />
                2) "회원"이 "티켓" 구매 시 결제 정보를 임의로 조작하여
                정상가격보다 못 미치는 금액을 결제한 경우
              </div>
            </p>

            <p className="clearfix"></p>

            <h4>제 23 조 (정보의 제공, 광고의 게재)</h4>
            <p>
              1. 회사는 광고를 포함하여 다양한 정보를 공지사항, e-mail,
              푸시알림(PNS) 등의 방법으로 회원에게 제공할 수 있으며 회원은
              수신을 거부할 수 있습니다. 다만, 회사는 서비스 이용에 필수적으로
              요구되는 정보(예:관련규정/정책의변경등)에 대해서는 회원의
              수신거부의사와 무관하게 이를 제공할 수 있습니다.
              <br />
              2. 회사는 본 서비스의 운용과 관련하여 어복황제 서비스 화면,
              푸시알림(PNS), 홈페이지 등에 광고를 게재할 수 있습니다.
              <br />
              3. 회원은 회사에서 제공하는 사이트 광고에 대한 임의의 삭제, 비방
              기타 사이트 광고 방해 행위 등을 할 수 없습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 24 조 (개인정보보호)</h4>
            <p>
              1. “회사”는 “회원”의 개인정보를 보호하기 위하여 정보통신망법 및
              개인정보 보호법 등 관계 법령에서 정하는 바를 준수합니다.
              <br />
              2. “회사”는 “회원”의 개인정보를 수집 및 이용하는 때에는 당해
              “회원”에게 그 목적을 고지하고 동의를 받습니다.
              <br />
              3. “회사”는 수집된 개인정보를 목적 외의 용도로 이용할 수 없으며,
              새로운 이용목적이 발생한 경우 또는 제3자에게 제공하는 경우에는
              이용 및 제공단계에서 당해 “회원”에게 그 목적을 고지하고 동의를
              받습니다. 다만, 관련 법령에 달리 정함이 있는 경우에는 예외로
              합니다.
              <br />
              4. “회사”는 “회원”의 개인정보를 보호하기 위한 개인정보취급방침을
              수립하여 서비스 초기화면에 게시합니다. 다만, 개인정보취급방침의
              구체적 내용은 연결화면을 통하여 볼 수 있습니다.
              <br />
              5. “회사”의 공식 사이트 이외의 링크된 사이트에서는 “회사”의
              개인정보취급방침이 적용되지 않습니다. 링크된 사이트 및 구매
              상품이나 서비스를 제공하는 제3자의 개인정보 취급과 관련하여는 해당
              사이트 및 해당 제3자의 개인정보취급방침을 확인할 책임이 “회원”에게
              있으며, “회사”는 이에 대하여 책임을 부담하지 않습니다.
              <br />
              6. “회사”는 제2항과 제3항에 의해 “회원”의 동의를 받아야 하는
              경우에는 개인정보보호책임자의 신원(소속, 성명 및 전화번호, 기타
              연락처), 정보의 수집목적 및 이용목적, 제3자에 대한 정보제공
              관련사항(제공받는 자, 제공목적 및 제공할 정보의 내용) 등
              정보통신망 이용촉진 및 정보보호 등에 관한 법률 제24조 제2항이
              규정한 사항을 미리 명시하거나 고지해야 하며 “회원”은 언제든지 이
              동의를 철회할 수 있습니다.
              <br />
              7. “회원”은 언제든지 “회사”가 가지고 있는 자신의 개인정보에 대해
              열람 및 오류정정을 요구할 수 있으며 회사는 이에 대해 지체 없이
              필요한 조치를 취할 의무를 집니다. “회원”이 오류의 정정을 요구한
              경우에는 그 오류를 정정할 때까지 당해 개인정보를 이용하지
              않습니다.
              <br />
              8. “회원”의 기존 개인정보와 이용자가 본인인증 시 인증한 개인정보가
              상이할 경우, “회사”는 이용자가 본인인증 시 인증한 개인정보로
              회원정보를 수정할 수 있습니다.
              <br />
              9. “회사”는 개인정보 보호를 위하여 관리자를 한정하여 그 수를
              최소화하며 신용카드, 은행계좌 등을 포함한 “회원”의 개인정보의
              분실, 도난, 유출, 변조 등으로 인한 “회원”의 손해에 대하여 모든
              책임을 집니다.
              <br />
              10. “회사” 또는 회사로부터 개인정보를 제공받은 제3자는 개인정보의
              수집목적 또는 제공받은 목적을 달성한 때에는 당해 개인정보를 지체
              없이 파기합니다. 단, 선불형지급수단의 이용과 관련한 개인정보는
              관련 회계처리가 모두 종료되는 시점에 파기합니다.
              <br />
              11. 이용계약이 종료된 경우, “회사”는 당해 “회원”의 정보를 파기하는
              것을 원칙으로 합니다. 다만, 예외 상황 발생 시 개인정보처리방침에
              준하여 회원 정보를 보관합니다. 이 경우 “회사”는 보관하고 있는
              “회원” 정보를 그 보관의 목적으로만 이용합니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 25 조 (결제수단)</h4>
            <p>
              “회사”에서 제공하는 서비스를 이용하는 것에 대한 결제방법은 다음 각
              호의 방법 중 가용한 방법으로 할 수 있습니다.
              <br />
              1. 온라인무통장입금
              <br />
              2. 폰뱅킹, 인터넷뱅킹, 메일뱅킹 등의 각종 계좌이체
              <br />
              3. 신용카드, 선불카드, 직불카드 등의 각종 카드 결제
              <br />
              4. 휴대폰 결제
              <br />
              5. 전자화폐에 의한 결제
              <br />
              6. 마일리지 등 “회사”가 지급한 포인트에 의한 결제
              <br />
              7. “회사”와 계약을 맺었거나 “회사”가 인정한 상품권에 의한 결제
              <br />
              8. 기타 전자적 지급 방법에 의한 대금 지급 등
            </p>
            <p className="clearfix"></p>

            <h4>제 26 조 (예약 및 결제)</h4>
            <p>
              1. “재화 등”에 대한 판매 계약은 “회원”이 “업주”가 제시한 상품의
              판매 조건에 응하여 청약의 의사표시를 하고 이에 대하여 “업주”가
              승낙의 의사표시를 함으로써 “회원”과 “업주”간에 체결됩니다.
              <br />
              2. “회원”은 다음 또는 이와 유사한 방법에 의한 구매를 신청할 수
              있습니다.
              <br />
              <div className="indent">
                (1) 상품예약서비스
                <br />
              </div>
              3. “회사”가 “업주” 등 제3자에게 이용자의 개인정보를 제공할 필요가
              있는 경우 ① 개인정보를 제공받는 자, ② 개인정보를 제공받는 자의
              개인정보 이용목적, ③ 제공하는 개인정보의 항목, ④ 개인정보를
              제공받는 자의 개인정보 보유 및 이용기간을 구매자에게 알리고 동의를
              받습니다.
              <br />
              4. “회사”가 제3자에게 구매자의 개인정보를 취급할 수 있도록 업무를
              위탁하는 경우에는 ① 개인정보 취급위탁을 받는 자, ② 개인정보
              취급위탁을 하는 업무의 내용을 구매자에게 알리고 동의를 받습니다.
              다만, 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」에서
              정하고 있는 방법으로 개인정보 취급방침을 통해 알림으로써
              동의절차를 갈음할 수 있습니다.
              <br />
              5. “재화 등”에 대한 전화예약(낚시배, 낚시좌대 등)은 “업주”의
              책임에 따라 제공되며, “회사”는 예약정보의 중계 이 외에 “재화 등”의
              전화예약(낚시배, 낚시좌대 등)에 대한 책임을 부담하지 않습니다.
              <br />
              6. “회원”이 “회사”에서 제공하는 상품예약서비스를 통해 직접 예약 및
              구매한 경우, “회사”는 예약내역 및 취소, 환불, 거래정보 등에 대해
              수집 및 관여하며, 개인정보취급방침에 준하여 수집된 정보 중 일부를
              “업주”에게 제공할 수 있습니다.
            </p>
            <p className="clearfix"></p>

            <h4>제 27 조 (환급)</h4>
            <p>
              1. “회사”는 “회원”이 구매한 상품이 천재지변, 품절, 기타 현장 상황
              등의 사유로 제공을 할 수 없을 때에는 지체 없이 그 사유를
              “회원”에게 통지하고 사전에 재화 등의 대금을 받은 경우에는
              이용일로부터 3일 이내(영업일 기준)에 환급하거나 환급에 필요한
              조치를 취합니다.{" "}
            </p>

            <h4>제 28 조 (취소, 변경, 환불 등)</h4>
            <p>
              1. 서비스 이용에 대한 취소 및 변경, 환불규정은 전자상거래 등에서의
              소비자보호에 관한 법령을 준수합니다.
              <br />
              2. “업주”는 별도의 취소 및 변경, 환불 규정을 제정할 수 있으며 이를
              상세페이지에 기재하고 “회원”의 동의를 받은 경우 우선 적용됩니다.
              <br />
              3. “회사”는 “업주”에게 전 2항의 규정이 없는 경우를 위하여 시설 별
              취소, 변경, 환불 규정을 제정할 수 있으며 이를 상세페이지에
              기재하고 “회원”의 동의를 받아 적용합니다.
              <br />
              4. “회사”의 예약서비스 취소, 환불 규정은 아래와 같습니다.
            </p>
            <div className="indent">
              (1) 업체 사정에 의해 취소 발생 시 100% 환불이 가능합니다.
              <br />
              (2) 예약 상품 별 예약정보에 기재된 취소, 환불 규정을 반드시 확인
              후 이용해주시기 바랍니다.
              <br />
              (3) 예약 이후의 취소는 취소/환불 규정에 의거하여 적용됩니다.
              <br />
              (4) 취소, 변경 불가 상품은 규정과 상관없이 취소, 변경이
              불가합니다.
              <br />
              (5) 당일 결제를 포함한 당일 취소는 취소, 변경이 불가합니다.
              <br />
              (6) 전월 휴대폰 결제 건은 단순 변심의 사유로 예약 취소할 경우,
              취소 규정 외에 환불 수수료 5%가 발생합니다.
              <br />
              (7) 예약 취소가 불가능한 시간에 고객 사정에 의한 취소 시,
              어복황제가 제공하는 모든 혜택에서 제외될 수 있으며(할인 쿠폰
              미제공, 이벤트 대상자 제외, 혜택받기 포인트 미지급), 본 예약 시
              사용한 쿠폰은 소멸됩니다.
              <br />
              (8) “업체”의 상세 정보가 수시로 변경될 수 있으며 이로 인한
              불이익은 어복황제가 책임지지 않습니다.
              <br />
              (9) “회원”의 단순 변심에 의한 취소 및 환불일 경우 이의 처리에
              발생하는 수수료는 “회원”이 부담합니다.
              <br />
              (10) 구매 취소 시점과 해당 카드사의 환불 처리기준에 따라
              취소금액의 환급 방법과 환급일은 다소 차이가 있을 수 있으며, 사용한
              신용카드의 환불 정책은 신용카드회사에 직접 문의하여야 합니다.
              <br />
              (11) 개별 "상품 등"의 성격에 따라 "회사"는 별도 계약 및 이용조건에
              따른 구매취소 및 청약철회 관련 규정을 정할 수 있으며 이 경우 별도
              계약 및 이용조건상의 구매취소 및 청약철회 규정이 우선 적용됩니다.
              <br />
              (12) 티켓 환불 기간은 취소 요청일로부터 3일 이내(영업일 기준)
              이내에 처리하며, 주말 및 휴일일 경우 다음 영업일에 환불
              처리합니다.
              <br />
              (13) "회원"이 타인의 신용카드 또는 휴대전화 번호를 도용하는 등 본
              약관에서 금지하는 부정한 방법으로 부당한 이익을 편취하였다고
              의심되는 경우 "회사"는 "회원"의 티켓 구매를 취소처리 하거나 티켓의
              사용을 제한할 수 있으며. "회원"이 충분한 소명 자료를 제출할 때까지
              환불을 보류할 수 있습니다.
              <br />
              (14) 취소/환불 규정 상세
            </div>
            <p className="clearfix"></p>

            <table className="table table-bordered">
              <colgroup>
                <col style={{ width: "10%" }} />
                <col style={{ width: "10%" }} />
                <col style={{ width: "80%" }} />
              </colgroup>
              <tbody>
                <tr>
                  <th rowSpan="4">고객 변심에 의한 환불</th>
                  <th rowSpan="3">일반결제상품</th>
                  <td>1) 이용일 기준 7일~3일 전 : 100% 환불</td>
                </tr>
                <tr>
                  <td>
                    2) 이용일 기준 2일 전 : 50% 환불(고객센터 운영시간 내)
                  </td>
                </tr>
                <tr>
                  <td>3) 이용일 기준 1일 전~당일, No-show : 환불불가</td>
                </tr>
                <tr>
                  <th>티켓결제상품</th>
                  <td>
                    1) 구매일 기준 7일 이내 : 100% 환불. 단, 7일 이내 부분 취소
                    환불 및 7일 이후 환불 처리는 회사에 요청
                  </td>
                </tr>
                <tr>
                  <th rowSpan="3" colSpan="2">
                    천재지변에 의한 환불 - 해상
                  </th>
                  <td>1) 적용업체 - 선상, 갯바위, 좌대낚시, 해상콘도</td>
                </tr>
                <tr>
                  <td>
                    2) 해당 지역에 풍랑, 폭풍, 태풍주의보 3종류의 주의보
                    발효시(기상청 발표기준) 100% 환불
                  </td>
                </tr>
                <tr>
                  <td>3) 이용 시작 이후에는 환불 조항이 적용되지 않는다.</td>
                </tr>
                <tr>
                  <th rowSpan="3">기타</th>
                  <th rowSpan="3">일반·티켓 결제상품</th>
                  <td>
                    1) 업체의 사정에 의해 환불이 필요할 경우 “회사”에게
                    요청한다.
                  </td>
                </tr>
                <tr>
                  <td>
                    2) 일부상황에서 부분 취소가 불가한 경우, 당사의 환불 규정에
                    의거하여 적용된다.
                    <br />※ 부분 취소 불가 케이스
                    <br />- 할부 결제 이용 시, 카드사 포인트 결제, 세이브 서비스
                    이용, 기타 카드 (기프트, 포인트, 선불, 법인, 해외),
                    <br /> 최초 결제일로부터 3개월 경과 시{" "}
                  </td>
                </tr>
              </tbody>
            </table>

            <p>
              <strong>고객 변심에 의한 환불 일반결제상품</strong>
            </p>
            <p>
              1) 이용일 기준 7일~3일 전 : 100% 환불
              <br />
              2) 이용일 기준 2일 전 : 50% 환불(고객센터 운영시간 내)
              <br />
              3) 이용일 기준 1일 전~당일, No-show : 환불불가
              <br />
              티켓결제상품 1) 구매일 기준 7일 이내 : 100% 환불. 단, 7일 이내
              부분 취소 환불 및 7일 이후 환불 처리는 회사에 요청
            </p>
            <p className="clearfix"></p>

            <p>
              <strong>천재지변에 의한 환불 - 해상</strong>
            </p>
            <p>
              1) 적용업체 - 선상, 갯바위, 좌대낚시, 해상콘도
              <br />
              2) 해당 지역에 풍랑, 폭풍, 태풍주의보 3종류의 주의보 발효시(기상청
              발표기준) 100% 환불
              <br />
              3) 이용 시작 이후에는 환불 조항이 적용되지 않는다.
            </p>
            <p className="clearfix"></p>

            <p>
              <strong>기타 일반·티켓 결제상품</strong>{" "}
            </p>
            <p>
              1) 업체의 사정에 의해 환불이 필요할 경우 “회사”에게 요청한다.
              <br />
              2) 일부상황에서 부분 취소가 불가한 경우, 당사의 환불 규정에
              의거하여 적용된다.
            </p>
            <p className="clearfix"></p>

            <p>
              <strong>※ 부분 취소 불가 케이스</strong>
              <br />- 할부 결제 이용 시, 카드사 포인트 결제, 세이브 서비스 이용,
              기타 카드 (기프트, 포인트, 선불, 법인, 해외), 최초 결제일로부터
              3개월 경과 시
            </p>
            <p className="clearfix"></p>
          </div>

          <h4>제 29 조 (책임제한)</h4>
          <p>
            1. “회사”는 “업주”와 “회원” 간의 상품거래를 중개하는 플랫폼
            서비스만을 제공할 뿐, “재화 등”을 판매하는 당사자가 아니며, “재화
            등”에 대한 정보 등에 대한 책임은 “업주”에게 있습니다.
            <br />
            2. “회사”는 통신판매중개자로서 상품의 매매와 관련하여 발생하는 사용,
            사용 후 취소 등의 거래 진행은 거래 당사자인 “업주”와 구매자 간에
            수행되어야 하며, “회사”는 거래 진행에 관여하지 않습니다.
            <br />
            3. “회사”는 “업주”가 게재한 정보, 자료, 사실의 신뢰도, 정확성,
            타당성, 적시성 등 내용에 관해서는 어떠한 보증이나 대리를 하지
            않습니다. 따라서 구매자는 상품을 구매 시 스스로 책임을 지게 됩니다.
            <br />
            4. “회사”는 천재지변 또는 이에 준하는 불가항력으로 인하여 “서비스”를
            제공할 수 없는 경우에는 서비스 제공에 관한 책임이 면제됩니다.
            <br />
            5. “회사”는 “회원”의 귀책사유로 인한 “서비스” 이용의 장애에 대하여는
            책임을 지지 않습니다.
            <br />
            6. “회사”는 “회원” 및 “업주”가 게재한 이용후기, 사진 등
            정보/자료/사실의 신뢰도, 정성에 대해서는 책임을 지지 않습니다.
            <br />
            7. “회사”는 제3자가 서비스 내 화면 또는 링크된 웹사이트를 통하여
            광고한 제품 또는 서비스의 내용과 품질에 대하여 감시할 의무 기타
            어떠한 책임도 지지 아니합니다.
            <br />
            8. “회사”는 “회원”이 서비스를 이용하여 기대하는 수익을 상실한 것에
            대하여 책임을 지지 않으며, 그 밖의 서비스를 통하여 얻은 자료로 인한
            손해에 관하여 책임을 지지 않습니다.
            <br />
            9. “회사”는 “회원”간 또는 “회원”과 제3자 상호간에 서비스를 매개로
            하여 거래 등을 한 경우에는 책임이 면제됩니다.
            <br />
            10. “회사”는 구매자 스스로 자신의 개인정보를 타인에게 유출하거나
            제공하여 발생하는 피해에 대하여 일절 책임을 지지 않습니다.
            <br />
            11. “회사” 및 “회사”의 임직원 그리고 대리인은 고의 또는 중대한
            과실이 없는 한 다음과 같은 사항으로부터 발생하는 손해에 대해 책임을
            지지 아니합니다.
            <br />
            <div className="indent">
              1) 회원 정보의 허위 또는 부정확성에 기인하는 손해
              <br />
              2) 서비스에 대한 접속 및 서비스의 이용과정에서 “회원”의 귀책사유로
              발생하는 손해
              <br />
              3) 서버에 대한 제3자의 모든 불법적인 접속 또는 서버의 불법적인
              이용으로부터 발생하는 손해 및 제3자의 불법적인 행위를 방지하거나
              예방하는 과정에서 발생하는 손해
              <br />
              4) 제3자가 서비스를 이용하여 불법적으로 전송, 유포하거나 또는
              전송, 유포되도록 한 모든 바이러스, 스파이웨어 및 기타 악성
              프로그램으로 인한 손해
            </div>
          </p>
          <p className="clearfix"></p>

          <h4>제 30 조 (분쟁해결)</h4>
          <p>
            1. “회사”는 이용자가 제기하는 정당한 의견이나 불만을 반영하고 그
            피해를 보상처리하기 위하여 고객상담 및 피해보상처리기구를
            설치·운영합니다.
            <br />
            2. “회사”는 이용자로부터 제출되는 불만사항 및 의견은 우선적으로 그
            사항을 처리합니다. 다만, 신속한 처리가 곤란한 경우에는 이용자에게 그
            사유와 처리일정을 통보해 드립니다.
            <br />
            3. “회사”와 이용자 간에 발생한 전자상거래 분쟁과 관련하여 이용자의
            피해구제신청이 있는 경우에는 공정거래위원회 또는 시·도지사가
            의뢰하는 분쟁조정기관의 조정에 따를 수 있습니다.
          </p>
          <p className="clearfix"></p>

          <h4>제 31 조 (준거법 및 관할법원)</h4>
          <p>
            1. 이 약관의 해석 및 회사와 회원간의 분쟁에 대하여는 대한민국의 법을
            적용합니다.
            <br />
            2. 서비스 이용 중 발생한 회원과 회사간의 소송은 민사소송법에 의한
            관할법원에 제소합니다.
            <br />
            3. 제소 당시 이용자의 주소 또는 거소가 분명하지 않거나 외국 거주자의
            경우에는 민사소송법상의 관할법원에 제기합니다.
            <br />
            4. 회사는 이용자간에 제기된 전자상거래 소송에는 한국 법을
            적용합니다.
          </p>
          <p className="clearfix"></p>

          <h4>&lt;부칙&gt;</h4>
          <p>1. 이 약관은 0000년 00월 00일부터 시행됩니다.</p>

          <p className="clearfix">
            <br />
          </p>
          <p className="clearfix">
            <br />
          </p>
        </div>
      </>
    );
  })
);
