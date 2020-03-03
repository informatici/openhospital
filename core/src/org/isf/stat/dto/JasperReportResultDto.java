package org.isf.stat.dto;

import java.io.Serializable;

import net.sf.jasperreports.engine.JasperPrint;

public class JasperReportResultDto implements Serializable {

    private JasperPrint jasperPrint;
    private String jasperFile;
    private String filename;

    public JasperReportResultDto(JasperPrint jasperPrint, String jasperFile, String filename) {
        this.jasperPrint = jasperPrint;
        this.jasperFile = jasperFile;
        this.filename = filename;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public String getJasperFile() {
        return jasperFile;
    }

    public void setJasperFile(String jasperFile) {
        this.jasperFile = jasperFile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
