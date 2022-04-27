package alphaBank.security.authentication;

public interface SecurityContext {
	boolean addUser(UserProfile user);

	UserProfile removeUser(String login);

	UserProfile getUser(String login);

}
