package com.tobe.fishking.v2.controller.post;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.PostDTO;
import com.tobe.fishking.v2.model.board.PostListDTO;
import com.tobe.fishking.v2.model.board.UpdatePostDTO;
import com.tobe.fishking.v2.model.board.WritePostDTO;
import com.tobe.fishking.v2.service.FileService;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.common.FileService;
import com.tobe.fishking.v2.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Api(tags = {"게시글"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/api")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UploadService uploadService;

    /*게시판별로 게시글 목록을 조회.
    * 일단 '목록'출력만 할때 사용. Post에서 목록 출력할때 필요한 몇가지 정보들만을 담아서 반환해준다. */
    @ApiOperation(value = "게시글 목록 조회", notes = "게시글의 '내용'을 제외한 게시글정보들이 페이지형식으로 반환됩니다")
    @GetMapping("/posts/{board_id}/{page}")
    public Page<PostListDTO> getPostListInPageForm(@PathVariable(value = "board_id") Long board_id, @PathVariable(value="page") int page)
            throws ResourceNotFoundException {
        Page<PostListDTO> p = postService.getPostListInPageForm(board_id, page);
        return p;
    }
    
    /*FAQ용 Post 리스트 조회.
    * 위의 getPostListInPageForm()와 거의 같지만 FAQ용으로 만든 컨트롤러 메소드.
    * FAQ같은 경우, 내용이 별로없어서 FAQ목록 조회할때 내용까지 모두 전달해서 한번에 볼수있도록. */
    /*FAQ라도 나중에 이미지를 넣고 할수도있기때문에 굳이 따로만들지 않고 위의 메소드를 같이 사용하기로함.
    @ApiOperation(value = "게시글의 모든 글 가져오기", notes = "게시글의 모든 글들을 내용포함해서 가져옵니다. ")
    @GetMapping("/posts/{board_id}")
    public List<PostResponse> getPostList(@PathVariable("board_id") Long boardId) throws ResourceNotFoundException {
        return postService.getPostList(boardId);
    }*/

    //1대1 문의내역 목록 조회. 로그인 처리, 스프링 시큐리티 어떻게 돌아가는지 공부후에 완성.
    /*@ApiOperation(value = "1:1문의내역 목록 조회", notes = "1:1문의 내역을 목록을 조회합니다.")
    @GetMapping("/post/{board_id}/{member}/{page}")
    public Page<PostResponse> getQnAList(
            @PathVariable(value = "board_id") Long board_id,
            @PathVariable(value = "member")
            @PathVariable(value="page") int page
    ){

    }*/

    //게시글 하나 조회.
    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회합니다")
    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable(value = "id") Long id, int filePublish) throws ResourceNotFoundException {
        PostDTO postDTO = postService.getPostById(id, filePublish);
        return postDTO;
    }

    //게시글 저장. Post entity, FileEntity저장을 한다. 생성한 Post엔터티의 id를 반환.
    /*!!!!!!!!!!이미지 저장할 폴더위치, 저장을 어떤식으로 할지 정해진다음 수정해야함.*/
    @ApiOperation(value = "게시글 저장", notes = "parnedId, tagName, secret은 nullable")
    @PostMapping("/post")
    public Long writePost(@ModelAttribute WritePostDTO writePostDTO,
                             /*@RequestParam("file") ArrayList<MultipartFile> files,*/
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("filePublish") int filePublish) throws Exception {
        //게시글 저장.
        Long postId = postService.writePost(writePostDTO, file, filePublish);

        return postId;
    }

    /*Post하나 업데이트.
    * 반환값 : 업데이트된 Post id.
    */
    @ApiOperation(value = "게시물 수정", notes = "게시물을 수정합니다")
    @PutMapping("/post")
    public Long updatePost(UpdatePostDTO postDTO, @RequestParam("file") MultipartFile file,
                           @RequestParam("filePublish") int filePublish) throws ResourceNotFoundException, IOException {
        return postService.updatePost(postDTO, file, filePublish);
    }



}
