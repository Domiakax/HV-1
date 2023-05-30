package dao;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import dbConnection.ReadingRowMapper;
import model.Reading;

public interface ReadingDAO extends IDAO<Reading>{

	static final int yearBorder = 2;
	
	@Override
	@SqlUpdate("""
			Create table if not exists Reading(
			id int primary key auto_increment,
			uuid uuid unique not null,
			customer_id int,
			dateOfReading date,
			comment varchar(255),
			kindOfReading varchar(255),
			meterId varchar(255),
			substitute boolean,
			meterCount double
			)
			""")
	void createTable();	
	
	@Override
	@SqlUpdate("""
			Insert into Reading(uuid, customer_id, dateOfReading,comment, kindofreading, meterId, substitute,metercount)
			Select :uuid, id, :dateOfReading, :comment, :kindOfMeter, :meterId, :substitute, :metercount
			From customer c
			where c.uuid = :customer.uuid; 
			""")
	void insert(@BindBean Reading o);		
	
	@Override
	@SqlQuery("""
			Select r.uuid as reading_uuid, r.dateOfReading as reading_dateOfReading, 
				   r.comment as reading_comment, r.kindOfReading as reading_kindOfMeter,
				   r.meterId as reading_meterId, r.substitute as reading_substitute, r.meterCount as reading_meterCount,
				   c.uuid as customer_uuid, c.firstname as customer_firstname, c.lastname as customer_lastname
				   From reading r left join customer c on r.customer_id = c.id
				   where r.uuid = :r_uuid
			""")
	@RegisterRowMapper(ReadingRowMapper.class)
	Reading findById(@Bind("r_uuid") UUID uuid);
	
	//2 Jahre
	@Override
	@SqlQuery("""
			Select r.uuid as reading_uuid, r.dateOfReading as reading_dateOfReading, 
				   r.comment as reading_comment, r.kindOfReading as reading_kindOfMeter,
				   r.meterId as reading_meterId, r.substitute as reading_substitute, r.meterCount as reading_meterCount,
				   c.uuid as customer_uuid, c.firstname as customer_firstname, c.lastname as customer_lastname
				   From reading r left join customer c on r.customer_id = c.id
			  	   where r.dateOfReading >= Date(concat_ws('-', year(now())-2,'01','01'))
			  """)
	@RegisterRowMapper(ReadingRowMapper.class)
	List<Reading> getAll(); 
	
	//ToDo
	@Override
	@SqlUpdate("""
			Update Reading set customer_id = (Select id from customer where uuid = :customer.uuid),
			dateOfReading = :dateOfReading, comment = :comment, kindOfReading = :kindOfMeter, meterId = :meterId, 
			substitute = :substitute, meterCount = :metercount
			where uuid = :uuid
			""")
	boolean update(@BindBean Reading r);

	@Override
	@SqlUpdate("""
			Delete from Reading where uuid = :r_uuid 
			""")
	boolean delete(@Bind("r_uuid") UUID id);
	
	@SqlUpdate("""
			Update reading set customer_id = null where customer_id = (Select id from customer where uuid = :c_uuid)
			""")
	void customerDeleted(@Bind("c_uuid") UUID customerUUID);

	
}
