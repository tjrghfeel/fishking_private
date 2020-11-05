package com.tobe.fishking.v2.controller.post;

import com.google.gson.Gson;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.FileDTO;
import com.tobe.fishking.v2.model.board.PostDTO;
import com.tobe.fishking.v2.model.board.WritePostDTO;
import com.tobe.fishking.v2.repository.FilesRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.service.FileService;
import com.tobe.fishking.v2.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    @ApiOperation(value="noName", notes="noName")
    @GetMapping("/hello222")
    public List<Integer> hello(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(11);
        list.add(22);
        return list;
    }

    //게시판별로 게시글 목록 조회
    /*@ApiOperation(value = "게시글 목록 조회", notes = "게시글 목록을 조회합니다")
    @GetMapping("/posts/{board_id}/{page}")
    public Page<PostDTO> getPostList(@PathVariable(value = "board_id") Long board_id, @PathVariable(value="page") int page)
            throws ResourceNotFoundException {
        Page<PostDTO> p = postService.getPostList(board_id, page);
        return p;
    }*/

    //게시글 하나 조회
    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회합니다")
    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        PostDTO postDTO = postService.getPostById(id);
        return postDTO;
    }

    //게시글 저장. Post entity, FileEntity저장을 한다.
    @ApiOperation(value = "게시글 저장", notes = "게시글을 등록합니다")
    @PostMapping("/post")
    public boolean writePost(@ModelAttribute PostDTO postDTO, @RequestParam("file") ArrayList<MultipartFile> files,
                             @RequestParam("filePublish") String filePublish) throws Exception {
        //게시글 저장.
        Long postId = postService.writePost(postDTO);

        //파일 저장.
        for(int i=0; i<files.size(); i++) {//파일 여러개일경우, 각 파일들에 대해 반복.
            MultipartFile file = files.get(i);
            String origFilename = file.getOriginalFilename();
            String savePath = System.getProperty("user.dir") + "\\files";

            String filePath = savePath + "\\" + origFilename;
            file.transferTo(new File(filePath));//파일저장.

            //파일 타입 확인.
            String fileType = file.getContentType().split("/")[0];
            FileType enumFileType;
            if(fileType.equals("image")){enumFileType = FileType.image;}
            else if(fileType.equals("text")){enumFileType = FileType.txt;}
            else if(fileType.equals("video")){enumFileType = FileType.video;}
            else enumFileType = FileType.attachments;

            FileDTO fileDTO = FileDTO.builder()
                    .filePublish(filePublish)
                    .originalFile(origFilename)
                    .fileNo(i)
                    .fileType(enumFileType.getValue())
                    .postId(postId)
                    .size(file.getSize())
                    .fileUrl(filePath)
                    //.bid()
                    //.caption()
                    //.pid()
                    .build();

            fileService.storeFileEntity(fileDTO);
        }

        return true;
    }

}
