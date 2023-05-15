package main;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import dbConnection.DatabaseConnector;
import model.Customer;
import model.ICustomer;
import model.IReading;
import model.Reading;

public class Main {
	
	public static void main(String[] args) {
		DatabaseConnector connector = DatabaseConnector.getDatabaseConnector();
		Customer c = new Customer();
		c.setFirstname("a");
		c.setLastname("A");
		c.setUuid(UUID.randomUUID());
		connector.addCustomer(c);
		c.setFirstname("b");
//		c.setUuid(null);
		boolean b = connector.updateCustomer(c);
		System.out.println("Customer updated: " + b);
	
		ICustomer found = connector.findCustomer(c.getUuid());
		System.out.println("Customer found: " + found);
		
		List<? extends ICustomer> list = connector.getAllCustomer();
		System.out.println("Alle Kunden:");
		list.stream().forEach(System.out::println);
	
//		Tested
//		System.out.println("To Delete:" + c.toString());
//		boolean deleted = connector.deletCustomer(c.getUuid());
//		System.out.println("Deleted:" +deleted);
		
		System.out.println(connector.deletCustomer(null));
		
		System.out.println("--------");
		System.out.println("add reading");
		Reading ir = new Reading();
		ir.setUuid(UUID.randomUUID());
		ir.setCustomer(c);
		ir.setDateOfReading(LocalDate.now());
		ir.setComment("test");
		ir.setKindOfMeter("bla");
		connector.addReading(ir);
		
		System.out.println("Reading added");
		
		Reading r = connector.findReadingByUUID(ir.getUuid());
		System.out.println("Found Reading:");
		System.out.println(r);
		
		System.out.println("All readings");
		List<Reading> readings = connector.getAllReadings();
		readings.stream().forEach(System.out::println);
	
		System.out.println("--Update---");
		ir.setComment("Updated");
		ir.setDateOfReading(LocalDate.of(2022, 8, 10));
		b = connector.updateReading(ir);
		System.out.println("updated: "+b);
		System.out.println("----Delete Customer");
		connector.deletCustomer(c.getUuid());
	}

}
