package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.utils.KSPayWebHostBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {

    public void payResult(String rcid, String rctype, String rhash, String rcancel) {
        /* rcid 없으면 결제를 끝까지 진행하지 않고 중간에 결제취소 */
        String	authyn =  "";
        String	trno   =  "";
        String	trddt  =  "";
        String	trdtm  =  "";
        String	amt    =  "";
        String	authno =  "";
        String	msg1   =  "";
        String	msg2   =  "";
        String	ordno  =  "";
        String	isscd  =  "";
        String	aqucd  =  "";
        String	result =  "";

        String	resultcd =  "";

        if(rcancel.equals("0")) {
            KSPayWebHostBean ipg = new KSPayWebHostBean(rcid);
            if (ipg.kspay_send_msg("1")) {                  //KSNET 결제결과 중 아래에 나타나지 않은 항목이 필요한 경우 Null 대신 필요한 항목명을 설정할 수 있습니다.
                authyn = ipg.kspay_get_value("authyn");     // 승인구분 (O X)
                trno	 = ipg.kspay_get_value("trno"  );   // 거래번호
                trddt	 = ipg.kspay_get_value("trddt" );   // 거래일자 YYYYMMDD
                trdtm	 = ipg.kspay_get_value("trdtm" );   // 거래시간 HHMMTT
                amt		 = ipg.kspay_get_value("amt"   );   // 금액
                authno = ipg.kspay_get_value("authno");     // 카드사 승인번호
                msg1	 = ipg.kspay_get_value("msg1"  );   // 메제시1 넘긴값 그대로 리턴
                msg2	 = ipg.kspay_get_value("msg2"  );   // 메세지2 승인성공 시 OK와 승인번호
                ordno	 = ipg.kspay_get_value("ordno" );   // 주문번호 넘긴값 그대로 리턴
                isscd	 = ipg.kspay_get_value("isscd" );   // 발급사코드/가상계좌번호/계좌이체번호
                aqucd	 = ipg.kspay_get_value("aqucd" );   // 매입사코드
                result = ipg.kspay_get_value("result");     // 결제수단

                if (null != authyn && 1 == authyn.length()) {
                    if (authyn.equals("O")) {
                        resultcd = "0000";
                    } else {
                        resultcd = authno.trim();
                    }
                }
                System.out.println(rcid);
                System.out.println(rctype);
                System.out.println(resultcd);
                System.out.println(authyn);
                System.out.println(trno);
                System.out.println(trddt);
                System.out.println(trdtm);
                System.out.println(amt);
                System.out.println(authno);
                System.out.println(msg1);
                System.out.println(msg2);
                System.out.println(ordno);
                System.out.println(isscd);
                System.out.println(aqucd);
                System.out.println(result);
            }
        } else {
            authyn="X";
            msg1 = "취소";
        }
    }
}
