package org.easybatch.integration.opencsv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;

import java.io.StringReader;
import java.util.List;

/**
 * A record mapper that uses <a href="http://opencsv.sourceforge.net">Open CSV</a> to map a delimited record to domain object.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class OpenCsvRecordMapper<T> implements RecordMapper<T> {

    private CSVReader openCsvReader;

    private char delimiter = ',';

    private char qualifier;

    private ColumnPositionMappingStrategy<T> strategy;

    CsvToBean csvToBean;

    public OpenCsvRecordMapper(Class<? extends T> recordClass, String[] columns) {
        this.strategy = new ColumnPositionMappingStrategy<T>();
        this.strategy.setType((Class<T>) recordClass);
        this.strategy.setColumnMapping(columns);
        csvToBean = new CsvToBean();
    }

    @Override
    public T mapRecord(Record record) throws Exception {
        String recordContent = (String) record.getRawContent();
        openCsvReader = new CSVReader(new StringReader(recordContent), delimiter, qualifier);
        List list = csvToBean.parse(strategy, openCsvReader);
        return (T) list.get(0);
    }

    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }

    public void setQualifier(final char qualifier) {
        this.qualifier = qualifier;
    }

}