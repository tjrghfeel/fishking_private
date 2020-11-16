package com.tobe.fishking.v2.model.board;

import java.time.LocalDateTime;

/*Post들을 Page형식으로 프론트로 반환해주기위해 repository에서 Post엔터티로부터 값받을때 쓰는 인터페이스.*/
public interface PostListDTO {
    Long getId();
    Long getBoardId();
    String getTitle();
    LocalDateTime getCreatedDate();
    LocalDateTime getModifiedDate();
    String getAuthorName();
    //int getChannelType();
    //String getContents();
    //String getCreatedAt();
    boolean getIsSecret();
    //String getReturnNoAddress();
    //int getReturnType();
    Long getAuthorId();
    //int getParentId();
    //Long getCreatedBy();
    //Long getModifiedBy();

}
