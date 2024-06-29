package com.example.sync_book.util;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Factory for creating test matchers.
 * <p>
 * Comparing actual and expected objects via AssertJ
 * Support converting json MvcResult to objects for comparison.
 */
public class MatcherFactory<T> {

    public static <T> Matcher<T> usingAssertions(Class<T> clazz, BiConsumer<T, T> assertion, BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
        return new Matcher<>(clazz, assertion, iterableAssertion);
    }

    public static  <T> Matcher<T> usingIterableAssertions(Class<T> clazz, String... fieldsToIgnore) {
        return usingAssertions(clazz,
                (a, b) -> assertThat(a).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(b),
                (a, b) -> assertThat(a).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(b)
        );
    }

    public static class Matcher<T> {
        private final Class<T> clazz;
        private final BiConsumer<T, T> assertion;
        private final BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion;

        public Matcher(Class<T> clazz, BiConsumer<T, T> assertion, BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
            this.clazz = clazz;
            this.assertion = assertion;
            this.iterableAssertion = iterableAssertion;
        }

        public void assertMatch(T actual, T expected){
            assertion.accept(actual, expected);
        }

        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            iterableAssertion.accept(actual, expected);
        }

        public ResultMatcher contentJson(T expected) {
            return result -> assertMatch(JsonUtil.readValue(getContent(result), clazz), expected);
        }

        private String getContent(MvcResult result) throws UnsupportedEncodingException {
            return result.getResponse().getContentAsString();
        }

        public ResultMatcher contentJson(Iterable<T> expected) {
            return result -> assertMatch(JsonUtil.readValues(getContent(result), clazz), expected);
        }

        public T readFromJson(ResultActions action) throws UnsupportedEncodingException {
            {
                return JsonUtil.readValue(getContent(action.andReturn()), clazz);
            }
        }
    }
}
