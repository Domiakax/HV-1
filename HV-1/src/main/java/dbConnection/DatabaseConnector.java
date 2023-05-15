package dbConnection;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import dao.CustomerDAO;
import dao.ReadingDAO;
import model.Customer;
import model.ICustomer;
import model.Reading;

public class DatabaseConnector {
	
	private static DatabaseConnector connector;
	private static final String urlDatabase = "jdbc:mariadb://localhost:3306/hv1";
	private static final String dbUser = "root";
	private static final String dbUserPW = "";
	
	private static CustomerDAO kundeDao;
	private static ReadingDAO ablesungDao;
	
	
	private DatabaseConnector() {
		System.out.println("Setup DatabaseConnector");
		final Jdbi jdbi  = Jdbi.create(urlDatabase, dbUser, dbUserPW);
		jdbi.installPlugin(new SqlObjectPlugin());
		final Handle handle = jdbi.open();
		handle.registerArgument(new UUIDArgumentFactory());
		handle.registerRowMapper(BeanMapper.factory(Customer.class));
		handle.registerRowMapper(BeanMapper.factory(Reading.class));
		System.out.println("Setup Create Tables");
		kundeDao = handle.attach(CustomerDAO.class);
		System.out.println("Kunde added to jdbi");
		ablesungDao = handle.attach(ReadingDAO.class);
		System.out.println("Reading added to jdbi");
		System.out.println("create Table");
		kundeDao.createTable();
		ablesungDao.createTable();
	}
	
	public static DatabaseConnector getDatabaseConnector() {
		if(connector == null) {
			connector = new DatabaseConnector();
		}
		return connector;
	}
	
	public void addCustomer(Customer c) {
		kundeDao.insert(c);
	}
	
	public boolean updateCustomer(Customer c) {
		return kundeDao.update(c);
	}
	
	public ICustomer findCustomer(UUID uuid) {
		return kundeDao.findById(uuid);
	}
	
	public List<Customer> getAllCustomer(){
		return kundeDao.getAll();
	}
	
	public boolean deletCustomer(UUID uuid) {
		ablesungDao.customerDeleted(uuid);
		boolean deleted = kundeDao.delete(uuid);
		return deleted;
	}
	
	//Not finished
	public boolean addReading(Reading ir) {
		ablesungDao.insert(ir);
		return true;
	}
	
	public Reading findReadingByUUID(UUID uuid) {
		return ablesungDao.findById(uuid);
	}
	
	public List<Reading> getAllReadings(){
		return ablesungDao.getAll();
	}
	
	public boolean updateReading(Reading r) {
		return ablesungDao.update(r);
	}
}
