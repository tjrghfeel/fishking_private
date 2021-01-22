package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.common.PhoneNumberInfo;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/*사용자 프로필 눌렀을때 보이는 정보전달을 위한 DTO. isMe, isShip필드값에 따라 들어있는 정보가 조금씩 다르다.  */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long memberId;
    private String nickName;
//    private Role roles;
    private String profileImage; //프로파일 사진 경로+이름
    private String backgroundImage;//배경이미지 url
//    private SNSType snsType ;
//    private String snsId ;
//    private String statusMessage ;

    private int postCount;//작성글수. 리뷰, 조항일지, 조행기 등 본인이 작성한 글의 개수.
    private int likeCount;//좋아요수. 업체회원 : 글 등에서 받은 좋아요수 / 일반회원 : 회원이 좋아요 누른 수.
    private Boolean isMe;//현재 프로필DTO가 본인의 것임을 나타내는 필드.
    private int takeCount;//업체 찜 수. 업체회원 : 받은 찜수. / 일반회원 : 회원이 찜한 수.

    //업체인 경우 추가되는 필드.
    private Boolean isCompany;//이 멤버가 업체(company)인지를 나타내는 필드.
    private Long companyId;//company id



}
