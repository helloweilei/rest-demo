package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.exceptions.CsvParseException;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Service
public class CsvParserServiceImpl<T> implements CsvParserService<T> {
    @Override
    public List<T> parse(File file, Class<? extends T> type) throws CsvParseException {
        try {
            return new CsvToBeanBuilder<T>(new FileReader(file))
                    .withType(type)
                    .build().parse();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CsvParseException();
        }
    }
}
