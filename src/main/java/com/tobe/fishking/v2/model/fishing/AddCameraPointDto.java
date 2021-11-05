package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCameraPointDto {
    @NotNull(message = "항구명 항목이 비었습니다. ")
    @Size(max=30, message = "항구명은 30자를 넘을 수 없습니다. ")
    private String name;
    @NotNull(message = "주소(시/도) 항목이 비었습니다. ")
    private String sido;
    @NotNull(message = "주소(군/구) 항목이 비었습니다. ")
    private String gungu;
    @NotNull(message = "주소항목이 비었습니다. ")
    private String address;
    @NotNull(message = "위도(latitude)항목이 비었습니다. ")
    private Double lat;
    @NotNull(message = "경도(longitude)항목이 비었습니다. ")
    private Double lon;
//    @NotNull(message = "")
//    @Size(max=30, message = "")
    private String adtId;
//    @NotNull(message = "")
//    @Size(max=30, message = "")
    private String adtPw;
//    @NotNull(message = "대표 이미지 file id 항목이 비었습니다.")
    private Long imgFileId;

    private List<AddShipCamera> nhnCameras;

//    @NotNull(message = "삭제 여부 항목이 비었습니다.")
//    private Boolean isDeleted;
//    @NotNull(message = "활성화 여부 항목이 비었습니다.")
//    private Boolean isActive;


}