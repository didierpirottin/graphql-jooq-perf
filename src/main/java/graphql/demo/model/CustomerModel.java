package graphql.demo.model;

import java.util.List;

public record CustomerModel(
        String id,
        String firstName,
        String lastName,
        AddressModel address,
        List<AccountModel> accounts) {
}
