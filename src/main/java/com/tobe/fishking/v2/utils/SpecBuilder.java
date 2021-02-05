package com.tobe.fishking.v2.utils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class SpecBuilder {

    public static <T> Builder<T> builder(Class<T> type) {
        return new Builder<T>();
    }

    public static class Builder<T> {
        private List<Specification<T>> specs = new ArrayList<>();

        public Builder<T> and(Specification<T> spec) {
            specs.add(spec);
            return this;
        }

        public Builder<T> whenHasText(String str,
                                      Function<String, Specification<T>> specSupplier) {
            if (StringUtils.hasText(str)) {
                specs.add(specSupplier.apply(str));
            }
            return this;
        }

        public Builder<T> when(String str,
                               Function<String, Specification<T>> specSupplier) {
            specs.add(specSupplier.apply(str));
            return this;
        }

        public Builder<T> whenHasTextThenBetween(String from, String to,
                                                 BiFunction<String, String, Specification<T>> specSupplier) {
            if (StringUtils.hasText(from) && StringUtils.hasText(to)) {
                specs.add(specSupplier.apply(from, to));
            }
            return this;

        }

        public Builder<T> whenIsTrue(Boolean cond,
                                     Supplier<Specification<T>> specSupplier) {
            if (cond != null && cond.booleanValue()) {
                specs.add(specSupplier.get());
            }
            return this;

        }

        public Specification<T> toSpec() {
            if (specs.isEmpty())
                return Specification.where(null);
            else if (specs.size() == 1)
                return specs.get(0);

            else {
                return specs.stream().reduce(
                        Specification.where(null),
                        (specs, spec) -> specs.and(spec),
                        (specs1, specs2) -> specs1.and(specs2));

            }

        }

    }

}