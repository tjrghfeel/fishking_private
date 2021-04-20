package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Company;

import com.tobe.fishking.v2.model.admin.company.CompanyManageDtoForPage;
import com.tobe.fishking.v2.model.fishing.CompanyListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tobe.fishking.v2.repository.BaseRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompanyRepository extends BaseRepository<Company, Long> {

    /* nativeQeury를 이용해 리포지토리로부터 CompanyResponse의 값들을 바로 받아오려했으나 FileEntity의 url을 받아오는 부분에서
    쿼리문을 어떻게짜야할지 몰라서 일단 다른방법으로 하였다. 추후 수정필요.
    @Query(
            value = "select " +
                    "c.id id," +
                    "c.member_id member," +
                    "c.company_name companyName," +
                    "c.ship_owner shipOwner," +
                    "c.gungu gungu," +
                    "c.sido sido," +
                    "c.tel tel," +
                    "c.biz_no bizNo," +
                    "c.harbor harbor," +
                    "c.bank bank," +
                    "c.account_no accountNo," +
                    "c.owner_wording ownerWording," +
                    "c.is_open isOpen," +
                    "c.skb_account skbAccount," +
                    "c.skb_password skbPassword," +
                    "c.companny_address companyAddress," +
                    "c.is_registerd isRegistered," +
                    "c.created_by createdBy," +
                    "c.modified_by modifiedBy," +
                    "c.biz_no_file_id bizNoFileId," +
                    "c.represent_file_id representFileId," +
                    "c.account_file_id accountFileId " +
                    "from company c " +
                    "where c.is_register = false " +
                    "order by c.created_date desc",
            countQuery = "select p.id from company c where c.is_register = false ",
            nativeQuery = true
    )
    List<CompanyResponse> findCompanyRegisterRequestList();*/

    @Query(
            value = "select " +
                    "c.id id," +
                    "c.member_id member," +
                    "c.company_name companyName," +
                    "c.ship_owner shipOwner," +
                    "c.gungu gungu," +
                    "c.sido sido," +
                    "c.tel tel," +
                    //"c.biz_no bizNo," +
                    //"c.harbor harbor," +
                    //"c.bank bank," +
                    //"c.account_no accountNo," +
                    //"c.owner_wording ownerWording," +
                    "c.is_open isOpen," +
                    //"c.skb_account skbAccount," +
                    //"c.skb_password skbPassword," +
                    //"c.companny_address companyAddress," +
                    "c.is_registered isRegistered " +
//                    "c.created_by createdBy," +
//                    "c.modified_by modifiedBy," +
//                    "c.biz_no_file_id bizNoFileId," +
//                    "c.represent_file_id representFileId," +
//                    "c.account_file_id accountFileId " +
                    "from company c " +
                    "where c.is_registered = :isRegistered " +
                    "order by c.created_date desc",
            countQuery = "select p.id from company c where c.is_registered = :isRegistered ",
            nativeQuery = true
    )
    Page<CompanyListDTO> findCompanyRegisterRequestList(@Param("isRegistered") Boolean isRegistered, Pageable pageable);


    List<Company> findAllByIsRegistered(Boolean registered);

    Company findByMember(Member member);

    Boolean existsByMember(Member member);

    /*관리자용 업체 목록 검색 메소드 */
    @Query(value = "" +
            "select " +
            "   c.id companyId, " +
            "   m.id memberId, " +
            "   m.member_name memberName, " +
            "   m.nick_name memberNickName, " +
            "   m.areacode areaCode, " +
            "   m.localnumber localNumber, " +
            "   c.company_name companyName, " +
            "   c.ship_owner shipOwner, " +
            "   c.sido sido, " +
            "   c.gungu gungu, " +
            "   c.tel tel, " +
            "   c.phone_number phoneNumber, " +
            "   c.biz_no bizNo, " +
            "   c.harbor harbor, " +
            "   c.account_no accountNum, " +
            "   c.bank bank, " +
            "   c.biz_no_file_id_id bizNoFileId, " +
            "   c.represent_file_id_id representFileId, " +
            "   c.account_file_id_id accountFileId, " +
            "   c.biz_no_file_download_url bizNoFileDownloadUrl, " +
            "   c.represent_file_download_url representFileDownloadUrl, " +
            "   c.account_file_download_url accountFileDownloadUrl, " +
            "   c.owner_wording ownerWording, " +
            "   c.is_open isOpen, " +
            "   c.skb_account skbAccount, " +
            "   c.company_address companyAddress, " +
            "   c.is_registered isRegistered, " +
            "   c.adt_id adtId, " +
            "   c.nhn_id nhnId, " +
            "   c.created_by createdBy, " +
            "   c.registered_date registeredDate, " +
            "   c.created_date createdDate, " +
            "   (select group_concat(s2.ship_name separator ', ') from company c2 left join ship s2 on c2.id = s2.company_id " +
            "       where c2.id = c.id group by c2.id) shipNameList " +
            "from company c join member m on (c.member_id = m.id) left join ship s on c.id = s.company_id " +
            "where " +
            "   if(:companyId is null, true, (c.id = :companyId)) " +
            "   and if(:memberId is null, true, (m.id = :memberId)) " +
            "   and if(:memberName is null, true, (m.member_name like %:memberName%)) " +
            "   and if(:memberNickName is null, true, (m.nick_name like %:memberNickName%)) " +
            "   and if(:areaCode is null, true, (m.areacode like %:areaCode%)) " +
            "   and if(:localNumber is null, true, (m.localnumber like %:localNumber%)) " +
            "   and if(:companyName is null, true, (c.company_name like %:companyName%)) " +
            "   and if(:shipOwner is null, true, (c.ship_owner like %:shipOwner%)) " +
            "   and if(:sido is null, true, (c.sido like %:sido%)) " +
            "   and if(:gungu is null, true,(c.gungu like %:gungu%)) " +
            "   and if(:tel is null, true,(c.tel like %:tel%)) " +
            "   and if(:phoneNumber is null, true, (c.phone_number like %:phoneNumber%)) " +
            "   and if(:bizNo is null, true,(c.biz_no like %:bizNo%)) " +
            "   and if(:harbor is null, true, (c.harbor like %:harbor%)) " +
            "   and if(:accountNum is null, true,(c.account_no like %:accountNum%)) " +
            "   and if(:bank is null, true,(c.bank like %:bank%)) " +
            "   and if(:isOpen is null, true,(c.is_open = :isOpen)) " +
            "   and if(:skbAccount is null, true,(c.skb_account like %:skbAccount%)) " +
            "   and if(:companyAddress is null, true,(c.company_address like %:companyAddress%)) " +
            "   and if(:isRegistered is null, true, (c.is_registered = :isRegistered)) " +
            "   and if(:adtId is null, true, (c.adt_id like %:adtId%)) " +
            "   and if(:nhnId is null, true, (c.nhn_id like %:nhnId%)) " +
            "   and if(:createdBy is null, true, (c.created_by = :createdBy)) " +
            "   and if(:createdDayStart is null, true, (c.created_date > :createdDayStart)) " +
            "   and if(:createdDayEnd is null, true, (c.created_date < :createdDayEnd)) " +
            "   and if(:shipName is null, true, (s.ship_name like %:shipName%)) " +
            "group by c.id ",
            countQuery = "select c.id " +
                    "from company c join member m on (c.member_id = m.id) left join ship s on c.id = s.company_id " +
                    "where " +
                    "   if(:companyId is null, true, (c.id = :companyId)) " +
                    "   and if(:memberId is null, true, (m.id = :memberId)) " +
                    "   and if(:memberName is null, true, (m.member_name like %:memberName%)) " +
                    "   and if(:memberNickName is null, true, (m.nick_name like %:memberNickName%)) " +
                    "   and if(:areaCode is null, true, (m.areacode like %:areaCode%)) " +
                    "   and if(:localNumber is null, true, (m.localnumber like %:localNumber%)) " +
                    "   and if(:companyName is null, true, (c.company_name like %:companyName%)) " +
                    "   and if(:shipOwner is null, true, (c.ship_owner like %:shipOwner%)) " +
                    "   and if(:sido is null, true, (c.sido like %:sido%)) " +
                    "   and if(:gungu is null, true,(c.gungu like %:gungu%)) " +
                    "   and if(:tel is null, true,(c.tel like %:tel%)) " +
                    "   and if(:phoneNumber is null, true, (c.phone_number like %:phoneNumber%)) " +
                    "   and if(:bizNo is null, true,(c.biz_no like %:bizNo%)) " +
                    "   and if(:harbor is null, true, (c.harbor like %:harbor%)) " +
                    "   and if(:accountNum is null, true,(c.account_no like %:accountNum%)) " +
                    "   and if(:bank is null, true,(c.bank like %:bank%)) " +
                    "   and if(:isOpen is null, true,(c.is_open = :isOpen)) " +
                    "   and if(:skbAccount is null, true,(c.skb_account like %:skbAccount%)) " +
                    "   and if(:companyAddress is null, true,(c.company_address like %:companyAddress%)) " +
                    "   and if(:isRegistered is null, true, (c.is_registered = :isRegistered)) " +
                    "   and if(:adtId is null, true, (c.adt_id like %:adtId%)) " +
                    "   and if(:nhnId is null, true, (c.nhn_id like %:nhnId%)) " +
                    "   and if(:createdBy is null, true, (c.created_by = :createdBy)) " +
                    "   and if(:createdDayStart is null, true, (c.created_date > :createdDayStart)) " +
                    "   and if(:createdDayEnd is null, true, (c.created_date < :createdDayEnd)) " +
                    "   and if(:shipName is null, true, (s.ship_name like %:shipName%)) " +
                    "group by c.id ",
            nativeQuery = true
    )
    Page<CompanyManageDtoForPage> findCompanyListByConditions(
            @Param("companyId") Long companyId,
            @Param("memberId") Long memberId,
            @Param("memberName") String memberName,
            @Param("memberNickName") String memberNickName,
            @Param("areaCode") String areaCode,
            @Param("localNumber") String localNumber,
            @Param("companyName") String companyName,
            @Param("shipOwner") String shipOwner,
            @Param("sido") String sido,
            @Param("gungu") String gungu,
            @Param("tel") String tel,
            @Param("phoneNumber") String phoneNumber,
            @Param("bizNo") String bizNo,
            @Param("harbor") String harbor,
            @Param("accountNum") String accountNum,
            @Param("bank") String bank,
            @Param("isOpen") Boolean isOpen,
            @Param("skbAccount") String skbAccount,
            @Param("companyAddress") String companyAddress,
            @Param("isRegistered") Boolean isRegistered,
            @Param("adtId") String adtId,
            @Param("nhnId") String nhnId,
            @Param("createdBy") Long createdBy,
            @Param("createdDayStart") LocalDate createdDayStart,
            @Param("createdDayEnd") LocalDate createdDayEnd,
            @Param("shipName") String shipName,
//            @Param("sort") String sort,
            Pageable pageable
    );


    @Query(value = "SELECT COUNT(id) FROM Company WHERE isRegistered = true")
    Long findAllByIsRegistered();

    @Query("select s.company from Ship s where s.id = :shipId")
    Company getCompanyByShip(Long shipId);
}
