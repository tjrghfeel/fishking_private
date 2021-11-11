package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.EntryExitReport;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntryExitReportRepository extends BaseRepository<EntryExitReport, Long> {
    @Query("select e from EntryExitReport e where e.goods = :goods and e.date = :date")
    List<EntryExitReport> getReportByGoodsAndDate(Goods goods, LocalDate date);
}
