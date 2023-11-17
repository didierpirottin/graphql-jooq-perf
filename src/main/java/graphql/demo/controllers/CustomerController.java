package graphql.demo.controllers;

import graphql.demo.mappers.CustomerModelMapper;
import graphql.demo.model.CustomerModel;
import graphql.demo.model.CustomerPredicate;
import graphql.schema.DataFetchingFieldSelectionSet;
import lombok.RequiredArgsConstructor;
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
    private final DSLContext dslContext;

    @QueryMapping
    List<CustomerModel> customers(@Argument CustomerPredicate filter, DataFetchingFieldSelectionSet selectionSet) {
        SelectSelectStep<Record> select = selectStep(selectionSet);
        SelectJoinStep<Record> query = getRecordSelectJoinStep(filter, selectionSet, select);
        whereStep(filter, query);
        return executeAndMap(query);
    }

    private static List<CustomerModel> executeAndMap(SelectJoinStep<Record> query) {
        return query
                .fetch()
                .stream()
                .map(CustomerModelMapper::mapCustomerRecordToModel)
                .toList();
    }

    private static void whereStep(CustomerPredicate filter, SelectJoinStep<Record> query) {
        if (filter != null) {
            filter.applyOn(query);
        }
    }


    private static SelectJoinStep<Record> getRecordSelectJoinStep(CustomerPredicate filter, DataFetchingFieldSelectionSet selectionSet, SelectSelectStep<Record> select) {
        SelectJoinStep<Record> query = select.from(CUSTOMERS);
        joinAddresses(filter, selectionSet, query);
        return query;
    }

    private static void joinAddresses(CustomerPredicate filter, DataFetchingFieldSelectionSet selectionSet, SelectJoinStep<Record> query) {
        if (selectionSet.contains("address") ||
                (filter != null && filter.address() != null)) {
            query.leftJoin(ADDRESSES).on(ADDRESSES.ID.eq(CUSTOMERS.ADDRESS_ID));
        }
    }


    private SelectSelectStep<Record> selectStep(DataFetchingFieldSelectionSet selectionSet) {
        SelectSelectStep<Record> select = selectCustomers(selectionSet);
        selectAddresses(selectionSet, select);
        selectAccounts(selectionSet, select);
        return select;
    }

    private void selectAccounts(DataFetchingFieldSelectionSet selectionSet, SelectSelectStep<Record> select) {
        if (selectionSet.contains("accounts")) {
            select.select(
                    multiset(
                            dslContext
                                    .selectFrom(ACCOUNTS)
                                    .where(ACCOUNTS.CUSTOMER_ID.eq(CUSTOMERS.ID)))
                            .as("Accounts_Multiset"));
        }
    }

    private static void selectAddresses(DataFetchingFieldSelectionSet selectionSet, SelectSelectStep<Record> select) {
        if (selectionSet.contains("address")) {
            select.select(ADDRESSES.asterisk());
        }
    }


    private SelectSelectStep<Record> selectCustomers(DataFetchingFieldSelectionSet selectionSet) {
        SelectSelectStep<Record> select = dslContext.select();
        if (selectionSet.contains("id")) {
            select = select.select(CUSTOMERS.ID);
        }
        if (selectionSet.contains("firstName")) {
            select = select.select(CUSTOMERS.FIRST_NAME);
        }
        if (selectionSet.contains("lastName")) {
            select = select.select(CUSTOMERS.LAST_NAME);
        }
        return select;
    }

}
