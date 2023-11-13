package graphql.demo.model;

import org.jooq.Record;
import org.jooq.SelectJoinStep;

import static graphql.demo.jooq.generated.Tables.CUSTOMERS;
import static graphql.demo.model.Utils.ifNonNull;

public record CustomerPredicate(
        StringPredicate firstName,
        StringPredicate lastName,
        AddressPredicate address) {

    public void applyOn(SelectJoinStep<Record> query) {
        ifNonNull(firstName, firstName -> query.where(firstName.conditions(CUSTOMERS.FIRST_NAME)));
        ifNonNull(lastName, lastName -> query.where(lastName.conditions(CUSTOMERS.LAST_NAME)));
        ifNonNull(address, address -> address.applyOn(query));
    }
}
