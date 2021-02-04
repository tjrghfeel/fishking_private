package com.tobe.fishking.v2.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import com.tobe.fishking.v2.entity.fishing.TblSubmitQueue;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.model.auth.PhoneAuthDto;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
//import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.common.CouponService;
import com.tobe.fishking.v2.service.fishking.FishingDiaryService;
import org.apache.commons.io.FilenameUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.in;
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
    @Autowired
    CouponService couponService;
    @Autowired
    PhoneAuthRepository phoneAuthRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    TblSubmitQueueRepository tblSubmitQueueRepository;
    @Autowired
    FishingDiaryService fishingDiaryService;

    @Test
    public void fileTest() throws Exception {
        URL url = new URL("https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=xQF6XDWPhMC665JO2kSq&state=STATE_STRING&redirect_uri=CALLBACK_URL");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        Map<String,String> arguments = new HashMap<>();
        arguments.put("username", "root");
        arguments.put("pw", "sjh76HSn!"); // This is a fake password obviously
        StringJoiner sj = new StringJoiner("&");
        for(Map.Entry<String,String> entry : arguments.entrySet())
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        // Do something with http.getInputStream()
        return;
    }

    @Test
    public void noName() throws Exception {
        System.out.println("result1 >>> "+AES.aesDecode("이서준",env.getProperty("encrypKey.key")));
        System.out.println("result1 >>> "+AES.aesEncode("이서준",env.getProperty("encrypKey.key")));
    }

    @Transactional
    public void updateMember() throws ResourceNotFoundException {


        return;
    }


}