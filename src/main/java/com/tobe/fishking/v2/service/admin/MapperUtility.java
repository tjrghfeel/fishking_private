package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.enums.common.CouponType;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.utils.HolidayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*native query로 인터페이스형dto에 바로 db데이터를 담을때, 해당 데이터에 대해 처리를 해주는 함수들의 모음 클래스.
* - */
@Component
public class MapperUtility {
    @Autowired
    Environment env;
    HolidayUtil holidayUtil;

    /*String형 암호화에 대해 decode하는 함수.
    * - 개인정보들이 암호화되어 db에 들어가있는데 native query로 검색시 암호화된데이터가 그대로 나오는 이슈가있다.
    *   이를 dto interface에서 수동으로 해독을 해주는 메소드이다.  */
    public String decodeString(String columnValue)
            throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if(columnValue==null) return null;
        return AES.aesDecode(columnValue, env.getProperty("encrypKey.key"));
    }

    /*mysql의 group_concat()으로 콤마(,)기준으로 파일이름과 파일path를 받았을때, 이들을 합쳐 해당파일에대한 downloadUrl 배열형태로 반환. 
    * - 파일이름이나 path중 하나라도 null이면, 파일이 없다고 판단하고 null반환. */
    public ArrayList<String>  transFileUrlArray(String fileNameListString, String filePathListString){
        ArrayList<String> result = new ArrayList<String>();
        if(fileNameListString!=null && filePathListString!=null) {
            String[] fileNameListArray = fileNameListString.split(",");
            String[] filePathListArray = filePathListString.split(",");

            for(int i=0; i<fileNameListArray.length; i++){
                result.add(env.getProperty("file.downloadUrl") + "/" + filePathListArray[i] + "/" + fileNameListArray[i]);
            }
        }
        return result;
    }
    public ArrayList<String>  transFileUrlArray(String fileUrlListString){
        ArrayList<String> result = new ArrayList<String>();
        if(fileUrlListString!=null) {
            String[] fileUrlListArray = fileUrlListString.split(",");

            for(int i=0; i<fileUrlListArray.length; i++){
                result.add(env.getProperty("file.downloadUrl") + "/" + fileUrlListArray[i] );
            }
        }
        return result;
    }

    /*도커환경인지 로컬환경인지에 따라 downloadUrl 설정해주는 메소드*/
    public String transDownloadUrl(String inputUrl){
        return env.getProperty("file.downloadUrl") + inputUrl;
    }
    /*path와 file name을 합쳐서 도커환경인지 로컬환경인지에 따라 download url을 만들어주는 메소드 */
    public String transDownLoadUrl(String filePath, String fileName){
        if(filePath!=null && fileName !=null) {
            return env.getProperty("file.downloadUrl") + "/" + filePath + "/" + fileName;
        }
        else return null;
    }

    /*알림 메세지 내용 생성 메소드*/
    public String makeAlertMessage(int alertType, String content){
        String message = AlertType.values()[alertType].getMessage();
        return message+" "+content;
    }
    /*int값을 boolean값으로 반환.*/
    public Boolean transIntToBoolean(int value){return (value == 0)?false:true;}
    /*낚시일과 낚시 시작시간을 합쳐서 반환.*/
    public String transFishingDate(String fishingDate, String fishingStartTime){return fishingDate+" "+fishingStartTime;}

    /*enum값 변환 메소드*/
    public String transEnumQuestionType(Integer ordinal){ if(ordinal!=null){return QuestionType.values()[ordinal].getValue();}else return null; }
    public String transEnumChannelType(Integer ordinal){ if(ordinal!=null){return ChannelType.values()[ordinal].getValue();}else return null; }
    public String transEnumCouponType(Integer ordinal){ if(ordinal!=null){return CouponType.values()[ordinal].getValue();}else return null;}
    public String transEnumFishingType(Integer ordinal){ if(ordinal!=null){return FishingType.values()[ordinal].getValue();}else return null;}
    public String transEnumDependentType(Integer ordinal){if(ordinal!=null){return DependentType.values()[ordinal].getValue();}else return null;}
    public String transEnumOrderStatus(Integer ordinal){if(ordinal!=null){return OrderStatus.values()[ordinal].getValue();}else return null;}
    public String transEnumAlertType(Integer ordinal){if(ordinal!=null){return AlertType.values()[ordinal].getValue();}else return null;}
    public String transEnumFilePublish(Integer ordinal){if(ordinal!=null){return FilePublish.values()[ordinal].getValue();}else return null;}
    public String transEnumMeridiem(Integer ordinal){if(ordinal!=null){return Meridiem.values()[ordinal].getValue();}else return null;}

    /*public String transEnum(int ordinal){
        return .values()[ordinal].getValue();
    }*/

    public String getTide(String date){
        date = date.replace("-","");
        String todayLunar = holidayUtil.convertSolarToLunar(date);
        Integer lunarDay = Integer.parseInt(todayLunar.substring(8));
        Integer tide = (lunarDay+6)%15+1;
        if(tide == 15) return "조금";
        else{return tide.toString()+"물";}
    }





    public String noName(String value1,String value2){
        String result = value1 + ","+value2;
        return result;

    }
}
