package io.github.thanhduybk.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.thanhduybk.common.annotation.FormEncodedSupported;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MappingFormEncodedHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    private static final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
    private static final MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    private static final ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    public MappingFormEncodedHttpMessageConverter() {
        super(new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.isAnnotationPresent(FormEncodedSupported.class);
    }

    @Override
    @NonNull
    protected Object readInternal(@NonNull Class<?> clazz, @NonNull HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return jackson2HttpMessageConverter.read(clazz, httpInputMessage);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void writeInternal(@NonNull Object o, @NonNull HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        Map<String, Object> valueMap = objectMapper.convertValue(o, new TypeReference<Map<String, Object>>() {
        });

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();

        for (String key : valueMap.keySet()) {
            if (valueMap.get(key) instanceof Collection) {
                multiValueMap.put(key, (List<Object>) valueMap.get(key));
            } else {
                multiValueMap.add(key, valueMap.get(key));
            }
        }

        formHttpMessageConverter.write(multiValueMap, MediaType.APPLICATION_FORM_URLENCODED, httpOutputMessage);
    }
}
