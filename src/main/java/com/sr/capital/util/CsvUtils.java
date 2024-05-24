package com.sr.capital.util;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

import static com.opencsv.ICSVWriter.DEFAULT_SEPARATOR;

@Component
public class CsvUtils {

    public <T> byte[] createCSV(List<T> t, String headers) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             OutputStreamWriter streamWriter = new OutputStreamWriter(stream)) {
            StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(streamWriter).withSeparator(DEFAULT_SEPARATOR).build();
            streamWriter.append(headers);
            sbc.write(t);
            streamWriter.flush();
            return stream.toByteArray();
        }
    }

    public <T> byte[] createCSV(T t, String headers) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return createCSV(Collections.singletonList(t), headers);
    }
}
