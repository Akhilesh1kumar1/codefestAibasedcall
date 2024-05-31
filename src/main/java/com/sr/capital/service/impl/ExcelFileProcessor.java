package com.sr.capital.service.impl;

import com.sr.capital.entity.FileUploadData;
import com.sr.capital.service.FileProcessor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ExcelFileProcessor implements FileProcessor {
    @Override
    public void processFile(InputStream fileInputStream, FileUploadData fileUploadData, String tenantId) {

    }
}
