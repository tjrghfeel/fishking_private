package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.TblSubmitQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TblSubmitQueueRepository extends JpaRepository<TblSubmitQueue, Long> {

    /*tbl_submit_queue에 문자인증 요청건 저장
     * - !!!!! cmp_msg_id확인후 수정필요, usr_id확인후 정확한 id추가필요, snd_phn_id확인후 정확한 번호추가필요*/
    @Query(value = "" +
            "insert into tbl_submit_queue(" +
            "    usr_id, sms_gb, used_cd, reserved_fg, reserved_dttm, saved_fg, rcv_phn_id, snd_phn_id, snd_msg, " +
            "   content_cnt, sms_status  " +
            ") " +
            "values (" +
            "   'aaa', '1', '00', 'I', :time, '0', :phoneNum, '01011112222', " +
            "   :content, 0, '0'" +
            ")",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void sendSms(
            @Param("time") String time,
            @Param("phoneNum") String phoneNum,
            @Param("content") String content
    );
}
