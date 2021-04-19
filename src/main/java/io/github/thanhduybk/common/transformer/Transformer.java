package io.github.thanhduybk.common.transformer;

public interface Transformer<F, T> {
    T transform(F from);
}
