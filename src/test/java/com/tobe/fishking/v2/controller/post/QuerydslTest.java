package com.tobe.fishking.v2.controller.post;

import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.repository.board.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerydslTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void querydslCustomTest() {
        //given
        String title = "안녕";

        //when
        List<Post> result = postRepository.findByTitle(title);

        //then
        assertThat(result.size(), is(3));
    }

}
