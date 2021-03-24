package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.post.*;
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

    public void checkAdminAuthor(String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        if(member.getRoles() != Role.admin){throw new RuntimeException("관리자 권한이 아닙니다.");}
    }
    /*공지사항, FAQ, 1:1문의 조건 검색*/
    @Transactional
    public Page<PostManageDtoForPage> getPostList(PostSearchConditionDto dto, int page, String token)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, ResourceNotFoundException {
        /*관리자 권한 확인*/
        checkAdminAuthor(token);

        /*findAllByConditions()에 넘겨줄 인자들 변환해주는 부분*/
        Integer channelType = (dto.getChannelType()==null)? null : (ChannelType.valueOf(dto.getChannelType()).ordinal());
        Integer questionType = (dto.getQuestionType()==null)? null : (QuestionType.valueOf(dto.getQuestionType()).ordinal());
        Integer returnType = (dto.getReturnType()==null)? null : (ReturnType.valueOf(dto.getReturnType()).ordinal());
        if(dto.getCreatedDateEnd()!=null){ dto.setCreatedDateEnd(dto.getCreatedDateEnd().plusDays(1L));}
        if(dto.getModifiedDateEnd()!=null){dto.setModifiedDateEnd(dto.getModifiedDateEnd().plusDays(1L));}
        Pageable pageable=null;
        if(dto.getSort()!=null) {
            pageable = PageRequest.of(page, 3, JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));//JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")")
        }
        else pageable = PageRequest.of(page,3);

        return postRepository.findAllByConditions(
                dto.getId(),
                dto.getBoardId(),
                dto.getParent_id(),
                channelType,
                questionType,
                dto.getTitle(),
                dto.getContents(),
                dto.getAuthorId(),
                dto.getNickName(),
                returnType,
                dto.getReturnNoAddress(),
                dto.getCreatedAt(),
                dto.getIsSecret(),
                dto.getCreatedDateStart(),
                dto.getCreatedDateEnd(),
                dto.getModifiedDateStart(),
                dto.getModifiedDateEnd(),
                dto.getTargetRole(),
                dto.getCreatedBy(),
                dto.getModifiedBy(),
                dto.getIsReplied(),
                pageable
        );
    }

    /*공지사항, FAQ, 1:1문의 상세보기*/
    @Transactional
    public PostManageDetailDto getPostDetail(Long postId, String token) throws ResourceNotFoundException {
        PostManageDetailDto result = null;

        /*현재회원 권한 확인*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        if(member.getRoles() != Role.admin){ throw new RuntimeException("관리자 권한이 아닙니다.");}

        /*post가져와 필요한 데이터 dto에 저장. */
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+postId));
        String channelType = null;
        if(post.getChannelType()!=null){ channelType = post.getChannelType().getValue(); }
        String questionType = null;
        if(post.getQuestionType()!=null){ questionType = post.getQuestionType().getValue(); }
        String returnType = null;
        if(post.getReturnType()!=null){ returnType = post.getReturnType().getValue(); }
        String targetRole = (post.getTargetRole())? "일반회원" : "업주";

        result = PostManageDetailDto.builder()
                .postId(post.getId())
                .boardId(post.getBoard().getId())
                .boardName(post.getBoard().getName())
                .parentId(post.getParent_id())
                .channelType(channelType)
                .questionType(questionType)
                .title(post.getTitle())
                .content(post.getContents())
                .authorId(post.getAuthor().getId())
                .authorNickName(post.getAuthor().getNickName())
                .returnType(returnType)
                .returnNoAddress(post.getReturnNoAddress())
                .createdAt(post.getCreatedAt())
                .isSecret(post.getIsSecret())
                .createdById(post.getCreatedBy().getId())
                .createdByNickName(post.getCreatedBy().getNickName())
                .modifiedById(post.getModifiedBy().getId())
                .modifiedByNickName(post.getModifiedBy().getNickName())
                .targetRole(targetRole)
                .isReplied(post.getIsReplied())
                .build();

        return result;
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
                .targetRole(parentPost.getTargetRole())
                .files(dto.getFileList())
                .build();


        postService.writePost(writePostDTO,token);
        parentPost.setIsReplied(true);

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
