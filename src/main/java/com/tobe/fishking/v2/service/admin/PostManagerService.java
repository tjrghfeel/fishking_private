package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.post.ModifyOne2oneAnswerDto;
import com.tobe.fishking.v2.model.admin.post.PostManageDtoForPage;
import com.tobe.fishking.v2.model.admin.post.PostSearchConditionDto;
import com.tobe.fishking.v2.model.admin.post.WriteOne2oneAnswerDto;
import com.tobe.fishking.v2.model.board.UpdatePostDTO;
import com.tobe.fishking.v2.model.board.WritePostDTO;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.common.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PostManagerService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    Environment env;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    PostService postService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AlertService alertService;

    /*공지사항, FAQ, 1:1문의 조건 검색*/
    @Transactional
    public Page<PostManageDtoForPage> getPostList(PostSearchConditionDto dto, int page)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        /*findAllByConditions()에 넘겨줄 인자들 변환해주는 부분*/
        String key = env.getProperty("encrypKey.key");
        if(dto.getAuthorName()!=null){dto.setAuthorName(AES.aesEncode(dto.getAuthorName(),key));}
        Integer channelType = (dto.getChannelType()==null)? null : (ChannelType.valueOf(dto.getChannelType()).ordinal());
        Integer questionType = (dto.getQuestionType()==null)? null : (QuestionType.valueOf(dto.getQuestionType()).ordinal());
        Integer returnType = (dto.getReturnType()==null)? null : (ReturnType.valueOf(dto.getReturnType()).ordinal());

        Pageable pageable = PageRequest.of(page,10, JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));

        return postRepository.findAllByConditions(
                dto.getId(),
                dto.getBoardId(),
                dto.getParent_id(),
                channelType,
                questionType,
                dto.getTitle(),
                dto.getContents(),
                dto.getAuthorId(),
                dto.getAuthorName(),
                returnType,
                dto.getReturnNoAddress(),
                dto.getCreatedAt(),
                dto.getIsSecret(),
                dto.getCreateDateStart(),
                dto.getCreateDateEnd(),
                dto.getModifiedDateStart(),
                dto.getModifiedDateEnd(),
                pageable
        );
    }

    @Transactional
    public Long makeOne2oneAnswer(WriteOne2oneAnswerDto dto,String token) throws ResourceNotFoundException, IOException {
        Board board = boardRepository.findBoardByFilePublish(FilePublish.one2one);
        Post parentPost = postRepository.findById(dto.getParentId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+dto.getParentId()));
        Member manager = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));

        WritePostDTO writePostDTO = WritePostDTO.builder()
                .boardId(board.getId())
                .channelType(parentPost.getChannelType().getKey())
                .title("noTitle")
                .contents(dto.getContent())
                .returnType(parentPost.getReturnType().getKey())
                .returnNoAddress(parentPost.getReturnNoAddress())
                .createdAt(parentPost.getCreatedAt())
                .questionType(parentPost.getQuestionType().getKey())
                .isSecret(false)
                .parentId(dto.getParentId())
                .files(dto.getFileList())
                .build();


        postService.writePost(writePostDTO,token);

        /*1:1답변완료알림 추가*/
        AddAlertDto addAlertDto = AddAlertDto.builder()
                .memberId(parentPost.getAuthor().getId())
                .alertType("oneToQuery")
                .entityType("post")
                .pid(parentPost.getId())
                .createdBy(manager.getId())
                .content(parentPost.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"일자 문의.")
                .build();
        alertService.addAlert(addAlertDto);

        return parentPost.getId();
    }

    @Transactional
    public Long modifyOne2oneAnswer(ModifyOne2oneAnswerDto dto, String token) throws ResourceNotFoundException, IOException {
        Post answerPost = postRepository.findById(dto.getAnswerPostId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id ::"+dto.getAnswerPostId()));

        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                .postId(dto.getAnswerPostId())
                .boardId(answerPost.getBoard().getId())
                .channelType(answerPost.getChannelType().getKey())
                .title("noTitle")
                .contents(dto.getContent())
                .returnType(answerPost.getReturnType().getKey())
                .returnNoAddress(answerPost.getReturnNoAddress())
                .createdAt(answerPost.getCreatedAt())
                .questionType(answerPost.getQuestionType().getKey())
                .isSecret(false)
                .files(dto.getFileList())
                .build();

        postService.updatePost(updatePostDTO,token);
        return answerPost.getParent_id();
    }

}
