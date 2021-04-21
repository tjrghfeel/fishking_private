package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.auth.Gender;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class NoNameDTO {
    public long book;
    public long bookRunning;
    public long waitBook;
    public long bookFix;
    public long bookCancel;
    public long fishingComplete;
    public long bookConfirm;
    public NoNameDTO(long book, long bookRunning, long waitBook, long bookFix, long bookCancel, long fishingComplete, long bookConfirm){
        this.book=book;
        this.bookRunning = bookRunning;
        this.waitBook = waitBook;
        this.bookFix = bookFix;
        this.bookCancel =bookCancel;
        this.fishingComplete = fishingComplete;
        this.bookConfirm = bookConfirm;
    }
}
