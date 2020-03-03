package org.isf.stat.manager;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.medicals.model.Medical;
import org.isf.stat.dto.JasperReportResultDto;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.db.DbSingleJpaConn;
import org.isf.utils.db.UTF8Control;
import org.isf.utils.excel.ExcelExporter;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class JasperReportsManager {

    private final Logger logger = LoggerFactory.getLogger(JasperReportsManager.class);
    
    @Autowired
    private HospitalBrowsingManager hospitalManager;

    public JasperReportResultDto getExamsListPdf() throws OHServiceException {

        try {
            final Map<String, Object> parameters = new HashMap<String, Object>();
            Hospital hospital = hospitalManager.getHospital();

            parameters.put("hospital", hospital.getDescription());

            String jasperFileName = "examslist";

            StringBuilder pdfFilename = new StringBuilder();
            pdfFilename.append("rpt");
            pdfFilename.append(File.separator);
            pdfFilename.append("PDF");
            pdfFilename.append(File.separator);
            pdfFilename.append(jasperFileName);
            pdfFilename.append(".pdf");

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename.toString(), parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename.toString());
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getDiseasesListPdf() throws OHServiceException {

        try{
            HashMap<String, String> parameters = new HashMap<String, String>();
            Hospital hospital = hospitalManager.getHospital();
            parameters.put("hospital", hospital.getDescription());

            String jasperFileName = "diseaseslist";
            StringBuilder pdfFilename = new StringBuilder();
            pdfFilename.append("rpt");
            pdfFilename.append(File.separator);
            pdfFilename.append("PDF");
            pdfFilename.append(File.separator);
            pdfFilename.append(jasperFileName);
            pdfFilename.append(".pdf");

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename.toString(), parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename.toString());
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportAdmissionPdf(int admID, int patID, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("admID", String.valueOf(admID)); // real param
            parameters.put("patientID", String.valueOf(patID)); // real param

            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + admID +".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename.toString(), parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public JasperReportResultDto getGenericReportBillZPL(Integer billID, String jasperFileName, boolean show, boolean askForPrint) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            
            parameters.put("billID", String.valueOf(billID)); // real param

            StringBuilder sbFilename = new StringBuilder();
            sbFilename.append("rpt");
            sbFilename.append(File.separator);
            sbFilename.append(jasperFileName);
            sbFilename.append("Txt");
            sbFilename.append(".jasper");

            String txtFilename = "rpt/PDF/" + jasperFileName + "_" + billID + ".txt";
            JasperReportResultDto result = generateJasperReport(sbFilename.toString(), txtFilename, parameters);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportBillTxt(Integer billID, String jasperFileName, boolean show, boolean askForPrint) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("billID", String.valueOf(billID)); // real param

            StringBuilder sbFilename = new StringBuilder();
            sbFilename.append("rpt");
            sbFilename.append(File.separator);
            sbFilename.append(jasperFileName);
            sbFilename.append("Txt");
            sbFilename.append(".jasper");

            String txtFilename = "rpt/PDF/" + jasperFileName + "_" + billID + ".txt";
            JasperReportResultDto result = generateJasperReport(sbFilename.toString(), txtFilename, parameters);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportBillPdf(Integer billID, String jasperFileName, boolean show, boolean askForPrint) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("billID", String.valueOf(billID)); // real param

            String pdfFilename = "rpt/PDF/" + jasperFileName + "_" + billID + ".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename.toString(), parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    
    

    public JasperReportResultDto getGenericReportOpdPdf(int opdID, int patID, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("opdID", String.valueOf(opdID)); // real param
            parameters.put("patientID", String.valueOf(patID)); // real param

            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + opdID +".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename.toString(), parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportPatientPdf(Integer patientID, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("patientID", String.valueOf(patientID)); // real param

            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + patientID +".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename.toString(), parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public JasperReportResultDto getGenericReportPharmaceuticalOrderPdf(String jasperFileName) throws OHServiceException {

        try{
            Date date = new Date();
            Format formatter;
            formatter = new SimpleDateFormat("E d, MMMM yyyy");
            String todayReport = formatter.format(date);
            formatter = new SimpleDateFormat("yyyyMMdd");
            String todayFile = formatter.format(date);
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("Date", todayReport);

            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + todayFile.toString()+".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public JasperReportResultDto getGenericReportPharmaceuticalStockPdf(Date date, String jasperFileName, String filter, String groupBy, String sortBy) throws OHServiceException {
    	
    	try{
    		if (date == null)
				date = new Date();
			Format formatter;
			formatter = new SimpleDateFormat("E d, MMMM yyyy");
		    String dateReport = formatter.format(date);
		    formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String dateQuery = formatter.format(date);
		    formatter = new SimpleDateFormat("yyyyMMdd");
		    String dateFile = formatter.format(date);
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("Date", dateReport);
			parameters.put("todate", dateQuery);
			if (groupBy != null) parameters.put("groupBy", groupBy);
			if (sortBy != null) parameters.put("sortBy", sortBy);
			if (filter != null) parameters.put("filter", filter);

            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + dateFile.toString()+".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public void getGenericReportPharmaceuticalStockExcel(Date date, String jasperFileName, String exportFilename, String filter, String groupBy, String sortBy) throws OHServiceException {

        try {
        	if (date == null)
				date = new Date();
			Format formatter;
		    formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String dateQuery = formatter.format(date);
            File jasperFile = new File(compileJasperFilename(jasperFileName));
            
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
            JRQuery query = jasperReport.getMainDataset().getQuery();
            
            String queryString = query.getText();
            queryString = queryString.replace("$P{todate}", "'" + dateQuery + "'");
			if (groupBy != null) queryString = queryString.replace("$P{groupBy}", "'" + groupBy + "'");
			if (sortBy != null) queryString = queryString.replace("$P!{sortBy}", "'" + sortBy + "'");
			if (filter != null) queryString = queryString.replace("$P{filter}", "'" + filter + "'");

            DbQueryLogger dbQuery = new DbQueryLogger();
            ResultSet resultSet = dbQuery.getData(queryString, true);

            File exportFile = new File(exportFilename);
            ExcelExporter xlsExport = new ExcelExporter();
			if (exportFile.getName().endsWith(".xls"))
				xlsExport.exportResultsetToExcelOLD(resultSet, exportFile);
			else
				xlsExport.exportResultsetToExcel(resultSet, exportFile);

        } catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public JasperReportResultDto getGenericReportPharmaceuticalStockCardPdf(String jasperFileName, String exportFileName, Date dateFrom, Date dateTo, Medical medical, Ward ward) throws OHServiceException {
    	
    	try{
    		if (dateFrom == null) {
    			dateFrom = new Date();
    		}
    		if (dateTo == null) {
    			dateTo = new Date();
    		}
    		Format formatter;
		    formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String dateFromQuery = formatter.format(dateFrom);
		    String dateToQuery = formatter.format(dateTo);
		    formatter = new SimpleDateFormat("yyyyMMdd");
		    
		    String language = GeneralData.LANGUAGE;
		    ResourceBundle resourceBundle;
			try {
				resourceBundle = ResourceBundle.getBundle(
						jasperFileName, 
						new Locale(language), 
						new UTF8Control());
			} catch (MissingResourceException e) {
				logger.error(">> no resource bundle for language = " + language + " found for this report.");
				logger.error(e.getMessage());
				resourceBundle = ResourceBundle.getBundle(jasperFileName, new Locale("en"));
			}
			
			HashMap<String, Object> parameters = getHospitalParameters();
			parameters.put("fromdate", dateFromQuery);
			parameters.put("todate", dateToQuery);
			if (medical != null) parameters.put("productID", String.valueOf(medical.getCode()));
			parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));
			parameters.put("REPORT_RESOURCE_BUNDLE", resourceBundle); //we need to pass our custom resource bundle
			if (ward != null) {
				parameters.put("WardCode", String.valueOf(ward.getCode()));
				parameters.put("WardName", String.valueOf(ward.getDescription()));
			}

            String pdfFilename = "rpt/PDF/"+exportFileName.toString()+".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public void getGenericReportPharmaceuticalStockCardExcel(String jasperFileName, String exportFileName, Date dateFrom, Date dateTo, Medical medical, Ward ward) throws OHServiceException {

        try {
        	if (dateFrom == null) {
    			dateFrom = new Date();
    		}
    		if (dateTo == null) {
    			dateTo = new Date();
    		}
    		Format formatter;
		    formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String dateFromQuery = formatter.format(dateFrom);
		    String dateToQuery = formatter.format(dateTo);
		    formatter = new SimpleDateFormat("yyyyMMdd");
		    
            File jasperFile = new File(compileJasperFilename(jasperFileName));
            
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
            JRQuery query = jasperReport.getMainDataset().getQuery();
            
            String queryString = query.getText();
            queryString = queryString.replace("$P{fromdate}", "'" + dateFromQuery + "'");
			queryString = queryString.replace("$P{todate}", "'" + dateToQuery + "'");
			if (medical != null) queryString = queryString.replace("$P{productID}", "'" + medical.getCode() + "'");
			if (ward != null) queryString = queryString.replace("$P{WardCode}", "'" + ward.getCode() + "'");

            DbQueryLogger dbQuery = new DbQueryLogger();
            ResultSet resultSet = dbQuery.getData(queryString, true);

            File exportFile = new File(exportFileName);
            ExcelExporter xlsExport = new ExcelExporter();
			if (exportFile.getName().endsWith(".xls"))
				xlsExport.exportResultsetToExcelOLD(resultSet, exportFile);
			else
				xlsExport.exportResultsetToExcel(resultSet, exportFile);

        } catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }
    
    public JasperReportResultDto getGenericReportPharmaceuticalStockWardPdf(Date date, String jasperFileName, Ward ward) throws OHServiceException {
    	
    	try{
    		if (date == null)
				date = new Date();
			Format formatter;
			formatter = new SimpleDateFormat("E d, MMMM yyyy");
		    String dateReport = formatter.format(date);
		    formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String dateQuery = formatter.format(date);
		    formatter = new SimpleDateFormat("yyyyMMdd");
		    String dateFile = formatter.format(date);
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("Date", dateQuery);
			parameters.put("DateReport", dateReport);
			parameters.put("Ward", ward.getDescription());
			parameters.put("WardCode", ward.getCode());

            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + dateFile.toString()+".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportUserInDatePdf(String fromDate, String toDate, String aUser, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = compileGenericReportUserInDateParameters(fromDate, toDate, aUser);
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String pdfFilename =  "rpt/PDF/" + jasperFileName + "_" + aUser + "_" + date +".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportUserInDateTxt(String fromDate, String toDate, String aUser, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = compileGenericReportUserInDateParameters(fromDate, toDate, aUser);

            StringBuilder sbFilename = new StringBuilder();
            sbFilename.append("rpt");
            sbFilename.append(File.separator);
            sbFilename.append(jasperFileName);
            sbFilename.append("Txt");
            sbFilename.append(".jasper");

            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String txtFilename = "rpt/PDF/" + jasperFileName + "_" + aUser + "_" + date + ".txt";
            JasperReportResultDto result = generateJasperReport(sbFilename.toString(), txtFilename, parameters);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportDischargePdf(int admID, int patID, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = getHospitalParameters();
            parameters.put("admID", String.valueOf(admID)); // real param
            parameters.put("patientID", String.valueOf(patID)); // real param
            String pdfFilename = "rpt/PDF/"+jasperFileName + "_" + admID +".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportFromDateToDatePdf(String fromDate, String toDate, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = compileGenericReportFromDateToDateParameters(fromDate, toDate);
            String pdfFilename = "rpt/PDF/"+jasperFileName+".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }



    public void getGenericReportFromDateToDateExcel(String fromDate, String toDate, String jasperFileName, String exportFilename) throws OHServiceException {

        try{
            File jasperFile = new File(compileJasperFilename(jasperFileName));
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
            JRQuery query = jasperReport.getMainDataset().getQuery();
            String queryString = query.getText();
            queryString = queryString.replace("$P{fromdate}", "'" + fromDate + "'");
            queryString = queryString.replace("$P{todate}", "'" + toDate + "'");

            DbQueryLogger dbQuery = new DbQueryLogger();
            ResultSet resultSet = dbQuery.getData(queryString, true);

            File exportFile = new File(exportFilename);
            ExcelExporter xlsExport = new ExcelExporter();
			if (exportFile.getName().endsWith(".xls"))
				xlsExport.exportResultsetToExcelOLD(resultSet, exportFile);
			else
				xlsExport.exportResultsetToExcel(resultSet, exportFile);

        } catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public JasperReportResultDto getGenericReportMYPdf(Integer month, Integer year, String jasperFileName) throws OHServiceException {

        try{
            HashMap<String, Object> parameters = compileGenericReportMYParameters(month, year);
            String pdfFilename = "rpt/PDF/"+jasperFileName+"_"+year+"_"+month+".pdf";

            JasperReportResultDto result = generateJasperReport(compileJasperFilename(jasperFileName), pdfFilename, parameters);
            JasperExportManager.exportReportToPdfFile(result.getJasperPrint(), pdfFilename);
            return result;
        } catch(OHServiceException e){
            //Already managed, ready to return OHServiceException
            throw e;
        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    public void getGenericReportMYExcel(Integer month, Integer year, String jasperFileName, String exportFilename) throws OHServiceException {

        try{
            File jasperFile = new File(compileJasperFilename(jasperFileName));
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
            JRQuery query = jasperReport.getMainDataset().getQuery();
            String queryString = query.getText();
            queryString = queryString.replace("$P{year}", "'" + year + "'");
            queryString = queryString.replace("$P{month}", "'" + month + "'");

            DbQueryLogger dbQuery = new DbQueryLogger();
            ResultSet resultSet = dbQuery.getData(queryString, true);

            File exportFile = new File(exportFilename);
            ExcelExporter xlsExport = new ExcelExporter();
            if (exportFile.getName().endsWith(".xls"))
				xlsExport.exportResultsetToExcelOLD(resultSet, exportFile);
			else
				xlsExport.exportResultsetToExcel(resultSet, exportFile);

        } catch (OHException e) {
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.stat.reporterror"), OHSeverityLevel.ERROR));
        }
    }

    private HashMap<String,Object> compileGenericReportMYParameters(Integer month, Integer year) throws OHServiceException {
        HashMap<String, Object> parameters = getHospitalParameters();
        parameters.put("year", String.valueOf(year)); // real param
        parameters.put("month", String.valueOf(month)); // real param
        return  parameters;
    }

    private HashMap<String,Object> compileGenericReportUserInDateParameters(String fromDate, String toDate, String aUser) throws OHServiceException {
        HashMap<String, Object> parameters = getHospitalParameters();
        parameters.put("fromdate", fromDate + ""); // real param
        parameters.put("todate", toDate + ""); // real param
        parameters.put("user", aUser + ""); // real param
        return  parameters;
    }

    private HashMap<String,Object> compileGenericReportFromDateToDateParameters(String fromDate, String toDate) throws OHServiceException {
        HashMap<String, Object> parameters = getHospitalParameters();
        parameters.put("fromdate", fromDate + ""); // real param
        parameters.put("todate", toDate + ""); // real param
        return parameters;
    }

    private HashMap<String,Object> getHospitalParameters() throws OHServiceException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Hospital hosp = hospitalManager.getHospital();

        parameters.put("Hospital", hosp.getDescription());
        parameters.put("Address", hosp.getAddress());
        parameters.put("City", hosp.getCity());
        parameters.put("Email", hosp.getEmail());
        parameters.put("Telephone", hosp.getTelephone());
        parameters.put("Currency", hosp.getCurrencyCod());
        return parameters;
    }

    private JasperReportResultDto generateJasperReport(String jasperFilename, String filename, Map parameters) throws JRException, OHException {
        File jasperFile = new File(jasperFilename);
        final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
        final Map localParameters = parameters;
        Connection connection = DbSingleJpaConn.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, localParameters, connection);
        return new JasperReportResultDto(jasperPrint, jasperFilename, filename);
    }

    private String compileJasperFilename(String jasperFileName) {
        StringBuilder sbFilename = new StringBuilder();
        sbFilename.append("rpt");
        sbFilename.append(File.separator);
        sbFilename.append(jasperFileName);
        sbFilename.append(".jasper");
        return  sbFilename.toString();
    }
    
    public String compileDefaultFilename(String defaultFileName) {
    	StringBuilder sbFilename = new StringBuilder();
		sbFilename.append("PDF");
		sbFilename.append(File.separator);
		sbFilename.append(defaultFileName);
        return  sbFilename.toString();
    }
}
