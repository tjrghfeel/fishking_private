package com.tobe.fishking.v2.service.admin;

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
}
