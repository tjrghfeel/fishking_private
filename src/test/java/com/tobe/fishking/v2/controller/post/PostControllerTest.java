package com.tobe.fishking.v2.controller.post;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.common.PhoneNumberInfo;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.MemberManageDtoForPage;
import com.tobe.fishking.v2.model.board.FAQDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
//import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.board.PostService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {


    @Autowired
    FileRepository fileRepo;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostService postService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MockMvc mvc;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    LoveToRepository loveToRepository;
    @Autowired
    FishingDiaryRepository fishingDiaryRepository;
    @Autowired
    FishingDiaryCommentRepository fishingDiaryCommentRepository;
    @Autowired
    AlertsRepository alertsRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    TakeRepository takeRepository;
    @Autowired
    Environment env;
    @Autowired
    PasswordEncoder encoder;

    @Test
    public void fileTest() throws Exception {

        return;
    }

    @Test
    public void noName() throws Exception {


        /*member.setMemberName("김예린");
        member.setNickName("예");
        member.setEmail("ssss123@naver.com");
        String encodedPw = encoder.encode("1234");
        member.setPassword(encodedPw);
        member.setProfileImage(env.getProperty("file.downloadUrl")+ "/profile/noProfileImage.jpg");
        PhoneNumber phoneNumber = new PhoneNumber("123","5555");
        member.setPhoneNumber(phoneNumber);
        Address address = new Address("서울시","하남시","하남동");
        member.setAddress(address);

        memberRepository.save(member);*/

        return;
    }
    @Transactional
    public void updateMember() throws ResourceNotFoundException {


        return;
    }


}