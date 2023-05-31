package model;

import java.util.UUID;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
//@AllArgsConstructor
public class Customer implements ICustomer{
	
	public static final String FIELD_UUID = "customer_uuid";
	public static final String FIELD_FIRSTNAME = "customer_firstname";
	public static final String FIELD_LASTNAME= "customer_lastname";
	
	@EqualsAndHashCode.Include
	@ColumnName(FIELD_UUID)
	private UUID uuid;
	@ColumnName(FIELD_FIRSTNAME)
	private String firstname;
	@ColumnName(FIELD_LASTNAME)
	private String lastname;

}
