package com.tobe.fishking.v2.model.response;

import com.tobe.fishking.v2.model.fishing.AddEvent;
import com.tobe.fishking.v2.model.fishing.AddShipCamera;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class UpdateShipResponse {

    private Long id;
    private String name;
    private String fishingType;
    private String address;
    private String sido;
    private String sigungu;
    private String tel;
    private Double weight;
    private Integer boardingPerson;
    private Double latitude;
    private Double longitude;
    private String profileImage;
    private List<String> fishSpecies;
    private List<String> services;
    private List<String> facilities;
    private List<String> devices;
    private List<AddEvent> events;
    private List<String> positions;
    private List<Map<String, Object>> seaRocks;
    private String ownerWordingTitle;
    private String ownerWording;
    private String noticeTitle;
    private String notice;
    private List<AddShipCamera> adtCameras;
    private String skbId;
    private String skbPw;
    private List<AddShipCamera> nhnCameras;
    private String nhnId;
    private String router;
    private Long videoId;
    private String video;
    private String capName;
    private String capBirth;
    private String capSex;
    private String capPhone;
    private String capAddr;
    private String capEmerNum;
    private String capNumber;
    private String shipNumber;

}
