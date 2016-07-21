package io.summerx.framework.codec.encoder;

import io.summerx.framework.codec.digest.DigestHelper;

import java.security.MessageDigest;

/**
 * <p>
 * 请在这里添加类说明.
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 5:27 PM
 */
public abstract class AbstractTextEncoder implements TextEncoder {

    @Override
    public String encode(String plainText) {
        if (plainText == null) {
            return null;
        }

        MessageDigest digest = DigestHelper.getDigest(getDigestAlgorithms());
        digest.update(plainText.getBytes());
        byte[] bytes = digest.digest();
        StringBuffer sBuf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sBuf.append(Integer.toHexString((bytes[i] >> 4) & 0x0f));
            sBuf.append(Integer.toHexString((bytes[i]) & 0x0f));
        }
        return sBuf.toString();
    }

    @Override
    public String encode(String plainText, String slat) {
        if (plainText == null) {
            return null;
        }
        if (slat == null) {
            return encode(plainText);
        } else {
            return encode(plainText + SEP + slat);
        }
    }

    protected abstract String getDigestAlgorithms();
}
