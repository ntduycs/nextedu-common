package io.github.thanhduybk.common.annotation;

import java.lang.annotation.*;

/**
 * @author ntduy
 * <p>
 * All requests that are intended to be used with xxx-form-encoded content-type MUST be annotated with this annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FormEncodedSupported {
}
