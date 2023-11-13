package graphql.demo.mappers;

import graphql.demo.jooq.generated.tables.records.AccountsRecord;
import graphql.demo.model.AccountModel;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

import static graphql.demo.jooq.generated.tables.Accounts.ACCOUNTS;

public class AccountModelMapper {
    public static AccountModel mapAccountRecordToModel(Record record) {
        if (record == null) {
            return null;
        }
        AccountsRecord accountRecord = record.into(ACCOUNTS);
        return new AccountModel(
                accountRecord.getId(),
                accountRecord.getIban(),
                accountRecord.getBalance(),
                accountRecord.getCurrency()
        );
    }

    public static List<AccountModel> mapMultisetToModel(Result<Record> accountsMultiset) {
        return accountsMultiset.stream()
                .map(AccountModelMapper::mapAccountRecordToModel)
                .toList();
    }

}
