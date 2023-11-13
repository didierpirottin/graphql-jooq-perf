package graphql.demo.model;

public record AddressModel(
        String streetNumber,
        String streetName,
        String zipCode,
        String city,
        String country
) {
}
