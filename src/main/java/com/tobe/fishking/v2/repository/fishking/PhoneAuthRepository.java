package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    PhoneAuth findByPhoneNumber(PhoneNumber pNum);
}
