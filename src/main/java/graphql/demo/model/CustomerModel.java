package graphql.demo.model;

public record CustomerModel(
        String id,
        String firstName,
        String lastName,
        String addressId) {
}
