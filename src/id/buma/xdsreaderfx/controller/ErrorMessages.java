/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.buma.xdsreaderfx.controller;

import java.util.Optional;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 *
 * @author Bayu Anandavi Muhardika
 * 
 */

public class ErrorMessages {

    public ErrorMessages(){

    }

    public void showWarningAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("PERINGATAN!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("ERROR!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean showConfirmation(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> pilihan = alert.showAndWait();
        return pilihan.get() == ButtonType.OK;
    }

    public void showInfoAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Alert showWaitAlert(String message){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }
    
    public Dialog showWaitDialog(String message){
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.DECORATED);
        //dialog.setGraphic(new ImageView(getClass().getResource("assets/loading.gif").toString()));
        //Image img = new Image("https://docs.oracle.com/en/dcommon/img/oracle-doc-logo.png");
        Image img = new Image("id/buma/labsysfx/assets/loading-2.gif");
        ImageView imgV = new ImageView(img);
        dialog.setGraphic(imgV);
        dialog.setTitle("Update Check");
        dialog.setHeaderText(message);
        dialog.setContentText(null);
        dialog.initModality(Modality.APPLICATION_MODAL);
        return dialog;
    }
    
    public Dialog showProgressDialog(String message, ReadOnlyDoubleProperty progressProp){
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("Update");
        dialog.setHeaderText(message);
        dialog.setContentText(null);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getDialogPane().setMaxWidth(800);
        ProgressBar progress = new ProgressBar();
        progress.setMaxWidth(dialog.getWidth()*0.5);
        progress.progressProperty().unbind();
        progress.progressProperty().bind(progressProp);
        dialog.getDialogPane().setContent(progress);
        return dialog;
    }

}
