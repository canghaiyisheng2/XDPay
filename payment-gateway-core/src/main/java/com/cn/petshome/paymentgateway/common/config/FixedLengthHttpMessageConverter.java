package com.cn.petshome.paymentgateway.common.config;

import com.cn.xidian.fixedLength.Resolver;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * 定长报文格式相关配置
 * @date 2022/4/6 15:29
 */

public class FixedLengthHttpMessageConverter implements HttpMessageConverter<Object> {

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Lists.newArrayList(MediaType.parseMediaType("application/fixed-length"));
    }

    @SneakyThrows
    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream body = inputMessage.getBody();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = body.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        String bodyString = byteArrayOutputStream.toString();
        return new Resolver<>(clazz).parse(bodyString);
    }

    @SneakyThrows
    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String fixedLengthString = new Resolver<>(o.getClass()).stringify(o);

        byte[] bytes = fixedLengthString.getBytes(StandardCharsets.UTF_8);
        OutputStream os = outputMessage.getBody();
        os.write(bytes);
    }
}
