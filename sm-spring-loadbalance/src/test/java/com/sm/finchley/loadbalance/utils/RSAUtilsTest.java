package com.sm.finchley.loadbalance.utils;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class RSAUtilsTest {
    /**
     * 用测试生成的公钥，私钥赋值
     */
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFtTlL61IqIGd+fRLUhJ0MjsqFXFJswCohJ45m51WvbxDPRP3gllW0WChk74D5JEOpMDSWo4C7RfoGlBRNW7kQ6qYGukYZ5jgYpzoT0+gp3on96fQXEyQJysv9xiTPIdmSXXVVj1HAOJw29RbzxIVKUSzzPXvEtXRTtCC1+wkAJQIDAQAB";

    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIW1OUvrUiogZ359EtSEnQyOyoVcUmzAKiEnjmbnVa9vEM9E/eCWVbRYKGTvgPkkQ6kwNJajgLtF+gaUFE1buRDqpga6RhnmOBinOhPT6Cneif3p9BcTJAnKy/3GJM8h2ZJddVWPUcA4nDb1FvPEhUpRLPM9e8S1dFO0ILX7CQAlAgMBAAECgYBC4amtbiKFa/wY61tV7pfYRjzLhKi+OUlZmD3E/4Z+4KGZ7DrJ8qkgMtDR3HO5LAikQrare1HTW2d7juqw32ascu+uDObf4yrYNKin+ZDLUYvIDfLhThPxnZJwQ/trdtfxO3VM//XbwZacmwYbAsYW/3QPUXwwOPAgbC2oth8kqQJBANKLyXcdjZx4cwJVl7xNeC847su8y6bPpcBASsaQloCIPiNBIg1h76dpfEGIQBYWJWbBsxtHe/MhOmz7fNFDS2sCQQCiktYZR0dZNH4eNX329LoRuBiltpr9tf36rVOlKr1GSHkLYEHF2qtyXV2mdrY8ZWpvuo3qm1oSLaqmop2rN9avAkBHk85B+IIUF77BpGeZVJzvMOO9z8lMRHuNCE5jgvQnbinxwkrZUdovh+T+QlvHJnBApslFFOBGn51FP5oHamFRAkEAmwZmPsinkrrpoKjlqz6GyCrC5hKRDWoj/IyXfKKaxpCJTH3HeoIghvfdO8Vr1X/n1Q8SESt+4mLFngznSMQAZQJBAJx07bCFYbA2IocfFV5LTEYTIiUeKdue2NP2yWqZ/+tB5H7jNwQTJmX1mn0W/sZm4+nJM7SjfETpNZhH49+rV6U=";

    @Test
    void testGenerateRasKey() {
        // Setup
        final Map<String, String> expectedResult = new HashMap<>();

        // Run the test
        final Map<String, String> result = RSAUtils.generateRasKey();

        // Verify the results
        System.out.println("随机生成的公钥为:" + result.get(RSAUtils.PUBLIC_KEY));
        System.out.println("随机生成的私钥为:" + result.get(RSAUtils.PRIVATE_KEY));
    }

    @Test
    void testEncrypt() {
        //assertThat(RSAUtils.encrypt("str", "publicKey")).isEqualTo("result");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "rosh");
        jsonObject.put("password", "123456");
        String str = jsonObject.toJSONString();
        String encrypt = RSAUtils.encrypt(str, PUBLIC_KEY);
        System.out.println(encrypt);
    }

    @Test
    void testDecrypt() {
        //assertThat(RSAUtils.decrypt("str", "privateKey")).isEqualTo("result");
        String decrypt = RSAUtils.decrypt("Yeidauky/iN1/whevov2+ntzXJKAp2AHfESu5ixnDqH5iB7ww+TcfqJpDfkPHfb12Y0sVXw0gBHNJ4inkh7l2/SJBze3pKQU/mg3oyDokTia3JZIs+e80/iJcSfN+yA1JaqY+eJPYiBiOGAF2S6x0ynvJg/Wj0fwp2Tq3PDzRMo=",
                PRIVATE_KEY);

        System.out.println(decrypt);
    }
}
