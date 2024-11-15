package com.colak.springtutorial.service;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class JasperReportService {

    public JasperPrint generateReport(Map<String, Object> parameters, Object dataSource) throws JRException {
        // Load the .jrxml file
        InputStream reportStream = getClass().getResourceAsStream("/reports/report.jrxml");
        if (reportStream == null) {
            throw new JRException("Report template not found");
        }

        // Compile the JRXML file into a JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Fill the report with data
        return JasperFillManager.fillReport(jasperReport, parameters, (JRDataSource) dataSource);
    }
}

