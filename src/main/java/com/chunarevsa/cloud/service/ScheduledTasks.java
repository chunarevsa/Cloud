package com.chunarevsa.cloud.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
    
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 300000)
	public void updateInfo() throws ParserConfigurationException, MalformedURLException, IOException, SAXException {
    
    log.info("Старт обновления базы данных", dateFormat.format(new Date()));


	String url = "https://s3.amazonaws.com/cloudaware-test";
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setNamespaceAware(false);
    f.setValidating(false);
    DocumentBuilder b = f.newDocumentBuilder();
    URLConnection urlConnection = new URL(url).openConnection();
    urlConnection.addRequestProperty("Accept", "application/xml");
    Document doc = b.parse(urlConnection.getInputStream());
    doc.getDocumentElement().normalize();
    System.out.println ("Root element: " +  doc.getDocumentElement().getNodeName());


	log.info("База данных успешно обновлена", dateFormat.format(new Date()));
	}

}
