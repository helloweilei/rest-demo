package charlie.springbootmvc.restervice.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CsvInstantConverter extends AbstractBeanField<Instant, Instant> {
    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            return Instant.from(zonedDateTime);
        } catch(IllegalArgumentException ex) {
            throw new CsvConstraintViolationException();
        } catch (DateTimeException ex) {
            throw new CsvDataTypeMismatchException();
        }
    }
}
