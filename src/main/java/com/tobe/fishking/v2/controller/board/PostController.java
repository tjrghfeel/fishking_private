package com.tobe.fishking.v2.controller.board;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.*;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.common.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Api(tags = {"고객센터"})
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
    @ApiOperation(value = "FAQ조회", notes = "모든 FAQ를 조회합니다. \n" +
            "- 글 목록 조회이지만 글 내용까지 한꺼번에 담겨서 반환된다. \n" +
            "- 요청 필드 )\n" +
            "   role : String / 어떤 유형의 회원에 대한 faq인지 결정을 위한 필드 / member : 일반회원, shipowner : 업주회원" +
            "- 필드 )\n" +
            "   id : faq 글 id\n" +
            "   questionType : 문의유형 / order(예약결제),cancel(취소)\n" +
            "   title : faq 제목\n" +
            "   contents : faq 내용\n" +
            "   authorId : 작성자 id\n" +
            "   createdBy : 글 생성자 id\n" +
            "   modifiedBy : 글 수정자 id\n")
    @GetMapping("/faq/{page}")
    public Page<FAQDto> getFAQList(@PathVariable("page") int page,@RequestParam("role") String role) throws EmptyListException {
//        return postService.getFAQList(page,role);
        Page<FAQDto> faq = postService.getFAQList(page,role);
        if (faq.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return faq;
        }
    }

    /*1:1문의내역 리스트 조회*/
    @ApiOperation(value = "1:1문의내역 리스트 조회", notes = "자신이 문의한 내역을 볼 수 있다. \n" +
            "- 필드 )\n" +
            "   id : 문의글 id\n" +
            "   questionType : 문의 유형 / order(예약결제),cancel(취소)\n" +
            "   date : 문의일자\n" +
            "   replied : 답변 여부 \n")
    @GetMapping("/qna/{page}")
    public Page<QnADtoForPage> getQnAList(
            @PathVariable("page") int page,
            HttpServletRequest request
    ) throws ResourceNotFoundException, EmptyListException {
        String sessionToken = request.getHeader("Authorization");
//        return postService.getQnAList(page, sessionToken);
        Page<QnADtoForPage> qna = postService.getQnAList(page, sessionToken);
        if (qna.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return qna;
        }
    }

    /*1:1문의내역 상세보기*/
    @ApiOperation(value = "1:1문의내역 상세보기", notes = "" +
            "- 한 1:1문의 건에 대해 상세 내용을 볼 수 있다. \n" +
            "- 필드 )\n" +
            "   id : 문의글 id\n" +
            "   questionType : 문의유형 / order(예약결제),cancel(취소)\n" +
            "   replied : 답변여부\n" +
            "   date : 문의일자\n" +
            "   contents : 문의 내용\n" +
            "   authorId : 작성자 id\n" +
            "   authorName : 작성자명\n" +
            "   returnType : 답변방법 / email(\"이메일\"),tel(\"연락처\")\n" +
            "   returnAddress : 답변 주소 또는 번호\n" +
            "   createdBy : 글 생성자 id\n" +
            "   modifiedBy : 글 수정자 id\n" +
            "   fileList : 문의글의 파일download url 리스트\n" +
            "   replyContents : 답변글 내용\n" +
            "   replyDate : 답변 시간\n" +
            "   replyAuthorId : 답변자 id\n" +
            "   replyCreatedBy : 글 생성자 id\n" +
            "   replyModifiedBy : 글 수정자 id\n" +
            "   replyFileList : 답변글의 파일 download url 리스트\n")
    @GetMapping("/qna/detail")
    public QnADetailDto getQnADetail(@RequestParam("postId") Long postId) {
        return postService.getQnADetail(postId);
    }

    /*공지사항 리스트 보기*/
    @ApiOperation(value = "공지사항 리스트 보기", notes = "" +
            "- 공지사항 리스트 반환 api \n" +
            "- 요청 필드 )\n" +
            "   role : String / 어떤 유형의 회원에 대한 faq인지 결정을 위한 필드 / member : 일반회원, shipowner : 업주회원" +
            "- 필드 ) \n" +
            "   id : 공지사항 글 id\n" +
            "   channelType : 공지사항 유형 / order(\"예약결제\"), cancel(\"취소\")\n" +
            "   title : 공지사항 제목\n" +
            "   date : 공지사항 작성일\n")
    @GetMapping("/notice/{page}")
    public Page<NoticeDtoForPage> getNoticeList(@PathVariable("page") int page, @RequestParam("role") String role) throws EmptyListException {
//        return postService.getNoticeList(page,role);
        Page<NoticeDtoForPage> notices = postService.getNoticeList(page,role);
        if (notices.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return notices;
        }
    }

    /*공지사항 상세보기*/
    @ApiOperation(value = "공지사항 상세보기", notes = "" +
            "- 공지사항 내용 상세보기 api \n" +
            "- 필드 )\n" +
            "   id : 공지사항 글 id\n" +
            "   channelType : 공지사항 유형 / order(\"예약결제\"), cancel(\"취소\")\n" +
            "   title : 공지사항 제목\n" +
            "   contents : 공지사항 내용 \n" +
            "   fileList : 파일 download url 리스트\n")
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
   /* @ApiOperation(value = "게시글 저장", notes = "1:1문의, FAQ, 공지사항 작성 api. \n" +
            "- parnedId, tagName, secret은 nullable. \n  " +
            "- files필드는 MultipartFile[]로 이미지파일 리스트를 저장하는 필드이다.\n" +
            " ")
    @PostMapping("/post")
    public Long writePost(
            @RequestBody WritePostDTO writePostDTO,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        //게시글 저장.
//        MultipartFile[] files = writePostDTO.getFiles();
        Long postId = postService.writePost(writePostDTO, sessionToken);

        return postId;
    }*/
    /*1:1문의 글올리기*/
    @ApiOperation(value = "1:1문의하기 글올리기",notes = "" +
            "- 요청 필드 )\n" +
            "   questionType : String / 문의 카테고리\n" +
            "       ㄴ order : 예약결제\n" +
            "       ㄴ cancel : 취소\n" +
            "   contents : String / 문의 내용\n" +
            "   returnType : String / 답변받을 방법\n" +
            "       ㄴ email : email \n" +
            "       ㄴ tel : 전화번호\n" +
            "   returnAddress : String / 'returnType'에서 결정한 방법에 해당하는 값 입력. 이메일주소 또는 전화번호\n" +
            "   fileList : Integer 배열 / 파일id 배열\n" +
            "   targetRole : 어떤회원유형이 문의글을 올리는지\n" +
            "       ㄴ member : 일반회원\n" +
            "       ㄴ shipowner : 업체회원\n"+
            "- 반환 ) 생성된 1:1문의글의 id ")
    @PostMapping("/post/one2one")
    public Long writeOne2one(@RequestBody QnaWriteDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, IOException {
        return postService.writeOne2one(dto,token);
    }
    /*faq 글올리기*/
    @ApiOperation(value = "FAQ 생성",notes = "" +
            "- 요청 필드 )\n" +
            "   questionType : String / 문의 카테고리\n" +
            "       ㄴ order : 예약결제\n" +
            "       ㄴ cancel : 취소\n" +
            "   title : String / 제목\n" +
            "   contents : String / 내용\n" +
            "   fileList : Integer 배열 / 파일id 배열\n" +
            "   targetRole : String / 어떤 유형의 회원에 대한 faq인지\n" +
            "       ㄴ member : 일반회원\n" +
            "       ㄴ shipowner : 업체회원\n")
    @PostMapping("/post/faq")
    public Long writeFaq(@RequestBody FaqWriteDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, IOException {
        return postService.writeFaq(dto,token);
    }
    /*공지사항 올리기*/
    @ApiOperation(value = "공지사항 생성",notes = "" +
            "- 요청필드 )\n" +
            "   channelType : String / 공지 유형\n" +
            "       ㄴ notice : 공지\n" +
            "       ㄴ event : 이벤트\n" +
            "   title : String / 제목\n" +
            "   contents : String / 내용\n" +
            "   fileList : Integer 배열 / 파일id 배열\n" +
            "   targetRole : 어떤 유형의 회원에 대한 공지사항인지\n" +
            "       ㄴ member : 일반회원\n" +
            "       ㄴ shipowner : 업체회원\n"+
            "")
    @PostMapping("/post/notice")
    public Long writeNotice(@RequestBody NoticeWriteDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, IOException {
        return postService.writeNotice(dto,token);
    }

    /*Post하나 업데이트.
     * 반환값 : 업데이트된 Post id.
     */
    /*@ApiOperation(value = "게시물 수정", notes = "1:1문의, FAQ, 공지사항 수정 api. \n" +
            "- files필드는 MultipartFile[]로 이미지파일 리스트를 저장하는 필드이다." +
            "- 파일을 올리지 않을 경우, files필드에 빈 배열 객체를 넣으면 됩니다.  ")
    @PostMapping("/post/update")
    public Long updatePost(
            @RequestBody UpdatePostDTO postDTO,
            HttpServletRequest request
    ) throws ResourceNotFoundException, IOException {
        String sessionToken = request.getHeader("Authorization");
//        MultipartFile[] files = postDTO.getFiles();
        return postService.updatePost(postDTO, sessionToken);
    }*/
    /*1:1문의 수정*/
    @ApiOperation(value = "1:1문의하기 수정",notes = "" +
            "- 요청 필드 )\n" +
            "   postId : 1:1문의글의 id\n" +
            "   questionType : String / 문의 카테고리\n" +
            "       ㄴ order : 예약결제\n" +
            "       ㄴ cancel : 취소\n" +
            "   contents : String / 문의 내용\n" +
            "   returnType : String / 답변받을 방법\n" +
            "       ㄴ email : email \n" +
            "       ㄴ tel : 전화번호\n" +
            "   returnAddress : String / 'returnType'에서 결정한 방법에 해당하는 값 입력. 이메일주소 또는 전화번호\n" +
            "   fileList : Integer 배열 / 파일id 배열\n" +
            "   targetRole : String / 어떤 유형의 회원이 작성한 문의인지\n" +
            "       ㄴ member : 일반회원\n" +
            "       ㄴ shipowner : 업체회원\n" +
            "- 반환 ) 생성된 1:1문의글의 id ")
    @PutMapping("/post/one2one")
    public Long updateOne2one(@RequestBody QnaUpdateDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, IOException {
        return postService.updateOne2one(dto,token);
    }

    /*faq 수정*/
    @ApiOperation(value = "FAQ 수정",notes = "" +
            "- 요청 필드 )\n" +
            "   postId : FAQ 글의 id\n" +
            "   questionType : String / 문의 카테고리\n" +
            "       ㄴ order : 예약결제\n" +
            "       ㄴ cancel : 취소\n" +
            "   title : String / 제목\n" +
            "   contents : String / 내용\n" +
            "   fileList : Integer 배열 / 파일id 배열\n" +
            "   targetRole : String / 어떤 유형의 회원에 대한 faq인지\n" +
            "       ㄴ member : 일반회원\n" +
            "       ㄴ shipowner : 업체회원\n")
    @PutMapping("/post/faq")
    public Long updateFaq(@RequestBody FaqUpdateDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, IOException {
        return postService.updateFaq(dto,token);
    }
    /*공지사항 수정*/
    @ApiOperation(value = "공지사항 수정",notes = "" +
            "- 요청필드 )\n" +
            "   postId : 공지사항 글의 id\n" +
            "   channelType : String / 공지 유형\n" +
            "       ㄴ notice : 공지\n" +
            "       ㄴ event : 이벤트\n" +
            "   title : String / 제목\n" +
            "   contents : String / 내용\n" +
            "   fileList : Integer 배열 / 파일id 배열\n" +
            "   targetRole : String / 어떤 유형의 회원에 대한 공지사항인지\n" +
            "       ㄴ member : 일반회원\n" +
            "       ㄴ shipowner : 업체회원\n")
    @PutMapping("/post/notice")
    public Long updateNotice(@RequestBody NoticeUpdateDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, IOException {
        return postService.updateNotice(dto,token);
    }

    /*글 삭제하기*/
    @ApiOperation(value = "공지사항, FAQ, 1:1문의 삭제",notes = "" +
            "- 관리자 등급이 아닌 경우 공지사항,FAQ를 삭제할 수 없다. \n" +
            "- 요청 필드 )\n" +
            "   ")
    @DeleteMapping("/post")
    public boolean deletePost(@RequestBody DeletePostDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return postService.deletePost(dto,token);
    }



}
