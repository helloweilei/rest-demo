package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.exceptions.CsvParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface CsvParserService<T> {
    List<T> parse(File file, Class<? extends T> type) throws CsvParseException;
}
