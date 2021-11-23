package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.EntryExitAttend;
import com.tobe.fishking.v2.entity.fishing.EntryExitReport;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryExitAttendRepository extends BaseRepository<EntryExitAttend, Long>, EntryExitAttendRepositoryCustom {
    @Query("select a from EntryExitAttend a where a.report = :report")
    List<EntryExitAttend> getEntryExitAttendsByReport(EntryExitReport report);

    @Query("select count(a) from EntryExitAttend a where a.report = :report and a.type='2'")
    Integer getSailorCount(EntryExitReport report);
}
