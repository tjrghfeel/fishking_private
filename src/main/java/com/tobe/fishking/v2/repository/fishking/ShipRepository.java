package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShipRepository extends BaseRepository<Ship, Long> {

    /*member가 만든 company가 만든 ship을 가져오는 메소드.
    * - !!!!! company와 ship은 '1대다'관계로 db가 구성되어있지만, 부장님말씀대로 1대1이라 가정하고 만든 쿼리. */
    @Query("select s from Ship s where s.company = (select c from Company c where c.member = :member) ")
    Ship findByMember(@Param("member") Member member);



}
