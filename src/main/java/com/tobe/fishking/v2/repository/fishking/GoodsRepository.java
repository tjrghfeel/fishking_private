package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GoodsRepository extends BaseRepository<Goods, Long> {

    //--선상/갯바위 별 조회
    List<Goods> findAllByFishingType(FishingType fishingType);

    @Query("select g.fishSpecies, count(g.id) as cnt from Goods g group by g.fishSpecies")
    List<Object[]> countTotalGoodsByFishSpecies();


    @Query("select s.seaDirection, count(g.id) as cnt from Goods as g inner join Ship as s on s.id  = g.ship.id   group by s.seaDirection")
    List<Object[]> countTotalGoodsByRegion();

    /*주어진 FishingType에 해당하는 Goods를 주어진 Member가 몇개나 찜했는지 쿼리. */
    @Query("select count(g) " +
            "from Goods as g " +
            "where g.fishingType = :fishingType and g.id in (select t.linkId from Take as t where t.createdBy = :member and t.takeType = 0)")
    int findTakeCountAboutFishType(Member member, FishingType fishingType);

    /*인자 Member가 찜한 인자 FishingType에 해당하는 Goods 목록을 쿼리. */
    @Query()
    List<TakeResponse> findTakeListAboutFishType(Member member, FishingType fishingType);

}
