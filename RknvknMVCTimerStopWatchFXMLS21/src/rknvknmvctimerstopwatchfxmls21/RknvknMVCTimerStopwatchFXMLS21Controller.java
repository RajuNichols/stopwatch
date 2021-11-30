/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rknvknmvctimerstopwatchfxmls21;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 *
 * @author nicho
 */
public class RknvknMVCTimerStopwatchFXMLS21Controller implements Initializable, PropertyChangeListener {
    
    @FXML
    private ImageView handImageView;
    @FXML
    private Text digitalNumberText;
    @FXML
    private Text timerNumberText;
    @FXML
    private Text lapNumberText;
    @FXML
    private Text averageLapNumberText;
    @FXML
    private Button recordResetButton;
    @FXML
    private Button startStopButton;
    @FXML
    private LineChart<String, Number> recordLapLineChart;
    @FXML
    private NumberAxis yAxisRecordLap;
    @FXML
    private CategoryAxis xAxisRecordLap;
    @FXML
    private AreaChart<String, Number> averageLapAreaChart;
    @FXML
    private NumberAxis yAxisAverageLap;
    @FXML
    private CategoryAxis xAxisAverageLap;
    
    AnalogModel analogModel;
    DigitalModel digitalModel;
    
    private double graphLapInput;
    private double graphAverageInput;
    
    private int lineChartUpperBound=5;
    private int areaChartUpperBound=5;
    
    private XYChart.Series<String, Number> recordLapSeries;
    
    private XYChart.Series<String, Number> averageLapSeries;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        analogModel= new AnalogModel();
        digitalModel= new DigitalModel();
        
        analogModel.setupTimeline();
        digitalModel.setupTimelines();
        
        analogModel.addPropertyChangeListener((PropertyChangeListener) this);
        digitalModel.addPropertyChangeListener(this);
        
        Alert notNumbersAlert= new Alert(Alert.AlertType.ERROR);
        notNumbersAlert.setContentText("Input was not a number or the number was negative...");
        
        //Got the following code from Junlin Wang. Could not figure out this in
        //challenge #6 and asked for help when I submitted.
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Timer Start Time Set Up");
        dialog.setHeaderText("Set up the start time:");
        dialog.setContentText("Please set up the start time in seconds (Integer):");
        boolean setTime = false;
        
        while (!setTime){
            Optional<String> input = dialog.showAndWait();
            if (input.isPresent()){
                if((input.get().matches("[0-9]+")  && input.get().length() > 0 ) == false){
                    notNumbersAlert.showAndWait();
                }else{
                    digitalModel.setTempSecondTimer(Integer.valueOf(input.get()));
                    setTime = true;
                }
            }
        }
        // code ends here.
        
        timerNumberText.setText("Timer: " + digitalModel.getSecondTimer() + ":" + digitalModel.getHundrethSecondTimer());
        
        recordLapSeries= new XYChart.Series();
        yAxisRecordLap.setAutoRanging(false);
        yAxisRecordLap.setLowerBound(0);
        yAxisRecordLap.setUpperBound(lineChartUpperBound);
        recordLapLineChart.getData().add(recordLapSeries);
        recordLapLineChart.setAnimated(false);
        
        averageLapSeries= new XYChart.Series();
        yAxisAverageLap.setAutoRanging(false);
        yAxisAverageLap.setLowerBound(0);
        yAxisAverageLap.setUpperBound(areaChartUpperBound);
        averageLapAreaChart.getData().add(averageLapSeries);
        averageLapAreaChart.setAnimated(false);
        
    }    
    @FXML
    private void handleRecordReset(ActionEvent event) {
        if(analogModel.isRunning()){
            if(digitalModel.getSecondTimer()<= 0 && digitalModel.getHundrethSecondTimer()==0){
                    Alert error= new Alert(Alert.AlertType.ERROR);
                    error.setContentText("Time is up... No more records...");
                    error.show();
            }else{
                if(digitalModel.getHundrethSecond()!=0 && digitalModel.getSecond()!=0){
                    
                    
                    graphLapInput=digitalModel.getLapTime();
                    
                    graphAverageInput= digitalModel.getAverageLapTime();
                    
                    if(graphLapInput-1>lineChartUpperBound){
                        yAxisRecordLap.setUpperBound(graphLapInput+1);
                        lineChartUpperBound= (int)graphLapInput+1;
                    }
                    
                    if(graphAverageInput-1>areaChartUpperBound){
                        yAxisAverageLap.setUpperBound(graphAverageInput+1);
                        areaChartUpperBound= (int)graphAverageInput+1;
                    }
                    
                    digitalModel.setLapText();
                    
                    digitalModel.setAverageText();
            
                    recordLapSeries.getData().add(new XYChart.Data(Integer.toString(digitalModel.getCount()), graphLapInput));
                    
                    averageLapSeries.getData().add(new XYChart.Data(Integer.toString(digitalModel.getCount()), graphAverageInput));
                    
                    digitalModel.moveTemps();
                }
            }
        }else{
            analogModel.reset();
            digitalModel.reset();
            
            lineChartUpperBound=5;
            areaChartUpperBound=5;
            
            recordLapSeries.getData().clear();
            averageLapSeries.getData().clear();
            yAxisRecordLap.setUpperBound(lineChartUpperBound);
            yAxisAverageLap.setUpperBound(areaChartUpperBound);
               
            lapNumberText.setText("Lap: 00:00.00");
            averageLapNumberText.setText("Average Lap Time: 00:00.00");
            digitalNumberText.setText("00:00.00");
            timerNumberText.setText("Timer: " + digitalModel.getSecondTimer() + "." + digitalModel.getHundrethSecondTimer());
            handImageView.setRotate(0);
                
            recordResetButton.setText("Record");
                
        }
    }

    @FXML
    private void handleStartStop(ActionEvent event) {
        if(analogModel.isRunning()){
                analogModel.pause();
                digitalModel.pause();
                startStopButton.setText("Start");
                recordResetButton.setText("Reset");
            }else{
                analogModel.play();
                digitalModel.play();
                startStopButton.setText("Stop");
                recordResetButton.setText("Record");
            }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        
        if(evt.getPropertyName().equals("Analog")){
            handImageView.setRotate(Double.parseDouble(evt.getNewValue().toString()));
        }else{
            if(evt.getPropertyName().equals("Digital")){
                digitalNumberText.setText(evt.getNewValue().toString());
            }else{
                if(evt.getPropertyName().equals("Timer")){
                    timerNumberText.setText(evt.getNewValue().toString());
                }else{
                    if(evt.getPropertyName().equals("Lap")){
                        lapNumberText.setText(evt.getNewValue().toString());
                    }else{
                        if(evt.getPropertyName().equals("Average")){
                            averageLapNumberText.setText(evt.getNewValue().toString());
                        }
                    }
                }
            }
        }
    }
    
}
