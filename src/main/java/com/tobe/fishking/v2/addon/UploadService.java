package com.tobe.fishking.v2.addon;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.Constants;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.FileImageOnlyException;
import com.tobe.fishking.v2.exception.FileUploadFailException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.board.BoardService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UploadService {
    private Environment env;
    private FileRepository fileRepository;
    private PostRepository postRepository;
    private BoardRepository boardRepo;

//    private AmazonS3Client amazonS3Client;

    private BoardService boardService;

    private MemberRepository memberRepository;

    /*파일 업로드 미리보기를 위한 파일 저장 메소드.
     * - FileEntity의 isDelete필드를 true로 둔상태로 생성.
     * - 생성한 FileEntity를 반환. */
    public FileEntity filePreUpload(MultipartFile file, FilePublish filePublish, String sessionToken) throws IOException, ResourceNotFoundException, JCodecException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken :: "+sessionToken));

        Map<String, Object> fileInfo = initialFile(file, filePublish, sessionToken);

        FileEntity fileEntity = FileEntity.builder()
                .filePublish(filePublish)
                .fileNo(1)
                .fileType(checkFileType(file))
                .originalFile(file.getOriginalFilename())
                .storedFile((String)fileInfo.get("fileName"))
                .thumbnailFile((String)fileInfo.get("thumbnailName"))
                .fileUrl((String)fileInfo.get("path"))
                .size(file.getSize())
                .createdBy(member)
                .modifiedBy(member)
                .locations("sampleLocation")
                .isDelete(true)
                .build();

        return fileRepository.save(fileEntity);
    }

    /*파일 타입 구하는 메소드 */
    public FileType checkFileType(MultipartFile file){
        String fileType = file.getContentType().split("/")[0];
        FileType enumFileType;
        if(fileType.equals("image")){enumFileType = FileType.image;}
        else if(fileType.equals("text")){enumFileType = FileType.txt;}
        else if(fileType.equals("video")){enumFileType = FileType.video;}
        else enumFileType = FileType.attachments;

        return enumFileType;
    }

    /*파일 저장 메소드
     * 인자 enum filePublish의 이름과 똑같은 폴더가 파일저장위치에 존재하며 이곳에 파일을 저장한다.
     * 가령, FilePublish.one2one일경우, application.yml에 설정된 파일저장위치 'file.location'에 나와있는위치에
     *  enum FilePublish이름인 one2one이라는 폴더가 존재한다. 여기에 저장하도록 메소드가 동작한다.
     * 반환 ) 섬네일파일명, 원본파일의 풀url, 원본파일의 저장명을 반환한다. */
    @Transactional
    public Map<String, Object> initialFile(MultipartFile file, /*FileType fileType,*/
                                           FilePublish filePublish, String sessionToken)
            throws IOException, ResourceNotFoundException, JCodecException {
        Map<String, Object> result = new HashMap<>();

            /*!!!!!테스트용 임의 주석처리 by 석호. (sessionToken같은 시큐리티? 아직 적용안함)
            Member member = memberService.getMemberBySessionToken(sessionToken);
            String memberId = member.getUid();*/

        /*!!!!!임의 Member 사용. by 석호. 나중에 지워야함. */
        Member member = memberRepository.findById(8L)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+8L));

            /*String fileLocation = this.getFileLocation(desireFileLocation);
            String filePath = this.getFilePath(desireFileLocation);
            */

        Board board  = boardRepo.findBoardByFilePublish(filePublish);

        String fileLocation = env.getProperty("file.location") + File.separator + board.getUploadPath();/*filePublish.toString();*/
        String filePath = board.getUploadPath();

            /* 파일 타입에 대해 체크해주는 부분인듯하다. 처음부터있었던 로직인데 당장필요없을듯하여 일단 주석처리. by석호
            FileType currentFileType = FileType.image;
            if (!file.getContentType().startsWith("image/")) {
                currentFileType = FileType.attachments;
            }
            if (fileType.equals(FileType.image) && !currentFileType.equals(fileType)) {
                throw new FileImageOnlyException(fileType.toString());
            }*/

        List<String> fileResult = new ArrayList<>();
        fileResult = this.saveUploadFile(file, /*memberId*/"seok ho", fileLocation, filePath, true);
        /*위에 memberId인자 주석풀어줘야함. by 석호. */
        if (fileResult.isEmpty()) {
            throw new FileUploadFailException();
        }
        String fileName = fileResult.get(0);//새로만든 저장파일명.
        String fileUrl = fileResult.get(1);//uploadFilePath
        String fileUploadUrl = "/"+board.getUploadPath()+"/"+fileName;
        String thumbnailName = fileResult.get(2);//thumbnailFileName
        String thumbnailPath = fileResult.get(3);//섬네일 path
