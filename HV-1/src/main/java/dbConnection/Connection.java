package dbConnection;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import dao.ICustomerDAO;
import dao.IReadingDAO;
import dev.hv.db.init.IConnection;
import dev.hv.db.init.IFacilityService;
import model.Customer;
import model.Reading;
import service.FacilityService;

public class Connection implements IConnection{
	
	private static IFacilityService controller;
	private static Connection singleton;
	
	private final Jdbi jdbi;
	private final Handle handle;
	
	private ICustomerDAO iCustomerDAO;
	private IReadingDAO iReadingDAO;
	
	private Connection() {
		jdbi = null;
		handle = null;
	}
	
	private Connection(String url, String user, String pw) {
		jdbi = Jdbi.create(url, user, pw);
		jdbi.installPlugin(new SqlObjectPlugin());
		handle = jdbi.open();
		setUpHandle();
		controller = new FacilityService(this);
	}
	
	
	private void setUpHandle() {
		handle.registerArgument(new UUIDArgumentFactory());
		handle.registerRowMapper(BeanMapper.factory(Customer.class));
		handle.registerRowMapper(BeanMapper.factory(Reading.class));
		System.out.println("Add to JDBI");
		iCustomerDAO = handle.attach(ICustomerDAO.class);
		iReadingDAO = handle.attach(IReadingDAO.class);
		System.out.println("added to jdbi");
	}
	
	public static Connection getConnection() {
		if(singleton == null) {
			singleton = new Connection();
		}
		return singleton;
	}
	

	@Override
	public IFacilityService getFacilityService() {
		return controller;
	}


	@Override
	public void closeConnection() {
		handle.close();
		controller = null;
	}


	@Override
	public ICustomerDAO getCustomerDAO() {
		return iCustomerDAO;
	}


	@Override
	public IReadingDAO getReadingDAO() {
		return iReadingDAO;
	}


	@Override
	public IConnection openConnection(String url, String user, String password) {
		try {
			singleton = new Connection(url, user, password);
			System.out.println("Connection created");
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return singleton;
	}


	@Override
	public void createAllTables() {
		iCustomerDAO.createTable();
		iReadingDAO.createTable();
	}


	@Override
	public void truncateAllTables() {
		iReadingDAO.truncate();
		iCustomerDAO.truncate();
	}

	@Override
	public void removeAllTables() {
		iReadingDAO.removeTable();
		iCustomerDAO.removeTable();
	}


	

}
