package graphql.demo.mappers;

import graphql.demo.jooq.generated.tables.records.AddressesRecord;
import graphql.demo.model.AddressModel;
import org.jooq.Record;

import static graphql.demo.jooq.generated.tables.Addresses.ADDRESSES;

public class AddressModelMapper {
    public static AddressModel mapAddressRecordToModel(Record record) {
        if (record == null) {
            return null;
        }
        AddressesRecord addressRecord = record.into(ADDRESSES);
        if (addressRecord.getId() == null) {
            return null;
        }
        return new AddressModel(
                addressRecord.getStreetNumber(),
                addressRecord.getStreetName(),
                addressRecord.getZipCode(),
                addressRecord.getCity(),
                addressRecord.getCountry()
        );
    }

}
