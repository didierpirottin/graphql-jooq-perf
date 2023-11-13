package graphql.demo.model;

public record AccountModel(
        String id,
        String iban,
        Float balance,
        String currency
) {
}
