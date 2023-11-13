package graphql.demo.model;

import org.jooq.Record;
import org.jooq.SelectJoinStep;

import static graphql.demo.jooq.generated.tables.Addresses.ADDRESSES;
import static graphql.demo.model.Utils.ifNonNull;

public record AddressPredicate(
        StringPredicate streetNumber,
        StringPredicate streetName,
        StringPredicate zipCode,
        StringPredicate city,
        StringPredicate country
) {

    public SelectJoinStep<Record> applyOn(SelectJoinStep<Record> query) {
        ifNonNull(streetNumber, streetNumber -> query.where(streetNumber.conditions(ADDRESSES.STREET_NUMBER)));
        ifNonNull(streetName, streetName -> query.where(streetName.conditions(ADDRESSES.STREET_NAME)));
        ifNonNull(zipCode, zipCode -> query.where(zipCode.conditions(ADDRESSES.ZIP_CODE)));
        ifNonNull(city, city -> query.where(city.conditions(ADDRESSES.CITY)));
        ifNonNull(country, country -> query.where(country.conditions(ADDRESSES.COUNTRY)));
        return query;
    }
}
