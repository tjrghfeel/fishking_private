package com.tobe.fishking.v2.model.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipSearchConditionDto {
    private String shipName;
    private String fishingType;
    private String address;
    private String companyPhoneNumber;
    private String seaDirection;
    private String[] fishSpecies = new String[0];
    private String[] services = new String[0];
    private String[] facilities = new String[0];
    private String[] devices = new String[0];
    private Boolean isLive;
    private String companyName;
    private Double totalAvgByReview;
    private Boolean isActive;
    private Boolean departStatus;

    private Integer pageCount=10;
    private String sort="createdDate";
}
