package com.tobe.fishking.v2.service.board;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.PostDTO;
import com.tobe.fishking.v2.model.board.PostListDTO;
import com.tobe.fishking.v2.model.board.UpdatePostDTO;
import com.tobe.fishking.v2.model.board.WritePostDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.board.TagRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class PostService {
    @Autowired
    private Environment env;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UploadService uploadService;

    /*PostResponse를 Page형태로 반환해주는 메소드.
    * 반환하는 PostResponse에는 contents필드가 포함되어있지 않다. */
    @Transactional(readOnly = true)
    public Page<PostListDTO> getPostListInPageForm(Long board_id, int page) throws ResourceNotFoundException {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(()->new ResourceNotFoundException("Board not found for this id :: " + board_id));
        Pageable pageable = PageRequest.of(page, 10);
        Page<PostListDTO> postResponse = postRepository.findAllByBoard(board.getId(), pageable);

        return postResponse;
    }

    /*Board의 Post 리스트를 반환하는 메소드.
    * 반환하는 PostResponse에는 Post의 모든 필드가 들어있다. */
    /*FAQ용으로 만들었지만, PostController에 getPostList()에 적힌 이유로 일단 보류.
    @Transactional(readOnly = true)
    public List<PostResponse> getPostList(Long boardId) throws ResourceNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new ResourceNotFoundException("Board not found for this id ::"+boardId));

        return postRepository.findAllByBoard(boardId);
    }*/

    //Post하나 반환 메소드.
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id, int filePublish) throws ResourceNotFoundException {
        PostDTO postDTO = null;
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post not found for this id :: " + id));

        /*파일들 가져오는 부분. */
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublish(post.getId(), FilePublish.values()[filePublish]);
        List<String> fileUrlList = new LinkedList<String>();
        for(int i=0; i<fileList.size(); i++){
            fileUrlList.add(fileList.get(i).getDownloadUrl());
        }

        postDTO = new PostDTO(post, fileUrlList);

        /*비활성화된 글이라면 빈 dto를 반환시켜줌.*/
        if(postDTO.isActive()==false) {
            PostDTO dto = new PostDTO();
            dto.setActive(false);
            return dto;
        }

        return postDTO;
    }

    //Post entity저장 및 FileEntity저장.
    /*postDTO에는 boardId, channelType, title, content, authorId, returnType, returnNoAddress, createdAt, tagsName,
    isSecret, parentId가 들어있음.*/
    @Transactional
    public Long writePost(WritePostDTO postDTO, MultipartFile file, int filePublish) throws ResourceNotFoundException, IOException {
        //Post 저장 부분.
        Board boardOfPost = boardRepository.findById(postDTO.getBoardId())
                .orElseThrow(()->new ResourceNotFoundException("Board not found for this id :: "+postDTO.getBoardId()));
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
                .authorName(authorOfPost.getMemberName())
                .contents(postDTO.getContents())
                .title(postDTO.getTitle())
                .channelType(ChannelType.values()[postDTO.getChannelType()])
                .board(boardOfPost)
                .author(authorOfPost)
                .isSecret(postDTO.isSecret())
                .tags(tagList)
                .questionType(QuestionType.values()[postDTO.getQuestionType()])
                .parent_id(postDTO.getParentId())
                .build();
        post = postRepository.save(post);

        //파일 저장.
            //파일 타입 확인.
        if(file!=null) {
            String fileType = file.getContentType().split("/")[0];
            FileType enumFileType;
            if (fileType.equals("image")) {
                enumFileType = FileType.image;
            } else if (fileType.equals("text")) {
                enumFileType = FileType.txt;
            } else if (fileType.equals("video")) {
                enumFileType = FileType.video;
            } else enumFileType = FileType.attachments;

            //UploadService를 이용한 파일저장.
            Map<String, Object> fileInfo
                    = uploadService.initialFile(file, enumFileType, FilePublish.values()[filePublish], ""/*, 세션토큰 인자 */);

            //File entity 저장.
            FileEntity currentFile = FileEntity.builder()
                    .fileName(file.getOriginalFilename())
                    .storedFile((String) fileInfo.get("fileName"))
                    .downloadUrl((String) fileInfo.get("fileDownloadUrl"))
                    .thumbnailFile((String) fileInfo.get("thumbUploadPath"))
                    .downloadThumbnailUrl((String) fileInfo.get("thumbDownloadUrl"))
                    .originalFile(file.getOriginalFilename())
                    .size(file.getSize())
                    .fileUrl((String) fileInfo.get("fileUrl"))
                    .fileType(enumFileType)
                    .createdBy(authorOfPost)
                    .modifiedBy(authorOfPost)
                    .filePublish(FilePublish.values()[filePublish])
                    .locations("sampleLocation")
                    .pid(post.getId())
                    /*아래 세 필드 not null이라 추가 필요.
                    .location()
                    * .bid()
                    * .modifiedBy()
                    * .filePublish()*/
                    .build();
            long fileId = fileRepository.save(currentFile).getId();
        }

        return post.getId();
    }

    /*Post 수정 및 해당Post의 기존 File들 삭제, 넘어온 파일 다시올리기, FileEntity 저장.
    * */
    @Transactional
    public Long updatePost(UpdatePostDTO postDTO,
                           MultipartFile file,
                           int filePublish) throws ResourceNotFoundException, IOException {
        //Post entity 수정.
        Post post = postRepository.findById(postDTO.getPostId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+postDTO.getPostId()));

        post.updatePost(postDTO);

        /*Post에 올려놓은 File들 삭제하고 다시 올림. */
            /*파일들 모두 삭제.*/
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublish(post.getId(), FilePublish.values()[filePublish]);
        for(int i=0; i<fileList.size(); i++){
            FileEntity fileEntity = fileList.get(i);
            String fileUrl = fileEntity.getFileUrl();
            String fileThumnailUrl = fileEntity.getThumbnailFile();
            uploadService.removeFile(new File(fileUrl));
            uploadService.removeFile(new File(fileThumnailUrl));

            fileRepository.delete(fileEntity);
        }
            /*새로 업로드*/
        String fileType = file.getContentType().split("/")[0];
        FileType enumFileType;
        if(fileType.equals("image")){enumFileType = FileType.image;}
        else if(fileType.equals("text")){enumFileType = FileType.txt;}
        else if(fileType.equals("video")){enumFileType = FileType.video;}
        else enumFileType = FileType.attachments;

        //UploadService를 이용한 파일저장.
        Map<String, Object> fileInfo = uploadService.initialFile(file, enumFileType, FilePublish.values()[filePublish], ""/*, 세션토큰 인자 */);

        //File entity 저장.
        FileEntity currentFile = FileEntity.builder()
                .fileName(file.getOriginalFilename())
                .storedFile((String)fileInfo.get("fileName"))
                .downloadUrl((String)fileInfo.get("fileDownloadUrl"))
                .thumbnailFile((String)fileInfo.get("thumbUploadPath"))
                .downloadThumbnailUrl((String)fileInfo.get("thumbDownloadUrl"))
                .originalFile(file.getOriginalFilename())
                .size(file.getSize())
                .fileUrl((String)fileInfo.get("fileUrl"))
                .fileType(enumFileType)
                .createdBy(post.getModifiedBy())
                .modifiedBy(post.getModifiedBy())
                .filePublish(FilePublish.values()[filePublish])
                .locations("sampleLocation")
                .pid(post.getId())
                /*아래 세 필드 not null이라 추가 필요.
                .location()
                * .bid()
                * .modifiedBy()
                * .filePublish()*/
                .build();
        long fileId = fileRepository.save(currentFile).getId();

        return post.getId();
    }
}
