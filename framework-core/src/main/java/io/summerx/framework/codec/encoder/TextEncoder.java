package io.summerx.framework.codec.encoder;

/**
 * <p>
 * 文本编码器
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 5:26 PM
 */
public interface TextEncoder {

    String SEP = "#";

    String encode(String plainText);

    String encode(String plainText, String slat);
}
