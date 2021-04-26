package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyingStatusMessageDto {
    @Pattern(regexp = Constants.STRING)
    @Size(max=60)
    private String statusMessage;
}
