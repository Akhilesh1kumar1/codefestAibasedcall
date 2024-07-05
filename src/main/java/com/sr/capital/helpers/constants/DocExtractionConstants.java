package com.sr.capital.helpers.constants;

import java.util.List;

public class DocExtractionConstants {

    public static class FileTypes {

        public static final String PDF = "application/pdf";
        public static final String JPG = "image/jpg";
        public static final String JPEG = "image/jpeg";
        public static final String PNG = "image/png";
        public static final String CSV = "text/csv";

        public static final String EXCEL = "application/vnd.ms-excel";

        public static final String EXCELS = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


        public static final List<String> supportedFileTypes = List.of(PDF, JPG, JPEG, PNG,CSV,EXCEL, EXCELS);
    }

    public static final Integer LIST_SIZE = 10000;
    public static final Integer DPI = 300;
}
