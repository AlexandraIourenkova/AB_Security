package alphaBank.security.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import alphaBank.security.authentication.SecurityContext;
import alphaBank.security.authentication.SessionService;
import alphaBank.security.authentication.UserProfile;
import alphaBank.security.dao.UserAccountingRepository;
import alphaBank.security.model.AccountEntity;

@Service
@Order(10)
public class AuthenticationFilter implements Filter {

	UserAccountingRepository repository;
	SecurityContext securityContext;
	SessionService sessionService;

	@Autowired
	public AuthenticationFilter(UserAccountingRepository repository, SecurityContext securityContext,
			SessionService sessionService) {
		this.repository = repository;
		this.sessionService = sessionService;
		this.securityContext = securityContext;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		System.out.println(request.getSession().getId());
		if (checkEndPoints(request.getServletPath(), request.getMethod())) {
			String token = request.getHeader("Authorization");
			String sessionId = request.getSession().getId();
			AccountEntity userAccount = sessionService.getUser(sessionId);
			if (token == null && userAccount == null) {
				response.sendError(401, "Header Authorization not found");
				return;
			}
			if (token != null) {
				String[] credentials = getCredentials(token).orElse(null);
				if (credentials == null || credentials.length < 2) {
					response.sendError(401, "Token not valid");
					return;
				}
				userAccount = repository.findById(credentials[0]).orElse(null);
				if (userAccount == null) {
					response.sendError(401, "User not found");
					return;
				}
				if (!BCrypt.checkpw(credentials[1], userAccount.getPasswordEncoded())) {
					response.sendError(401, "User or password not valid");
					return;
				}
				request = new WrappedRequest(request, userAccount.getId());
				UserProfile user = UserProfile.builder().login(userAccount.getId())
						.password(userAccount.getPasswordEncoded()).roles(userAccount.getRoles()).build();
				securityContext.addUser(user);
			}
		}
		chain.doFilter(request, response);

	}

	private boolean checkEndPoints(String path, String method) {
		return !(("POST".equalsIgnoreCase(method) && path.matches("[/]account[/]register[/]?"))
				|| (path.matches("[/]forum[/]posts([/]\\w+)+[/]?")));
	}

	private Optional<String[]> getCredentials(String token) {
		String[] res = null;
		try {
			token = token.split(" ")[1];
			byte[] bytesDecode = Base64.getDecoder().decode(token);
			token = new String(bytesDecode);
			res = token.split(":");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(res);
	}

	private class WrappedRequest extends HttpServletRequestWrapper {
		String login;

		public WrappedRequest(HttpServletRequest request, String login) {
			super(request);
			this.login = login;
		}

		@Override
		public Principal getUserPrincipal() {
			return () -> login;
		}
	}
}
