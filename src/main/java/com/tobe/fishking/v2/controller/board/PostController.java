package com.tobe.fishking.v2.controller.board;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.*;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.common.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Api(tags = {"게시글"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class PostController {
    @Autowired
    private PostService postService;

    /*게시판별로 게시글 목록을 조회.
    * 일단 '목록'출력만 할때 사용. Post에서 목록 출력할때 필요한 몇가지 정보들만을 담아서 반환해준다. */
    /*@ApiOperation(value = "게시글 목록 조회", notes = "게시글의 '내용'을 제외한 게시글정보들이 페이지형식으로 반환됩니다")
    @GetMapping("/posts/{board_id}/{page}")
    public Page<PostListDTO> getPostListInPageForm(@PathVariable(value = "board_id") Long board_id, @PathVariable(value="page") int page)
            throws ResourceNotFoundException {
        Page<PostListDTO> p = postService.getPostListInPageForm(board_id, page);
        return p;
    }*/

    /*FAQ 조회*/
    @ApiOperation(value = "FAQ조회")
    @GetMapping("/faq/{page}")
    public Page<FAQDto> getFAQList(@PathVariable("page") int page){
        return postService.getFAQList(page);
    }

    /*1:1문의내역 리스트 조회*/
    @ApiOperation(value = "1:1문의내역 리스트 조회")
    @GetMapping("/qna/{page}")
    public Page<QnADtoForPage> getQnAList(
            @PathVariable("page") int page,
            /*@RequestParam("sessionToken") String sessionToken,*/
            @RequestParam("memberId") Long memberId
    ) throws ResourceNotFoundException {
        return postService.getQnAList(page, memberId);
    }

    /*1:1문의내역 상세보기*/
    @ApiOperation(value = "1:1문의내역 상세보기")
    @GetMapping("/qna/detail")
    public QnADetailDto getQnADetail(@RequestParam("postId") Long postId){
        return postService.getQnADetail(postId);
    }

    /*공지사항 리스트 보기*/
    @ApiOperation(value = "공지사항 리스트 보기")
    @GetMapping("/notice/{page}")
    public Page<NoticeDtoForPage> getNoticeList(@PathVariable("page") int page){
        return postService.getNoticeList(page);
    }

    /*공지사항 상세보기*/
    @ApiOperation(value = "공지사항 상세보기")
    @GetMapping("/notice")
    public NoticeDetailDto getNoticeDetail(@RequestParam("postId") Long postId) throws ResourceNotFoundException {
        return postService.getNoticeDetail(postId);
    }



    ///////////////////////////
    
    //게시글 하나 조회. FAQ, QNA, 공지사항 리스트 및 하나 조회에 대해 따로따로 위에 만들어놓음.
    /*@ApiOperation(value = "게시글 조회", notes = "게시글을 조회합니다")
    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable(value = "id") Long id, int filePublish) throws ResourceNotFoundException {
        PostDTO postDTO = postService.getPostById(id, filePublish);

        return postDTO;
    }*/

    //게시글 저장. Post entity, FileEntity저장을 한다. 생성한 Post엔터티의 id를 반환.
    @ApiOperation(value = "게시글 저장", notes = "parnedId, tagName, secret은 nullable / " +
            "writePostDTO의 files필드는 MultipartFile[]로 이미지파일 리스트를 저장하는 필드이다. ")
    @PostMapping("/post")
    public Long writePost(
            @RequestBody WritePostDTO writePostDTO/*,
            @RequestPart(value = "files", required = false) MultipartFile[] files*/
    ) throws Exception {
        //게시글 저장.
        MultipartFile[] files = writePostDTO.getFiles();
        Long postId = postService.writePost(writePostDTO, files);

        return postId;
    }

    /*Post하나 업데이트.
    * 반환값 : 업데이트된 Post id.
    */
    @ApiOperation(value = "게시물 수정", notes = "게시물을 수정합니다")
    @PostMapping("/post/update")
    public Long updatePost(
            @RequestBody UpdatePostDTO postDTO/*,
            @RequestParam("files") MultipartFile[] files*/
    ) throws ResourceNotFoundException, IOException {
        MultipartFile[] files = postDTO.getFiles();
        return postService.updatePost(postDTO, files);
    }



}
