package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.board.FishingDiarySmallResponse;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddShipDTO {

    private @Valid String name;
    private @Valid String fishingType;
    private @Valid String address;
    private @Valid String sido;
    private @Valid String sigungu;
    private @Valid String tel;
    private @Valid Double weight;
    private @Valid Integer boardingPerson;
    private @Valid Double latitude;
    private @Valid Double longitude;
    private @Valid String profileImage;
    private @Valid List<String> services;
    private @Valid List<String> facilities;
    private @Valid List<String> devices;
    private @Valid List<String> events;
    private @Valid List<String> positions;
    private @Valid String ownerWordingTitle;
    private @Valid String ownerWording;
    private @Valid String noticeTitle;
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
                .positions(positions.stream().reduce(",", String::concat))
                .code(code.getCode())
                .build();
    }

}
