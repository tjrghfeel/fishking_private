package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    PhoneAuth findByPhoneNumber(PhoneNumber pNum);

    @Query(value = "select * from phone_auth a " +
            "where a.areacode = :areaCode " +
            "   and a.localnumber = :localNumber " +
            "   and a.is_certified = false " +
            "order by a.created_date desc limit 1",
            nativeQuery = true
    )
    PhoneAuth getLastPhoneAuth(
            @Param("areaCode") String areaCode,
            @Param("localNumber") String localNumber
    );


}
