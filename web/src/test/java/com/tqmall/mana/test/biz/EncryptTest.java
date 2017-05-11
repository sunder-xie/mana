package com.tqmall.mana.test.biz;

import com.tqmall.mana.component.bean.ConstantBean;
import com.tqmall.mana.component.util.mana.ManaUtil;
import org.apache.shiro.crypto.AesCipherService;
import org.junit.Test;

import java.security.Key;

/**
 * Created by huangzhangting on 16/12/8.
 */
public class EncryptTest {

    @Test
    public void createKey(){
        AesCipherService aesCipherService = new AesCipherService();
        //设置key长度
        aesCipherService.setKeySize(128);
        //生成key
        Key key = aesCipherService.generateNewKey();

        String keyStr = ManaUtil.bytesToStr(key.getEncoded());

        System.out.println(keyStr);
    }

    @Test
    public void encrypt(){
        String key = ConstantBean.ENCRYPT_KEY;
        byte[] keys = ManaUtil.strToBytes(key);
        AesCipherService aesCipherService = new AesCipherService();
        String string = "test99999999";
        //加密
        String text = aesCipherService.encrypt(string.getBytes(), keys).toHex();
        System.out.println(text);
    }

    @Test
    public void testDecrypt(){
        String key = ConstantBean.ENCRYPT_KEY;

        String text = "64931ec41c340925df26b7a5158c509eb83c9fbed5b06f66d44cc15543cbefab";

        String val = ManaUtil.aesDecrypt(text, key);

        System.out.println(val);

    }
}
