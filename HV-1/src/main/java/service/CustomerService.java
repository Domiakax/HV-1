package service;

import java.util.List;
import java.util.UUID;

import dev.hv.db.dao.ICustomerDAO;
import dev.hv.db.dao.IReadingDAO;
import dev.hv.db.init.IConnection;
import dev.hv.db.init.ICustomerService;
import dev.hv.db.model.ICustomer;

public class CustomerService implements ICustomerService{
	
	private final ICustomerDAO customerDAO;
	private final IReadingDAO readingDAO;
	
	public CustomerService(IConnection connection) {
		customerDAO = connection.getCustomerDAO();
		readingDAO = connection.getReadingDAO();
	}
	
	public boolean deleteCustomer(UUID uuid) {
//		readingDAO.customerDeleted(uuid);
		boolean deleted = customerDAO.delete(uuid);
		return deleted;
	}

	@Override
	public boolean insert(ICustomer c) {
		if(c == null || c.getUuid() == null) {
			return false;
		}
		try {
			System.out.println("Customer add before jdbi");
			return customerDAO.insert(c);
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public ICustomer findById(UUID uuid) {
		return customerDAO.findById(uuid);
	}

	@Override
	public List<? extends ICustomer> getAll() {
		return customerDAO.getAll();
	}

	@Override
	public boolean update(ICustomer c) {
		if(c == null) {
			return false;
		}
		return customerDAO.update(c);
	}

	@Override
	public boolean delete(UUID uuid) {
		readingDAO.customerDeleted(uuid);
		boolean deleted = customerDAO.delete(uuid);
		return deleted;
	}


}
