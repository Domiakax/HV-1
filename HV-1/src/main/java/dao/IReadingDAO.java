package dao;

import java.util.List;
import java.util.UUID;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import dbConnection.ReadingRowMapper;
import dev.hv.db.model.IReading;
import model.Reading;

public interface IReadingDAO extends dev.hv.db.dao.IReadingDAO{

	static final int yearBorder = 2;
	
	@Override
	@SqlUpdate("""
			Create table if not exists Reading(
			id int primary key auto_increment,
			uuid uuid unique not null,
			customer_id int,
			dateOfReading date,
			comment varchar(255),
			meterId varchar(255),
			substitute boolean,
			meterCount double
			)
			""")
	void createTable();	
	
	@Override
	@SqlUpdate("""
			Drop Table if exists Reading
			""")
	void removeTable();
	
	@Override
	@SqlUpdate("""
			Insert into Reading(uuid, customer_id, dateOfReading,comment, meterId, substitute,metercount)
			Select :uuid, id, :dateOfReading, :comment, :meterId, :substitute, :metercount
			From customer c
			where c.uuid = :customer.uuid; 
			""")
	boolean insert(@BindBean IReading o);		
	
	@Override
	@SqlQuery("""
			Select r.uuid as reading_uuid, r.dateOfReading as reading_dateOfReading, 
				   r.comment as reading_comment,
				   r.meterId as reading_meterId, r.substitute as reading_substitute, r.meterCount as reading_meterCount,
				   c.uuid as customer_uuid, c.firstname as customer_firstname, c.lastname as customer_lastname
				   From reading r left join customer c on r.customer_id = c.id
				   where r.uuid = :r_uuid
			""")
	@RegisterRowMapper(ReadingRowMapper.class)
	Reading findById(@Bind("r_uuid") UUID uuid);
	
	//2 Jahre
	//where r.dateOfReading >= Date(concat_ws('-', year(now())-2,'01','01'))
	@Override
	@SqlQuery("""
			Select r.uuid as reading_uuid, r.dateOfReading as reading_dateOfReading, 
				   r.comment as reading_comment,
				   r.meterId as reading_meterId, r.substitute as reading_substitute, r.meterCount as reading_meterCount,
				   c.uuid as customer_uuid, c.firstname as customer_firstname, c.lastname as customer_lastname
				   From reading r left join customer c on r.customer_id = c.id
			  """)
	@RegisterRowMapper(ReadingRowMapper.class)
	List<Reading> getAll(); 
	
	//ToDo
	@Override
	@SqlUpdate("""
			Update Reading set customer_id = (Select id from customer where uuid = :customer.uuid),
			dateOfReading = :dateOfReading, comment = :comment, meterId = :meterId, 
			substitute = :substitute, meterCount = :metercount
			where uuid = :uuid
			""")
	boolean update(@BindBean IReading r);

	@Override
	@SqlUpdate("""
			Delete from Reading where uuid = :r_uuid 
			""")
	boolean delete(@Bind("r_uuid") UUID id);
	
	@SqlUpdate("""
			Update reading set customer_id = null where customer_id = (Select id from customer where uuid = :c_uuid)
			""")
	void customerDeleted(@Bind("c_uuid") UUID customerUUID);

	@Override
	@SqlUpdate("""
			Truncate Reading
			""")
	void truncate();
}
