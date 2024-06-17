package com.sr.capital.service;


import com.sr.capital.entity.primary.FileUploadData;

import java.io.InputStream;

public interface FileProcessor {

    void processFile(InputStream fileInputStream, FileUploadData fileUploadData, String tenantId);
}
