package graphql.demo.controllers;

import graphql.demo.services.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DemoController {
    private final OpenAiService openAiService;

    @QueryMapping
    String demo() {
        return openAiService.demoWelcomeMessage();
    }
}
