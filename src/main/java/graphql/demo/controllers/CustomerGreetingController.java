package graphql.demo.controllers;

import graphql.demo.model.CustomerModel;
import graphql.demo.services.OpenAiService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class CustomerGreetingController {
    private final OpenAiService openAiService;

//    @SchemaMapping(typeName = "Customer")
//    String greeting(CustomerModel customer) {
//        return openAiService.greeting(customer.firstName());
//    }

    @SchemaMapping(typeName = "Customer")
    CompletableFuture<String> greeting(CustomerModel customer, DataFetchingEnvironment env) {
        DataLoader<String, String> dataLoader = env.getDataLoader(CustomerGreetingDataloader.GREETING_DATALOADER);
        return dataLoader.load(customer.firstName());
    }

}
