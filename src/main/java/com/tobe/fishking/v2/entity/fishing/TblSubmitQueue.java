package com.tobe.fishking.v2.entity.fishing;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_submit_queue")
public class TblSubmitQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CMP_MSG_ID")
    private Long cmpMsgId;

    @Column(name="CMP_MSG_GROUP_ID",columnDefinition = "varcher(20) null" )
    private String cmpMsgGroupId;

    @Column(name="USR_ID", columnDefinition = "varchar(16) not null")
    private String usrId;

    @Column(name="SMS_GB", columnDefinition = "char default '1' null")
    private  String smsGb;

    @Column(name="USED_CD", columnDefinition = "char(2) not null")
    private String usedCd;

    @Column(name="RESERVED_FG", columnDefinition = "char not null")
    private   String reservedFg;

    @Column(name="RESERVED_DTTM", columnDefinition = "char(14) not null")
    private String reservedDttm;

    @Column(name="SAVED_FG", columnDefinition = "char default '0' null")
    private         String savedFg;

    @Column(name="RCV_PHN_ID", columnDefinition = "varchar(24) not null")
    private String rcvPhnId;

    @Column(name="SND_PHN_ID", columnDefinition = "varchar(24) null")
    private   String sndPhnId;

    @Column(name="NAT_CD", columnDefinition = "varchar(8) null")
    private String natCd;

    @Column(name="ASSIGN_CD", columnDefinition = "varchar(5) default '00000' null")
    private  String assignCd;

    @Column(name="SND_MSG", columnDefinition = "varchar(2000)              null")
    private String sndMsg;

    @Column(name="CALLBACK_URL", columnDefinition = "varchar(120)               null")
    private  String callbackUrl;

    @Column(name="CONTENT_CNT", columnDefinition = "int        default 0       null")
    private Integer contentCnt;

    @Column(name="CONTENT_MIME_TYPE", columnDefinition = "varchar(128)               null")
    private  String contentMimeType;

    @Column(name="CONTENT_PATH", columnDefinition = "varchar(1024)              null")
    private String contentPath;

    @Column(name="CMP_SND_DTTM", columnDefinition = "char(14)                   null")
    private String comSndDttm;

    @Column(name="CMP_RCV_DTTM", columnDefinition = "char(14)                   null")
    private String cmpRcvDttm;

    @Column(name="REG_SND_DTTM", columnDefinition = "char(14)                   null")
    private   String regSndDttm;

    @Column(name="REG_RCV_DTTM", columnDefinition = "char(14)                   null")
    private String regRcvDttm;

    @Column(name="MACHINE_ID", columnDefinition = "char(2)                    null")
    private   String machineId;

    @Column(name="SMS_STATUS", columnDefinition = "char       default '0'     null")
    private String smsStatus;

    @Column(name="RSLT_VAL", columnDefinition = "char(4)                    null")
    private   String rsltVal;

    @Column(name="MSG_TITLE", columnDefinition = "varchar(200)               null")
    private String msgTitle;

    @Column(name="TELCO_ID", columnDefinition = "char(4)                    null")
    private   String telcoId;

    @Column(name="ETC_CHAR_1", columnDefinition = " varchar(100)               null")
    private String etcChar1;
    @Column(name="ETC_CHAR_2", columnDefinition = " varchar(100)               null")
    private   String etcChar2;
    @Column(name="ETC_CHAR_3", columnDefinition = " varchar(100)               null")
    private String etcChar3;
    @Column(name="ETC_CHAR_4", columnDefinition = " varchar(100)               null")
    private  String etcChar4;

    @Column(name="ETC_INT_5", columnDefinition = "int                        null")
    private Integer etcInt5;

    @Column(name="ETC_INT_6", columnDefinition = "int                        null")
    private Integer etcInt6;


}
