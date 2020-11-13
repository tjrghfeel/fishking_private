package com.tobe.fishking.v2.controller.post;

import com.google.gson.Gson;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.BoardType;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.PostDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.service.post.PostService;
import jdk.internal.org.jline.utils.Log;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

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

    @Test
    public void noName() throws Exception {
        /*Member member = Member.builder()
                .uid("zfasdf")
                .memberName("kai")
                .password("pwKai")
                .email("emailKai")
                .gender(Gender.boy)
                .roles(Role.MEMBER)
                .profileImage("kaiProfileImage")
                .isActive(true)
                .certifiedNo("kaiCertifiedNo")
                .isCertified(true)
                .joinDt("20201105")
                .snsType(SNSType.facebook)
                .snsId("kaiSnsId")
                .statusMessage("kaiStatusMessage")
                .build();

        memberRepository.save(member);*/

        Member member = memberRepository.findById(8L)
                .orElseThrow(()->new ResourceNotFoundException("aaa"));

        Board board = Board.builder()
                .boardType(BoardType.faq)
                .name("FAQ")
                .url("FAQUrl")
                .uploadPath("faq")
                .downloadPath("faq")
                .filePublish(FilePublish.faq)
                .createdBy(member)
                .modifiedBy(member)
                .secret("F")
                .build();
        boardRepository.save(board);

        /*Member member = memberRepository.findById(5L)
                .orElseThrow(()->new ResourceNotFoundException("aaa"));

        Board board = boardRepository.findById(3L)
                .orElseThrow(()->new ResourceNotFoundException("aaa"));

        Post post = Post.builder()
                .board(board)
                .channelType(ChannelType.channel3)
                .title("noName1Title")
                .contents("noName1Content")
                .authorName("noName1AuthorName")
                .returnType(ReturnType.tel)
                .returnNoAddress("noName1ReturnAddress")
                .createdAt("noName1CreatedAt")
                .createdBy(member)

                .modifiedBy(member)
                .build();
        postRepository.save(post);*/

        /*System.out.println("mvc : "+mvc);
        mvc.perform(get("/v1/api/post/5"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());*/

        //assertThat("hello",is("hello"));

    }

    @Test
    public void getPostList() throws Exception {
        mvc.perform(get("/v1/api/posts/3/0"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void noName2() throws Exception {
        mvc.perform(get("/v1/api/hello222"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void writePostTest() throws ResourceNotFoundException {
        //빌드비어있을때 생성되는지 확인.
        Post post = Post.builder()
                .title("postTitle")
                .build();

        PostDTO postDTO = new PostDTO(post);
    }

    @Test
    public void getPageDTO() throws ResourceNotFoundException {
        Member member = memberRepository.findById(5L)
                .orElseThrow(()->new ResourceNotFoundException("mema;fj"));

        int aaa = goodsRepository.findTakeCountAboutFish(member, FishingType.sealocks);
        System.out.println(aaa);
    }


}