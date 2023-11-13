package graphql.demo.controllers;

import graphql.demo.services.OpenAiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static graphql.demo.model.Utils.mapToFlux;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;
import static org.dataloader.DataLoaderOptions.newOptions;

@Service
@RequiredArgsConstructor
public class CustomerGreetingDataloader {
    public static final String GREETING_DATALOADER = "GREETING_DATALOADER";

    private final OpenAiService openAiService;
    private final BatchLoaderRegistry batchLoaderRegistry;

    @PostConstruct
    void initDataLoader() {
        batchLoaderRegistry.forTypePair(String.class, String.class)
                .withName(GREETING_DATALOADER)
                .withOptions(newOptions()
                        .setMaxBatchSize(5)
                )
                .registerBatchLoader((List<String> names, BatchLoaderEnvironment env)
                        -> generateGreetings(names));
    }

    private Flux<String> generateGreetings(List<String> names) {
        CompletableFuture<List<String>> futureList =
                supplyAsync(() -> openAiService.greetings(names),
                        newVirtualThreadPerTaskExecutor());
        return mapToFlux(futureList);
    }

}
