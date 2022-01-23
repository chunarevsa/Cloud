package com.chunarevsa.cloud.service;

import com.chunarevsa.cloud.repository.DataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    private final DataRepository dateRepository;

    @Autowired
    public MainService(DataRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    public String getInfo() {

        

        return "main";
    }

}
