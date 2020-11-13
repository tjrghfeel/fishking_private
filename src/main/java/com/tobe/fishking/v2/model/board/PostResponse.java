package com.tobe.fishking.v2.model.board;

import java.time.LocalDateTime;

/*Post들을 Page형식으로 프론트로 반환해주기위해 repository에서 Post엔터티로부터 값받을때 쓰는 인터페이스.
* 목록만을 뿌려주고 내용전체는 바로 보여줄필요가 없으므로 용량이큰 contents (추가될수도있음) 필드를 제외하였다. */
public interface PostResponse {
    Long getId();
    LocalDateTime getCreatedDate();
    LocalDateTime getModifiedDate();
    String getAuthorName();
    int getChannelType();
    String getContents();
    String getCreatedAt();
    boolean getIsSecret();
    String getReturnNoAddress();
    int getReturnType();
    String getTitle();
    Long getAuthorId();
    Long getBoardId();
    Long getCreatedBy();
    Long getModifiedBy();

}
