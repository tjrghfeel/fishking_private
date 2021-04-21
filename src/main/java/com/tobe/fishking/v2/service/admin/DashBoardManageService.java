package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.DashBoardManageDto;
import com.tobe.fishking.v2.model.fishing.OrderResponse;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.repository.fishking.OrdersRepository;
import com.tobe.fishking.v2.repository.fishking.RideShipRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashBoardManageService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository;
    private final RideShipRepository rideShipRepository;
    private final OrdersRepository ordersRepository;
    private final PostRepository postRepository;

    @Transactional
    public DashBoardManageDto getDashBoard(String token) throws ServiceLogicException {
        DashBoardManageDto result = new DashBoardManageDto( );
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다.");}

        //회원현황
        Pageable pageable1 = PageRequest.of(0,1);
        Map<String, Long> memberCount= memberRepository.getDashBoardMember(pageable1).get(0);
        long totalCount = memberCount.get("member") + memberCount.get("shipowner") + memberCount.get("police");
        DashBoardManageDto.MemberData memberData = result.new MemberData(totalCount, memberCount.get("member"), memberCount.get("shipowner"), memberCount.get("police")  );
        result.setMemberData(memberData);

        //상품현황
        Pageable pageable2 = PageRequest.of(0,1);
        Map<String, Long> goodsCount = goodsRepository.getDashBoardGoods(pageable2).get(0);
        DashBoardManageDto.GoodsData goodsData = result.new GoodsData(goodsCount.get("ship"), goodsCount.get("searock"));
        result.setGoodsData(goodsData);

        //예약현황
        Pageable pageable3 = PageRequest.of(0,1);
        Map<String, Long> ordersCount = ordersRepository.countByOrderStatus(pageable3).get(0);
        DashBoardManageDto.OrderData orderData = result.new OrderData(
                ordersCount.get("book"),ordersCount.get("bookRunning"),ordersCount.get("waitBook"),
                ordersCount.get("bookFix"),ordersCount.get("bookCancel"), ordersCount.get("fishingComplete"),ordersCount.get("bookConfirm"));
        result.setOrderData(orderData);

        //승선

        //공지사항
        Pageable pageable4 = PageRequest.of(0,5);
        List<Post> noticeData = postRepository.getDashBoardPost(FilePublish.notice, pageable4);
        List<DashBoardManageDto.NoticeData> noticeData1 = new ArrayList<>();
        for(Post post : noticeData){
            DashBoardManageDto.NoticeData notice = result.new NoticeData(post);
            noticeData1.add(notice);
        }
        result.setNoticeData(noticeData1);

        //1:1
        Pageable pageable5 = PageRequest.of(0,5);
        List<Post> oneToOneList = postRepository.getDashBoardPost(FilePublish.one2one, pageable5);
        List<DashBoardManageDto.OneToOneData> oneToOneData = new ArrayList<>();
        for(Post post : oneToOneList){
            DashBoardManageDto.OneToOneData oneToOne = result.new OneToOneData(post);
            oneToOneData.add(oneToOne);
        }
        result.setOneToOneData(oneToOneData);

        return result;
    }
}
