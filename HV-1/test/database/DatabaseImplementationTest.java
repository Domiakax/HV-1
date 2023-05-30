package database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dbConnection.DatabaseConnector;
import model.Customer;
import model.ICustomer;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

@TestMethodOrder(OrderAnnotation.class)
class DatabaseImplementationTest {

//	private static final String urlDatabase = "jdbc:mariadb://localhost:3306/hv1";
//	private static final String dbUser = "root";
//	private static final String dbUserPW = "";
	
	private static Customer c1;
	private static Customer c2;
	
	
	@BeforeAll
	static void setUp() {
		c1 = new Customer();
		c1.setFirstname("a");
		c1.setLastname("A");
		c1.setUuid(UUID.randomUUID());
		
		c2 = new Customer();
		c2.setFirstname("b");
		c2.setLastname("B");
		c2.setUuid(UUID.randomUUID());
	}
	
	@Test
	@Order(1)
	void c1Positive() {
		DatabaseConnector.getDatabaseConnector().addCustomer(c1);
		DatabaseConnector.getDatabaseConnector().addCustomer(c2);
	}
	
	@Test
	@Order(2)
	void r1Positive() {
		ICustomer c1Search = DatabaseConnector.getDatabaseConnector().findCustomer(c1.getUuid());
		assertEquals(c1, c1Search);
	}
	
	@Test
	@Order(3)
	void r1Negative() {
		ICustomer c1Search = DatabaseConnector.getDatabaseConnector().findCustomer(null);
		assertNull(c1Search);
	}

}
