package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository repository;
}
