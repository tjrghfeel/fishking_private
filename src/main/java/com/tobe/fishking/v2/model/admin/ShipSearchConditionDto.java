package com.tobe.fishking.v2.model.admin;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipSearchConditionDto {
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String shipName;
    private String fishingType;
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String address;
//    @Size(max=100)
//    @Pattern(regexp = Constants.NUMBER)
    private String companyPhoneNumber;
    private String seaDirection;
    private String[] fishSpecies = new String[0];
    private String[] services = new String[0];
    private String[] facilities = new String[0];
    private String[] devices = new String[0];
    private Boolean isLive;
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String companyName;
    private Double totalAvgByReview;
    private Boolean isActive;
    private Boolean departStatus;

    private Integer pageCount=10;
//    @Size(max=100)
//    @Pattern(regexp = Constants.STRING)
    private String sort="createdDate";
}
