package com.colak.springtutorial.controller;

import com.colak.springtutorial.service.JasperReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReportController {

    @Autowired
    private JasperReportService jasperReportService;

    // http://localhost:8080/generateReport?format=pdf
    // http://localhost:8080/generateReport?format=html
    @GetMapping("/generateReport")
    public ResponseEntity<byte[]> generateReport(@RequestParam String format) {
        try {
            // Example data to fill the report
            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_MONTH", "November");
            parameters.put("P_YEAR", "2024");

            // Sample data (In real-world use, fetch this from your database or other source)
            List<UserActivity> userActivities = Arrays.asList(
                    new UserActivity("user1", "Login", "2024-11-01 08:30", "192.168.1.1", "Desktop", "New York", "Chrome", "Windows"),
                    new UserActivity("user2", "Logout", "2024-11-02 09:00", "192.168.1.2", "Mobile", "California", "Safari", "MacOS")
            );

            // Set data source (using JRBeanCollectionDataSource in this case)
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userActivities);

            // Generate the report
            JasperPrint jasperPrint = jasperReportService.generateReport(null, dataSource);

            // Export the report to the desired format (PDF or HTML)
            if ("pdf".equalsIgnoreCase(format)) {
                return exportPdf(jasperPrint);
            } else if ("html".equalsIgnoreCase(format)) {
                return exportHtml(jasperPrint);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    private ResponseEntity<byte[]> exportPdf(JasperPrint jasperPrint) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);

        exporter.setExporterInput(exporterInput);
        exporter.setExporterOutput(exporterOutput);
        exporter.exportReport();

        byte[] reportData = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(reportData);
    }

    public ResponseEntity<byte[]> exportHtml(JasperPrint jasperPrint) throws JRException {
        // Create the HTML exporter
        HtmlExporter exporter = new HtmlExporter();

        // Set the exporter input (JasperPrint instance)
        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);

        // Prepare output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Set the exporter output (HTML content)
        SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(outputStream);

        // Configure the exporter
        exporter.setExporterInput(exporterInput);
        exporter.setExporterOutput(exporterOutput);

        // Perform the export
        exporter.exportReport();

        // Get the generated HTML content
        byte[] reportData = outputStream.toByteArray();

        // Set the HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.html");

        // Return the HTML content as the response
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_HTML)
                .body(reportData);
    }
}

