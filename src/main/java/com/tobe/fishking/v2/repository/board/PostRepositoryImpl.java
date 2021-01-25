package com.tobe.fishking.v2.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.board.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.board.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findByTitle(String title) {
        return queryFactory.selectFrom(post)
                .where(post.title.contains(title))
                .fetch();
    }
}
