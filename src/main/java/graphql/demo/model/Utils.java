package graphql.demo.model;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Utils {
    static <T> void ifNonNull(T nullableValue, Consumer<T> consumer) {
        Optional.ofNullable(nullableValue)
                .ifPresent(value -> consumer.accept(value));
    }

    static <T, R> R mapIfNonNull(T nullableValue, Function<T, R> mapper) {
        return Optional.ofNullable(nullableValue)
                .map(value -> mapper.apply(value))
                .orElse(null);
    }

    static <T> Flux<T> mapToFlux(CompletableFuture<List<T>> futureList) {
        Mono<List<T>> monoList = Mono.fromFuture(() -> futureList);
        return monoList.flatMapMany(Flux::fromIterable);
    }

}
