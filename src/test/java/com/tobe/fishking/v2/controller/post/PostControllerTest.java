package com.tobe.fishking.v2.controller.post;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    public void fileTest() throws Exception {

  /*      FileEntity file =  fileRepo.findByPidAndFilePublish(FilePublish.post, 4 );

        assertThat("hello",is("hello"));
*/
    }

    @Test
    public void noName() throws Exception {
        String randomNum=null;
        randomNum = Integer.toString((int)(Math.random()*10000));
        randomNum = String.format("%04d",((int)(Math.random()*10000)));



        System.out.println("=====================================");
        System.out.println("=====================================");
        System.out.println("####result ) "+randomNum);
        System.out.println("=====================================");
        System.out.println("=====================================");
    }


}