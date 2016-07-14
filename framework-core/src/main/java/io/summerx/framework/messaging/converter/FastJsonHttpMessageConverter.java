package io.summerx.framework.messaging.converter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * 基於FastJSON的MessageConverter实现
 *
 * @author summerx
 * @Date 2016-07-14
 */
public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter {

    @Override
    protected boolean supports(Class clazz) {
        return false;
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
