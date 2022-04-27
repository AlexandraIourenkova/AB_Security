package alphaBank.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {
	String country;
	String city;
	String street;
	String bldNumber;
	String flatNumber;
	String postIndex;
}
