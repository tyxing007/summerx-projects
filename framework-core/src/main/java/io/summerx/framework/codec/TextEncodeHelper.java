package io.summerx.framework.codec;

import io.summerx.framework.codec.encoder.MD5TextEncoder;
import io.summerx.framework.codec.encoder.SHATextEncoder;
import io.summerx.framework.codec.encoder.TextEncoder;

/**
 * <p>
 * 请在这里添加类说明.
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 5:39 PM
 */
public class TextEncodeHelper {

    private static TextEncoder md5TextEncoder = new MD5TextEncoder();

    private static TextEncoder shaTextEncoder = new SHATextEncoder();

    public static String md5Encode(String plainText) {
        return md5TextEncoder.encode(plainText);
    }

    public static String md5Encode(String plainText, String slat) {
        return md5TextEncoder.encode(plainText, slat);
    }
    public static String shaEncode(String plainText) {
        return shaTextEncoder.encode(plainText);
    }

    public static String shaEncode(String plainText, String slat) {
        return shaTextEncoder.encode(plainText, slat);
    }
}
