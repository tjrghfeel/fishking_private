package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.addon.KSPayApprovalCancelBean;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.ReserveType;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.utils.KSPayWebHostBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PayService {

    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final GoodsFishingDateRepository goodsFishingDateRepository;
    private final CalculateRepository calculateRepository;
    private final ShipRepository shipRepository;

    public Long payResult(String rcid, String rctype, String rhash, String rcancel) {
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

                        Orders order = ordersRepository.getOrdersByOrderNumber(ordno);
                        OrderDetails orderDetails = orderDetailsRepository.findByOrders(order);
                        Member member = order.getCreatedBy();
                        Goods goods = orderDetails.getGoods();
                        GoodsFishingDate goodsFishingDate = goodsFishingDateRepository.findByGoodsIdAndDateString(goods.getId(), order.getFishingDate());
                        order.paid(member, trno);
                        if (goods.getReserveType() == ReserveType.auto) {
                            if (orderDetails.getPersonnel() <= (goods.getMaxPersonnel() - goodsFishingDate.getReservedNumber())) {
                                order.changeStatus(OrderStatus.bookConfirm);
                                goodsFishingDate.addReservedNumber(orderDetails.getPersonnel());
                            } else {
                                order.changeStatus(OrderStatus.waitBook);
                                goodsFishingDate.addWaitNumber(orderDetails.getPersonnel());
                            }
                        } else {
                            order.changeStatus(OrderStatus.bookRunning);
                        }
                        ordersRepository.save(order);
                        goodsFishingDateRepository.save(goodsFishingDate);
                        LocalDate date = LocalDate.now();
                        String year = String.valueOf(date.getYear());
                        String month = String.valueOf(date.getMonthValue()).length() != 2
                                ? "0" + String.valueOf(date.getMonthValue())
                                : String.valueOf(date.getMonthValue());
                        Calculate calculate = Calculate.builder()
                                .orders(order)
                                .year(year)
                                .month(month)
                                .ship(goods.getShip())
                                .isCancel(false)
                                .build();
                        calculateRepository.save(calculate);
                        Ship ship = goods.getShip();
                        ship.addSell(member);
                        shipRepository.save(ship);
                        return order.getId();
//                        return 0L;
                    } else {
                        resultcd = authno.trim();
                        return -1L;
                    }
                }
            }
        } else {
            authyn="X";
            msg1 = "취소";
            return -1L;
        }
        return -1L;
    }

    @Transactional
    public String cancelOrder(Long orderId, String token) {
        String EncType = "0";      // 0: 암화안함, 1:openssl, 2: seed
        String Version = "0210";   // 전문버전
        String Type = "00";     // 구분
        String Resend = "0";      // 전송구분 : 0 : 처음,  2: 재전송
        String RequestDate = new SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date()); // 요청일자 : yyyymmddhhmmss
        String KeyInType = "K";      // KeyInType 여부 : S : Swap, K: KeyInType
        String LineType = "1";      // lineType 0 : offline, 1:internet, 2:Mobile
        String ApprovalCount = "1";      // 복합승인갯수
        String GoodType = "0";      // 제품구분 0 : 실물, 1 : 디지털
        String HeadFiller = "";       // 예비

        String StoreId = "2040700001";   // *상점아이디
        String OrderNumber = "";                            // 주문번호
        String UserName = "";                                // *주문자명
        String IdNum = "";                                // 주민번호 or 사업자번호
        String Email = "";                                // *email
        String GoodName = "";                                // *제품명
        String PhoneNo = "";                                // *휴대폰번호

        String ApprovalType = "1010";   // 승인구분
        String TrNo = "trno";     // 거래번호

        // Server로 부터 응답이 없을시 자체응답
        String rApprovalType = "1011";
        String rTransactionNo = "";                // 거래번호
        String rStatus = "X";               // 상태 O : 승인, X : 거절
        String rTradeDate = "";                // 거래일자
        String rTradeTime = "";                // 거래시간
        String rIssCode = "00";              // 발급사코드
        String rAquCode = "00";              // 매입사코드
        String rAuthNo = "9999";            // 승인번호 or 거절시 오류코드
        String rMessage1 = "취소거절";        // 메시지1
        String rMessage2 = "C잠시후재시도";   // 메시지2
        String rCardNo = "";                // 카드번호
        String rExpDate = "";                // 유효기간
        String rInstallment = "";                // 할부
        String rAmount = "";                // 금액
        String rMerchantNo = "";                // 가맹점번호
        String rAuthSendType = "N";               // 전송구분
        String rApprovalSendType = "N";               // 전송구분(0 : 거절, 1 : 승인, 2: 원카드)
        String rPoint1 = "000000000000";    // Point1
        String rPoint2 = "000000000000";    // Point2
        String rPoint3 = "000000000000";    // Point3
        String rPoint4 = "000000000000";    // Point4
        String rVanTransactionNo = "";
        String rFiller = "";                // 예비
        String rAuthType = "";                // ISP : ISP거래, MP1, MP2 : MPI거래, SPACE : 일반거래
        String rMPIPositionType = "";                // K : KSNET, R : Remote, C : 제3기관, SPACE : 일반거래
        String rMPIReUseType = "";                // Y : 재사용, N : 재사용아님
        String rEncData = "";                // MPI, ISP 데이터

        Orders orders = ordersRepository.getOne(orderId);
        Member member = orders.getCreatedBy();

        OrderNumber = orders.getOrderNumber();
        UserName = member.getMemberName();
        Email = member.getEmail();
        PhoneNo = member.getPhoneNumber().getFullNumber();
        GoodName = orders.getGoods().getName();
        TrNo = orders.getTradeNumber();

        try {
            KSPayApprovalCancelBean ipg = new KSPayApprovalCancelBean("175.126.62.209", 29991);

            ipg.HeadMessage(EncType, Version, Type, Resend, RequestDate, StoreId, OrderNumber, UserName, IdNum, Email,
                    GoodType, GoodName, KeyInType, LineType, PhoneNo, ApprovalCount, HeadFiller);

            ipg.CancelDataMessage(ApprovalType, "0", TrNo, "", "", "", "", "");

            if (ipg.SendSocket("1")) {
                rApprovalType = ipg.ApprovalType[0];
                rTransactionNo = ipg.TransactionNo[0];        // 거래번호
                rStatus = ipg.Status[0];               // 상태 O : 승인, X : 거절
                rTradeDate = ipg.TradeDate[0];            // 거래일자
                rTradeTime = ipg.TradeTime[0];            // 거래시간
                rIssCode = ipg.IssCode[0];              // 발급사코드
                rAquCode = ipg.AquCode[0];              // 매입사코드
                rAuthNo = ipg.AuthNo[0];               // 승인번호 or 거절시 오류코드
                rMessage1 = ipg.Message1[0];             // 메시지1
                rMessage2 = ipg.Message2[0];             // 메시지2
                rCardNo = ipg.CardNo[0];               // 카드번호
                rExpDate = ipg.ExpDate[0];              // 유효기간
                rInstallment = ipg.Installment[0];          // 할부
                rAmount = ipg.Amount[0];               // 금액
                rMerchantNo = ipg.MerchantNo[0];           // 가맹점번호
                rAuthSendType = ipg.AuthSendType[0];         // 전송구분= new String(this.read(2));
                rApprovalSendType = ipg.ApprovalSendType[0];     // 전송구분(0 : 거절, 1 : 승인, 2: 원카드)
                rPoint1 = ipg.Point1[0];               // Point1
                rPoint2 = ipg.Point2[0];               // Point2
                rPoint3 = ipg.Point3[0];               // Point3
                rPoint4 = ipg.Point4[0];               // Point4
                rVanTransactionNo = ipg.VanTransactionNo[0];     // Van거래번호
                rFiller = ipg.Filler[0];               // 예비
                rAuthType = ipg.AuthType[0];             // ISP : ISP거래, MP1, MP2 : MPI거래, SPACE : 일반거래
                rMPIPositionType = ipg.MPIPositionType[0];      // K : KSNET, R : Remote, C : 제3기관, SPACE : 일반거래
                rMPIReUseType = ipg.MPIReUseType[0];         // Y : 재사용, N : 재사용아님
                rEncData = ipg.EncData[0];              // MPI, ISP 데이터

                orders.cancelled(member, rAuthNo);
                ordersRepository.save(orders);

                LocalDate date = LocalDate.now();
                String year = String.valueOf(date.getYear());
                String month = String.valueOf(date.getMonthValue()).length() != 2
                        ? "0" + String.valueOf(date.getMonthValue())
                        : String.valueOf(date.getMonthValue());

                Calculate calculate = Calculate.builder()
                        .orders(orders)
                        .year(year)
                        .month(month)
                        .ship(orders.getGoods().getShip())
                        .isCancel(true)
                        .build();
                calculateRepository.save(calculate);
            }
        } catch (Exception e) {
            rMessage2 = "P잠시후재시도(" + e.toString() + ")";    // 메시지2
        } // end of catch

        return rMessage1 + rMessage2;
    }
}
