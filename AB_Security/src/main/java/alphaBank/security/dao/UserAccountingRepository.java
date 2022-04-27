package alphaBank.security.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import alphaBank.security.model.AccountEntity;

public interface UserAccountingRepository extends MongoRepository<AccountEntity, String> {

}