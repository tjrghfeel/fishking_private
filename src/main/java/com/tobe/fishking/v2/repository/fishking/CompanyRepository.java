package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Company;

import com.tobe.fishking.v2.model.fishing.CompanyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tobe.fishking.v2.repository.BaseRepository;

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

    List<Company> findAllByIsRegisitered(Boolean registered);
}
