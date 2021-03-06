package org.easybatch.xml;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * A record mapper that maps xml records to domain objects annotated with JaxB2 annotations.
 *
 * @param <T> the target domain object type
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class XmlRecordMapper<T> implements RecordMapper<T> {

    /**
     * Jaxb context.
     */
    private JAXBContext jaxbContext;

    /**
     * The Jaxb Unmarshaller used to map xml records to domain objects.
     */
    private Unmarshaller jaxbUnmarshaller;

    /**
     * Creates an XmlRecordMapper. Using this constructor, no validation against an xsd will be applied.
     * @param type the target domain object type.
     * @throws Exception thrown if an error occurs during the creation of Jaxb context.
     */
    public XmlRecordMapper(Class<? extends T> type) throws Exception {
        jaxbContext = JAXBContext.newInstance(type);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    /**
     *  Creates an XmlRecordMapper.
     * @param type the target domain object type.
     * @param xsd the xsd file against which xml records will be validated
     * @throws Exception thrown if an error occurs during the creation of Jaxb context.
     */
    public XmlRecordMapper(Class<? extends T> type, File xsd) throws Exception {
        jaxbContext = JAXBContext.newInstance(type);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsd);
        jaxbUnmarshaller.setSchema(schema);
    }

    /**
     * A constructor that let's you preconfigure the unmarshaller (with custom adapter, custom listener, etc)
     * to be used by the Xml record mapper.
     *
     * @param unmarshaller the unmarshaller to use
     */
    public XmlRecordMapper(Unmarshaller unmarshaller) {
        this.jaxbUnmarshaller = unmarshaller;
    }

    @Override
    public T mapRecord(final Record record) throws Exception {

        XmlRecord xmlRecord = (XmlRecord) record;

        //return mapped object
        return (T) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xmlRecord.getRawContent().getBytes()));

    }

}