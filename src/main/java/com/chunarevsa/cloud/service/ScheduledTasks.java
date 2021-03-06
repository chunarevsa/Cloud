package com.chunarevsa.cloud.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.chunarevsa.cloud.entity.Content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


@Component
public class ScheduledTasks {
    
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static ArrayList<Content> contents = new ArrayList<>();
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  /*<Contents>
            <Key>file_2015-08-06.txt</Key>
            <LastModified>2020-11-30T09:38:22.000Z</LastModified>
            <ETag>"090228db8da1203d89d73341c95932b4"</ETag>
            <Size>11</Size>
        <Owner>
            <ID>14fbada9d6aac53a2d851e6c777ffea7cd9ac4d213bee68af9f5d9b247c20c04</ID>
            <DisplayName>malammik</DisplayName>
        </Owner>
        <StorageClass>STANDARD</StorageClass>
    </Contents> */

	@Scheduled(fixedRate = 60000)
	public void updateInfo() throws ParserConfigurationException, MalformedURLException, IOException, SAXException {
    
    log.info("Старт обновления базы данных" + dateFormat.format(new Date()));


	String url = "https://s3.amazonaws.com/cloudaware-test";
    
    SAXParserFactory factory = SAXParserFactory.newInstance(); //
    factory.setNamespaceAware(false);
    factory.setValidating(false);

    URLConnection urlConnection = new URL(url).openConnection();
    urlConnection.addRequestProperty("Accept", "application/xml");

    SAXParser parser = factory.newSAXParser(); //
    AdvancedXMLHandler handler = new AdvancedXMLHandler();
    parser.parse(urlConnection.getInputStream(), handler);

    for (Content content: contents) {
        System.out.println(content);
    } 
    
    System.err.println(contents.size());

	log.info("База данных успешно обновлена" + dateFormat.format(new Date()));
	}

    private static class AdvancedXMLHandler extends DefaultHandler {

        private String key;
        private String lastModified;
        private String eTag;
        private String size;
        private String lastElementName;

        @Override
        public void startDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            lastElementName = qName;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            //System.err.println("endElement");

            if ( (key != null && !key.isEmpty()) && 
                 (lastModified != null && !lastModified.isEmpty()) &&
                 (eTag != null && !eTag.isEmpty()) &&
                 (size != null && !size.isEmpty()) ) {
                
                Content newContent = new Content(key, lastModified, eTag, size);
                System.err.println(newContent.getContentKey());

                contents.add(new Content(key, lastModified, eTag, size));

                key = null;
                lastModified = null;
                eTag = null;
                size = null;
            }
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            // TODO Auto-generated method stub
            super.ignorableWhitespace(ch, start, length);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            //System.err.println("characters");

            String info = new String(ch, start, length);
            
            String information = info.replace("\"", "").replace("\n", "").trim();
            //System.err.println(information);

            if (!information.isEmpty()) {
                if (lastElementName.equalsIgnoreCase("key"))
                    key = information;
                if (lastElementName.equalsIgnoreCase("lastModified"))
                    lastModified = information;
                if (lastElementName.equalsIgnoreCase("eTag"))
                    eTag = information;
                if (lastElementName.equalsIgnoreCase("size"))
                    size = information; 
         
            }
        }

    }

}
