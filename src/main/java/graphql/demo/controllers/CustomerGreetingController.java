package graphql.demo.controllers;

import graphql.demo.model.CustomerModel;
import graphql.demo.services.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CustomerGreetingController {
    private final OpenAiService openAiService;

    @SchemaMapping(typeName = "Customer")
    String greeting(CustomerModel customer) {
        return openAiService.greeting(customer.firstName());
    }
}
