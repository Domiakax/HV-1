package service;

import dev.hv.db.init.IConnection;
import dev.hv.db.init.ICustomerService;
import dev.hv.db.init.IFacilityService;
import dev.hv.db.init.IReadingService;

public class FacilityService implements IFacilityService{

	private final ICustomerService customerService;
	private final IReadingService readingService;
	
	
	public FacilityService(IConnection connection) {
		customerService = new CustomerService(connection);
		readingService = new ReadingService(connection);
	}
	
	@Override
	public ICustomerService getICustomerService() {
		return customerService;
	}

	@Override
	public IReadingService getIReadingService() {
		return readingService;
	}

	
	
}
