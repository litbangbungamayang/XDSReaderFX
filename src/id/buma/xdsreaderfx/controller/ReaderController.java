/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.controller;

import id.buma.xdsreaderfx.MainApp;
import id.buma.xdsreaderfx.dao.XdsDAOSQL;
import id.buma.xdsreaderfx.model.DataXDS;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public class ReaderController {
    
    MainApp mainApp;
    MainScreenController msc;
    public File workingDirectory;
    public static final File DEFAULT_DIRECTORY = new File(System.getProperty("user.home") + "/XDS");
    public ObservableList<File> listXdsFiles = FXCollections.observableArrayList();
    public ObservableList<String> listXdsFileName = FXCollections.observableArrayList();
    private static final XdsDAOSQL xdsdao = new XdsDAOSQL();
    private static final BusinessFlow bflow = new BusinessFlow();
    
    public ScheduledService xdsService = new ScheduledService() {
        @Override
        protected Task createTask() {
            return new Task() {
                @Override
                protected Object call() throws Exception {
                    Platform.runLater(() -> {
                        if (msc.svcStatus.get()){
                            listAllXds();
                            if (!listXdsFiles.isEmpty()){
                                if (readXds(listXdsFiles.get(0))){
                                    renameFile(listXdsFiles.get(0), Boolean.TRUE);
                                } else {
                                    renameFile(listXdsFiles.get(0), false);
                                }
                            }
                        } else {
                            cancel();
                        }
                    });
                    return null;
                }
            };
        }
    };
            
    public ReaderController(MainApp mainApp, MainScreenController msc){
        this.mainApp = mainApp;
        this.msc = msc;
        workingDirectory = DEFAULT_DIRECTORY;
        xdsService.setOnCancelled((event) -> {
            msc.svcStatus.set(false);
            xdsService.reset();
        });
        xdsService.setPeriod(Duration.seconds(1));
    }
    
    public ReaderController(){
        
    }

    public File getDirectory(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("XDS Working Dir");
        if (DEFAULT_DIRECTORY.exists()){
            dc.setInitialDirectory(DEFAULT_DIRECTORY);
        } else {
            DEFAULT_DIRECTORY.mkdir();
            dc.setInitialDirectory(DEFAULT_DIRECTORY);
        }
        return dc.showDialog(null);
    }
            
    public void listAllXds(){
        listXdsFiles.clear();
        listXdsFiles.addAll(workingDirectory.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".xls")
        ));
        listXdsFileName.clear();
        listXdsFileName.addAll(workingDirectory.list((dir, name) -> 
            name.toLowerCase().endsWith(".xls")
        ));
    }
    
    public Boolean readXds(File xdsFile){
        try (Workbook workbook = WorkbookFactory.create(xdsFile)){
            Sheet sheet = workbook.getSheet("Results");
            String confirmationCheck = "NIRSystems XDS ";
            Row rowConfirmation = sheet.getRow(7);
            Cell cellConfirmation = rowConfirmation.getCell(2);
            int dataNumber = 15;
            final int DATE_COLUMN = 1;
            final int TIME_COLUMN = 2;
            final int ID_COLUMN = 3;
            final int BRIX_COLUMN = 11;
            final int POL_COLUMN = 14;
            List<DataXDS> lxds = FXCollections.observableArrayList();
            if (cellConfirmation.getStringCellValue().equals(confirmationCheck)){
                do {
                    try {
                        Cell dateCell = sheet.getRow(dataNumber).getCell(DATE_COLUMN); //idColumn = 0
                        Cell timeCell = sheet.getRow(dataNumber).getCell(TIME_COLUMN); //idColumn = 1
                        Cell idCell = sheet.getRow(dataNumber).getCell(ID_COLUMN); //idColumn = 2
                        Cell brixCell = sheet.getRow(dataNumber).getCell(BRIX_COLUMN); //idColumn = 3
                        Cell polCell = sheet.getRow(dataNumber).getCell(POL_COLUMN); //idColumn = 4
                        String idAnalisa;
                        if (idCell.getCellType() == CellType.STRING){
                            idAnalisa = idCell.getStringCellValue();
                        } else {
                            Double value = idCell.getNumericCellValue();
                            Long longvalue = value.longValue();
                            idAnalisa = longvalue.toString();
                        }
                        SimpleDateFormat sdf =  new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                        String dateContent = dateCell.getStringCellValue();
                        if (dateContent.equals("")) dateContent = "01/01/1980";
                        String timeContent = timeCell.getStringCellValue();
                        if (timeContent.equals("")) timeContent = "00:00:00";
                        java.sql.Timestamp tglTs = new java.sql.Timestamp(sdf.parse(dateContent + " " + timeContent).getTime());
                        Double brix = brixCell.getNumericCellValue();
                        Double pol = polCell.getNumericCellValue();
                        DecimalFormat dfDuaDesimal = new DecimalFormat("#.##");
                        DecimalFormat dfSatuDesimal = new DecimalFormat("#.#");
                        dfDuaDesimal.setRoundingMode(RoundingMode.HALF_UP);
                        dfSatuDesimal.setRoundingMode(RoundingMode.HALF_UP);
                        // NILAI-NILAI ANALISA
                        brix = Double.valueOf(dfSatuDesimal.format(brix));
                        //brix = brix;
                        pol = Double.valueOf(dfSatuDesimal.format(pol));
                        Double purity = Double.valueOf(dfSatuDesimal.format((pol/brix)*100));
                        // KASUS KHUSUS
                            if (purity > 83){
                                pol = Double.valueOf(dfSatuDesimal.format(0.83*brix));
                                purity = 83.0;
                            }
                        // --------------------
                        // --------------------
                        //System.out.println("Row = " + dataNumber);
                        DataXDS dataxds = new DataXDS(idAnalisa, tglTs ,brix, pol, purity, bflow.cekRafaksi(idAnalisa, brix, pol, purity));
                        lxds.add(dataxds);
                        dataNumber ++;
                    } catch (Exception e){
                        writeLog("File name = " + xdsFile.getName() + "; Error at row = " + (dataNumber + 1) + " ;" + e.toString());
                        lxds.clear();
                        break;
                    }
                } while (!sheet.getRow(dataNumber).getCell(ID_COLUMN).getStringCellValue().equals(""));
                if (!lxds.isEmpty()){
                    if (xdsdao.insertXds(lxds)) return true;
                }
            } else {
                return false;
            }
        } catch (IOException | EncryptedDocumentException ex) {
            Logger.getLogger(ReaderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void renameFile(File xdsFile, Boolean status){
        if (status){
            try {
                LocalDateTime timestamp = LocalDateTime.now();
                String strTimestamp = timestamp.toString().replace(":", "_");
                Files.copy(xdsFile.toPath(), xdsFile.toPath().resolveSibling(xdsFile.getName() + "." + strTimestamp + ".uploaded"));
                xdsFile.delete();
            } catch (IOException ex) {
                Logger.getLogger(ReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                LocalDateTime timestamp = LocalDateTime.now();
                String strTimestamp = timestamp.toString().replace(":", "_");
                Files.copy(xdsFile.toPath(), xdsFile.toPath().resolveSibling(xdsFile.getName() + "." + strTimestamp + ".failed"));
                xdsFile.delete();
            } catch (IOException ex) {
                Logger.getLogger(ReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void writeLog(String logMessage){
        File logFile = new File("log.txt");
        try {
            logFile.createNewFile();
            String newLine = System.getProperty("line.separator");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))){
                String timestamp = String.valueOf((LocalDateTime.now().getDayOfMonth()<10?"0":"") + LocalDateTime.now().getDayOfMonth()) + "-" +
                        String.valueOf(LocalDateTime.now().getMonth()) + "-" +
                        String.valueOf(LocalDateTime.now().getYear()) + " " +
                        String.valueOf((LocalDateTime.now().getHour()<10?"0":"") + LocalDateTime.now().getHour()) + ":" +
                        String.valueOf((LocalDateTime.now().getMinute()<10?"0":"") + LocalDateTime.now().getMinute()) + ":" +
                        String.valueOf((LocalDateTime.now().getSecond()<10?"0":"") + LocalDateTime.now().getSecond());
                bw.write(timestamp + " : " + logMessage + newLine);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReaderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
