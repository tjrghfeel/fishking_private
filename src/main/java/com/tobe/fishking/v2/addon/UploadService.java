package com.tobe.fishking.v2.addon;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.enums.Constants;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.FileImageOnlyException;
import com.tobe.fishking.v2.exception.FileUploadFailException;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.board.BoardService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

@AllArgsConstructor
@Service
public class UploadService {
    private Environment env;
    private MemberService memberService;
    private FileRepository fileRepository;

//    private AmazonS3Client amazonS3Client;

    private BoardService boardService;


    public Map<String, Object> initialFile(MultipartFile file, FileType fileType, FilePublish filePublish, String sessionToken) throws IOException {
        Map<String, Object> result = new HashMap<>();

        Member member = memberService.getMemberBySessionToken(sessionToken);
        String memberId = member.getUid();

            /*String fileLocation = this.getFileLocation(desireFileLocation);
            String filePath = this.getFilePath(desireFileLocation);
            */

        Board board = boardService.getBoardByFilePublish(filePublish);

        String fileLocation = env.getProperty("file.location") + board.getUploadPath();
        String filePath = board.getUploadPath();


        FileType currentFileType = FileType.image;
        if (!file.getContentType().startsWith("image/")) {
            currentFileType = FileType.attachments;
        }
        if (fileType.equals(FileType.image) && !currentFileType.equals(fileType)) {
            throw new FileImageOnlyException(fileType.toString());
        }

        List<String> fileResult = new ArrayList<>();
        fileResult = this.saveUploadFile(file, memberId, fileLocation, filePath, true);

        if (fileResult.isEmpty()) {
            throw new FileUploadFailException();
        }

        String fileUrl = fileResult.get(0);
        String fileThumbnail = fileResult.get(1);

        FileEntity currentFile = FileEntity.builder().fileName(file.getOriginalFilename()).size(file.getSize()).fileUrl(fileUrl).thumbnailFile(fileThumbnail).fileType(fileType).createdBy(member).build();

        long fileId = fileRepository.save(currentFile).getId();

        result.clear();
        result.put("thumb", currentFile.getThumbnailFile());
        result.put("fileUrl", currentFile.getFileUrl());
        result.put("fileName", currentFile.getFileName());
        result.put("tempFileSeq", fileId);
        return result;
    }

    public Map<String, Object> initialFile(MultipartFile file, FileType fileType, FilePublish filePublish) throws IOException {
        Map<String, Object> result = new HashMap<>();

        String fileLocation = "/data/file/user";


        Board board = boardService.getBoardByFilePublish(filePublish);


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


    public List<String> saveUploadFile(MultipartFile file,
                                       String userID,
                                       String fileLocation,
                                       String filePath,
                                       boolean thumbnail) throws IOException {
        Map<String, Object> fileData = new HashMap<String, Object>();
        List<String> fileNames = new ArrayList<>();

        String fileName = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSS", Locale.KOREA);
        String tag = "_" + sdf.format(new Date(System.currentTimeMillis()));
        fileName = userID + tag + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        String uploadFilePath = fileLocation.substring(1) + "/" + fileName;

        //  String bucket = env.getProperty("cloud.aws.s3.bucket");


        File uploadFile = convert(file, fileName).orElseThrow(FileUploadFailException::new);

    /*    amazonS3Client.putObject(new PutObjectRequest(
                bucket, uploadFilePath, uploadFile
        ).withCannedAcl(CannedAccessControlList.PublicRead));
*/


        //fileNames.add(amazonS3Client.getUrl(bucket, uploadFilePath).toString());

        fileNames.add(uploadFilePath);

        if (thumbnail && file.getContentType().startsWith("image/")) {
            File thumbs = makeThumbnail(uploadFile, fileName)
                    .orElseThrow(FileUploadFailException::new);

/*
            amazonS3Client.putObject(new PutObjectRequest(
                    bucket, fileLocation.substring(1) + "/" + thumbs.getName(), thumbs
            ).withCannedAcl(CannedAccessControlList.PublicRead));

            fileNames.add(amazonS3Client.getUrl(bucket, fileLocation.substring(1) + "/" + thumbs.getName()).toString());
*/
            fileNames.add(fileLocation.substring(1) + "/" + thumbs.getName());


            removeNewFile(thumbs);
        } else {
            fileNames.add("");
        }
        removeNewFile(uploadFile);
        return fileNames;
    }


    private Optional<File> makeThumbnail(File originalFile, String fileName) {
        String tFileName = "";

        try {
            tFileName = "thumb_" + fileName;

            int width = Integer.parseInt(Constants.thumbnailWidth);
            int height = Integer.parseInt(Constants.thumbnailHeight);

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

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("파일이 삭제되었습니다.");
        } else {
            System.out.println("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file, String fileName) throws IOException {
        File convertFile = new File(fileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
