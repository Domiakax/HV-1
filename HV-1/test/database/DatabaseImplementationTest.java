package database;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dbConnection.DatabaseConnector;
import model.Customer;
import model.ICustomer;
import model.IReading;
import model.Reading;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

@TestMethodOrder(OrderAnnotation.class)
class DatabaseImplementationTest {

//	private static final String urlDatabase = "jdbc:mariadb://localhost:3306/hv1";
//	private static final String dbUser = "root";
//	private static final String dbUserPW = "";
	
	private static Customer c1;
	private static Customer c2;
	private static Customer notInDatabase;
	private static Customer UUID_Null;
	private static Reading ir1;
	private static Reading ir2;
	private static Reading irBefore2Years;
	
	
	@BeforeAll
	static void setUp() {
		DatabaseConnector.getDatabaseConnector().truncateTables();
		c1 = new Customer();
		c1.setFirstname("a");
		c1.setLastname("A");
		c1.setUuid(UUID.randomUUID());
		
		c2 = new Customer();
		c2.setFirstname("b");
		c2.setLastname("B");
		c2.setUuid(UUID.randomUUID());
		
		notInDatabase = new Customer();
		notInDatabase.setUuid(UUID.randomUUID());
		
		UUID_Null = new Customer();
		
		ir1 = new Reading();
		ir1.setUuid(UUID.randomUUID());
		ir1.setCustomer(c2);
		ir1.setDateOfReading(LocalDate.of(2022,12,31));
		ir1.setComment("test");
		ir1.setKindOfMeter("bla");
		ir1.setMetercount(200.5);
		
		ir2 = new Reading();
		ir2.setUuid(UUID.randomUUID());
		ir2.setCustomer(c2);
		ir2.setDateOfReading(LocalDate.of(2022,12,31));
		ir2.setComment("test");
		ir2.setKindOfMeter("bla");
		ir2.setMetercount(200.5);
		
		irBefore2Years = new Reading();
		irBefore2Years.setUuid(UUID.randomUUID());
		irBefore2Years.setCustomer(c1);
		irBefore2Years.setDateOfReading(LocalDate.of(2020, 12, 31));
	}
	
	@Test
	@Order(0)
	void c_CustomerPositive() {
		assertTrue(DatabaseConnector.getDatabaseConnector().addCustomer(c1));
		assertTrue(DatabaseConnector.getDatabaseConnector().addCustomer(c2));
	}
	
	@Test
	@Order(1)
	void c_CustomerNegative() {
		assertFalse(DatabaseConnector.getDatabaseConnector().addCustomer(c1));
		assertFalse(DatabaseConnector.getDatabaseConnector().addCustomer(new Customer()));
		assertFalse(DatabaseConnector.getDatabaseConnector().addCustomer(null));
	}
	
	@Test
	@Order(2)
	void r_SingleCustomerPositive() {
		ICustomer c1Search = DatabaseConnector.getDatabaseConnector().findCustomer(c1.getUuid());
		assertEquals(c1, c1Search);
	}
	
	@Test
	@Order(3)
	void r_SingleCustomerNegative() {
		ICustomer c1Search = DatabaseConnector.getDatabaseConnector().findCustomer(null);
		assertNull(c1Search);
		assertNull(DatabaseConnector.getDatabaseConnector().findCustomer(UUID.randomUUID()));
	}
	
	@Test
	@Order(4)
	void r_AllCustomersPositive() {
		List<? extends ICustomer> customers = DatabaseConnector.getDatabaseConnector().getAllCustomer();
		assertTrue(customers.contains(c1));
		assertTrue(customers.contains(c2));
		assertTrue(customers.size() == 2);
	}
	
	@Test
	@Order(5)
	void u_CustomerPositive() {
		c1.setFirstname("updated");
		boolean updated = DatabaseConnector.getDatabaseConnector().updateCustomer(c1);
		assertTrue(updated);
	}
	
	@Test
	@Order(6)
	void u_CustomerNegative() {
		assertFalse(DatabaseConnector.getDatabaseConnector().updateCustomer(null));
		assertFalse(DatabaseConnector.getDatabaseConnector().updateCustomer(notInDatabase));
		assertFalse(DatabaseConnector.getDatabaseConnector().updateCustomer(UUID_Null));
	}
	
