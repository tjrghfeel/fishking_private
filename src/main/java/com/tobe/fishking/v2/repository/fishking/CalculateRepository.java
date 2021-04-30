package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Calculate;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.admin.CalculateManageDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CalculateRepository extends BaseRepository<Calculate, Long>, CalculateRepositoryCustom {

    @Query(value = "" +
            "select v.*  " +
            "from " +
            "   (select " +
//            "   c.id calculateId, " +
            "      s.id shipId, " +
            "      s.ship_name shipName, " +
            "      cp.id companyId, " +
            "      cp.company_name companyName, " +
            "      c.year year, " +
            "      c.month month, " +
            "      c.modified_by_id modifiedById, " +
            "      sum( if(c.is_cancel=true, c.amount, 0) ) cancelAmount, " +
            "      sum( if(c.is_cancel=false, c.amount, 0) ) amount, " +
            "      sum( if(c.is_cancel=true, (-1*c.amount), c.amount)) totalAmount, " +
            "      if( sum(c.is_calculate) = count(c.id), true, false) isCalculated " +
            "   from ship s join company cp on s.company_id = cp.id left join calculate c on c.ship = s.id " +
            "     " +
            "   group by s.id, c.year, c.month " +
            "   order by c.year desc, c.month desc) v " +
            "where " +
            "   if(:shipName is null, true, v.shipName like %:shipName%) " +
            "   and if(:companyName is null, true, v.companyName like %:companyName%) " +
            "   and if(:yearStart is null, true, v.year >= :yearStart) " +
            "   and if(:yearEnd is null, true, v.year <= :yearEnd) " +
            "   and if(:monthStart is null, true, v.month >= :monthStart) " +
            "   and if(:monthEnd is null, true, v.month <= :monthEnd) " +
            "   and if(:totalAmountStart is null, true, v.totalAmount >= :totalAmountStart) " +
            "   and if(:totalAmountEnd is null, true, v.totalAmount <= :totalAmountEnd) " +
            "   and if(:isCalculated is null, true, v.isCalculated = :isCalculated) ",
            countQuery = "" +
                    "select v.* " +
                    "from " +
                    "   (select " +
//            "   c.id calculateId, " +
                    "      s.id shipId, " +
                    "      s.ship_name shipName, " +
                    "      cp.id companyId, " +
                    "      cp.company_name companyName, " +
                    "      c.year year, " +
                    "      c.month month, " +
                    "      c.modified_by_id modifiedById, " +
                    "      sum( if(c.is_cancel=true, c.amount, 0) ) cancelAmount, " +
                    "      sum( if(c.is_cancel=false, c.amount, 0) ) amount, " +
                    "      sum( if(c.is_cancel=true, (-1*c.amount), c.amount)) totalAmount, " +
                    "      if( sum(c.is_calculate) = count(c.id), true, false) isCalculated " +
                    "   from ship s join company cp on s.company_id = cp.id left join calculate c on c.ship = s.id " +
                    "   group by s.id, c.year, c.month " +
                    "   order by c.year desc, c.month desc) v " +
                    "where " +
                    "   if(:shipName is null, true, v.shipName like %:shipName%) " +
                    "   and if(:companyName is null, true, v.companyName like %:companyName%) " +
                    "   and if(:yearStart is null, true, v.year >= :yearStart) " +
                    "   and if(:yearEnd is null, true, v.year <= :yearEnd) " +
                    "   and if(:monthStart is null, true, v.month >= :monthStart) " +
                    "   and if(:monthEnd is null, true, v.month <= :monthEnd) " +
                    "   and if(:totalAmountStart is null, true, v.totalAmount >= :totalAmountStart) " +
                    "   and if(:totalAmountEnd is null, true, v.totalAmount <= :totalAmountEnd) " +
                    "   and if(:isCalculated is null, true, v.isCalculated = :isCalculated) ",
            nativeQuery = true
    )
    Page<CalculateManageDtoForPage> getCalculateList(
            @Param("shipName") String shipName,  @Param("companyName") String companyName, @Param("yearStart") String yearStart,
            @Param("yearEnd") String yearEnd, @Param("monthStart") String monthStart, @Param("monthEnd")String monthEnd,
            @Param("totalAmountStart") Long totalAmountStart, @Param("totalAmountEnd") Long totalAmountEnd,
            @Param("isCalculated") Boolean isCalculated,
            Pageable pageable
    );

    List<Calculate> findAllByShipAndYearAndMonth(Ship ship, String year, String month);


    @Query(value = "" +
            "select v.*  " +
            "from " +
            "   (select " +
            "      s.ship_name shipName, " +
            "      cp.company_name companyName, " +
            "      concat(ifnull(c.year, ''), '-', ifnull(c.month, '')) yearmonth, " +
            "      sum( if(c.is_cancel=true, c.amount, 0) ) cancelAmount, " +
            "      sum( if(c.is_cancel=false, c.amount, 0) ) amount, " +
            "      sum( if(c.is_cancel=true, (-1*c.amount), c.amount)) totalAmount, " +
            "      if( sum(c.is_calculate) = count(c.id), '완료', '대기') isCalculated " +
            "   from ship s join company cp on s.company_id = cp.id left join calculate c on c.ship = s.id " +
            "     " +
            "   group by s.id, c.year, c.month " +
            "   order by c.year desc, c.month desc) v " +
            "where " +
            "   if(:shipName is null, true, v.shipName like %:shipName%) " +
            "   and if(:companyName is null, true, v.companyName like %:companyName%) " +
            "   and if(:yearStart is null, true, v.year >= :yearStart) " +
            "   and if(:yearEnd is null, true, v.year <= :yearEnd) " +
            "   and if(:monthStart is null, true, v.month >= :monthStart) " +
            "   and if(:monthEnd is null, true, v.month <= :monthEnd) " +
            "   and if(:totalAmountStart is null, true, v.totalAmount >= :totalAmountStart) " +
            "   and if(:totalAmountEnd is null, true, v.totalAmount <= :totalAmountEnd) " +
            "   and if(:isCalculated is null, true, v.isCalculated = :isCalculated) ",
            nativeQuery = true
    )
    List<Map<String, String>> getCalculateListForExcel(
            @Param("shipName") String shipName,  @Param("companyName") String companyName, @Param("yearStart") String yearStart,
            @Param("yearEnd") String yearEnd, @Param("monthStart") String monthStart, @Param("monthEnd")String monthEnd,
            @Param("totalAmountStart") Long totalAmountStart, @Param("totalAmountEnd") Long totalAmountEnd,
            @Param("isCalculated") Boolean isCalculated
    );
}
