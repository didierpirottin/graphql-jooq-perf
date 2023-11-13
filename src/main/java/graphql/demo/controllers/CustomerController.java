package graphql.demo.controllers;

import graphql.demo.mappers.CustomerModelMapper;
import graphql.demo.model.CustomerModel;
import graphql.demo.model.CustomerPredicate;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

import static graphql.demo.jooq.generated.Tables.*;
import static org.jooq.impl.DSL.multiset;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    public static final String ADDRESS_ATTRIBUTE = "address";
    private final DSLContext dslContext;

    @QueryMapping
    List<CustomerModel> customers(@Argument CustomerPredicate filter, DataFetchingFieldSelectionSet selectionSet) {
        SelectSelectStep<Record> select = dslContext.select(CUSTOMERS.asterisk());
        if (selectionSet.contains("address")) {
            select.select(ADDRESSES.asterisk());
        }
        if (selectionSet.contains("accounts")) {
            select.select(
                    multiset(
                            dslContext
                                    .selectFrom(ACCOUNTS)
                                    .where(ACCOUNTS.CUSTOMER_ID.eq(CUSTOMERS.ID)))
                            .as("Accounts_Multiset"));
        }

        @NotNull SelectJoinStep<Record> query = select
                .from(CUSTOMERS);
        if (selectionSet.contains(ADDRESS_ATTRIBUTE)) {
            query.leftJoin(ADDRESSES).on(ADDRESSES.ID.eq(CUSTOMERS.ADDRESS_ID));
        }

        if (filter != null) {
            filter.applyOn(query);
        }
        return query
                .fetch()
                .stream()
                .map(CustomerModelMapper::mapCustomerRecordToModel)
                .toList();
    }
}
