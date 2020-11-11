package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ParamsPopular {


    @ApiModelProperty(notes = "키워드입력화면")
    private SearchPublish searchPublish;

    @ApiModelProperty(notes = "키워드")
    private String searchKeyWord;
    @ApiModelProperty(notes = "회원ID")
    private Member createdBy;
}
