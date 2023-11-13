package graphql.demo.model;

import org.jooq.Condition;
import org.jooq.TableField;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static graphql.demo.model.Utils.mapIfNonNull;


public record StringPredicate(
        String is,
        String isNot,
        List<String> isOneOf,
        String contains,
        String startsWith,
        String endsWith) {

    public List<Condition> conditions(TableField field) {
        return Stream.of(
                        mapIfNonNull(is, is -> field.eq(is)),
                        mapIfNonNull(isNot, isNot -> field.ne(isNot)),
                        mapIfNonNull(startsWith, startsWith -> field.startsWith(startsWith)),
                        mapIfNonNull(endsWith, endsWith -> field.endsWith(endsWith)),
                        mapIfNonNull(contains, contains -> field.contains(contains)),
                        mapIfNonNull(isOneOf, isOneOf -> field.in(isOneOf)))
                .filter(Objects::nonNull)
                .toList();
    }
}
