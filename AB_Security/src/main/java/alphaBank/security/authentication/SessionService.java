package alphaBank.security.authentication;

import alphaBank.security.model.AccountEntity;

public interface SessionService {
	AccountEntity addUser(String sessionId, AccountEntity user);
	AccountEntity getUser(String sessionId);
	AccountEntity removeUser(String sessionId);

}
