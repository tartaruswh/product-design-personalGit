package com.bewg.pd.common.util;

import java.util.UUID;

/**
 *
 * @Author lizy
 *
 */
public class UUIDGenerator {

    /**
     * 产生一个32位的UUID
     *
     * @return
     */
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
