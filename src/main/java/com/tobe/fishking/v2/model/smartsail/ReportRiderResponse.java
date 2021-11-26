package com.tobe.fishking.v2.model.smartsail;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.tobe.fishking.v2.addon.CommonAddon.addDashToPhoneNum;

@Getter
@Setter
@NoArgsConstructor
public class ReportRiderResponse {

    private Long riderId;
    private Long reportRiderId;
    private String name;
    private String phone;
    private String emergencyPhone;
    private String birthday;
    private String sex;
    private String address;
    private String type;
    private String status;

    @QueryProjection
    public ReportRiderResponse(
            Long riderId,
            Long reportRiderId,
            String name,
            String phone,
            String emergencyPhone,
            String birthday,
            String sex,
            String address,
            String type,
            Boolean status
    ) {
        String idNum = birthday.substring(2).replaceAll("-", "") + "-";
        if (Integer.parseInt(birthday.substring(0,2)) >= 20) {
            idNum = idNum + (sex.equals("M") ? "3" : "4");
        } else {
            idNum = idNum + (sex.equals("M") ? "1" : "2");
        }
        this.riderId = riderId;
        this.reportRiderId = reportRiderId;
        this.name = name;
        this.phone = addDashToPhoneNum(phone);
        this.emergencyPhone = addDashToPhoneNum(emergencyPhone);
        this.birthday = idNum;
        this.sex = sex.equals("M") ? "남" : "여";
        this.address = address;
        this.type = type.equals("0") ? "승선객" : type.equals("1") ? "선장" : "선원";
        this.status = !type.equals("0") ? type.equals("1") ? "선장" : "선원" : status ? "승선완료" : "미승선";
    }

}