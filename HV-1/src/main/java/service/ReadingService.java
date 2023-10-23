package service;

import java.util.List;
import java.util.UUID;

import dev.hv.db.dao.ICustomerDAO;
import dev.hv.db.dao.IReadingDAO;
import dev.hv.db.init.IConnection;
import dev.hv.db.init.IReadingService;
import dev.hv.db.model.ICustomer;
import dev.hv.db.model.IReading;
import model.Customer;

public class ReadingService implements IReadingService{

	private final IReadingDAO readingDAO;
	private final ICustomerDAO customerDAO;
	
	public ReadingService(IConnection connection) {
		readingDAO = connection.getReadingDAO();
		customerDAO = connection.getCustomerDAO();
	}

	@Override
	public boolean insert(IReading ir) {
		System.out.println("addReading");
		System.out.println(ir);
		if(ir == null) {
			return false;
		}
		ICustomer ic = ir.getCustomer();
		if(ic == null) {
			return false;
		}
		Customer c = new Customer(ic);
		UUID c_Id = c.getUuid();
		ICustomer icInDatabase =  customerDAO.findById(c_Id);
		System.out.println("Serach Customer in DB");
		System.out.println(icInDatabase);
		if(icInDatabase == null || !icInDatabase.equals(c)) {
			return false;
		}
		System.out.println("Customer found at Reading");
		try {
			return readingDAO.insert(ir);
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public IReading findById(UUID uuid) {
		return readingDAO.findById(uuid);
	}

	@Override
	public List<? extends IReading> getAll() {
		return readingDAO.getAll();
	}

	@Override
	public boolean update(IReading r) {
		if(r == null || r.getCustomer() == null) {
			return false;
		}
		if(customerDAO.findById((r.getCustomer().getUuid())) == null) {
			return false;
		}
		return readingDAO.update(r);
	}

	@Override
	public boolean delete(UUID uuid) {
		return readingDAO.delete(uuid);
	}

}
