package dao;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import model.Customer;
import model.ICustomer;

public interface CustomerDAO extends IDAO<ICustomer>{
	
	@Override
	@SqlUpdate("""
			Create Table if not exists Customer(
			id int primary key auto_increment, 
			uuid uuid unique not null,
			lastname varchar(255),
			firstname varchar(255))
			""")
	void createTable();
	
	@Override
	@SqlUpdate("""
			Insert into Customer(uuid, firstname, lastname) 
			values (:uuid, :firstname, :lastname)
			""")
	boolean insert(@BindBean ICustomer c);
	
	@Override
	@SqlUpdate("""
			Update Customer set firstname = :firstname, lastname = :lastname
			where uuid = :uuid
			""")
	boolean update(@BindBean ICustomer c);

	@Override
	@RegisterBeanMapper(Customer.class)
	@SqlQuery("""
			Select * from customer where uuid = :c_uuid 
			""")
	Customer findById(@Bind("c_uuid") UUID id);

	@Override
	@RegisterBeanMapper(Customer.class)
	@SqlQuery("""
			Select * from customer 
			""")
	List<Customer> getAll();

	@Override
	@SqlUpdate("""
			Delete from Customer where uuid = :c_uuid
			""")
	boolean delete(@Bind("c_uuid") UUID id); 
	
	@Override
	@SqlUpdate("""
			Truncate customer
			""")
	void truncate();		

	
}
