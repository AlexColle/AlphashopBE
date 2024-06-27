package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.repository.BarcodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)

public class BarcodeServiceImpl implements BarcodeService{
    @Autowired
    BarcodeRepository barcodeRepository;

    @Override
    public Barcode SelByBarcode(String Barcode) {
        return barcodeRepository.findByBarcode(Barcode);
    }
}
