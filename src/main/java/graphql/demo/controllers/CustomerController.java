package graphql.demo.controllers;

import graphql.demo.mappers.AccountModelMapper;
import graphql.demo.mappers.AddressModelMapper;
import graphql.demo.mappers.CustomerModelMapper;
import graphql.demo.model.AccountModel;
import graphql.demo.model.AddressModel;
import graphql.demo.model.CustomerModel;
import graphql.demo.model.CustomerPredicate;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

import static graphql.demo.jooq.generated.Tables.*;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final DSLContext dslContext;

    @QueryMapping
    List<CustomerModel> customers(@Argument CustomerPredicate filter) {
        @NotNull SelectJoinStep<Record> query = dslContext.select(CUSTOMERS.asterisk())
                .from(CUSTOMERS);
        if (filter != null) {
            filter.applyOn(query);
        }
        return query
                .fetch()
                .stream()
                .map(CustomerModelMapper::mapCustomerRecordToModel)
                .toList();
    }

    @SchemaMapping(typeName = "Customer")
    AddressModel address(CustomerModel customer) {
        if (customer.addressId() == null) {
            return null;
        }
        return dslContext.select(ADDRESSES.asterisk())
                .from(ADDRESSES)
                .where(ADDRESSES.ID.eq(customer.addressId()))
                .fetchOne()
                .map(AddressModelMapper::mapAddressRecordToModel);
    }

    @SchemaMapping(typeName = "Customer")
    List<AccountModel> accounts(CustomerModel customer) {
        return dslContext.selectFrom(ACCOUNTS)
                .where(ACCOUNTS.CUSTOMER_ID.eq(customer.id()))
                .fetch()
                .stream()
                .map(AccountModelMapper::mapAccountRecordToModel)
                .toList();
    }
}
