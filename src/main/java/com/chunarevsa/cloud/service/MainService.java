package com.chunarevsa.cloud.service;


import com.chunarevsa.cloud.repository.ContentsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    private final ContentsRepository contentsRepository;

    @Autowired
    public MainService(ContentsRepository contentsRepository) {
        this.contentsRepository = contentsRepository;
    }

    public String getInfo() {


        return "main";
    }

}
