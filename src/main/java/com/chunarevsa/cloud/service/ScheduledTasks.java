package com.chunarevsa.cloud.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.chunarevsa.cloud.entity.Content;
import com.chunarevsa.cloud.entity.Owner;
import com.chunarevsa.cloud.entity.StorageClass;
import com.chunarevsa.cloud.entity.TempContent;
import com.chunarevsa.cloud.repository.ContentsRepository;
import com.chunarevsa.cloud.repository.OwnerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


@Component
public class ScheduledTasks {
    
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static ArrayList<TempContent> tempContents = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final OwnerRepository ownerRepository;
    private final ContentsRepository contentsRepository;

    @Autowired
    public ScheduledTasks(OwnerRepository ownerRepository, ContentsRepository contentsRepository) {
        this.ownerRepository = ownerRepository;
        this.contentsRepository = contentsRepository;
    }
    
	@Scheduled(fixedRate = 60000) //TODO: 300000
	public void updateInfo() throws ParserConfigurationException, MalformedURLException, IOException, SAXException {
    
    log.info("Старт обновления базы данных :" + dateFormat.format(new Date()));


	String url = "https://s3.amazonaws.com/cloudaware-test";
    
    SAXParserFactory factory = SAXParserFactory.newInstance(); 
    factory.setNamespaceAware(false);
    factory.setValidating(false);

    URLConnection urlConnection = new URL(url).openConnection();
    urlConnection.addRequestProperty("Accept", "application/xml");

    SAXParser parser = factory.newSAXParser(); 
    AdvancedXMLHandler handler = new AdvancedXMLHandler();
    parser.parse(urlConnection.getInputStream(), handler);

    System.err.println("Успешный парсинг");
    int newObject = 0; //новых записей
    int editedObject = 0; // измененных записей
    int oldObject = 0;
    for (TempContent tempContent: tempContents) {

        Content newContent = contentsRepository.findByContentKey(tempContent.getContentKey()).orElse(null);
        
        if (newContent == null) { // убрать двойные if
            
            newContent = new Content();
            newContent.setContentKey(tempContent.getContentKey());
            newContent.setLastModifided(tempContent.getLastModified());
            newContent.setETag(tempContent.getETag());
            newContent.setSize(tempContent.getSize()); // первая версия версии 

            if (tempContent.getStorageClass().equalsIgnoreCase("STANDARD")) {
                newContent.setStorageClass(StorageClass.STANDARD);
            } else {
                newContent.setStorageClass(StorageClass.NOT_STANDARD);
            }   
            
            validateAndSaveOwner(tempContent, newContent);
            newObject++;
    
        } else if (!newContent.getLastModifided().equalsIgnoreCase(tempContent.getLastModified())) {
            
            newContent.setLastModifided(tempContent.getLastModified());
            newContent.setETag(tempContent.getETag());
            newContent.setSize(tempContent.getSize()); // изменение версии 

            if (tempContent.getStorageClass().equalsIgnoreCase("STANDARD")) {
                newContent.setStorageClass(StorageClass.STANDARD);
            } else {
                newContent.setStorageClass(StorageClass.NOT_STANDARD);
            }

            validateAndSaveOwner(tempContent, newContent);
            editedObject++;

        } else {
            oldObject++;
        }

        
        

        //System.err.println(savedOwner);

        // первый вар: сохранять всех contents как в StoreApi
        /* System.err.println("первый вар");

        owner.getContents().add(newContent);
        saveContents(owner.getContents()); */

        // второй вар : сохранять только новый контент
        /*System.err.println("второй вар");
        owner.getContents().add(newContent);
        saveContent(newContent); */

        // третий вар: добавление уже сохраненного content
        /* System.err.println("3 вар");
        Content savedContent = saveContent(newContent);
        owner.getContents().add(savedContent); */
        
    } 
    
    System.err.println("Количесвто :" + tempContents.size());
    ownerRepository.findAll().forEach(owner -> System.out.println(owner.getDisplayName()));
    ownerRepository.findAll().forEach(owner -> System.out.println(owner.getBucketId()));
    tempContents.clear();
    System.err.println("Количество новых записей :" + newObject);
    System.err.println("Количество измененных записей :" + editedObject);
    System.err.println("Количество объектов без изменений :" + oldObject);
    
 
	log.info("База данных успешно обновлена :" + dateFormat.format(new Date()));
	}

    private void validateAndSaveOwner(TempContent tempContent, Content newContent) {
        
        Owner owner = ownerRepository.findByBucketId(tempContent.getBucketId()).orElse(null);

        if (owner == null) {
            System.err.println("Новый владелец");
            owner = new Owner();
            owner.setBucketId(tempContent.getBucketId());
            owner.setDisplayName(tempContent.getDisplayName());
        } 

        // четвёртый вар. новый список через set
        owner.getContents().add(newContent);
        Set<Content> contents = owner.getContents();
        owner.setContents(contents); 

        ownerRepository.save(owner);
    }

    private static class AdvancedXMLHandler extends DefaultHandler {

        private String key;
        private String lastModified;
        private String eTag;
        private String size;
        private String lastElementName;
        private String bucketId;
        private String displayName;
        private String storageClass;


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
                 (size != null && !size.isEmpty()) &&
                 (bucketId != null && !bucketId.isEmpty()) &&
                 (displayName != null && !displayName.isEmpty()) &&
                 (storageClass != null && !storageClass.isEmpty()) ) {
                
                tempContents.add(new TempContent(key, lastModified, eTag, size, bucketId, displayName, storageClass));

                key = null;
                lastModified = null;
                eTag = null;
                size = null;
                bucketId = null;
                displayName = null;
                storageClass = null;
            }
        }

        public AdvancedXMLHandler() {
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
                if (lastElementName.equalsIgnoreCase("Key"))
                    key = information;
                if (lastElementName.equalsIgnoreCase("lastModified"))
                    lastModified = information;
                if (lastElementName.equalsIgnoreCase("eTag"))
                    eTag = information;
                if (lastElementName.equalsIgnoreCase("size"))
                    size = information;
                if (lastElementName.equals("ID"))
                    bucketId = information; 
                if (lastElementName.equalsIgnoreCase("displayName"))
                    displayName = information; 
                if (lastElementName.equalsIgnoreCase("storageClass"))
                    storageClass = information;
            }
        }

    }

}
