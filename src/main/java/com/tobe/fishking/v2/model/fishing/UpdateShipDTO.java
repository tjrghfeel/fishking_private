package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.addon.CommonAddon;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.utils.HashUtil;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UpdateShipDTO {

    @ApiParam(value = "선박명")
    private @Valid String name;
    @ApiParam(value = "선박 구분")
    private @Valid String fishingType;
    @ApiParam(value = "선박 주소")
    private @Valid String addr;
    @ApiParam(value = "시도")
    private @Valid String sido;
    @ApiParam(value = "시군구")
    private @Valid String sigungu;
    @ApiParam(value = "전화번호")
    private @Valid String tel;
    @ApiParam(value = "무게")
    private @Valid Double weight;
    @ApiParam(value = "탑승인원")
    private @Valid Integer boardingPerson;
    @ApiParam(value = "위도")
    private @Valid Double latitude;
    @ApiParam(value = "경도")
    private @Valid Double longitude;
    @ApiParam(value = "선박 사진")
    private @Valid String profileImage;
    @ApiParam(value = "선박 영상 id")
    private @Valid Long videoId;
    @ApiParam(value = "어종 리스트", name = "fishSpecies[]")
    private @Valid List<String> fishSpecies;
    @ApiParam(value = "서비스 리스트", name = "services[]")
    private @Valid List<String> services;
    @ApiParam(value = "편의시설 리스트", name = "facilities[]")
    private @Valid List<String> facilities;
    @ApiParam(value = "장비 리스트", name = "devices[]")
    private @Valid List<String> devices;
    @ApiParam(value = "이벤트 리스트", name = "events[]")
    private @Valid List<AddEvent> events;
    @ApiParam(value = "사용할 위치 리스트 (예: [1,2,3,4]), 갯바위 타입의 경우에는 갯바위 id 리스트", name = "positions[]")
    private @Valid List<String> positions;
    @ApiParam(value = "한마디 제목")
    private @Valid String ownerWordingTitle;
    @ApiParam(value = "한마디")
    private @Valid String ownerWording;
    @ApiParam(value = "공지사항 제목")
    private @Valid String noticeTitle;
    @ApiParam(value = "공지사항")
    private @Valid String notice;
    @ApiParam(value = "skb id", name = "skbId")
    private @Valid String skbId;
    @ApiParam(value = "skb pw", name = "skbPw")
    private @Valid String skbPw;
    @ApiParam(value = "adt 카메라리스트", name = "adtCamerase[]")
    private @Valid List<AddShipCamera> adtCameras;
    @ApiParam(value = "nhn id", name = "nhnId")
    private @Valid String nhnId;
    @ApiParam(value = "nhn pw", name = "nhnPw")
    private @Valid String nhnPw;
    @ApiParam(value = "nhn 카메라리스트", name = "nhnCameras[]")
    private @Valid List<AddShipCamera> nhnCameras;
    private @Valid String router;
    @ApiParam(value = "선장 이름")
    private @Valid String capName;
    @ApiParam(value = "선장 생년월일")
    private @Valid String capBirth;
    @ApiParam(value = "선장 성별")
    private @Valid String capSex;
    @ApiParam(value = "선장 전화번호")
    private @Valid String capPhone;
    @ApiParam(value = "선장 주소")
    private @Valid String capAddr;
    @ApiParam(value = "선장 비상연락처")
    private @Valid String capEmerNum;
    @ApiParam(value = "선장 해기사번호")
    private @Valid String capNumber;
    @ApiParam(value = "선박번호")
    private @Valid String shipNumber;

    public Ship toEntity(Member member, Company company, List<ObserverCode> codes, List<String> positions) throws NoSuchAlgorithmException {
        Location loc = Location.builder().latitude(latitude).longitude(longitude).build();
        ObserverCode code = codes.stream()
                .sorted(Comparator.comparing(e -> e.distanceFrom(loc)))
                .collect(Collectors.toList())
                .get(0);
        String profile;
        if (profileImage != null) {
            if (profileImage.equals("")) {
                profile = "/ship/defaultship.png";
            } else {
                profile = profileImage.split("resource")[1];
//                profile = profileImage.split("files")[1];
            }
        } else {
            profile = "/ship/defaultship.png";
        }

        if (!capPhone.contains("-")) {
            capPhone = CommonAddon.addDashToPhoneNum(capPhone);
        }
        if (!capEmerNum.contains("-")) {
            capEmerNum = CommonAddon.addDashToPhoneNum(capEmerNum);
        }
        if (!capBirth.contains("-")) {
            capBirth = capBirth.substring(0,4) + "-" + capBirth.substring(4,6) + "-" + capBirth.substring(6);
        }

        return Ship.builder()
                .name(name)
                .fishingType(FishingType.valueOf(fishingType))
                .address(addr == null ? "" : addr)
                .sido(sido)
                .sigungu(sigungu)
                .tel(company.getTel())
                .weight(weight)
                .boardingPerson(boardingPerson)
                .ownerWordingTitle(ownerWordingTitle)
                .ownerWording(ownerWording)
                .noticeTitle(noticeTitle)
                .notice(notice)
                .location(loc)
                .profileImage(profile)
                .company(company)
                .createdBy(member)
                .positions(String.join(",", positions))
                .code(code.getCode())
                .router(router)
                .videoId(videoId)
                .nhnId(nhnId)
                .nhnPw(nhnPw)
                .skbId(skbId)
                .skbPw(skbPw)
                .capName(capName)
                .capBirth(capBirth)
                .capSex(capSex)
                .capPhone(capPhone)
                .capAddr(capAddr)
                .capEmerNum(capEmerNum)
                .capNumber(capNumber)
                .shipNumber(shipNumber)
//                .skbPw((skbPw != null)? HashUtil.sha256(skbPw) : null)
                .build();
    }
}
