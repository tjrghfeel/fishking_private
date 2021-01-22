package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class HelloService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    Environment env;
    @Autowired
    UploadService uploadService;

    public void noName(MultipartFile file) throws ResourceNotFoundException, IOException, JCodecException {
        uploadService.initialFile(file, FilePublish.fishingBlog, "19");
        return;
    }
}
