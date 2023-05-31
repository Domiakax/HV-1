package dbConnection;

import java.sql.SQLException;
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
import model.IReading;
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
	
	public void truncateTables() {
		ablesungDao.truncate();
		kundeDao.truncate();
	}
	
	public boolean addCustomer(ICustomer c) {
		if(c == null || c.getUuid() == null) {
			return false;
		}
		try {
			return kundeDao.insert(c);
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean updateCustomer(ICustomer c) {
		if(c == null) {
			return false;
		}
		return kundeDao.update(c);
	}
	
	public ICustomer findCustomer(UUID uuid) {
		return kundeDao.findById(uuid);
	}
	
	public List<? extends ICustomer> getAllCustomer(){
		return kundeDao.getAll();
	}
	
	public boolean deleteCustomer(UUID uuid) {
		ablesungDao.customerDeleted(uuid);
		boolean deleted = kundeDao.delete(uuid);
		return deleted;
	}
	
	public boolean addReading(IReading ir) {
		if(ir == null) {
			return false;
		}
		ICustomer ic = ir.getCustomer();
		if(ic == null) {
			return false;
		}
		UUID ic_Id = ic.getUuid();
		ICustomer icInDatabase =  kundeDao.findById(ic_Id);
		if(icInDatabase == null || !icInDatabase.equals(ic)) {
			return false;
		}
		try {
			return ablesungDao.insert(ir);
		}catch(Exception e) {
			return false;
		}
	}
	
	public Reading findReadingByUUID(UUID uuid) {
		return ablesungDao.findById(uuid);
	}
	
	public List<Reading> getAllReadings(){
		return ablesungDao.getAll();
	}
	
	public boolean updateReading(Reading r) {
		if(r == null || r.getCustomer() == null) {
			return false;
		}
		if(findCustomer(r.getCustomer().getUuid()) == null) {
			return false;
		}
		return ablesungDao.update(r);
	}
	
	public boolean deleteReading(UUID uuid) {
		return ablesungDao.delete(uuid);
	}
}
