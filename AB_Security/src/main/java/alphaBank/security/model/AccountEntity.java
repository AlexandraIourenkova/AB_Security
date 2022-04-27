package alphaBank.security.model;


import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AccountEntity {
		@Id
		String id;
		String firstName;
		String lastName;
		String maritialStatus;
		String employmentStatus;
		Address address;
		ContactDetails contactDetails;
		int balance;
		String passwordEncoded;
		Set<String> roles;
		
	}


