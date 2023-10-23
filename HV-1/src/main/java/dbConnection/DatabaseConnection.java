//package dbConnection;
//
//import java.util.List;
//import java.util.UUID;
//
//import org.jdbi.v3.core.Handle;
//import org.jdbi.v3.core.Jdbi;
//import org.jdbi.v3.core.mapper.reflect.BeanMapper;
//import org.jdbi.v3.sqlobject.SqlObjectPlugin;
//
//import dao.ICustomerDAO;
//import dao.IReadingDAO;
//import dev.hv.db.init.IDatabaseConnection;
//import dev.hv.db.model.ICustomer;
//import dev.hv.db.model.IReading;
//import model.Customer;
//import model.Reading;
//
//public class DatabaseConnection implements IDatabaseConnection{
//	
//	private static DatabaseConnection connector;
//	public static final String urlDatabase = "jdbc:mariadb://localhost:3306/hv1";
//	public static final String dbUser = "root";
//	public static final String dbUserPW = "";
//	
//	private static ICustomerDAO kundeDao;
//	private static IReadingDAO ablesungDao;
//	private final Handle handle;
//	
//	
//	private DatabaseConnection() {
//		final Jdbi jdbi  = Jdbi.create(urlDatabase, dbUser, dbUserPW);
//		jdbi.installPlugin(new SqlObjectPlugin());
//		handle = jdbi.open();
//		setUpDatabaseConnection();
//	}
//	
//	private DatabaseConnection(String url, String user, String password) {
//		final Jdbi jdbi  = Jdbi.create(url, user, password);
//		jdbi.installPlugin(new SqlObjectPlugin());
//		handle = jdbi.open();
//		setUpDatabaseConnection();
//	}
//	
//	private void setUpDatabaseConnection() {
//		System.out.println("Setup DatabaseConnector");
//		handle.registerArgument(new UUIDArgumentFactory());
//		handle.registerRowMapper(BeanMapper.factory(Customer.class));
//		handle.registerRowMapper(BeanMapper.factory(Reading.class));
//		System.out.println("Setup Create Tables");
//		kundeDao = handle.attach(ICustomerDAO.class);
//		System.out.println("Kunde added to jdbi");
//		ablesungDao = handle.attach(IReadingDAO.class);
//		System.out.println("Reading added to jdbi");
//		System.out.println("create Table");
//		createTables();
//	}
//	
//	public static DatabaseConnection getDatabaseConnector() {
//		if(connector == null) {
//			connector = new DatabaseConnection();
//		}
//		return connector;
//	}
//	
//	public static IDatabaseConnection getDatabaseConnection(String url, String user, String password) {
//		if(connector == null) {
//			connector = new DatabaseConnection(url, user, password);
//		}
//		return connector;
//	}
//	
//	public void truncateTables() {
//		ablesungDao.truncate();
//		kundeDao.truncate();
//	}
//	
//	public boolean insertCustomer(ICustomer c) {
//		if(c == null || c.getUuid() == null) {
//			return false;
//		}
//		try {
//			System.out.println("Customer add before jdbi");
//			return kundeDao.insert(c);
//		} catch(Exception e) {
//			return false;
//		}
//	}
//	
//	public boolean updateCustomer(ICustomer c) {
//		if(c == null) {
//			return false;
//		}
//		return kundeDao.update(c);
//	}
//	
//	public ICustomer getCustomer(UUID uuid) {
//		return kundeDao.findById(uuid);
//	}
//	
//	public List<? extends ICustomer> getAllCustomers(){
//		return kundeDao.getAll();
//	}
//	
//	public boolean deleteCustomer(UUID uuid) {
//		ablesungDao.customerDeleted(uuid);
//		boolean deleted = kundeDao.delete(uuid);
//		return deleted;
//	}
//	
//	public boolean insertReading(IReading ir) {
//		System.out.println("addReading");
//		System.out.println(ir);
//		if(ir == null) {
//			return false;
//		}
//		ICustomer ic = ir.getCustomer();
//		if(ic == null) {
//			return false;
//		}
//		Customer c = new Customer(ic);
//		UUID c_Id = c.getUuid();
//		ICustomer icInDatabase =  kundeDao.findById(c_Id);
//		System.out.println("Serach Customer in DB");
//		System.out.println(icInDatabase);
//		if(icInDatabase == null || !icInDatabase.equals(c)) {
//			return false;
//		}
//		System.out.println("Customer found at Reading");
//		try {
//			return ablesungDao.insert(ir);
//		}catch(Exception e) {
//			return false;
//		}
//	}
//	
//	public Reading getReading(UUID uuid) {
//		return ablesungDao.findById(uuid);
//	}
//	
//	public List<? extends IReading> getAllReadings(){
//		return ablesungDao.getAll();
//	}
//	
//	public boolean updateReading(IReading r) {
//		if(r == null || r.getCustomer() == null) {
//			return false;
//		}
//		if(getCustomer(r.getCustomer().getUuid()) == null) {
//			return false;
//		}
//		return ablesungDao.update(r);
//	}
//	
//	public boolean deleteReading(UUID uuid) {
//		return ablesungDao.delete(uuid);
//	}
//	
//	public void closeConnection() {
//		handle.close();
//		connector = null;
//	}
//
//	@Override
//	public void createTables() {
//		kundeDao.createTable();
//		ablesungDao.createTable();
//	}
//
//	@Override
//	public void removeAllTables() {
//		ablesungDao.removeTable();
//		kundeDao.removeTable();
//	}
//	
//}
