package model;

import java.time.LocalDate;
import java.util.UUID;

import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reading implements IReading{
	
	public static final String FIELD_UUID = "reading_uuid";
	public static final String FIELD_DATE_OF_READING = "reading_dateOfReading";
	public static final String FIELD_COMMENT = "reading_comment";
	public static final String FIELD_KIND_OF_METER = "reading_kindOfMeter";
	public static final String FIELD_METER_ID = "reading_meterId";
	public static final String FIELD_SUBSTITUTE = "reading_substitute";
	public static final String FIELD_METERCOUNT = "reading_meterCount";
	
	@EqualsAndHashCode.Include
	@ColumnName(FIELD_UUID)
	private UUID uuid;
	@Nested
	private ICustomer customer;
	@ColumnName(FIELD_DATE_OF_READING)
	private LocalDate dateOfReading;
	@ColumnName(FIELD_COMMENT)
	private String comment;
	@ColumnName(FIELD_KIND_OF_METER)
	private String kindOfMeter;
	@ColumnName(FIELD_METER_ID)
	private String meterId;
	@ColumnName(FIELD_SUBSTITUTE)
	private Boolean substitute;
	@ColumnName(FIELD_METERCOUNT)
	private Double metercount;

}
