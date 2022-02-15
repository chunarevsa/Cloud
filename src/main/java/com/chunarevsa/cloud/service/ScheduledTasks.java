package com.chunarevsa.cloud.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.chunarevsa.cloud.entity.Content;
import com.chunarevsa.cloud.entity.Owner;
import com.chunarevsa.cloud.entity.StorageClass;
import com.chunarevsa.cloud.entity.TempContent;
import com.chunarevsa.cloud.repository.ContentsRepository;
import com.chunarevsa.cloud.repository.OwnerRepository;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


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

    
    // Работа на прямую через ссылку
    /* SAXParserFactory factory = SAXParserFactory.newInstance(); 
    factory.setNamespaceAware(false);
    factory.setValidating(false);

    URLConnection urlConnection = new URL(url).openConnection();
    urlConnection.addRequestProperty("Accept", "application/xml");

    SAXParser parser = factory.newSAXParser(); 
    AdvancedXMLHandler handler = new AdvancedXMLHandler();
    parser.parse(urlConnection.getInputStream(), handler);  */

    // Для парсинга с фаила
    /* SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();

    AdvancedXMLHandler handler = new AdvancedXMLHandler();
    parser.parse(new File("src/main/resources/2cloudaware-test.xml"), handler); */

    // Скачивает и парсит
    /* System.err.println("1");
    System.err.println("2");
    URL website = new URL(url);
    System.err.println("3");
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
    System.err.println("4");
    FileOutputStream fos = new FileOutputStream("src/main/resources/temp/cloudAw.xml");
    System.err.println("5");
    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    System.err.println("6"); */

    //
   /*  URL website = new URL(url);
    File file = new File("");
    //FileUtils.copyURLToFile(website, file);
    System.err.println("TYT");
    InputStream inputStream = website.openStream();
    Files.copy(inputStream, file.toPath());
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    AdvancedXMLHandler handler = new AdvancedXMLHandler();
    parser.parse(file, handler); */

    //
    System.err.println("1");
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    System.err.println("2");
    factory.setNamespaceAware(false);
    factory.setValidating(false);
    System.err.println("3");
    URLConnection urlConnection = new URL(url).openConnection();
    urlConnection.addRequestProperty("Accept", "application/xml");
    System.err.println("4");
    // Получили из фабрики билдер, который парсит XML, создает структуру Document в виде иерархического дерева.
    DocumentBuilder builder = factory.newDocumentBuilder();
    // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно.
    System.err.println("5");
    Document document = builder.parse(urlConnection.getInputStream());
    System.err.println("6");
    String element = reader.readLine();
    System.err.println("7");
    NodeList matchedElementsList = document.getElementsByTagName(element);
    System.err.println("8");    
    if (matchedElementsList.getLength() == 0) {
        System.out.println("Тег " + element + " не был найден в файле.");
    } else {
        // Получение первого элемента.
        Node foundedElement = matchedElementsList.item(0);

        System.out.println("Элемент был найден!");

        // Если есть данные внутри, вызов метода для вывода всей информации
        if (foundedElement.hasChildNodes())
            printInfoAboutAllChildNodes(foundedElement.getChildNodes());
    }
    

    log.info("Успешный парсинг");

    int newObject = 0; //новых записей
    int editedObject = 0; // измененных записей
    int oldObject = 0;

    /* ArrayList<TempContent> tempCont = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
        tempCont.add(tempContents.get(i));
    } */


    /* for (TempContent tempContent: tempContents) {

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
        
    }  
    
    ownerRepository.findAll().forEach(owner -> System.out.println(owner.getDisplayName()));
    ownerRepository.findAll().forEach(owner -> System.out.println(owner.getBucketId()));
    tempContents.clear();*/
    System.err.println("Количество новых записей :" + newObject);
    System.err.println("Количество измененных записей :" + editedObject);
    System.err.println("Количество объектов без изменений :" + oldObject);
    
    //fos.close();
	log.info("База данных успешно обновлена :" + dateFormat.format(new Date()));
	}

    private void validateAndSaveOwner(TempContent tempContent, Content newContent) {
        
        String tempId = "14fbada9d6aac53a2d851e6c777ffea7cd9ac4d213bee68af9f5d9b247c20c04";
        if (!tempContent.getBucketId().equalsIgnoreCase(tempId)) {
            System.err.println(tempContent.getContentKey());
        }
        Owner owner = ownerRepository.findByBucketId(tempContent.getBucketId()).orElse(null);
        
        if (owner == null) {
            System.err.println("Новый владелец");
            owner = new Owner();
            owner.setBucketId(tempContent.getBucketId());
            owner.setDisplayName(tempContent.getDisplayName());
        } 

        owner.getContents().add(newContent);
        Set<Content> contents = owner.getContents();
        owner.setContents(contents); 

        ownerRepository.save(owner);

    }

