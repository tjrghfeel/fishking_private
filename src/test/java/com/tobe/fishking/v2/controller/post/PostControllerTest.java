package com.tobe.fishking.v2.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
/*import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;*/
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.model.auth.PhoneAuthDto;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.common.TidalLevelRepository;
import com.tobe.fishking.v2.repository.fishking.*;
//import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.FishkingScheduler;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.common.CouponService;
import com.tobe.fishking.v2.service.fishking.FishingDiaryService;
import com.tobe.fishking.v2.service.fishking.MyMenuService;
import org.apache.commons.io.FilenameUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Autowired
    TidalLevelRepository tidalLevelRepository;
    @Autowired
    MyMenuService myMenuService;
    @Autowired
    FishkingScheduler scheduler;
    @Autowired
    CouponMemberRepository couponMemberRepository;

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
    public void messaging() {
        String registrationToken = "f7BmI3-9SzeYbMHRDDYKIF:APA91bGo4z5dC3pfr8H6PT0qUxP0qIq1nBqLeGoRC3Y7IwDvUcWgwCsOnidXHWqKYZ8FG3XlIT0fxKhhqh2w07dbmZ7zrj0xdPS5tI_yFUe3y7MUT5vva5b4Z6EyrWPy3PDviPKhfrLE";
        String url = "https://fcm.googleapis.com/fcm/send";
        Map<String,String> parameter = new HashMap<>();
        parameter.put("json",
                "{ \"notification\": " +
                        "{" +
                        "\"title\": \"[?????????]\", " +
                        "\"body\": \"?????????\", " +
                        "\"android_channel_id\": \"notification.native_smartfishing\"" +
                        "}," +
                        "\"to\" : \""+registrationToken+"\"" +
                        "}");
        try {
            memberService.sendRequest(url, "JSON", parameter,"key=AAAAlI9VsDY:APA91bGtlb8VOtuRGVFU4jmWrgdDnNN3-qfKBm-5sz2LZ0MqsSvsDBzqHrLPapE2IALudZvlyB-f94xRCrp7vbGcQURaZon368Uey9HQ4_CtTOQQSEa089H_AbmWNVfToR42qA8JGje5");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void noName() throws Exception {
        String aaa = AES.aesDecode("H9Q8zROABmLt2zH9SphnHQ==",env.getProperty("encrypKey.key"));
        Pageable pageable = PageRequest.of(0,100);
        shipRepository.getShipList(
                null, null, null, null, null, false,new Long[0],
                false,new Long[0], false,new Long[0], false,new Long[0],
                null, null, null, null, null,pageable
        );


        System.out.println("result >>> " +aaa);
        return;

    }

    @Test
    public void array() {
        String now = "2021-04-23";

        List<Orders> confirm = ordersRepository.getOrderByStatus(now, OrderStatus.bookConfirm);
        List<Orders> wait = ordersRepository.getOrderByStatus(now, OrderStatus.waitBook);
        List<Orders> running = ordersRepository.getOrderByStatus(now, OrderStatus.bookRunning);

        List<Orders> copied = new ArrayList<>(confirm);
        copied.addAll(wait);
        copied.addAll(running);
        List<Goods> goodsList = copied.stream().map(Orders::getGoods).distinct().collect(Collectors.toList());
        int[] confirmCounts = new int[goodsList.size()];
//        int[] cancelCounts = new int[goodsList.size()];

        for (Orders o : confirm) {
            int goodsIdx = goodsList.indexOf(o.getGoods());
            confirmCounts[goodsIdx] += 1;
        }
        for (int a : confirmCounts) {
            System.out.println(a);
        }
    }

    @Test
    public void timeParse() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
//        System.out.println(time.substring(0,2) + ":" + time.substring(2,4));
        List<Orders> orders = ordersRepository.getOrderByStatusForScheduler("2021-04-26", "23", OrderStatus.waitBook);
        System.out.println(orders);
    }

    @Test
    public void removeTest() {
        List<Map<String, Object>> l = new ArrayList<>();
        for (int idx=0; idx < 4; idx++) {
            Map<String, Object> m = new HashMap<>();
            m.put("name", idx);
            m.put("list", new ArrayList<Integer>());
            l.add(m);
        }
        System.out.println(l);

        for (Map<String, Object> mm : l) {
            System.out.println(mm.get("name"));
            ((List<Integer>) mm.get("list")).add(5 + (int) mm.get("name"));
        }

        System.out.println(l);
    }

    @Transactional
    public void updateMember() throws ResourceNotFoundException {
        Member member = memberRepository.findById(20L)
                .orElseThrow(()->new ResourceNotFoundException(""));
        System.out.println("result >>> "+member.getMemberName());
        member.setMemberName("?????????");
        memberRepository.save(member);

        return;
    }


}