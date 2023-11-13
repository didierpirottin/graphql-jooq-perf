package graphql.demo.mappers;

import graphql.demo.jooq.generated.tables.records.CustomersRecord;
import graphql.demo.model.CustomerModel;
import org.jooq.Record;

import static graphql.demo.jooq.generated.tables.Customers.CUSTOMERS;

public class CustomerModelMapper {

    public static CustomerModel mapCustomerRecordToModel(Record record) {
        if (record == null) {
            return null;
        }
        CustomersRecord customerRecord = record.into(CUSTOMERS);
        return new CustomerModel(
                customerRecord.getId(),
                customerRecord.getFirstName(),
                customerRecord.getLastName(),
                customerRecord.getAddressId()
        );
    }

}
