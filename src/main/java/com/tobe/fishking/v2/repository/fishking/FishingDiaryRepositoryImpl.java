package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.model.board.FishingDiaryMainResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.fishing.QFishingDiary.fishingDiary;

@RequiredArgsConstructor
public class FishingDiaryRepositoryImpl implements FishingDiaryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FishingDiary> getDiaryByShipId(Long ship_id) {
        List<FishingDiary> results = queryFactory
                .selectFrom(fishingDiary)
                .where(fishingDiary.ship.id.eq(ship_id), fishingDiary.filePublish.eq(FilePublish.fishingDiary))
                .orderBy(fishingDiary.createdDate.desc())
                .fetch();
        return results;
    }

    @Override
    public List<FishingDiary> getBlogByShipId(Long ship_id) {
        List<FishingDiary> results = queryFactory
                .selectFrom(fishingDiary)
                .where(fishingDiary.ship.id.eq(ship_id), fishingDiary.filePublish.eq(FilePublish.fishingBlog))
                .orderBy(fishingDiary.createdDate.desc())
                .fetch();
        return results;
    }

    @Override
    public List<FishingDiaryMainResponse> getMainDiaries() {
        List<FishingDiaryMainResponse> results = queryFactory
                .select(Projections.constructor(FishingDiaryMainResponse.class,
                        fishingDiary
                ))
                .from(fishingDiary)
                .where(fishingDiary.filePublish.eq(FilePublish.fishingDiary))
                .orderBy(fishingDiary.createdDate.desc())
                .limit(10)
                .fetch();
        return results;
    }

}
