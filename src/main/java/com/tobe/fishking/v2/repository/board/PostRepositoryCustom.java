package com.tobe.fishking.v2.repository.board;

import com.tobe.fishking.v2.entity.board.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findByTitle(String title);
}
