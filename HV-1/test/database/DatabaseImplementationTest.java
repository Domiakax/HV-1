package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dbConnection.ConnectionFactory;
import dev.hv.db.init.IConnection;
import dev.hv.db.init.ICustomerService;
import dev.hv.db.init.IFacilityService;
import dev.hv.db.init.IReadingService;
import dev.hv.db.model.ICustomer;
import dev.hv.db.model.IReading;
import model.Customer;
import model.Reading;

@TestMethodOrder(OrderAnnotation.class)
class DatabaseImplementationTest {

	private static final String urlDatabase = "jdbc:mariadb://localhost:3306/hv1";
	private static final String dbUser = "root";
	private static final String dbUserPW = "";

	private static Customer c1;
	private static Customer c2;
	private static Customer notInDatabase;
	private static Customer UUID_Null;
	private static Reading ir1;
	private static Reading ir2;
	private static Reading irBefore2Years;
//	private static IFacilityService iService;
	private static ICustomerService customerService;
	private static IReadingService readingService;
	private static IConnection connection;

	@BeforeAll
	static void setUp() {
		connection = new ConnectionFactory().createConnection(urlDatabase, dbUser, dbUserPW);
		connection.createAllTables();
		connection.truncateAllTables();

		IFacilityService iService = connection.getFacilityService();
		customerService = iService.getICustomerService();
		readingService = iService.getIReadingService();

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
		ir1.setDateOfReading(LocalDate.of(2022, 12, 31));
		ir1.setComment("test");
		ir1.setMetercount(200.5);

		ir2 = new Reading();
		ir2.setUuid(UUID.randomUUID());
		ir2.setCustomer(c2);
		ir2.setDateOfReading(LocalDate.of(2022, 12, 31));
		ir2.setComment("test");
		ir2.setMetercount(200.5);

		irBefore2Years = new Reading();
		irBefore2Years.setUuid(UUID.randomUUID());
		irBefore2Years.setCustomer(c1);
		irBefore2Years.setDateOfReading(LocalDate.of(2020, 12, 31));
	}

	@Test
	@Order(0)
	void c_CustomerPositive() {
		assertTrue(customerService.insert(c1));
		assertTrue(customerService.insert(c2));
	}

	@Test
	@Order(1)
	void c_CustomerNegative() {
		assertFalse(customerService.insert(c1));
		assertFalse(customerService.insert(new Customer()));
		assertFalse(customerService.insert(null));
	}

	@Test
	@Order(2)
	void r_SingleCustomerPositive() {
		ICustomer c1Search = customerService.findById(c1.getUuid());
		assertEquals(c1, c1Search);
	}

	@Test
	@Order(3)
	void r_SingleCustomerNegative() {
		// Null - Object
		ICustomer c1Search = customerService.findById(null);
		assertNull(c1Search);
		// Random - UUID
		assertNull(customerService.findById(UUID.randomUUID()));
	}

	@Test
	@Order(4)
	void r_AllCustomersPositive() {
		List<? extends ICustomer> customers = customerService.getAll();
		assertTrue(customers.contains(c1));
		assertTrue(customers.contains(c2));
		assertTrue(customers.size() == 2);
	}

	@Test
	@Order(5)
	void u_CustomerPositive() {
		c1.setFirstname("updated");
		boolean updated = customerService.update(c1);
		assertTrue(updated);
		// Stored in Database
		ICustomer inDatabase = customerService.findById(c1.getUuid());
		assertEquals(c1, inDatabase);
	}

	@Test
	@Order(6)
	void u_CustomerNegative() {
		assertFalse(customerService.update(null));
		assertFalse(customerService.update(notInDatabase));
		assertFalse(customerService.update(UUID_Null));
	}

	@Test
	@Order(7)
	void c_ReadingPositive() {
		assertTrue(readingService.insert(ir1));
		assertTrue(readingService.insert(ir2));
		assertTrue(readingService.insert(irBefore2Years));
	}

	@Test
	@Order(8)
	void c_ReadingNegative() {
		assertFalse(readingService.insert(ir1));
		assertFalse(readingService.insert(null));
		Reading r = new Reading();
		assertFalse(readingService.insert(r));
		r.setUuid(UUID.randomUUID());
		assertFalse(readingService.insert(r));
		r.setCustomer(notInDatabase);
		assertFalse(readingService.insert(r));

	}

	@Test
	@Order(9)
	void u_ReadingPositive() {
		ir1.setCustomer(c1);
		ir1.setComment("updated");
		ir1.setMetercount(3000.0);
		assertTrue(readingService.update(ir1));
	}

	@Test
	@Order(10)
	void u_ReadingNegative() {
		assertFalse(readingService.update(null));
		Reading r = new Reading();
		assertFalse(readingService.update(r));
		r.setUuid(UUID.randomUUID());
		assertFalse(readingService.update(r));
		r.setCustomer(notInDatabase);
		assertFalse(readingService.update(r));
	}

	@Test
	@Order(11)
	void r_ReadingSinglePositive() {
		IReading result = readingService.findById(ir1.getUuid());
		assertEquals(ir1, result);
	}

	@Test
	@Order(12)
	void r_ReadingSingleNegative() {
		assertNull(readingService.findById(null));
		assertNull(readingService.findById(UUID.randomUUID()));
	}

	@Test
	@Order(13)
	void r_ReadingsAllPositive() {
		// 2 Jahre nicht beachten
		List<? extends IReading> result = readingService.getAll();
//		System.out.println("Result");
//		result.stream().forEach(System.out::println);
//		System.out.println("Result-Ende");
		assertTrue(result.contains(ir1));
		assertTrue(result.contains(ir2));
		assertTrue(result.contains(irBefore2Years));
		assertTrue(result.size() == 3);
	}

	@Test
	@Order(14)
	void d_ReadingPositive() {
		assertTrue(readingService.delete(irBefore2Years.getUuid()));
	}

	@Test
	@Order(15)
	void d_ReadingNegative() {
		assertFalse(readingService.delete(null));
		assertFalse(readingService.delete(UUID.randomUUID()));
	}

	@Test
	@Order(16)
	void d_CustomerPositive() {
		assertTrue(customerService.delete(c1.getUuid()));
		ir1.setCustomer(null);
		IReading result = readingService.findById(ir1.getUuid());
		assertEquals(ir1, result);
		assertNull(result.getCustomer());
	}

	@Test
	@Order(17)
	void d_CustomerNegative() {
		assertFalse(customerService.delete(null));
		assertFalse(customerService.delete(UUID.randomUUID()));
	}

	@Test
	@Order(18)
	void truncateTablesDeletes() {
		connection.truncateAllTables();
		assertTrue(customerService.getAll().size() == 0);
		assertTrue(readingService.getAll().size() == 0);
	}

	@AfterAll
	static void closeConnection() {
		connection.removeAllTables();
		connection.closeConnection();
	}

}
