package com.tobe.fishking.v2.controller.member;

import NiceID.Check.CPClient;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.enums.Constants;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.auth.*;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags={"프로필"})
@Controller
@RequestMapping("/v2/api")
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    Environment env;
    @Autowired
    CompanyRepository companyRepository;

    /*문자인증 확인
     * - 인증번호 보내면 일치하는지 확인. */
    @ApiOperation(value = "문자인증확인",notes = "" +
            "- 인증번호가 일치하는지 확인.\n" +
            "- 요청 필드 )\n" +
            "   phoneAuthId : 문자인증건에 대한 id\n" +
            "   authNum : 인증번호\n" +
            "- 반환 ) 일치할경우 true, 아닐경우 false. ")
    @PostMapping("/checkSmsAuth")
    @ResponseBody
    public boolean checkSignUpSmsAuth(
            @RequestBody PhoneAuthCheckDto dto
    ) throws ResourceNotFoundException {
        return memberService.checkSmsAuth(dto.getPhoneAuthId(),dto.getAuthNum());
    }

    /*아이디 중복 확인*/
    @ApiOperation(value = "아이디 중복 확인",notes = "" +
            "- 입력한 아이디가 이미 존재하는지 확인\n" +
            "- 입력 필드 )\n" +
            "   uid : 중복확인할 아이디\n" +
            "- 반환 ) \n" +
            "   0 : 중복 안됨\n" +
            "   1 : 중복 \n" +
            "   2 : 이메일 형식에 맞지 않음")
    @GetMapping("/signUp/checkUidDup")
    @ResponseBody
    public int checkUidDup(@RequestParam("uid") String uid){
        return memberService.checkUidDup(uid);
    }

    /*닉네임 중복 확인*/
    @ApiOperation(value = "닉네임 중복 확인", notes = "" +
            "- 입력 필드 ) \n" +
            "   nickName : 중복확인할 닉네임"
    )
    @GetMapping("/signUp/checkNickNameDup")
    @ResponseBody
    public int checkNickNameDup(@RequestParam("nickName") String nickName){
        return memberService.checkNickNameDup(nickName);
    }

    /*회원가입 중간단계 - 회원정보입력*/
    @ApiOperation(value = "회원정보입력 완료 및 pass인증 요청",notes = "" +
            "회원가입 단계중, 회원정보 입력후 pass인증 요청 api. pass인증버튼 클릭시 호출, 이전단계에서 입력한 회원정보를 함께 보내주어야한다. \n" +
            "회원정보 임시저장 및 pass인증을 호출해 준다. \n" +
            "요청 필드 ) \n" +
            "- memberId : Long / 선택 / sns를 통해 회원가입진행중이거나 본인인증 재시도하는 경우 입력. \n" +
            "\tㄴ sns로그인 후 리다이렉트 url에 있는 파라미터 또는 본인인증 실패로 redirect된 url의 url파라미터 'memberId'를 입력해주면된다.\n" +
            "- email : String / 필수 / 이메일. 이메일 형식에 맞아야한다\n" +
            "- pw : String / 필수 / 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "- nickName : String / 필수 / 닉네임. 4~10자. \n" +
            "" )
    @PostMapping("/signUp/infoAndPassRequest")
    public String insertMemberInfo(
//            @RequestBody SignUpDto dto,
            @RequestParam(value = "memberId",required = false) Long Id,
            @RequestParam("email") String email,
            @RequestParam("pw") String pw,
            @RequestParam("nickName") String nickName,
            @RequestParam(value = "registrationToken",required = false) String registrationToken,
            ModelMap model,
            HttpSession session
    ) throws ResourceNotFoundException, IOException, ServiceLogicException {
        SignUpDto dto = SignUpDto.builder()
                .memberId(Id)
                .email(email)
                .pw(pw)
                .nickName(nickName)
                .registrationToken(registrationToken)
                .build();

        //입력값 검증
        if(email == null || email.equals("") || !email.matches(Constants.EMAIL)  || email.length() > 50
                || pw == null || pw.equals("") || !pw.matches(Constants.PW) || pw.length() < 8 || pw.length() > 15
                || nickName==null || nickName.equals("") || !nickName.matches(Constants.STRING) || nickName.length() < 2 || nickName.length() > 7
        ){
            return "redirect:https://www.fishkingapp.com/cust/member/signup?msg=inValidSignUpValue";
        }

        /*회원정보 저장. */
        Long memberId = memberService.insertMemberInfo(dto);

        if(memberId == -1L){return "redirect:https://www.fishkingapp.com/cust/member/signup?msg=inValidSignUpValue";}

        /*nice 본인인증 호출. */
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = memberId.toString();        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
//        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "M";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String sReturnUrl = "https://fishkingapp.com/v2/api/niceSuccess";      // 성공시 이동될 URL
        String sErrorUrl = "https://fishkingapp.com/v2/api/niceFail";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);//보낼 데이터 암호화.
        //암호화 결과 코드 확인.
        if( iReturn == 0 )        {            sEncData = niceCheck.getCipherData();        }
        else if( iReturn == -1)        {            sMessage = "암호화 시스템 에러입니다.";        }
        else if( iReturn == -2)        {            sMessage = "암호화 처리오류입니다.";        }
        else if( iReturn == -3)        {            sMessage = "암호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        model.addAttribute("sMessage",sMessage);
        model.addAttribute("sEncData",sEncData);

        return "jsp/niceRequest";
    }
    //nice인증 컨트롤러메소드
    @GetMapping("/niceRequest")
    public String niceRequest(@RequestParam("memberId") Long memberId, ModelMap model, HttpSession session){
        /*nice 본인인증 호출. */
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = memberId.toString();        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
//        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "M";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String sReturnUrl = "https://www.fishkingapp.com/v2/api/niceSuccess";      // 성공시 이동될 URL
        String sErrorUrl = "https://www.fishkingapp.com/v2/api/niceFail";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);//보낼 데이터 암호화.
        //암호화 결과 코드 확인.
        if( iReturn == 0 )        {            sEncData = niceCheck.getCipherData();        }
        else if( iReturn == -1)        {            sMessage = "암호화 시스템 에러입니다.";        }
        else if( iReturn == -2)        {            sMessage = "암호화 처리오류입니다.";        }
        else if( iReturn == -3)        {            sMessage = "암호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        model.addAttribute("sMessage",sMessage);
        model.addAttribute("sEncData",sEncData);

        return "jsp/niceRequest";
    }

    /*nice 본인인증 성공시*/
    @RequestMapping("/niceSuccess")
    @ResponseBody
    public void getNiceSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) throws ResourceNotFoundException, IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        CPClient niceCheck = new  CPClient();
        System.out.println("================\n test >>> nice success \n================");
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");
        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sResponseNumber = "";		// 인증 고유번호
        String sAuthType = "";				// 인증 수단
        String sName = "";					// 성명
        String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
        String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
        String sBirthDate = "";				// 생년월일(YYYYMMDD)
        String sGender = "";				// 성별
        String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
        String sMobileNo = "";				// 휴대폰번호
        String sMobileCo = "";				// 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
        String session_sRequestNumber = null;
        System.out.println("nice response ) iReturn : "+iReturn);
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);

            sRequestNumber  = (String)mapresult.get("REQ_SEQ");
            sResponseNumber = (String)mapresult.get("RES_SEQ");
            sAuthType		= (String)mapresult.get("AUTH_TYPE");
            sName			= (String)mapresult.get("NAME");
            //sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate		= (String)mapresult.get("BIRTHDATE");
            sGender			= (String)mapresult.get("GENDER");
            sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
            sDupInfo		= (String)mapresult.get("DI");
            sConnInfo		= (String)mapresult.get("CI");
            sMobileNo		= (String)mapresult.get("MOBILE_NO");
            sMobileCo		= (String)mapresult.get("MOBILE_CO");

            session_sRequestNumber = sRequestNumber;
            System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
            if(!sRequestNumber.equals(session_sRequestNumber))
            {
                sMessage = "세션값 불일치 오류입니다.";
                sResponseNumber = "";
                sAuthType = "";
            }
        }
        else if( iReturn == -1){sMessage = "복호화 시스템 오류입니다.";}
        else if( iReturn == -4)        {            sMessage = "복호화 처리 오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        if(!sMessage.equals("")){response.sendRedirect("https://fishkingapp.com/cust/member/signup?restore=Y&memberId="+session_sRequestNumber+"&msg=niceResultParsingError"); return;}
        /*데이터 저장*/
        String encodedSessionToken = memberService.niceSuccess(session_sRequestNumber, sResponseNumber, sName, sMobileNo, sGender);
        if(encodedSessionToken == null){//해당 번호로 가입한회원이 이미 존재하는 경우.
            response.sendRedirect("https://fishkingapp.com/cust/member/signup?restore=Y&memberId="+session_sRequestNumber+"&msg=dupPhone"); return;
        }
        System.out.println("================\n test >>> encodedSesstionToken : "+encodedSessionToken+"\n================");
        response.sendRedirect("https://fishkingapp.com/cust/main/home?loggedIn=true&accesstoken="+encodedSessionToken);
    }
    /*nice 인증 실패시*/
    @RequestMapping("/niceFail")
    public void getNiceFail(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ResourceNotFoundException, IOException {
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
        System.out.println("================\n test >>> nice fail \n================");
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");
        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sErrorCode = "";				// 인증 결과코드
        String sAuthType = "";				// 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);//데이터 복호화.
        System.out.println("nice response ) iReturn : "+iReturn);
        //복호화 결과 코드 확인.
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);
            sRequestNumber 	= (String)mapresult.get("REQ_SEQ");
            sErrorCode 		= (String)mapresult.get("ERR_CODE");
            sAuthType 		= (String)mapresult.get("AUTH_TYPE");
        }
        else if( iReturn == -1)        {            sMessage = "복호화 시스템 에러입니다.";        }
        else if( iReturn == -4)        {            sMessage = "복호화 처리오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        /*인증 실패시 데이터 삭제*/
//        memberService.niceFail(Long.parseLong(sRequestNumber));

        response.sendRedirect("https://fishkingapp.com/cust/member/signup?restore=Y&memberId="+sRequestNumber+"&msg=certificationFail");
    }

    /*회원가입 중간단계 - 회원정보입력 - 출조용*/
    @ApiOperation(value = "회원정보입력 완료 및 pass인증 요청",notes = "" +
            "회원가입 단계중, 회원정보 입력후 pass인증 요청 api. pass인증버튼 클릭시 호출, 이전단계에서 입력한 회원정보를 함께 보내주어야한다. \n" +
            "회원정보 임시저장 및 pass인증을 호출해 준다. \n" +
            "요청 필드 ) \n" +
            "- memberId : Long / 선택 / sns를 통해 회원가입진행중이거나 본인인증 재시도하는 경우 입력. \n" +
            "\tㄴ sns로그인 후 리다이렉트 url에 있는 파라미터 또는 본인인증 실패로 redirect된 url의 url파라미터 'memberId'를 입력해주면된다.\n" +
            "- email : String / 필수 / 이메일. 이메일 형식에 맞아야한다\n" +
            "- pw : String / 필수 / 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "- nickName : String / 필수 / 닉네임. 4~10자. \n" +
            "" )
    @PostMapping("/signUp/infoAndPassRequest/smartfishing")
    public String insertMemberInfoForSmartfishing(
//            @RequestBody SignUpDto dto,
            @RequestParam(value = "memberId",required = false) Long Id,
            @RequestParam("email") String email,
            @RequestParam("pw") String pw,
            @RequestParam("nickName") String nickName,
            @RequestParam(value = "registrationToken",required = false) String registrationToken,
            ModelMap model,
            HttpSession session,
            HttpServletResponse response
    ) throws ResourceNotFoundException, IOException, ServiceLogicException {
        SignUpDto dto = SignUpDto.builder()
                .memberId(Id)
                .email(email)
                .pw(pw)
                .nickName(nickName)
                .registrationToken(registrationToken)
                .build();
        //입력값 검증
        if(email == null || email.equals("") || !email.matches(Constants.EMAIL)  || email.length() > 50
                || pw == null || pw.equals("") || !pw.matches(Constants.PW) || pw.length() < 8 || pw.length() > 100
                || nickName==null || nickName.equals("") || !nickName.matches(Constants.STRING) || nickName.length() < 2 || nickName.length() > 10
        ){
            return "redirect:https://fishkingapp.com/cust/member/signup?iscompany=Y&msg=inValidSignUpValue";
        }

        /*회원정보 저장. */
        Long memberId = memberService.insertMemberInfo(dto);

        if(memberId == -1L){return "redirect:https://fishkingapp.com/cust/member/signup?iscompany=Y&msg=inValidSignUpValue";}
        /*nice 본인인증 호출. */
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = memberId.toString();        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
//        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "M";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String sReturnUrl = "https://www.fishkingapp.com/v2/api/niceSuccess/smartfishing";      // 성공시 이동될 URL
        String sErrorUrl = "https://www.fishkingapp.com/v2/api/niceFail/smartfishing";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);//보낼 데이터 암호화.
        //암호화 결과 코드 확인.
        if( iReturn == 0 )        {            sEncData = niceCheck.getCipherData();        }
        else if( iReturn == -1)        {            sMessage = "암호화 시스템 에러입니다.";        }
        else if( iReturn == -2)        {            sMessage = "암호화 처리오류입니다.";        }
        else if( iReturn == -3)        {            sMessage = "암호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        model.addAttribute("sMessage",sMessage);
        model.addAttribute("sEncData",sEncData);

        return "jsp/niceRequest";
    }

    @GetMapping("/niceRequest/smartfishing")
    public String niceRequestForSmartFishing(@RequestParam("memberId") Long memberId, ModelMap model, HttpSession session){
        /*nice 본인인증 호출. */
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = memberId.toString();        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
//        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "M";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String sReturnUrl = "https://www.fishkingapp.com/v2/api/niceSuccess/smartfishing";      // 성공시 이동될 URL
        String sErrorUrl = "https://www.fishkingapp.com/v2/api/niceFail/smartfishing";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);//보낼 데이터 암호화.
        //암호화 결과 코드 확인.
        if( iReturn == 0 )        {            sEncData = niceCheck.getCipherData();        }
        else if( iReturn == -1)        {            sMessage = "암호화 시스템 에러입니다.";        }
        else if( iReturn == -2)        {            sMessage = "암호화 처리오류입니다.";        }
        else if( iReturn == -3)        {            sMessage = "암호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        model.addAttribute("sMessage",sMessage);
        model.addAttribute("sEncData",sEncData);

        return "jsp/niceRequest";
    }

    /*nice 본인인증 성공시 - 스마트 출조용*/
    @RequestMapping("/niceSuccess/smartfishing")
    @ResponseBody
    public void getNiceSuccessForSmartfishing(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) throws ResourceNotFoundException, IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        CPClient niceCheck = new  CPClient();
        System.out.println("================\n test >>> nice success \n================");
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");
        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sResponseNumber = "";		// 인증 고유번호
        String sAuthType = "";				// 인증 수단
        String sName = "";					// 성명
        String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
        String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
        String sBirthDate = "";				// 생년월일(YYYYMMDD)
        String sGender = "";				// 성별
        String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
        String sMobileNo = "";				// 휴대폰번호
        String sMobileCo = "";				// 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
        String session_sRequestNumber = null;
        System.out.println("nice response ) iReturn : "+iReturn);
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);

            sRequestNumber  = (String)mapresult.get("REQ_SEQ");
            sResponseNumber = (String)mapresult.get("RES_SEQ");
            sAuthType		= (String)mapresult.get("AUTH_TYPE");
            sName			= (String)mapresult.get("NAME");
            //sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate		= (String)mapresult.get("BIRTHDATE");
            sGender			= (String)mapresult.get("GENDER");
            sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
            sDupInfo		= (String)mapresult.get("DI");
            sConnInfo		= (String)mapresult.get("CI");
            sMobileNo		= (String)mapresult.get("MOBILE_NO");
            sMobileCo		= (String)mapresult.get("MOBILE_CO");

            session_sRequestNumber = sRequestNumber;
            System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
            if(!sRequestNumber.equals(session_sRequestNumber))
            {
                sMessage = "세션값 불일치 오류입니다.";
                sResponseNumber = "";
                sAuthType = "";
            }
        }
        else if( iReturn == -1){sMessage = "복호화 시스템 오류입니다.";}
        else if( iReturn == -4)        {            sMessage = "복호화 처리 오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        if(!sMessage.equals("")){
//            response.sendRedirect("/cust/member/signup?restore=Y&memberId="+session_sRequestNumber); return;
            response.sendRedirect("https://fishkingapp.com/cust/member/signup?iscompany=Y&restore=Y&memberId="+session_sRequestNumber+"&msg=niceResultParsingError"); return;
        }
        /*데이터 저장*/
        String encodedSessionToken = memberService.niceSuccess(session_sRequestNumber, sResponseNumber, sName, sMobileNo, sGender);
        if(encodedSessionToken == null){//해당 번호로 가입한회원이 이미 존재하는 경우.
//            response.sendRedirect("/cust/member/signup?restore=Y&memberId="+session_sRequestNumber); return;
            response.sendRedirect("https://fishkingapp.com/cust/member/signup?iscompany=Y&restore=Y&memberId="+session_sRequestNumber+"&msg=dupPhone"); return;
        }
        else{//번호중복이 없는 경우
            String sessionToken = AES.aesDecode(encodedSessionToken,env.getProperty("encrypKey.key"));
            Member member = memberService.getMemberBySessionToken(sessionToken);
            Company company = companyRepository.findByMember(member);
            if(company == null){response.sendRedirect("https://fishkingapp.com/smartfishing/apply?loggedIn=true&accesstoken="+encodedSessionToken);}
            else if(company.getIsRegistered() == true){response.sendRedirect("https://fishkingapp.com/smartfishing/dashboard?loggedIn=true&accesstoken="+encodedSessionToken);}
            else if(company.getIsRegistered() == false){response.sendRedirect("https://fishkingapp.com/smartfishing/login?msg=standByApproval");}
            else{response.sendRedirect("https://fishkingapp.com/smartfishing/login?msg=error");}
        }
