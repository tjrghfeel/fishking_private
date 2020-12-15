package com.tobe.fishking.v2;

import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class IntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
}