//            String thumbnailUploadUrl = "/"+
        String fileDownloadUrl = "/"+board.getDownloadPath()+"/"+fileName;
        String thumbDownloadUrl = "/"+board.getDownloadPath()+"/"+thumbnailName;

        result.clear();
        result.put("fileName", fileName);//파일명.
        result.put("thumbnailName",thumbnailName);//섬네일파일명.
        result.put("path",board.getDownloadPath());//경로.

        result.put("thumbUploadPath", thumbnailPath);
        result.put("fileUrl", fileUploadUrl);
        result.put("fileDownloadUrl", fileDownloadUrl);
        result.put("thumbDownloadUrl", thumbDownloadUrl);


        //result.put("tempFileSeq", fileId);
        return result;
    }

    /*public Map<String, Object> initialFile(MultipartFile file, FileType fileType, FilePublish filePublish) throws IOException, JCodecException {
        Map<String, Object> result = new HashMap<>();

        String fileLocation = "/data/file/user";




        Board board  = boardService.getBoardByFilePublish(filePublish);


        String filePath = board.getUploadPath();


        FileType currentFileType = FileType.image;
        if (!file.getContentType().startsWith("image/")) {
            currentFileType = FileType.attachments;
        }
        if (fileType.equals(FileType.image) && !currentFileType.equals(fileType)) {
            throw new FileImageOnlyException(fileType.toString());
        }

        List<String> fileResult = new ArrayList<>();
        fileResult = this.saveUploadFile(file, "signup", fileLocation, filePath, true);

        if (fileResult.isEmpty()) {
            throw new FileUploadFailException();
        }

        String fileUrl = fileResult.get(0);
        String fileThumbnail = fileResult.get(1);

        FileEntity currentFile = FileEntity.builder().fileName(file.getOriginalFilename()).size(file.getSize()).fileUrl(fileUrl).thumbnailFile(fileThumbnail).fileType(fileType).createdBy(null).build();
        long fileId = fileRepository.save(currentFile).getId();
        result.clear();
        result.put("thumb", currentFile.getThumbnailFile());
        result.put("fileUrl", currentFile.getFileUrl());
        result.put("fileName", currentFile.getFileName());
        result.put("tempFileSeq", fileId);
        return result;
    }
*/
    /*파일을 저장해주는 메소드.
        반환 ) ArrayList<String>을 반환. 크기는3이고 인덱스 순서대로 새로만든 저장될 파일명, 파일 업로드 path, 섬네일파일 업로드path.
     */
    public List<String> saveUploadFile(MultipartFile file,
                                       String userID,
                                       String fileLocation,
                                       String filePath,
                                       boolean thumbnail) throws IOException, JCodecException {
        Map<String, Object> fileData = new HashMap<String, Object>();
        List<String> fileNames = new ArrayList<>();

        //저장할 파일 이름 만들기. id,시간,확장자 조합하여 만듦.
        String fileName = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSS", Locale.KOREA);
        String tag = "_" + sdf.format(new Date(System.currentTimeMillis()));
        fileName = userID + tag + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        fileNames.add(fileName);

        //프로퍼티에 저장된 파일저장위치를 붙여 업로드할 하드상의 위치 저장.
        String uploadFilePath = fileLocation + File.separator + fileName;

        //  String bucket = env.getProperty("cloud.aws.s3.bucket");


        File uploadFile = convert(file, fileLocation, fileName).orElseThrow(FileUploadFailException::new);

        /*파일 크기가 4MB이상일 경우 용량줄이기. */
        uploadFile = resize(uploadFile, fileLocation, fileName);

    /*    amazonS3Client.putObject(new PutObjectRequest(
                bucket, uploadFilePath, uploadFile
        ).withCannedAcl(CannedAccessControlList.PublicRead));
*/

        //fileNames.add(amazonS3Client.getUrl(bucket, uploadFilePath).toString());

        fileNames.add(uploadFilePath);

        if (thumbnail && file.getContentType().startsWith("image/")) {
            File thumbs = makeThumbnail(uploadFile, fileLocation, fileName)
                    .orElseThrow(FileUploadFailException::new);

/*
            amazonS3Client.putObject(new PutObjectRequest(
                    bucket, fileLocation.substring(1) + "/" + thumbs.getName(), thumbs
            ).withCannedAcl(CannedAccessControlList.PublicRead));

            fileNames.add(amazonS3Client.getUrl(bucket, fileLocation.substring(1) + "/" + thumbs.getName()).toString());
*/
            fileNames.add(thumbs.getName());
            fileNames.add(thumbs.getPath());

            //removeNewFile(thumbs);
        }
        else if(thumbnail && file.getContentType().startsWith("video/")){
            int frameNumber = 0;
            Picture picture = FrameGrab.getFrameFromFile(uploadFile, frameNumber);
            fileName = fileName.replace(FilenameUtils.getExtension(fileName),".png");
            String thumbnailFileName = fileLocation + File.separator + "thumb_" + fileName;
            File thumbs = new File(thumbnailFileName);
            //for JDK (jcodec-javase)
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", thumbs);

            fileNames.add(thumbs.getName());
            fileNames.add(thumbs.getPath());
        }
        else {
            fileNames.add("");
        }
        //removeNewFile(uploadFile);
        return fileNames;
    }

    /*파일 리사이징
     * 1MB이하가 될때까지 반복문으로 이미지 퀄리티를 떨어뜨려 용량을 줄인다. */
    public File resize(File originalFile, String fileLocation, String fileName) throws IOException {
        long fileSize = originalFile.length();
        for(; fileSize > 1*1024*1024; ){
            BufferedImage image = ImageIO.read(originalFile);

            File output = originalFile;
            OutputStream out = new FileOutputStream(output);

            ImageWriter writer =  ImageIO.getImageWritersByFormatName("jpg").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()){
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(1f);
                String[] aaa = param.getCompressionTypes();
                String bbb = param.getCompressionType();
                aaa.toString();
            }

            writer.write(null, new IIOImage(image, null, null), param);

            out.close();
            ios.close();
            writer.dispose();

            //압축하여 줄은 용량이 2MB이하라면 더이상 jpeg압축이 안된다판단하고 반복종료.
            if(fileSize - output.length() < 2*1024*1024){ fileSize = output.length(); break;}

            fileSize = output.length();
        }

        //파일 크기 줄이기. 압축과 마찬가지로 1MB이하가 될때까지 줄임.
        try {
            for(;fileSize > 1*1024*1024;) {
                Image image = ImageIO.read(originalFile);
                double scale = (double) 6 / (double) 10;

                int scaledW = (int) (scale * image.getWidth(null));
                int scaledH = (int) (scale * image.getHeight(null));

                BufferedImage oBufferImage = ImageIO.read(originalFile);
                BufferedImage tBufferImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D graphic = tBufferImage.createGraphics();
                graphic.drawImage(oBufferImage, 0, 0, scaledW, scaledH, null);
                ImageIO.write(tBufferImage, "jpg", originalFile);

                fileSize = originalFile.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return originalFile;
    }


    private Optional<File> makeThumbnail(File originalFile, String fileLocation, String fileName) {
        String tFileName = "";

        try {

            tFileName = fileLocation + File.separator + "thumb_" + fileName;

            int width = Integer.parseInt(Constants.thumbnailWidth);
            int height = Integer.parseInt(Constants.thumbnailHeight);

            /*Image inImage = ImageIO.read(originalFile);
            double scale = (double) width / (double) inImage.getWidth(null);
            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));*/

            Image inImage = new ImageIcon(tFileName).getImage();
            double scale = (double) width / (double) inImage.getHeight(null);
            if (inImage.getWidth(null) > inImage.getHeight(null)) {
                scale = (double) height / (double) inImage.getWidth(null);
            }
            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));

            File tFile = new File(tFileName);

            BufferedImage oBufferImage = ImageIO.read(originalFile);
            BufferedImage tBufferImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphic = tBufferImage.createGraphics();
            graphic.drawImage(oBufferImage, 0, 0, scaledW, scaledH, null);
            ImageIO.write(tBufferImage, "jpg", tFile);

            return Optional.of(tFile);
        } catch (Exception e) {
            e.printStackTrace();
            tFileName = "";
        }
        return Optional.empty();
    }

    public Map<String, Object> uploadResponse(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        if ((request instanceof MultipartHttpServletRequest)) {
            try {
                ObjectMapper mapper = new ObjectMapper();

                response.setContentType("text/html");
                response.getOutputStream().write(mapper.writeValueAsBytes(map));
                response.flushBuffer();

            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        } else {
            return map;
        }
    }

    @Transactional
    public void removeFileEntity(Long fileEntityId) throws ResourceNotFoundException {
        FileEntity fileEntity = fileRepository.findById(fileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("files not found for this id ::"+fileEntityId));

        String fileUrl = fileEntity.getFileUrl();
        String fileName = fileEntity.getStoredFile();
        String thumbnailName = fileEntity.getThumbnailFile();

        File file = new File(env.getProperty("file.location")+File.separator+fileUrl+File.separator+fileName);
        File thumbnailFile = new File(env.getProperty("file.location")+File.separator+fileUrl+File.separator+thumbnailName);
        removeFile(file);
        removeFile(thumbnailFile);

        fileRepository.delete(fileEntity);
    }
    public void removeFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("파일이 삭제되었습니다.");
        } else {
            System.out.println("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file, String fileLocation, String fileName) throws IOException {
        File convertFile = new File(fileLocation + File.separator + fileName);

        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }


}
