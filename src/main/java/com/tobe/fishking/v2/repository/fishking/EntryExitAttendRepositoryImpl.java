package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.EntryExitReport;
import com.tobe.fishking.v2.model.smartsail.QReportRiderResponse;
import com.tobe.fishking.v2.model.smartsail.ReportRiderResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.fishing.QEntryExitAttend.entryExitAttend;
import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;

@RequiredArgsConstructor
public class EntryExitAttendRepositoryImpl implements EntryExitAttendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReportRiderResponse> getRiders(EntryExitReport report) {
        List<ReportRiderResponse> responses = queryFactory
                .select(new QReportRiderResponse(
                        entryExitAttend.rideShipId,
                        entryExitAttend.id,
                        entryExitAttend.name,
                        entryExitAttend.phone,
                        entryExitAttend.emerNum,
                        entryExitAttend.birth,
                        entryExitAttend.sex,
                        entryExitAttend.addr,
                        entryExitAttend.type,
                        rideShip.isRide
                ))
                .from(entryExitAttend).leftJoin(rideShip).on(entryExitAttend.rideShipId.eq(rideShip.id))
                .where(entryExitAttend.report.eq(report))
                .orderBy(entryExitAttend.type.asc())
                .fetch();
        return responses;
//        return null;
    }

}
