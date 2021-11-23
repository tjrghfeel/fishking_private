package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.EntryExitReport;
import com.tobe.fishking.v2.model.smartsail.ReportRiderResponse;

import java.util.List;

public interface EntryExitAttendRepositoryCustom {
    List<ReportRiderResponse> getRiders(EntryExitReport report);
}
