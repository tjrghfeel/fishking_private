package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.common.Location;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddShipDTO {

    @ApiParam(value = "선박명")
    private @Valid String name;
    @ApiParam(value = "선박 구분")
    private @Valid String fishingType;
    @ApiParam(value = "선박 주소")
    private @Valid String address;
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
    @ApiParam(value = "어종 리스트")
    private @Valid List<String> fishSpecies;
    @ApiParam(value = "서비스 리스트")
    private @Valid List<String> services;
    @ApiParam(value = "편의시설 리스트")
    private @Valid List<String> facilities;
    @ApiParam(value = "장비 리스트")
    private @Valid List<String> devices;
    @ApiParam(value = "이벤트 리스트")
    private @Valid List<AddEvent> events;
    @ApiParam(value = "사용할 위치 리스트 (예: [1,2,3,4])")
    private @Valid List<String> positions;
    @ApiParam(value = "한마디 제목")
    private @Valid String ownerWordingTitle;
    @ApiParam(value = "한마디")
    private @Valid String ownerWording;
    @ApiParam(value = "공지사항 제목")
    private @Valid String noticeTitle;
    @ApiParam(value = "공지사항")
    private @Valid String notice;

    public Ship toEntity(Member member, Company company, List<ObserverCode> codes) {
        Location loc = Location.builder().latitude(latitude).longitude(longitude).build();
        ObserverCode code = codes.stream()
                .sorted(Comparator.comparing(e -> e.distanceFrom(loc)))
                .collect(Collectors.toList())
                .get(0);
        return Ship.builder()
                .name(name)
                .fishingType(FishingType.valueOf(fishingType))
                .address(address)
                .sido(sido)
                .sigungu(sigungu)
                .tel(tel)
                .weight(weight)
                .boardingPerson(boardingPerson)
                .ownerWordingTitle(ownerWordingTitle)
                .ownerWording(ownerWording)
                .noticeTitle(noticeTitle)
                .notice(notice)
                .location(loc)
                .profileImage(profileImage)
                .company(company)
                .createdBy(member)
                .positions(String.join(",", positions))
                .code(code.getCode())
                .build();
    }

}
