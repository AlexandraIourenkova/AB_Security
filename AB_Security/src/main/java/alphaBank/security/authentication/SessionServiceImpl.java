package alphaBank.security.authentication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import alphaBank.security.model.AccountEntity;


@Service
public class SessionServiceImpl implements SessionService {
	Map<String, AccountEntity> users = new ConcurrentHashMap<>();

	@Override
	public AccountEntity addUser(String sessionId, AccountEntity user) {
		return users.put(sessionId, user);
	}

	@Override
	public AccountEntity getUser(String sessionId) {
		return users.get(sessionId);
	}

	@Override
	public AccountEntity removeUser(String sessionId) {
		return users.remove(sessionId);
	}

}