	@Test
	@Order(7)
	void c_ReadingPositive() {
		assertTrue(DatabaseConnector.getDatabaseConnector().addReading(ir1));
		assertTrue(DatabaseConnector.getDatabaseConnector().addReading(ir2));
		assertTrue(DatabaseConnector.getDatabaseConnector().addReading(irBefore2Years));
	}
	
	@Test
	@Order(8)
	void c_ReadingNegative() {
		assertFalse(DatabaseConnector.getDatabaseConnector().addReading(ir1));
		assertFalse(DatabaseConnector.getDatabaseConnector().addReading(null));
		Reading r = new Reading();
		assertFalse(DatabaseConnector.getDatabaseConnector().addReading(r));
		r.setUuid(UUID.randomUUID());
		assertFalse(DatabaseConnector.getDatabaseConnector().addReading(r));
		r.setCustomer(notInDatabase);
		assertFalse(DatabaseConnector.getDatabaseConnector().addReading(r));
		
	}
	
	@Test
	@Order(9)
	void u_ReadingPositive() {
		ir1.setCustomer(c1);
		ir1.setComment("updated");
		ir1.setMetercount(3000.0);
		assertTrue(DatabaseConnector.getDatabaseConnector().updateReading(ir1));
	}
	
	@Test
	@Order(10)
	void u_ReadingNegative() {
		assertFalse(DatabaseConnector.getDatabaseConnector().updateReading(null));
		Reading r = new Reading();
		assertFalse(DatabaseConnector.getDatabaseConnector().updateReading(r));
		r.setUuid(UUID.randomUUID());
		assertFalse(DatabaseConnector.getDatabaseConnector().updateReading(r));
		r.setCustomer(notInDatabase);
		assertFalse(DatabaseConnector.getDatabaseConnector().updateReading(r));
	}
	
	@Test
	@Order(11)
	void r_ReadingSinglePositive() {
		IReading result = DatabaseConnector.getDatabaseConnector().findReadingByUUID(ir1.getUuid());
		assertEquals(ir1, result);
	}
	
	@Test
	@Order(12)
	void r_ReadingSingleNegative() {
		assertNull(DatabaseConnector.getDatabaseConnector().findReadingByUUID(null));
		assertNull(DatabaseConnector.getDatabaseConnector().findReadingByUUID(UUID.randomUUID()));
	}
	
	@Test
	@Order(13)
	void r_ReadingsAllPositive() {
		//2 Jahre beachten
		List<? extends IReading> result = DatabaseConnector.getDatabaseConnector().getAllReadings();
//		System.out.println("Result");
//		result.stream().forEach(System.out::println);
//		System.out.println("Result-Ende");
		assertTrue(result.contains(ir1));
		assertTrue(result.contains(ir2));
		assertTrue(result.size() == 2);
	}
	
	@Test
	@Order(14)
	void d_ReadingPositive() {
		assertTrue(DatabaseConnector.getDatabaseConnector().deleteReading(irBefore2Years.getUuid()));
	}
	
	@Test
	@Order(15)
	void d_ReadingNegative() {
		assertFalse(DatabaseConnector.getDatabaseConnector().deleteReading(null));
		assertFalse(DatabaseConnector.getDatabaseConnector().deleteReading(UUID.randomUUID()));
	}
	
	@Test
	@Order(16)
	void d_CustomerPositive() {
		assertTrue(DatabaseConnector.getDatabaseConnector().deleteCustomer(c1.getUuid()));
		ir1.setCustomer(null);
		Reading result = DatabaseConnector.getDatabaseConnector().findReadingByUUID(ir1.getUuid());
		assertEquals(ir1, result);
		assertNull(result.getCustomer());
	}
	
	@Test
	@Order(17)
	void d_CustomerNegative() {
		assertFalse(DatabaseConnector.getDatabaseConnector().deleteCustomer(null));
		assertFalse(DatabaseConnector.getDatabaseConnector().deleteCustomer(UUID.randomUUID()));
	}

}
