package com.example.util.validation.validator;

import java.util.List;

public interface ChainElement<T> {
    void setNext(T step);

    static <T extends ChainElement<T>> T buildChain(List<T> elements) {
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("Chain elements can't be empty");
        }
        for (int i = 0; i < elements.size(); i++) {
            var current = elements.get(i);
            var next = i < elements.size() - 1 ? elements.get(i + 1) : null;
            current.setNext(next);
        }
        return elements.get(0);
    }
}