private static void printInfoAboutAllChildNodes(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            // У элементов есть два вида узлов - другие элементы или текстовая информация. Потому нужно разбираться две ситуации отдельно.
            if (node.getNodeType() == Node.TEXT_NODE) {
                // Фильтрация информации, так как пробелы и переносы строчек нам не нужны. Это не информация.
                String textInformation = node.getNodeValue().replace("\n", "").trim();

                if(!textInformation.isEmpty())
                    System.out.println("Внутри элемента найден текст: " + node.getNodeValue());
            }
            // Если это не текст, а элемент, то обрабатываем его как элемент.
            else {
                System.out.println("Найден элемент: " + node.getNodeName() + ", его атрибуты:");

                // Получение атрибутов
                NamedNodeMap attributes = node.getAttributes();

                // Вывод информации про все атрибуты
                for (int k = 0; k < attributes.getLength(); k++)
                    System.out.println("Имя атрибута: " + attributes.item(k).getNodeName() + ", его значение: " + attributes.item(k).getNodeValue());
            }

            // Если у данного элемента еще остались узлы, то вывести всю информацию про все его узлы.
            if (node.hasChildNodes())
                printInfoAboutAllChildNodes(node.getChildNodes());
        }
    }   

    private static class AdvancedXMLHandler extends DefaultHandler {

        private String contentKey;
        private String lastModified;
        private String eTag;
        private String size;
        private String bucketId;
        private String displayName;
        private String storageClass;

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

            if ( (contentKey != null && !contentKey.isEmpty()) && 
                 (lastModified != null && !lastModified.isEmpty()) &&
                 (eTag != null && !eTag.isEmpty()) &&
                 (size != null && !size.isEmpty()) &&
                 (bucketId != null && !bucketId.isEmpty()) &&
                 (displayName != null && !displayName.isEmpty()) &&
                 (storageClass != null && !storageClass.isEmpty()) ) {
                
                tempContents.add(new TempContent(contentKey, lastModified, eTag, size, bucketId, displayName, storageClass));

                contentKey = null;
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

            String tempId = "14fbada9d6aac53a2d851e6c777ffea7cd9ac4d213bee68af9f5d9b247c20c04";
            String info = new String(ch, start, length);

            String information = info.replace("\"", "").replace("\n", "").trim();

            if (!information.isEmpty()) {
                if (lastElementName.equalsIgnoreCase("Key"))
                    contentKey = information;
                if (lastElementName.equalsIgnoreCase("lastModified"))
                    lastModified = information;
                if (lastElementName.equalsIgnoreCase("eTag"))
                    eTag = information;
                if (lastElementName.equalsIgnoreCase("size"))
                    size = information;
                if (lastElementName.equals("ID")) {
                    if (!information.equalsIgnoreCase(tempId)) {
                        System.err.println("information :" + information);
                        System.err.println("char[] : " + ch.toString());
                        System.err.println("start : " + start);
                        System.err.println("length : " + length);
                        System.err.println();
                        
                    }
                    bucketId = information; 
                    }
                    
                if (lastElementName.equalsIgnoreCase("displayName"))
                    displayName = information; 
                if (lastElementName.equalsIgnoreCase("storageClass"))
                    storageClass = information;
            }
        }

    }

}