//        System.out.println("================\n test >>> encodedSesstionToken : "+encodedSessionToken+"\n================");
//        response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+encodedSessionToken);
    }
    /*nice 인증 실패시 - 스마트 출조용*/
    @RequestMapping("/niceFail/smartfishing")
    public void getNiceFailForSmartfishing(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ResourceNotFoundException, IOException {
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
        System.out.println("================\n test >>> nice fail \n================");
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");
        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sErrorCode = "";				// 인증 결과코드
        String sAuthType = "";				// 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);//데이터 복호화.
        System.out.println("nice response ) iReturn : "+iReturn);
        //복호화 결과 코드 확인.
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);
            sRequestNumber 	= (String)mapresult.get("REQ_SEQ");
            sErrorCode 		= (String)mapresult.get("ERR_CODE");
            sAuthType 		= (String)mapresult.get("AUTH_TYPE");
        }
        else if( iReturn == -1)        {            sMessage = "복호화 시스템 에러입니다.";        }
        else if( iReturn == -4)        {            sMessage = "복호화 처리오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        /*인증 실패시 데이터 삭제*/
//        memberService.niceFail(Long.parseLong(sRequestNumber));

//        response.sendRedirect("/cust/member/signup?restore=Y&memberId="+sRequestNumber);
        response.sendRedirect("https://fishkingapp.com/cust/member/signup?iscompany=Y&restore=Y&memberId="+sRequestNumber+"&msg=certificationFail");
    }

    /*비밀번호 찾기(재설정) 인증.
     * - 번호누르고 발송버튼 누르면 들어오는 요청. */
    @ApiOperation(value = "비밀번호 찾기(재설정) 또는 아이디찾기를 위한 문자인증",notes = "" +
            "- 비밀번호 재설정시 또는 아이디 찾기를 위해 문자인증요청하는 api. \n" +
            "- 요청필드 )\n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "- 응답 ) 문자인증건의 id ")
    @PostMapping("/findPw/smsAuthReq")
    @ResponseBody
    public Long requestSmsAuthForPwSearch(@RequestBody @Valid PhoneAuthDto dto) throws ServiceLogicException {
        return memberService.sendSmsForPwReset(dto);
    }
    /*아이디확인 및 비밀번호 재설정 - 인증번호 통과후 이름과 아이디 알려주는 api. */
    @ApiOperation(value = "아아디 확인 및 비밀번호 재설정 - 이름, 아이디 확인",notes = "" +
            "요청필드 ) \n" +
            "- phoneAuthId : Long / 문자인증건의 id\n" +
            "응답 필드 ) \n" +
            "- memberName : String / 회원실명\n" +
            "- uid : String / 회원 아이디\n")
    @PutMapping("/findPw/uid")
    @ResponseBody
    public CheckNameAndUidDto getNameAndUid(
            @RequestBody @Valid CheckUidDto dto
    ) throws ResourceNotFoundException {
        return memberService.getNameAndUid(dto);
    }
    /*비밀번호 찾기 이후, 재설정.
     * - 핸드폰인증 후 뜨는 비번변경창에서 새 비밀번호입력후 변경클릭하면 들어오는 요청.
     * - 세션토큰에 해당하는 member의 비번을 새 비밀번호로 변경*/
    @ApiOperation(value = "비밀번호 변경",notes = "" +
            "- 비밀번호 재설정에서 문자인증 통과후, 비번을 재설정하는 api.\n" +
            "- 요청 필드 )\n" +
            "   newPw : String / 새 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "   phoneAuthId : 문자인증건의 id")
    @PutMapping("/findPw/updatePw")
    @ResponseBody
    public boolean updatePw(
            @RequestBody @Valid ResetPwDto resetPwDto
    ) throws ResourceNotFoundException {
        return memberService.updatePw(resetPwDto);
    }

    /*아이디 찾기*/
    /*@ApiOperation(value = "아이디 찾기",notes = "" +
            "- 문자인증 이후, 아이디 반환해주는 api. \n ")
    @GetMapping("/findId/smsAuthReq")
    public String findId(@RequestParam Long phoneAuthId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.findId(phoneAuthId, token);
    }*/

    /*로그인*/
    @ApiOperation(value = "로그인",notes = "" +
            "로그인 성공시, 일반적인 회원인 경우 세션토큰반환. 관리자에 의해 임시로 생성된 회원인 경우 본인인증페이지로 이동시킨다. \n" +
            "요청 필드 ) \n" +
            "- memberId : String / 필수 / 회원 아이디\n" +
            "- password : String / 필수 / 비밀번호\n" +
            "- registrationToken : String / 선택 / 푸쉬알림을 위한 기기의 등록 토큰\n" +
            "응답 필드 ) \n" +
            "- auth : Boolean / 인증된 회원인지 여부\n" +
            "- token : String / 인증된 회원이고, 로그인 성공시 세션토큰\n" +
            "- memberId : Long / 회원의 id. 미인증회원일시 nice인증 요청보낼때 memberId 파라미터로 추가되어야하는 값. \n")
    @PostMapping("/login")
    @ResponseBody
    public LoginResultDto login(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException, ServiceLogicException {
        LoginResultDto resultDto = memberService.login(loginDTO);
//        if(resultDto.getAuth() == false){ return "redirect:/v2/api/niceRequest?memberId="+resultDto.getMemberId().toString();}
//        else return "forward:/v2/api/loginSuccess?sessionToken="+resultDto.getSessionToken();
        return resultDto ;
    }
//    @PostMapping("/loginSuccess")
//    @ResponseBody
//    public String loginSuccess(@RequestParam("sessionToken") String token){return token;}

    @ApiOperation(value = "관리자 로그인")
    @PostMapping("/admin/login")
    @ResponseBody
    public String adminLogin(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException, ServiceLogicException {
        return memberService.adminLogin(loginDTO);
    }

    @ApiOperation(value = "스마트출조 로그인",notes = "" +
            "auth = false인 경우, 본인인증이 완료되지 않은 회원이므로, 본인인증 api호출. {GET} /v2/api/niceRequest/smartfishing호출. \n" +
            "auth = true 이고, isRegistered = false인 경우, 본인인증은 되어있으나 업체등록요청 승인이 안된회원이므로 " +
            "알림창 후, 초기화면으로\n" +
            "auth = true, isRegistered = true인 경우, 본인인증과 업체등록승인이 된회원이므로 로그인처리 후 메인화면으로.\n" +
            "그 외 오류시, msg필드로 로그인 실패의 이유를 반환. \n" +
            "요청 필드 ) \n" +
            "- memberId : String / 필수 / 회원 아이디\n" +
            "- password : String / 필수 / 비밀번호\n" +
            "- registrationToken : String / 선택 / 푸쉬알림을 위한 기기의 등록 토큰\n" +
            "응답 필드 ) \n" +
            "- auth : Boolean / 인증된 회원인지 여부\n" +
            "- isRegistered : Boolean / 해당 회원" +
            "- token : String / 인증된 회원이고, 로그인 성공시 세션토큰\n" +
            "- memberId : Long / 회원의 id. 미인증회원일시 nice인증 요청보낼때 memberId 파라미터로 추가되어야하는 값. \n")
    @PostMapping("/smartfishing/login")
    @ResponseBody
//    public String smartfishingLogin(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException, NotAuthException, ServiceLogicException {
    public LoginResultDtoForSmartFishing smartfishingLogin(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException, NotAuthException, ServiceLogicException {
        LoginResultDtoForSmartFishing result = memberService.smartfishingLogin(loginDTO);
        return result;
//        return result.getToken();
    }

    @ApiOperation(value = "스마트승선 로그인",notes = "" +
            "요청 필드 ) \n" +
            "- memberId : String / 필수 / 회원 아이디\n" +
            "- password : String / 필수 / 비밀번호\n" +
            "- registrationToken : String / 선택 / 푸쉬알림을 위한 기기의 등록 토큰\n")
    @PostMapping("/smartsail/login")
    @ResponseBody
    public String smartsailLogin(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException, NotAuthException, ServiceLogicException {
        return memberService.smartfishingLogin(loginDTO).getToken();
    }

    @ApiOperation(value = "해경 로그인",notes = "" +
            "요청 필드 ) \n" +
            "- memberId : String / 필수 / 회원 아이디\n" +
            "- password : String / 필수 / 비밀번호\n" +
            "- registrationToken : String / 선택 / 푸쉬알림을 위한 기기의 등록 토큰\n")
    @PostMapping("/police/login")
    @ResponseBody
    public String policeLogin(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException, NotAuthException, ServiceLogicException {
        return memberService.policeLogin(loginDTO).getToken();
    }


    /*로그아웃*/
    @ApiOperation(value = "로그아웃")
    @PostMapping("/logout")
    @ResponseBody
    public boolean logout(
            @RequestHeader("Authorization") String token,
            @RequestBody LogoutDto dto
    ) throws ResourceNotFoundException {
        return memberService.logout(token, dto.getRegistrationToken());
    }

    /*pass인증 callback url*/
//    @ApiOperation(value = "pass인증 callback url",notes = "" +
//            "회원가입 단계 마지막 본인인증 단계에서 사용하는 pass의 callback url.\n" +
//            "실명과 핸드폰번호 데이터를 가지고 회원가입 처리 후, 로그인 처리.  \n" +
//            "")
//    @RequestMapping("/passAuthCode")
//    @ResponseBody
//    public void getPassAuthCode(
//            @RequestParam(value = "code",required = false) String code,
//            @RequestParam(value = "state",required = false) String state,
//            @RequestParam(value = "error",required = false) String error,
//            @RequestParam(value = "message",required = false) String message,
//            HttpServletResponse response
//    ) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ResourceNotFoundException {
//        String sessionToken = memberService.passAuth(code,state,error,message);
//
//        /*!!!!!form을 사용해 post로 프런트의 페이지로 보내주는 jsp로 이동. view사용하여 model에 세션코드저장해서보내줌.*/
//        response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+sessionToken);
//
//        return ;
//    }

    /*kakao 인증코드 받는 메소드. */
    @ApiOperation(value = "kakao 로그인 인증코드 받는 api",notes = "")
    @RequestMapping("/kakaoAuthCode")
    public void getKakaoAuthCode(
            @RequestParam(value = "code",required = false) String code,
            @RequestParam(value = "state",required = false) String state,
            @RequestParam(value = "error",required = false) String error,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            Model model,
            HttpServletResponse response
    ) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SnsLoginResponseDto dto = memberService.snsLoginForKakao(code, state, error, errorDescription);

        if(dto.getIsError()){
            response.sendRedirect("/cust/member/login?error="+dto.getErrorCode()+"&errorMessage="+dto.getErrorMessage());
        }
        else if(dto.getResultType().equals("signUp")){
//            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());
        }
        else{
            model.addAttribute("accessToken",dto.getSessionToken());
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력. ~/member/signup로 보내라고함.
        }
        return;
    }
    /*페이스북 인증코드 받는 메소드*/
    @ApiOperation(value = "페이스북 인증 코드 받는 api",notes = "" +
            "")
    @GetMapping("/facebookAuthCode")
    public void getFacebookAuthCode(
//            @RequestParam("response_type") String responseType,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_reason", required = false) String errorReason,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            HttpServletResponse response
    ) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if(error != null){
//            if(errorReason.equals("user_denied")){//사용자가 페북로그인 취소한경우, 로그인페이지로 되돌려보냄.
            response.sendRedirect("/cust/member/login?error="+errorReason+"&errorMessage="+errorDescription);
            return;
//            }
        }
        SnsLoginResponseDto dto = memberService.snsLoginForFacebook(code);

        if(dto.getResultType().equals("signUp")){
//            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());
        }
        else{
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;
    }

    /*naver 인증코드받는 메소드
     * - 인증코드를 받고 접근코드 요청을 보냄. */
    @ApiOperation(value = "naver 로그인 인증코드 받는 api",notes = "" +
            "처음 로그인하는 sns계정일 경우 ) \n" +
            "- 회원가입페이지로 리다이렉트.\n" +
            "- 리다이렉트url에 query string 파라미터로 'memberId' 전송. 현재 회원가입 진행중인 member의 id이다.  \n" +
            "이미 회원가입 되어있는 sns계정일 경우 )\n" +
            "- 메인페이지로 리다이렉트.\n" +
            "- ????? \n"
            /*"- 응답 필드 )\n" +
            "   snsType : String / sns종류\n" +
            "   resultType : String / sns연동 로그인 응답 종류\n" +
            "       ㄴ login : 해당 sns로 이미가입되어있어서 로그인이 됨을 의미. \n" +
            "       ㄴ signUp : 해당 sns로 가입되어있지않아 회원가입이 필요함을 의미. \n" +
            "   snsId : resultType이 'signUp'일 경우 값존재. sns로 로그인 연동시 sns으로부터 발급받는 id이다. 회원가입시 같이 넘겨주어야한다.\n" +
            "   sessionToken : resultType이 'login'인 경우 값존재. 로그인된 세션토큰이다. \n "*/)
    @RequestMapping("/naverAuthCode")
    public void getNaverAuthCode(
            @RequestParam(value = "code",required = false) String code,
            @RequestParam("state") String state,
            @RequestParam(value = "error",required = false) String error,
            @RequestParam(value = "error_description",required = false) String errorDescription,
            HttpServletResponse response
    ) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SnsLoginResponseDto dto = memberService.snsLoginForNaver(code,state,error,errorDescription);

        if(dto.getIsError()){
            response.sendRedirect("/cust/member/login?error="+dto.getErrorCode()+"&errorMessage="+dto.getErrorMessage());
        }
        else if(dto.getResultType().equals("signUp")){
//            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());
        }
        else{
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;
    }

    /*애플 로그인 받는 메소드*/
    @ApiOperation(value = "애플 로그인 리다이렉트 메소드",notes = "")
    @PostMapping("/appleAuthCode")
    public void getAppleAuthCode(
            AppleAuthCodeDto request,
            HttpServletResponse response
    ) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, ParseException, IllegalBlockSizeException {
        System.out.println("in /v2/api/appleAuthCode");
        SnsLoginResponseDto dto = memberService.snsLoginForApple(request.getId_token());

        if(dto.getResultType().equals("signUp")){
//            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());
        }
        else{
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;
    }

    /*사용자 프로필 정보 페이지 조회
     * - 프로필 조회 요청이 들어오면, 보고자하는 프로필의 member가 자기자신인지, 다른 일반 사용자인지, 업체인지에 따라 조금씩
     *   다른 정보가 들어있는 DTO를 반환해준다. */
    @ApiOperation(value = "사용자 프로필 정보 조회",notes = "상대방 프로필을 클릭할시 해당 사용자의프로필 정보를 가져오는 api. \n" +
            "- 클릭한 프로필의 사용자가 자기자신인지, 업주 회원인지, 일단 다른 회원인지에 따라 조금씩 다른 정보를 반환한다. \n" +
            "- 요청 필드 )\n" +
            "   userId : 프로필을 보고자하는 회원의 id \n" +
            "   헤더에 세션 토큰 (선택)\n" +
            "- 응답 필드 ) \n" +
            "   memberId : Long / 회원id \n" +
            "   nickName : String / 회원 닉네임 \n" +
            "   profileImage : String / 회원 프로필 이미지 url\n" +
            "   backgroundImage : String / 배경이미지 url" +
            "   postCount : Integer / 작성글수. 리뷰, 조항일지, 조행기 등 본인이 작성한 글의 개수. \n" +
            "   likeCount : Integer / 좋아요수. 업체회원일경우, 글 등에서 받은 좋아요수 / 일반회원일경우, 회원이 좋아요 누른 수.\n" +
            "   isMe : boolean / 현재 프로필이 내 프로필인지 여부. \n" +
            "   takeCount : Integer / 업체 찜 수. 업체회원인경우, 받은 찜수. / 일반회원인경우, 회원이 찜한 수.\n" +
            "   isCompany : boolean / 업체의 프로필인지 여부. \n" +
            "   companyId : Long / 업체의 id\n")
    @GetMapping("/profile")
    @ResponseBody
    public UserProfileDTO getUserProfile(
            @RequestParam("userId") Long userId,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return memberService.getUserProfile(userId, token);
    }

    /*상대방 글 보기 api*/
    @ApiOperation(value = "상대방 글 보기",notes = "" +
            "요청 필드 ) \n" +
            "- userId : 상대방 id\n" +
            "- 헤더에 세션토큰 (선택)\n" +
            "응답 필드 )\n" +
            "- id : 게시글의 id \n" +
            "- profileImage : 작성자 프로필 사진 download url \n" +
            "- address : 게시글의 대상 선상의 주소 \n" +
            "- shipId : 게시글 대상 상품의 선상id\n" +
            "- memberId : 작성자 id \n" +
            "- nickName : 작성자 닉네임\n" +
            "- fishingType : 선상인지 갯바위인지 \n" +
            "\tㄴ ship : 선상\n" +
            "\tㄴ sealocks : 갯바위\n " +
            "- isLikeTo : 게시글에 대한 좋아요 여부\n" +
            "- createdDate : 작성일자 \n" +
            "- likeCount : 좋아요 수 \n" +
            "- commentCount : 댓글 수 \n" +
            "- scrapCount : 스크랩 수 \n" +
            "- title : 글 제목\n" +
            "- contents : 게시글 내용(일부만 출력)\n" +
            "- fishingDiaryType : String / 글이 조항일지인지, 유저조행기인지\n" +
            "- fileList : 이미지 파일 download url 리스트")
    @GetMapping("/profile/fishingDiary/{page}")
    @ResponseBody
    public Page<FishingDiaryDtoForPage> getUserFishingDiary(
            @RequestParam("userId") Long userId,
            @PathVariable("page") int page,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return memberService.getUserFishingDiary(userId,page,token);
    }

    /*프로필 관리 페이지 조회.
     * - member의 프로필이미지, uid, nickName, 상태메세지, 휴대폰번호, 이메일 정보가 든 dto반환. */
    @ApiOperation(value = "프로필 관리 페이지 조회",notes = "프로필 관리를 위한 자기자신의 프로필 정보를 가져온다. " +
            "요청 필드 ) \n" +
            "- 헤더에 세션토큰\n" +
            "응답 필드 ) \n" +
            "- id : Long / 회원의 id\n" +
            "- uid : String / 회원의 uid. 로그인할때 입력하는 id.\n" +
            "- memberName : String / 회원 실명\n" +
            "- email : String / 회원 email.\n" +
            "- profileImage : String / 프로필사진 url\n" +
            "- profileBackgroundImage : String / 프로필 배경 사진 url\n" +
            "- statusMessage : String / 상태메시지\n" +
            "- areaCode : String / 전화번호 앞부분(ex 010 ) \n" +
            "- localNumber : String / 전화번호 뒷부분 ( ex 12345678 )\n")
    @GetMapping("/profileManage")
    @ResponseBody
    public ProfileManageDTO getProfileManage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.getProfileManage(token);
    }

    /*프사변경
     * - !!!!!정확하게는 모르겠지만, 클라이언트가 자기사진을 올려서 요청을 보내는형식이 될거라고생각됨.
     * - 따라서 여기선 이미지파일을 파라미터로 받아서 프사로 업데이트해주는 기능 구현.
     * - 반환값 : 성공,실패여부 */
    @ApiOperation(value = "사용자 프로필사진 변경")
    @PutMapping("/profileManage/profileImage")
    @ResponseBody
    public boolean updateProfileImage(
            @RequestBody ModifyingProfileImgDto dto,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        return memberService.updateProfileImage(dto,sessionToken);
    }
    /*프사 없애기.*/
    @ApiOperation(value = "프사 없애기",notes = "" +
            "- 프로필 사진은 내립니다. \n" +
            "- 반환 ) 성공시 : true / 실패시 : false ")
    @PutMapping("/profileManage/noProfileImage")
    @ResponseBody
    public boolean deleteProfileImage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.deleteProfileImage(token);
    }

    /*프로필 배경 이미지 변경*/
    @ApiOperation(value = "프사 배경이미지 변경", notes = "")
    @PutMapping("/profileManage/profileBackgroundImage")
    @ResponseBody
    public boolean updateProfileBackgroundImage(
            @RequestBody ModifyingProfileImgDto dto,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        return memberService.updateProfileBackgroundImage(dto,sessionToken);
    }
    /*프사배경이미지 없애기.*/
    @ApiOperation(value = "프사배경이미지 없애기",notes = "" +
            "- 프로필 배경사진을 내립니다. \n" +
            "- 반환 ) 성공시 : true / 실패시 : false ")
    @PutMapping("/profileManage/noProfileBackgroundImage")
    @ResponseBody
    public boolean deleteProfileBackgroundImage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.deleteProfileBackgroundImage(token);
    }

    /*닉네임 변경*/
    @ApiOperation(value = "닉네임 변경")
    @PutMapping("/profileManage/nickName")
    @ResponseBody
    public String modifyProfileNickName(
            @RequestBody @Valid ModifyingNickNameDto nickName,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileNickName(sessionToken, nickName);
    }

    /*상태 메세지 변경*/
    @ApiOperation(value = "상태메세지 변경")
    @PutMapping("/profileManage/statusMessage")
    @ResponseBody
    public String modifyProfileStatusMessage(
            @RequestBody @Valid ModifyingStatusMessageDto statusMessage,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileStatusMessage(sessionToken, statusMessage);
    }

    /*이메일 변경*/
    @ApiOperation(value = "이메일 변경",notes = "" +
            "- 이메일만 변경되며, 로그인시 사용하는 uid는 변경되지 않습니다. ")
    @PutMapping("/profileManage/email")
    @ResponseBody
    public String modifyProfileEmail(
            @RequestBody @Valid ModifyingEmailDto email,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileEmail(sessionToken, email);
    }

    /*비번 변경
     * - 현재 비밀번호와 변경할 비번이 들어온다. */
    @ApiOperation(value = "비번 변경")
    @PutMapping("/profileManage/password")
    @ResponseBody
    public boolean modifyProfilePassword(
            @RequestBody @Valid ModifyProfilePwDto dto,
            HttpServletRequest request
    ) throws ResourceNotFoundException, ServiceLogicException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfilePassword(sessionToken, dto.getCurrentPw(),dto.getNewPw());
    }

    /*탈퇴하기
     * 삭제처리된 member의 id를 반환. */
    @ApiOperation(value = "탈퇴하기")
    @DeleteMapping("/profileManage/delete")
    @ResponseBody
    public Long deleteMember(
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.inactivateMember(sessionToken);
    }

    /*휴대폰 번호 변경 문자인증요청*/
    /*@ApiOperation(value="휴대폰 번호 변경 문자인증api", notes = "" +
            "- 휴대폰 번호 변경을 위한 문자인증 요청 api")
    @PostMapping("/profileManage/sendSms")
    public Long sendSmsForModifyingPhoneNumber(@RequestBody PhoneAuthForModifyPhoneNumber dto, @RequestHeader("Authorization") String token){
        PhoneAuthDto phoneAuthDto = PhoneAuthDto.builder().areaCode(dto.getAreaCode()).localNumber(dto.getLocalNumber()).build();
        return memberService.sendSmsForSignup(phoneAuthDto);
    }*/
    /*휴대폰 번호 변경*/
    /*@ApiOperation(value = "휴대폰 번호 변경 api",notes = "" +
            "- 문자인증 후, 휴대폰 번호 변경하는 api\n" +
            "- 반환 ) 변경된 번호. ")
    @PutMapping("/profileManage/phoneNumber")
    public String modifyPhoneNumber(@RequestBody ModifyPhoneNumberDto dto, @RequestHeader("Authorization") String token){

    }*/

    /*휴대폰 번호 변경을 위한 문자인증 요청.*/
//    @ApiOperation(value = "휴대폰 번호 변경을 위한 문자인증 요청",notes = "" +
//            "")
//    @PostMapping("/profileManage/phoneNumber/sendSms")
//    @ResponseBody
//    public Long requestSmsAuthForModifyPhoneNum(@RequestBody PhoneAuthDto dto){
//        return memberService.sendSmsAuthForModifyPhoneNum(dto);
//    }
    /*문자인증 후, 휴대폰 번호 변경*/
//    @ApiOperation(value = "문자인증 후, 휴대폰 번호 변경",notes = "")
//    @PutMapping("/profileManage/phoneNumber")
//    @ResponseBody
//    public boolean modifyPhoneNumber(@RequestBody @Valid ModifyPhoneNumberDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
//        return memberService.modifyPhoneNumber(dto,token);
//    }

    //알림설정정보 조회.
    @ApiOperation(value = "설정 > 알림설정 페이지 조회", notes = "" +
            "요청필드 ) \n" +
            "- header : sessionToken\n" +
            "응답 필드 ) \n" +
            "- code : String / codeGroup id 165번에 해당하는 common code들의 code값. \n" +
            "- codeName : String / code의 code name\n" +
            "- isSet : Boolean / 현재 해당 알림이 설정되었는지 여부")
    @GetMapping("/setting/alertSet")
    @ResponseBody
    public List<AlertSetDtoForPage> getAlertSet(
            @RequestHeader("Authorization") String token
    ){
        return memberService.getAlertSet(token);
    }

    //알림 설정
    @ApiOperation(value = "설정 > 알림설정하기",notes = "" +
            "요청필드 ) \n" +
            "- code : String / 필수 / 설정하려는 알림의 code값. codeGroup id 165번에 해당하는 common code의 code값. \n" +
            "- isSet : String / 필수 / 알림을 설정할지 해제할지. 'true' 또는 'false'값을 입력. ")
    @PutMapping("/setting/alertSet/{code}/{isSet}")
    @ResponseBody
    public Boolean modifyAlertSet(
            @RequestHeader("Authorization") String token,
            @PathVariable("code") String code,
            @PathVariable("isSet") String isSet
    ){
        return memberService.modifyAlertSet(token, code, isSet);
    }

    //동영상설정정보 조회.
    @ApiOperation(value = "설정 > 동영상설정 페이지 조회", notes = "" +
            "요청필드 ) \n" +
            "- header : sessionToken\n" +
            "응답 필드 ) \n" +
            "- code : String / codeGroup id 166번에 해당하는 common code들의 code값. \n" +
            "- codeName : String / code의 code name\n" +
            "- isSet : Boolean / 현재 해당 동영상설정 항목의 설정되었는지 여부")
    @GetMapping("/setting/videoSetting")
    @ResponseBody
    public List<VideoSettingDtoForPage> getVideoSetting(
            @RequestHeader("Authorization") String token
    ){
        return memberService.getVideoSetting(token);
    }

    //동영상 설정
    @ApiOperation(value = "설정 > 동영상설정하기",notes = "" +
            "요청필드 ) \n" +
            "- code : String / 필수 / 설정하려는 동영상설정의 code값. codeGroup id 166번에 해당하는 common code의 code값. \n" +
            "- isSet : String / 필수 / 해당 동영상 설정 항목을 설정할지 해제할지. 'true' 또는 'false'값을 입력. ")
    @PutMapping("/setting/videoSetting/{code}/{isSet}")
    @ResponseBody
    public Boolean modifyVideoSetting(
            @RequestHeader("Authorization") String token,
            @PathVariable("code") String code,
            @PathVariable("isSet") String isSet
    ){
        return memberService.modifyVideoSetting(token, code, isSet);
    }

    //고객앱 > 프로필  >폰번호변경
    @GetMapping("/profileManage/phoneNum/niceRequest")
    public String modifyPhoneNumNiceRequest(@RequestParam("token") String token, ModelMap model, HttpSession session){
        Member member = memberService.getMemberBySessionToken(token);
        Long memberId = member.getId();

        session.setAttribute("REQ_SEQ" , memberId.toString());	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
        Map<String, String> map = niceRequest(memberId,
                "https://www.fishkingapp.com/v2/api/profileManage/phoneNum/niceSuccess",
                "https://www.fishkingapp.com/v2/api/profileManage/phoneNum/niceFail");

        model.addAttribute("sMessage",map.get("sMessage"));
        model.addAttribute("sEncData",map.get("sEncData"));

        return "jsp/niceRequest";
    }

    /*고객앱 > 프로필  >폰번호변경 nice 본인인증 성공시*/
    @RequestMapping("/profileManage/phoneNum/niceSuccess")
    @ResponseBody
    public void getNiceSuccessForModifyPhoneNum(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) throws ResourceNotFoundException, IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        CPClient niceCheck = new  CPClient();
        System.out.println("================\n test >>> nice success \n================");
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");

//        niceSuccessParsing(sEncodeData);
        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sResponseNumber = "";		// 인증 고유번호
        String sAuthType = "";				// 인증 수단
        String sName = "";					// 성명
        String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
        String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
        String sBirthDate = "";				// 생년월일(YYYYMMDD)
        String sGender = "";				// 성별
        String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
        String sMobileNo = "";				// 휴대폰번호
        String sMobileCo = "";				// 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
        String session_sRequestNumber = null;
        System.out.println("nice response ) iReturn : "+iReturn);
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);

            sRequestNumber  = (String)mapresult.get("REQ_SEQ");
            sResponseNumber = (String)mapresult.get("RES_SEQ");
            sAuthType		= (String)mapresult.get("AUTH_TYPE");
            sName			= (String)mapresult.get("NAME");
            //sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate		= (String)mapresult.get("BIRTHDATE");
            sGender			= (String)mapresult.get("GENDER");
            sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
            sDupInfo		= (String)mapresult.get("DI");
            sConnInfo		= (String)mapresult.get("CI");
            sMobileNo		= (String)mapresult.get("MOBILE_NO");
            sMobileCo		= (String)mapresult.get("MOBILE_CO");

            session_sRequestNumber = sRequestNumber;
            System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
            if(!sRequestNumber.equals(session_sRequestNumber))
            {
                sMessage = "세션값 불일치 오류입니다.";
                sResponseNumber = "";
                sAuthType = "";
            }
        }
        else if( iReturn == -1){sMessage = "복호화 시스템 오류입니다.";}
        else if( iReturn == -4)        {            sMessage = "복호화 처리 오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        if(!sMessage.equals("")){response.sendRedirect("https://fishkingapp.com/cust/main/my?msg=niceResultParsingError"); return;}
//        if(!sMessage.equals("")){response.sendRedirect("/cust/member/signup?restore=Y&memberId="+session_sRequestNumber); return;}
        /*데이터 저장*/
        Boolean result = memberService.modifyPhoneNum(Long.parseLong(session_sRequestNumber), sMobileNo);
        String token = memberService.getToken(Long.parseLong(session_sRequestNumber));
        if(result){ response.sendRedirect("https://fishkingapp.com/cust/main/my?msg=success&token="+token );}

//        if(encodedSessionToken == null){//해당 번호로 가입한회원이 이미 존재하는 경우.
//            response.sendRedirect("/cust/member/signup?restore=Y&memberId="+session_sRequestNumber); return;
//        }
//        System.out.println("================\n test >>> encodedSesstionToken : "+encodedSessionToken+"\n================");
//        response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+encodedSessionToken);
    }
    /*고객앱 > 프로필  >폰번호변경 nice 인증 실패시*/
    @RequestMapping("/profileManage/phoneNum/niceFail")
    public void getNiceFailForModifyPhoneNum(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ResourceNotFoundException, IOException {
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();
        System.out.println("================\n test >>> nice fail \n================");
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");
        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sErrorCode = "";				// 인증 결과코드
        String sAuthType = "";				// 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);//데이터 복호화.
        System.out.println("nice response ) iReturn : "+iReturn);
        //복호화 결과 코드 확인.
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);
            sRequestNumber 	= (String)mapresult.get("REQ_SEQ");
            sErrorCode 		= (String)mapresult.get("ERR_CODE");
            sAuthType 		= (String)mapresult.get("AUTH_TYPE");
        }
        else if( iReturn == -1)        {            sMessage = "복호화 시스템 에러입니다.";        }
        else if( iReturn == -4)        {            sMessage = "복호화 처리오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        /*인증 실패시 데이터 삭제*/
//        memberService.niceFail(Long.parseLong(sRequestNumber));
        String token = memberService.getToken(Long.parseLong(sRequestNumber));
        response.sendRedirect("https://fishkingapp.com/cust/main/my?msg=niceCertificationFail&token="+token);
    }

    //출조 본인인증
    @ApiOperation(value = "출조앱 > 설정 > 본인인증")
    @GetMapping("/smartfishing/setting/niceRequest")
    public String smartfishingNiceRequest(@RequestParam("token") String token, ModelMap model, HttpSession session) {
        Member member = memberService.getMemberBySessionToken(token);
        Long memberId = member.getId();

        session.setAttribute("REQ_SEQ", memberId.toString());    // 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
        Map<String, String> map = niceRequest(memberId,
                "https://www.fishkingapp.com/v2/api/smartfishing/setting/niceRequestSuccess",
                "https://www.fishkingapp.com/v2/api/smartfishing/setting/niceRequestFail");

        model.addAttribute("sMessage", map.get("sMessage"));
        model.addAttribute("sEncData", map.get("sEncData"));

        return "jsp/niceRequest";
    }

    @RequestMapping("/smartfishing/setting/niceRequestSuccess")
    public void getSmartfishingNiceSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) throws IOException {
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");

        Map<String, String> map = niceSuccessParsing(sEncodeData);
        //nice결과 파싱오류시,
        if(!map.get("sMessage").equals("")){response.sendRedirect("https://fishkingapp.com/smartfishing/dashboard?msg=niceResultParsingError"); return;}
        /*데이터 저장*/
        Boolean result = memberService.smartFishingNiceAuth(Long.parseLong(map.get("memberId")), map.get("phoneNum"), map.get("name"), map.get("gender"));
        //저장이 성공적이라면 출조앱 대시보드로 리다이렉트.
        if(result){ response.sendRedirect("https://fishkingapp.com/smartfishing/dashboard?msg=authSuccess");}
    }

    @RequestMapping("/smartfishing/setting/niceRequestFail")
    public void getSmartFishingNiceFail(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) throws IOException {
        String sEncodeData = memberService.requestReplace(request.getParameter("EncodeData"), "encodeData");
        Map<String, String> map = niceFailParsing(sEncodeData);

        /*인증 실패시 데이터 삭제*/
//        memberService.niceFail(Long.parseLong(sRequestNumber));
        String token = memberService.getToken(Long.parseLong(map.get("memberId")));
        response.sendRedirect("https://fishkingapp.com/smartfishing/dashboard?msg=niceCertificationFail");
    }

    public Map<String, String> niceRequest(Long memberId, String successUrl, String failUrl){
        /*nice 본인인증 호출. */
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = memberId.toString();        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
//        sRequestNumber = niceCheck.getRequestNO(sSiteCode);

        String sAuthType = "M";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String sReturnUrl = successUrl;      // 성공시 이동될 URL
        String sErrorUrl = failUrl;          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);//보낼 데이터 암호화.
        //암호화 결과 코드 확인.
        if( iReturn == 0 )        {            sEncData = niceCheck.getCipherData();        }
        else if( iReturn == -1)        {            sMessage = "암호화 시스템 에러입니다.";        }
        else if( iReturn == -2)        {            sMessage = "암호화 처리오류입니다.";        }
        else if( iReturn == -3)        {            sMessage = "암호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        HashMap<String, String> result = new HashMap<>();
        result.put("sMessage", sMessage);
        result.put("sEncData", sEncData);
        return result;
    }
    public Map<String, String> niceSuccessParsing(String sEncodeData){
        CPClient niceCheck = new  CPClient();

        System.out.println("nice response ) sEncodeData : "+sEncodeData);
        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sResponseNumber = "";		// 인증 고유번호
        String sAuthType = "";				// 인증 수단
        String sName = "";					// 성명
        String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
        String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
        String sBirthDate = "";				// 생년월일(YYYYMMDD)
        String sGender = "";				// 성별
        String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
        String sMobileNo = "";				// 휴대폰번호
        String sMobileCo = "";				// 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
        String session_sRequestNumber = null;
        System.out.println("nice response ) iReturn : "+iReturn);
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);

            sRequestNumber  = (String)mapresult.get("REQ_SEQ");
            sResponseNumber = (String)mapresult.get("RES_SEQ");
            sAuthType		= (String)mapresult.get("AUTH_TYPE");
            sName			= (String)mapresult.get("NAME");
            //sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate		= (String)mapresult.get("BIRTHDATE");
            sGender			= (String)mapresult.get("GENDER");
            sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
            sDupInfo		= (String)mapresult.get("DI");
            sConnInfo		= (String)mapresult.get("CI");
            sMobileNo		= (String)mapresult.get("MOBILE_NO");
            sMobileCo		= (String)mapresult.get("MOBILE_CO");

            session_sRequestNumber = sRequestNumber;
            System.out.println("================\n test >>> sRequestNumber : "+sRequestNumber+"\n================");
            if(!sRequestNumber.equals(session_sRequestNumber))
            {
                sMessage = "세션값 불일치 오류입니다.";
                sResponseNumber = "";
                sAuthType = "";
            }
        }
        else if( iReturn == -1){sMessage = "복호화 시스템 오류입니다.";}
        else if( iReturn == -4)        {            sMessage = "복호화 처리 오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        Map<String, String> result = new HashMap<>();
        result.put("sMessage", sMessage);
        result.put("memberId", sRequestNumber);
        result.put("phoneNum", sMobileNo);
        result.put("gender", sGender);
        result.put("name", sName);

        return result;
    }
    public Map<String, String> niceFailParsing(String sEncodeData){
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";				// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";			// NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sErrorCode = "";				// 인증 결과코드
        String sAuthType = "";				// 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);//데이터 복호화.
        System.out.println("nice response ) iReturn : "+iReturn);
        //복호화 결과 코드 확인.
        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();
            System.out.println("nice response ) sPlainData : "+sPlainData);
            System.out.println("nice response ) sCipherTime : "+sCipherTime);
            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
            System.out.println("nice response ) mapresult : "+mapresult);
            sRequestNumber 	= (String)mapresult.get("REQ_SEQ");
            sErrorCode 		= (String)mapresult.get("ERR_CODE");
            sAuthType 		= (String)mapresult.get("AUTH_TYPE");
        }
        else if( iReturn == -1)        {            sMessage = "복호화 시스템 에러입니다.";        }
        else if( iReturn == -4)        {            sMessage = "복호화 처리오류입니다.";        }
        else if( iReturn == -5)        {            sMessage = "복호화 해쉬 오류입니다.";        }
        else if( iReturn == -6)        {            sMessage = "복호화 데이터 오류입니다.";        }
        else if( iReturn == -9)        {            sMessage = "입력 데이터 오류입니다.";        }
        else if( iReturn == -12)        {            sMessage = "사이트 패스워드 오류입니다.";        }
        else        {            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;        }

        Map<String, String> result = new HashMap<>();
        result.put("memberId", sRequestNumber);
//        result.put()

        return result;
    }
}
