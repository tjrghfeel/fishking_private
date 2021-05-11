package com.tobe.fishking.v2.enums;

public class Constants {
    /**
     * \class Constants
     * \brief 프로그램에 사용될 상수 정의
     *
     * \var siteUrl 실명인증 사이트주소
     * \var siteCode 실명인증 사이트코드
     * \var sitePassword 실명인증 비밀번호
     */

    //실명인증
    public static final String siteUrl = "www.billiboard.co.kr";
    public static final String siteCode = "BC540";
    public static final String sitePassword = "SC4h2Pm4tKNh";


    //파일업로드
    public static final String TYPE_IMG_FILE                = "01";
    public static final String TYPE_FILE                    = "02";

    public static final String thumbnailWidth               = "300";
    public static final String thumbnailHeight              = "300";

    //선박 대표이미지용 섬네일 사이즈
    public static final String shipThumbnailWidth           ="767";
    public static final String shipThumbnailHeight          ="426";

    //파일 업로드 위치
    public static final String PREPEND_FILE_BOARD = "/board/";

    //입력값 검증
    public static final String NO_SPECIAL_C_STRING = "^[a-zA-Z0-9가-힣ㄱ-ㅎ]*$";
    public static final String STRING = "^[a-zA-Z0-9ㅏ-ㅣ가-힣ㄱ-ㅎ.!?\\s\\_-]*$";
    public static final String NUMBER = "^[0-9]*$";
    public static final String NUMBER2 = "^[0-9-]*$";
    public static final String EMAIL = "^[a-zA-Z0-9_.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,6}$";
    public static final String PW = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$";
    public static final String BIZ_NO = "^\\d{3}\\-\\d{2}\\-\\d{4}$";
}
