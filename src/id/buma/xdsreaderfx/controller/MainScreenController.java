/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import id.buma.xdsreaderfx.MainApp;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Bayu Anandavi Muhardika
 * 
 */
public class MainScreenController implements Initializable {
    
    @FXML
    private JFXButton btnStartSvc;
    @FXML
    private Button btnChooser;
    @FXML
    private Circle drwCircleStatus;
    @FXML
    private Text txtWorkingDirectory;
    @FXML
    private JFXListView lstFile;
    
    private MainApp mainApp;
    public BooleanProperty svcStatus = new SimpleBooleanProperty(false);
    public ReaderController rc = new ReaderController(mainApp,this);
    
    
    public void setMainApp (MainApp mainApp){
        this.mainApp = mainApp;
        txtWorkingDirectory.setText(rc.workingDirectory.toString());
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        btnStartSvc.setOnAction((event) -> {
            svcStatus.setValue(!svcStatus.get());
            if (svcStatus.get()){
                if (rc.xdsService.getState().equals(Worker.State.READY)) rc.xdsService.start();
                /*
                File xds = new File (rc.workingDirectory.toString() + "/1.xls");
                if (rc.readXds(xds)){
                    rc.renameFile(xds, Boolean.TRUE);
                } else {
                    rc.renameFile(xds, Boolean.FALSE);
                }
                */
            }
        });
        
        btnChooser.setOnAction((event) -> {
            rc.workingDirectory = rc.getDirectory();
            if (rc.workingDirectory == null) rc.workingDirectory = ReaderController.DEFAULT_DIRECTORY;
            System.out.println(rc.workingDirectory.toString());
            txtWorkingDirectory.setText(rc.workingDirectory.toString());
        });
        
        
        //*********** Bindings ********************//
        btnStartSvc.textProperty().bind(Bindings.when(svcStatus).then("Stop").otherwise("Start"));
        drwCircleStatus.fillProperty().bind(Bindings.when(svcStatus).then(javafx.scene.paint.Color.rgb(34, 251, 34))
                .otherwise(javafx.scene.paint.Color.RED));
        lstFile.itemsProperty().bind(new SimpleListProperty(rc.listXdsFileName));
    }    
    
}
