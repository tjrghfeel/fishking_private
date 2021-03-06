package com.tobe.fishking.v2.service.board;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.board.TagRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
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
    @Autowired
    private MemberService memberService;

    /*PostResponse를 Page형태로 반환해주는 메소드.
     * 반환하는 PostResponse에는 contents필드가 포함되어있지 않다. */
    @Transactional(readOnly = true)
    public Page<PostListDTO> getPostListInPageForm(Long board_id, int page) throws ResourceNotFoundException {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(()->new ResourceNotFoundException("Board not found for this id :: " + board_id));
        Pageable pageable = PageRequest.of(page, 3);
        Page<PostListDTO> postResponse = postRepository.findAllByBoard(board.getId(), pageable);

        return postResponse;
    }

    /*FAQ 조회*/
    @Transactional(readOnly = true)
    public Page<FAQDto> getFAQList(int page, String role, String title, String inputQuestionType){
        Boolean roleValue = null;
        if(role.equals("member")){roleValue=true;}
        else if(role.equals("shipowner")){roleValue=false;}
        Integer questionType = null;
        if(inputQuestionType != null){questionType = QuestionType.valueOf(inputQuestionType).ordinal();}

        Pageable pageable = PageRequest.of(page,20);
        return postRepository.findAllFAQList(roleValue,title,questionType, pageable);
    }
    /*faq 상세*/
    @Transactional
    public FAQDto getFAQ(String token, Long postId) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Post faq = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+postId));
        Role targetRole = null;
        if(faq.getTargetRole()==true){targetRole = Role.member;}
        else targetRole=Role.shipowner;
        if(member.getRoles() != Role.admin && member.getRoles()!=targetRole){
            throw new RuntimeException("해당 글에 대한 조회 권한이 없스니다.");
        }

        return postRepository.findFAQDetail(postId);
    }

    /*QnA 리스트 조회*/
    @Transactional(readOnly = true)
    public Page<QnADtoForPage> getQnAList(int page, String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        Pageable pageable = PageRequest.of(page, 20);
        return postRepository.findAllQnAList(member, pageable);
    }

    /*QnA detail 조회*/
    @Transactional(readOnly = true)
    public QnADetailDto getQnADetail(Long postId){
        return postRepository.findQnADetailByPostId(postId);
    }

    /*공지사항 리스트 조회*/
    @Transactional(readOnly = true)
    public Page<NoticeDtoForPage> getNoticeList(int page, String role, String inputChannelType, String inputTitle){
        Boolean roleValue = null;
        if(role.equals("member")){roleValue=true;}
        else if(role.equals("shipowner")){roleValue=false;}
        Integer channelType = null;
        if(inputChannelType != null){channelType = ChannelType.valueOf(inputChannelType).ordinal();}

        Pageable pageable = PageRequest.of(page,20);
        return postRepository.findNoticeList(roleValue, channelType, inputTitle, pageable);
    }

    /*공지사항 상세보기*/
    @Transactional(readOnly = true)
    public NoticeDetailDto getNoticeDetail(Long postId) throws ResourceNotFoundException {
        NoticeDetailDto post = postRepository.findNoticeDetailByPostId(postId);
        if(post == null){throw new RuntimeException("해당 글이 존재하지 않습니다.");}
        else return post;
        /*
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+postId));

        NoticeDetailDto result = NoticeDetailDto.builder()
                .id(post.getId())
                .channelType(post.getChannelType().getValue())
                .title(post.getTitle())
                .date(post.getCreatedDate())
                .contents(post.getContents())
                .build();

        List<FileEntity> fileList = fileRepository.findByPidAndFilePublish(post.getId(), post.getBoard().getFilePublish());
        if(fileList.size()!=0) {
            String fileListString = fileList.get(0).getDownloadUrl();
            for (int i = 1; i < fileList.size(); i++) {
                fileListString += "," + fileList.get(i).getDownloadUrl();
            }
            result.setFileList(fileListString);
        }

        return result;*/
    }

    //Post하나 반환 메소드. FAQ, QNA, 공지사항 리스트 및 하나 조회에 대해 따로따로 위에 만들어놓음.
    /*@Transactional(readOnly = true)
    public PostDTO getPostById(Long id, int filePublish) throws ResourceNotFoundException {
        PostDTO postDTO = null;
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post not found for this id :: " + id));

        *//*파일들 가져오는 부분. *//*
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublish(post.getId(), FilePublish.values()[filePublish]);
        List<String> fileUrlList = new LinkedList<String>();
        for(int i=0; i<fileList.size(); i++){
            fileUrlList.add(fileList.get(i).getDownloadUrl());
        }

        postDTO = new PostDTO(post, fileUrlList);

        return postDTO;
    }*/

    //Post entity저장 및 FileEntity저장.
    /*postDTO에는 boardId, channelType, title, content, authorId, returnType, returnNoAddress, createdAt, tagsName,
    isSecret, parentId가 들어있음.*/
    @Transactional
    public Long writePost(WritePostDTO postDTO, String sessionToken) throws ResourceNotFoundException, IOException {
        //Post 저장 부분.
        Board boardOfPost = boardRepository.findById(postDTO.getBoardId())
                .orElseThrow(()->new ResourceNotFoundException("Board not found for this id :: "+postDTO.getBoardId()));
        Member authorOfPost = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this sessionToken ::"+sessionToken));

        //Post entity의 List<Tag>만듦.
        /*List<Tag> tagList = new LinkedList<Tag>();
        List<String> tagNameList = postDTO.getTagsName();
        for(int i=0; i<tagNameList.size(); i++){
            Tag tempTag = null;
            tempTag = tagRepository.findByTagName(tagNameList.get(i));
            if(tempTag==null) throw new ResourceNotFoundException("Tag not found for this tagName :: "+tagNameList.get(i));
            tagList.add(tempTag);
        }*/

        Post post = Post.builder()
                .modifiedBy(authorOfPost)
                .createdBy(authorOfPost)
                .createdAt(postDTO.getCreatedAt())
                .returnNoAddress(postDTO.getReturnNoAddress())
                .returnType(ReturnType.valueOf(postDTO.getReturnType()))
                .authorName(authorOfPost.getNickName())
                .contents(postDTO.getContents())
                .title(postDTO.getTitle())
                .channelType(ChannelType.valueOf(postDTO.getChannelType()))
                .board(boardOfPost)
                .author(authorOfPost)
                .isSecret(postDTO.getIsSecret())
//                .tags(tagList)
                .questionType(QuestionType.valueOf(postDTO.getQuestionType()))
                .parent_id(postDTO.getParentId())
                .targetRole(postDTO.getTargetRole())
                .isDeleted(false)
                .isReplied(false)
//                .noticeStartDate(postDTO.getNoticeStartDate())
                .noticeEndDate(postDTO.getNoticeEndDate())
                .build();
        post = postRepository.save(post);

        if(postDTO.getFiles() != null) {
            FileEntity[] fileEntityList = new FileEntity[postDTO.getFiles().length];//fileEntity 목록 저장할 변수
            for (int i = 0; i < fileEntityList.length; i++) {
                Long fileEntityId = postDTO.getFiles()[i];
                fileEntityList[i] = fileRepository.findById(fileEntityId)
                        .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + fileEntityId));
            }
            for (int i = 0; i < fileEntityList.length; i++) {
                fileEntityList[i].saveTemporaryFile(post.getId());
            }
        }

        /*//파일 저장.
            //파일 타입 확인.
        for(int i=0; i<files.length; i++ ) {
            MultipartFile file = files[i];

            FileType enumFileType = uploadService.checkFileType(file);

            //UploadService를 이용한 파일저장.
            Map<String, Object> fileInfo
                    = uploadService.initialFile(file, boardOfPost.getFilePublish(), ""*//*, 세션토큰 인자 *//*);

            //File entity 저장.
            FileEntity currentFile = FileEntity.builder()
                    .fileName(file.getOriginalFilename())
                    .storedFile((String) fileInfo.get("fileName"))
//                    .downloadUrl((String) fileInfo.get("fileDownloadUrl"))
                    .thumbnailFile((String) fileInfo.get("thumbnailName"))
//                    .downloadThumbnailUrl((String) fileInfo.get("thumbDownloadUrl"))
                    .originalFile(file.getOriginalFilename())
                    .size(file.getSize())
                    .fileUrl((String) fileInfo.get("path"))
                    .fileType(enumFileType)
                    .createdBy(authorOfPost)
                    .modifiedBy(authorOfPost)
                    .filePublish(boardOfPost.getFilePublish())
                    .locations("sampleLocation")
                    .pid(post.getId())
                    .build();
            fileRepository.save(currentFile).getId();
        }*/

        return post.getId();
    }

    /*1:1문의 글쓰기*/
    @Transactional
    public Long writeOne2one(QnaWriteDto dto, String token) throws ResourceNotFoundException, IOException {
        /*WritePostDTO 생성*/
        Board board = boardRepository.findBoardByFilePublish(FilePublish.one2one);
        Boolean targetRole;
        if(dto.getTargetRole().equals("member")){targetRole=true;}
        else targetRole=false;

        WritePostDTO writePostDTO = WritePostDTO.builder()
                /*QnaWriteDto안에 있는것들*/
                .questionType(dto.getQuestionType())
                .contents(dto.getContents())
                .returnType(dto.getReturnType())
                .returnNoAddress(dto.getReturnAddress())
                .files(dto.getFileList())
                /*QnaWriteDto안에 없는것들*/
                .boardId(board.getId())
                .channelType("general")
                .title("noTitle")
                .targetRole(targetRole)
                .createdAt("sampleCreatedAt")
                .build();

        /*writePost()호출*/
        return writePost(writePostDTO, token);
    }
    /*FAQ 생성*/
    @Transactional
    public Long writeFaq(FaqWriteDto dto, String token) throws ResourceNotFoundException, IOException {
        /*WritePostDTO 생성*/
        Board board = boardRepository.findBoardByFilePublish(FilePublish.faq);
        Boolean targetRole;
        if(dto.getTargetRole().equals("member")){targetRole=true;}
        else targetRole=false;
        WritePostDTO writePostDTO = WritePostDTO.builder()
                /*FaqWriteDto안에 있는것들*/
                .questionType(dto.getQuestionType())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .files(dto.getFileList())
                /*FaqWriteDto안에 없는것들*/
                .boardId(board.getId())
                .channelType("general")
                .returnType("email")
                .returnNoAddress("sampleReturnAddress")
                .createdAt("sampleCreatedAt")
                .targetRole(targetRole)
                .build();

        /*writePost() 호출*/
        return writePost(writePostDTO,token);
    }
    /*공지사항 생성*/
    @Transactional
    public Long writeNotice(NoticeWriteDto dto, String token) throws ResourceNotFoundException, IOException {


        /*WritePostDTO 생성*/
        Board board = boardRepository.findBoardByFilePublish(FilePublish.notice);
        Boolean targetRole;
        if(dto.getTargetRole().equals("member")){targetRole=true;}
        else targetRole=false;
        WritePostDTO writePostDTO = WritePostDTO.builder()
                /*FaqWriteDto안에 있는것들*/
                .channelType(dto.getChannelType())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .files(dto.getFileList())
                /*FaqWriteDto안에 없는것들*/
                .boardId(board.getId())
                .questionType("order")
                .returnType("email")
                .returnNoAddress("sampleReturnAddress")
                .createdAt("sampleCreatedAt")
                .targetRole(targetRole)
//                .noticeStartDate(dto.getNoticeStartDate())
                .noticeEndDate(dto.getNoticeEndDate())
                .build();

        /*writePost() 호출*/
        return writePost(writePostDTO,token);
    }

    /*Post 수정 및 해당Post의 기존 File들 삭제, 넘어온 파일 다시올리기, FileEntity 저장.
     * */
    @Transactional
    public Long updatePost(UpdatePostDTO postDTO,
                           String sessionToken
    ) throws ResourceNotFoundException, IOException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()-> new ResourceNotFoundException("member not found for this sessionToken :: "+sessionToken));
        FileEntity[] fileEntityList = new FileEntity[postDTO.getFiles().length];//fileEntity 목록 저장할 변수
        for(int i=0; i<fileEntityList.length; i++){
            Long fileEntityId = postDTO.getFiles()[i];
            fileEntityList[i] = fileRepository.findById(fileEntityId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+fileEntityId));
        }

        //Post entity 수정.
        Post post = postRepository.findById(postDTO.getPostId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+postDTO.getPostId()));

        post.updatePost(postDTO);
        postRepository.save(post);

        /*Post에 올려놓은 File들 삭제하고 다시 올림. */
        /*파일들 모두 삭제.*/
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublishAndIsDelete(post.getId(), post.getBoard().getFilePublish(),false);
        for(int i=0; i<fileList.size(); i++){
            uploadService.removeFileEntity(fileList.get(i).getId());
        }

        for(int i=0; i<fileEntityList.length; i++){
            fileEntityList[i].saveTemporaryFile(post.getId());
        }

        /*for(int i=0; i<files.length; i++ ) {
            MultipartFile file = files[i];
            FileType enumFileType = uploadService.checkFileType(file);

            //UploadService를 이용한 파일저장.
            Map<String, Object> fileInfo = uploadService.initialFile(file, post.getBoard().getFilePublish(), ""*//*, 세션토큰 인자 *//*);

            //File entity 저장.
            FileEntity currentFile = FileEntity.builder()
                    .fileName(file.getOriginalFilename())
                    .storedFile((String) fileInfo.get("fileName"))
//                    .downloadUrl((String) fileInfo.get("fileDownloadUrl"))
                    .thumbnailFile((String) fileInfo.get("thumbnailName"))
//                    .downloadThumbnailUrl((String) fileInfo.get("thumbDownloadUrl"))
                    .originalFile(file.getOriginalFilename())
                    .size(file.getSize())
                    .fileUrl((String) fileInfo.get("path"))
                    .fileType(enumFileType)
                    .createdBy(post.getModifiedBy())
                    .modifiedBy(post.getModifiedBy())
                    .filePublish(post.getBoard().getFilePublish())
                    .locations("sampleLocation")
                    .pid(post.getId())
                    .build();
            fileRepository.save(currentFile).getId();
        }*/

        return post.getId();
    }
    /*1:1문의 수정*/
    @Transactional
    public Long updateOne2one(QnaUpdateDto dto, String token) throws ResourceNotFoundException, IOException {
        Member member = memberService.getMemberBySessionToken(token);
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+dto.getPostId()));
        if(post.getAuthor() != member){throw new RuntimeException("수정 권한이 없습니다.");}

        /*UpdatePostDTO생성*/
        Board board = boardRepository.findBoardByFilePublish(FilePublish.one2one);
        Boolean targetRole;
        if(dto.getTargetRole().equals("member")){targetRole=true;}
        else targetRole=false;
        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                /*dto에 있는 필드들*/
                .postId(dto.getPostId())
                .questionType(dto.getQuestionType())
                .contents(dto.getContents())
                .returnType(dto.getReturnType())
                .returnNoAddress(dto.getReturnAddress())
                .files(dto.getFileList())
                /*dto에 없는 필드들*/
                .boardId(board.getId())
                .channelType("general")
                .title("noTitle")
                .createdAt("sampleCreatedAt")
                .targetRole(targetRole)
                .build();
        /*updatePost()호출*/
        return updatePost(updatePostDTO, token);
    }
    /*faq 수정*/
    @Transactional
    public Long updateFaq(FaqUpdateDto dto, String token) throws ResourceNotFoundException, IOException {
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() != Role.admin){throw new RuntimeException("수정 권한이 없습니다.");}

        /*UpdatePostDTO생성*/
        Board board = boardRepository.findBoardByFilePublish(FilePublish.faq);
        Boolean targetRole;
        if(dto.getTargetRole().equals("member")){targetRole=true;}
        else targetRole=false;

        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                /*dto에 있는 필드들*/
                .postId(dto.getPostId())
                .questionType(dto.getQuestionType())
                .contents(dto.getContents())
                .title(dto.getTitle())
                .files(dto.getFileList())
                /*dto에 없는 필드들*/
                .boardId(board.getId())
                .returnType("email")
                .returnNoAddress("sampleReturnAddress")
                .channelType("general")
                .createdAt("sampleCreatedAt")
                .targetRole(targetRole)
                .build();
        /*updatePost()호출*/
        return updatePost(updatePostDTO, token);
    }
    /*공지사항 수정*/
    @Transactional
    public Long updateNotice(NoticeUpdateDto dto, String token) throws ResourceNotFoundException, IOException {
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() != Role.admin){throw new RuntimeException("수정 권한이 없습니다.");}

        /*UpdatePostDTO생성*/
        Board board = boardRepository.findBoardByFilePublish(FilePublish.notice);
        Boolean targetRole;
        if(dto.getTargetRole().equals("member")){targetRole=true;}
        else targetRole=false;
        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                /*dto에 있는 필드들*/
                .postId(dto.getPostId())
                .channelType(dto.getChannelType())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .files(dto.getFileList())
                /*dto에 없는 필드들*/
                .boardId(board.getId())
                .returnType("email")
                .returnNoAddress("sampleReturnAddress")
                .questionType("order")
                .createdAt("sampleCreatedAt")
                .targetRole(targetRole)
                .noticeEndDate(dto.getNoticeEndDate())
                .build();
        /*updatePost()호출*/
        return updatePost(updatePostDTO, token);
    }

    /*공지사항, FAQ, 1:1문의 삭제*/
    @Transactional
    public boolean deletePost(DeletePostDto dto, String token) throws ResourceNotFoundException {
        /*토큰에 해당하는 회원 확인*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Role role = member.getRoles();
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(()->new ResourceNotFoundException("post not found for this id :: "+dto.getPostId()));

        /*현재 회원이 관리자거나 현재회원이 작성한 글이면 삭제가능. */
        if(role == Role.admin || post.getAuthor() == member){
            post.delete();
            return true;
        }
        /*아니면 삭제 불가*/
        else {
            throw new RuntimeException("삭제할 수 없는 게시글 입니다.");
        }
    }
}
