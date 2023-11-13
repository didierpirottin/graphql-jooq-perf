package graphql.demo.mappers;

import graphql.demo.jooq.generated.tables.records.CustomersRecord;
import graphql.demo.model.AccountModel;
import graphql.demo.model.AddressModel;
import graphql.demo.model.CustomerModel;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

import static graphql.demo.jooq.generated.Tables.ADDRESSES;
import static graphql.demo.jooq.generated.tables.Customers.CUSTOMERS;
import static graphql.demo.mappers.AccountModelMapper.mapMultisetToModel;
import static java.util.Collections.emptyList;

public class CustomerModelMapper {

    public static CustomerModel mapCustomerRecordToModel(Record record) {
        if (record == null) {
            return null;
        }

        AddressModel addressModel = AddressModelMapper.mapAddressRecordToModel(record.into(ADDRESSES));

        List<AccountModel> accounts = emptyList();
        if (record.field("Accounts_Multiset") != null) {
            accounts = mapMultisetToModel((Result<Record>) record.get("Accounts_Multiset"));
        }

        CustomersRecord customerRecord = record.into(CUSTOMERS);
        return new CustomerModel(
                customerRecord.getId(),
                customerRecord.getFirstName(),
                customerRecord.getLastName(),
                addressModel,
                accounts
        );
    }

}
