package com.chunarevsa.cloud.service;

import java.text.SimpleDateFormat;
import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
    
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 300000)
	public void reportCurrentTime() {

		String url = "http://api.nbp.pl/api/exchangerates/rates/a/gbp/2012-01-01/2012-01-31/";   
    try
    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(url);

        doc.getDocumentElement().normalize();
        System.out.println ("Root element: " + 
                    doc.getDocumentElement().getNodeName());

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
		log.info("База данных успешно обновлена", dateFormat.format(new Date()));
	}

}
