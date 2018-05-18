package com.asdbll.twitter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import static com.asdbll.twitter.KafkaProducer.term;

import javax.swing.*;
import java.awt.*;

public class DrawGraph {

    DefaultPieDataset dataset = new DefaultPieDataset();
    JPanel panel = new JPanel();
    JFreeChart chart = ChartFactory.createPieChart3D("Twitter Sentimental Analysis For: " + term, dataset, true, true, false);
    ChartFrame frame = new ChartFrame("Twitter Sentimental Analysis", chart);
    PiePlot plot = (PiePlot) chart.getPlot();


    PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0}: ({1}) {2}");


    public void Dataset(double positive, double negative, double neutral){

        dataset.setValue("Positive", positive);
        dataset.setValue("Negative", negative);
        dataset.setValue("Neutral", neutral);
    }

    public void refreshChart() {
        plot.setLabelGenerator(gen);
        plot.setStartAngle(290);
        plot.setForegroundAlpha(0.50f);
        plot.setDirection(Rotation.CLOCKWISE);
        frame.pack();
        frame.setVisible(true);
        panel.removeAll();
        panel.revalidate(); // This removes the old chart
        chart = ChartFactory.createPieChart3D("Twitter Sentimental Analysis For: " + term, dataset, true, true, false);
        chart.removeLegend();
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel);
        panel.repaint(); // This method makes the new chart appear
    }
}