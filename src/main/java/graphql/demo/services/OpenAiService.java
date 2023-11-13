package graphql.demo.services;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static dev.langchain4j.model.input.structured.StructuredPromptProcessor.toPrompt;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;
import static java.util.logging.Logger.getAnonymousLogger;

@Service
public class OpenAiService {
    public static final PromptTemplate GREETING_PROMPT_TEMPLATE = PromptTemplate.from("write a greeting message for {{it}}. The message must be short, no more than 10 words");

    private ChatLanguageModel model;

    @Value("${demo.openai-proxy-url}")
    private String openAiProxyUrl;

    @PostConstruct
    private void initModel() {
        model =
                OpenAiChatModel.builder()
                        .apiKey("demo_")
                        .baseUrl(openAiProxyUrl)
                        .build();
    }

    public String demoWelcomeMessage() {
        return model.generate(
                """
                        Write a greeting message for a live coding demo talking about Spring for GraphQL.
                        The message must be short, no more than 50 words.
                                """);
    }


    public String greeting(String name) {
        getAnonymousLogger().info("Call openAI with name: " + name);
        return model.generate(GREETING_PROMPT_TEMPLATE.apply(name).text());
    }

    public List<String> greetings(List<String> names) {
        Logger.getAnonymousLogger().info("Call openAI with names: " + names + " on thread " + currentThread());
        MultiNamesGreeting multiNamesGreeting = new MultiNamesGreeting();
        multiNamesGreeting.names = names;

        Prompt prompt = toPrompt(multiNamesGreeting);

        AiMessage aiMessage = model.generate(prompt.toUserMessage()).content();
        List<String> messages = stream(aiMessage.text().split("\n"))
                .map(msg -> msg.trim())
                .toList();

        return names.stream()
                .map(name -> findMessageFor(name, messages))
                .toList();
    }

    private String findMessageFor(String name, List<String> messages) {
        return messages.stream()
                .filter(msg -> msg.contains(name))
                .findFirst()
                .orElse("Hello " + name + " !");
    }

    @StructuredPrompt({
            "For each of these names: {{names}}, write a short and funny greeting message.",
            "Structure your answer by putting each message on a new line.",
            "Keep the same ordre.",
    })
    static class MultiNamesGreeting {
        private List<String> names;
    }

}
