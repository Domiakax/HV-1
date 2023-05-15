package dbConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import model.Customer;
import model.Reading;

public class ReadingRowMapper implements RowMapper<Reading>{

	@Override
	public Reading map(ResultSet rs, StatementContext ctx) throws SQLException {
		Reading r = new Reading();
		r.setUuid(UUID.fromString(rs.getString(Reading.FIELD_UUID)));
		r.setComment(rs.getString(Reading.FIELD_COMMENT));
		String date = rs.getString(Reading.FIELD_DATE_OF_READING);
		
		if(date!=null) {
			r.setDateOfReading(LocalDate.parse(date));
		}
		r.setKindOfMeter(rs.getString(Reading.FIELD_KIND_OF_METER));
		r.setMetercount(rs.getDouble(Reading.FIELD_METERCOUNT));
		r.setMeterId(rs.getString(Reading.FIELD_METER_ID));
		r.setSubstitute(rs.getBoolean(Reading.FIELD_SUBSTITUTE));
		
		String customerUUIDString = rs.getString(Customer.FIELD_UUID); 
		if(customerUUIDString != null) {
			Customer c = new Customer();
			c.setUuid(UUID.fromString(customerUUIDString));
			c.setFirstname(rs.getString(Customer.FIELD_FIRSTNAME));
			c.setLastname(rs.getString(Customer.FIELD_LASTNAME));
			r.setCustomer(c);
		}
		return r;
	}

}
