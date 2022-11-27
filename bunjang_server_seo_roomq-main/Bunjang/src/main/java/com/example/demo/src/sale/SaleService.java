package com.example.demo.src.sale;

import com.example.demo.utils.JwtService;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

    private final SaleProvider saleProvider;
    private final JwtService jwtService;
    private final SaleDao saleDao;

    public SaleService(SaleProvider saleProvider, JwtService jwtService, SaleDao saleDao) {
        this.saleProvider = saleProvider;
        this.jwtService = jwtService;
        this.saleDao = saleDao;
    }


}
