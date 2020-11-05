package com.tobe.fishking.v2.service.post;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.PostDTO;
import com.tobe.fishking.v2.model.board.WritePostDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.board.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MemberRepository memberRepository;

    //Post를 Page형태로 반환해주는 메소드.
    @Transactional(readOnly = true)
    public Page<Post> getPostList(Long board_id, int page) throws ResourceNotFoundException {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(()->new ResourceNotFoundException("Board not found for this id :: " + board_id));
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());
        //Page<PostDTO> pageDTO = postRepository.findAllByBoard2(board.getId(), pageable);
        Page<Post> pageDTO = postRepository.findAllByBoard1(board, pageable);

        //Page<Post>를 Page<PostDTO>로 변환
        /*Page<PostDTO> pageDTO = p.map(new Function<Post, PostDTO>() {
            @Override
            public PostDTO apply(Post post) {
                return new PostDTO(post);
            }
        });*/

        return pageDTO;
    }

    //Post하나 반환 메소드.
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post not found for this id :: " + id));

        return new PostDTO(post);
    }

    //postDTO를 받아서 Post entity생성 및 jpa로 저장.
    /*postDTO에는 boardId, channelType, title, content, authorId, returnType, returnNoAddress, createdAt, tagsName,
    isSecret, parentId가 들어있음.*/
    @Transactional
    public Long writePost(PostDTO postDTO) throws ResourceNotFoundException {

        Board boardOfPost = boardRepository.findById(postDTO.getBoardId())
                .orElseThrow(()->new ResourceNotFoundException("Post not found for this id :: "+postDTO.getBoardId()));
        Member authorOfPost = memberRepository.findById(postDTO.getAuthorId())
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this id ::"+postDTO.getAuthorId()));

        //Post entity의 List<Tag>만듦.
        List<Tag> tagList = new LinkedList<Tag>();
        List<String> tagNameList = postDTO.getTagsName();
        for(int i=0; i<tagNameList.size(); i++){
            Tag tempTag = null;
            tempTag = tagRepository.findByTagName(tagNameList.get(i));
            if(tempTag==null) throw new ResourceNotFoundException("Tag not found for this tagName :: "+tagNameList.get(i));
            tagList.add(tempTag);
        }

        Post post = Post.builder()
                .modifiedBy(authorOfPost)
                .createdBy(authorOfPost)
                .createdAt(postDTO.getCreatedAt())
                .returnNoAddress(postDTO.getReturnNoAddress())
                .returnType(ReturnType.values()[postDTO.getReturnType()])
                //.authorName(postDTO.getAuthorName())
                .contents(postDTO.getContents())
                .title(postDTO.getTitle())
                .channelType(ChannelType.values()[postDTO.getChannelType()])
                .board(boardOfPost)
                .author(authorOfPost)
                .isSecret(postDTO.isSecret())
                .tags(tagList)

                .build();

        post = postRepository.save(post);

        return post.getId();
    }

    //WritePostDTO를 받아 PostEntity를 만들어줌.
    public Post transferWritePostDtoToPost(PostDTO postDTO) throws ResourceNotFoundException {
        Board boardOfPost = boardRepository.findById(postDTO.getBoardId())
                .orElseThrow(()->new ResourceNotFoundException("Post not found for this id :: "+postDTO.getBoardId()));
        Member authorOfPost = memberRepository.findById(postDTO.getAuthorId())
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this id ::"+postDTO.getAuthorId()));

        //Post entity의 List<Tag>만듦.
        List<Tag> tagList = new LinkedList<Tag>();
        List<String> tagNameList = postDTO.getTagsName();
        for(int i=0; i<tagNameList.size(); i++){
            Tag tempTag = null;
            tempTag = tagRepository.findByTagName(tagNameList.get(i));
            if(tempTag==null) throw new ResourceNotFoundException("Tag not found for this tagName :: "+tagNameList.get(i));
            tagList.add(tempTag);
        }

        Post post = Post.builder()
                .modifiedBy(authorOfPost)
                .createdBy(authorOfPost)
                .createdAt(postDTO.getCreatedAt())
                .returnNoAddress(postDTO.getReturnNoAddress())
                .returnType(ReturnType.values()[postDTO.getReturnType()])
                //.authorName(postDTO.getAuthorName())
                .contents(postDTO.getContents())
                .title(postDTO.getTitle())
                .channelType(ChannelType.values()[postDTO.getChannelType()])
                .board(boardOfPost)
                .author(authorOfPost)
                .isSecret(postDTO.isSecret())
                .tags(tagList)
                .build();

        return post;
    }
}
