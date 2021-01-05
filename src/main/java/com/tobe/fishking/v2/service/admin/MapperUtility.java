package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.enums.common.CouponType;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.service.AES;
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
import java.util.ArrayList;

/*native query로 인터페이스형dto에 바로 db데이터를 담을때, 해당 데이터에 대해 처리를 해주는 함수들의 모음 클래스.
* - */
@Component
public class MapperUtility {
    @Autowired
    Environment env;

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
    public ArrayList<String> transFileUrlArray(String fileNameListString, String filePathListString){
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

    /*도커환경인지 로컬환경인지에 따라 downloadUrl 설정해주는 메소드*/
    public String transDownloadUrl(String inputUrl){
        return env.getProperty("file.downloadUrl") + inputUrl;
    }
    /*path와 file name을 합쳐서 도커환경인지 로컬환경인지에 따라 download url을 만들어주는 메소드 */
    public String transDownLoadUrl(String filePath, String fileName){
        return env.getProperty("file.downloadUrl") + "/" + filePath + "/" + fileName;
    }

    /*알림 메세지 내용 생성 메소드*/
    public String makeAlertMessage(int alertType, String content){
        String message = AlertType.values()[alertType].getMessage();
        return message+" "+content;
    }

    public String noName(String value1,String value2){
        String result = value1 + ","+value2;
        return result;

    }

    /*enum값 변환 메소드*/
    public String transEnumQuestionType(int ordinal){
        return QuestionType.values()[ordinal].getValue();
    }
    public String transEnumChannelType(int ordinal){
        return ChannelType.values()[ordinal].getValue();
    }
    public String transEnumCouponType(int ordinal){
        return CouponType.values()[ordinal].getValue();
    }
    public String transEnumFishingType(int ordinal){
        return FishingType.values()[ordinal].getValue();
    }
    public String transEnumDependentType(int ordinal){
        return DependentType.values()[ordinal].getValue();
    }
    public String transEnumOrderStatus(int ordinal){
        return OrderStatus.values()[ordinal].getValue();
    }
    public String transEnumAlertType(int ordinal){
        return AlertType.values()[ordinal].getValue();
    }
    /*public String transEnum(int ordinal){
        return .values()[ordinal].getValue();
    }*/

}
