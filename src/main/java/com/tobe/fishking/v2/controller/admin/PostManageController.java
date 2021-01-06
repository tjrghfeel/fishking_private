package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.model.admin.post.PostManageDtoForPage;
import com.tobe.fishking.v2.model.admin.post.PostSearchConditionDto;
import com.tobe.fishking.v2.service.admin.PostManagerService;
import com.tobe.fishking.v2.service.board.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Api(tags={"고객센터 관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class PostManageController {
    @Autowired
    PostService postService;
    @Autowired
    PostManagerService postManagerService;

    /*게시글 조건 검색*/
    @ApiOperation(value = "공지사항, FAQ, 1:1문의 조건검색",notes = "" +
            "- 관리자용api\n" +
            "- ")
    @GetMapping("/manage/post/{page}")
    public Page<PostManageDtoForPage> getPostList(PostSearchConditionDto dto, @PathVariable("page") int page)
            throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return postManagerService.getPostList(dto,page);
    }


}
