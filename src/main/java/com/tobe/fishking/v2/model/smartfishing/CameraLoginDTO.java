package com.tobe.fishking.v2.model.smartfishing;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CameraLoginDTO {
    private String id;
    private String pw;
    private Long shipId;
}
