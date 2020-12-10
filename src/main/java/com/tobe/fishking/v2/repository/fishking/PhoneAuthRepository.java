package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    PhoneAuth findByPhoneNumber(PhoneNumber pNum);

    @Query("select a from PhoneAuth a where a.phoneNumber.areaCode = :areaCode and a.phoneNumber.localNumber = :localNumber")
    PhoneAuth findByAreaCodeAndLocalNumber(@Param("areaCode") String areaCode, @Param("localNumber") String localNumber);
}
