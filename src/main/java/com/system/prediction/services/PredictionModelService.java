package com.system.prediction.services;

import com.system.pojo.ProcessedRecord;
import com.system.settings.AppSettings;
import org.joda.time.DateTime;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PredictionModelService {

    private static final String DEFAULT_ATTRIBUTE = "first-last";
    private final ArrayList<Attribute> atts;
    private Instances trainingSet;

    public PredictionModelService() {
        this.atts = createAttributes();
    }

    public void createTrainTestData(List<ProcessedRecord> processedRecords) {
        this.trainingSet = createInstances(atts, processedRecords);
        this.trainingSet.setClassIndex(0);
    }

    public Instances initialComputation() throws Exception {
        trainingSet = filterString(trainingSet);
        LinearRegression model = new LinearRegression();
        model.buildClassifier(trainingSet);

        Instance testInstance = trainingSet.instance(0);
        testInstance.setValue(0, 0);
        double predictedDemand = model.classifyInstance(testInstance);
//        System.out.println("Predicted demand: " + predictedDemand);

        Instances newSet = new Instances("", atts, AppSettings.endDay - AppSettings.startDay + 1);
        Enumeration<Instance> enumerateInstances = trainingSet.enumerateInstances();
        enumerateInstances.nextElement();
        while(enumerateInstances.hasMoreElements()) {
            newSet.add(enumerateInstances.nextElement());
        }
        newSet.add(testInstance);
        return newSet;
    }

    public Instances compute(Instances newInstances) throws Exception {
        newInstances.setClassIndex(0);
        trainingSet = filterString(newInstances);
        LinearRegression model = new LinearRegression();
        model.buildClassifier(trainingSet);

        Instance testInstance = trainingSet.instance(0);
        testInstance.setValue(0, 0);
        double predictedDemand = model.classifyInstance(testInstance);
//        System.out.println("Predicted demand: " + predictedDemand);

        Instances newSet = new Instances("", atts, AppSettings.endDay - AppSettings.startDay + 1);
        Enumeration<Instance> enumerateInstances = trainingSet.enumerateInstances();
        enumerateInstances.nextElement();
        while(enumerateInstances.hasMoreElements()) {
            newSet.add(enumerateInstances.nextElement());
        }
        newSet.add(testInstance);
        return newSet;
    }

    public void predict(DateTime dateTime) throws Exception {
        LinearRegression model = new LinearRegression();
        model.buildClassifier(trainingSet);

        Instance testInstance = trainingSet.instance(0);
        testInstance.setValue(0, 0);
        double predictedDemand = model.classifyInstance(testInstance);
        System.out.println("Predicted demand: " + predictedDemand + " for date: " + dateTime.toString());
    }

    private static Instances filterString(Instances input) throws Exception {
        StringToNominal stn = new StringToNominal();
        stn.setAttributeRange(DEFAULT_ATTRIBUTE);
        stn.setInputFormat(input);
        return Filter.useFilter(input, stn);
    }

    private Instances createInstances(ArrayList<Attribute> atts, List<ProcessedRecord> recordList) {
        Instances instances = new Instances("MyRelation", atts, 0);

        for (ProcessedRecord processedRecord : recordList) {
            double[] vals = new double[instances.numAttributes()];
            vals[0] = processedRecord.getDemand();
            vals[1] = processedRecord.getStationId();
            vals[2] = instances.attribute(2).addStringValue(processedRecord.getStationName());
            vals[3] = instances.attribute(3).addStringValue(processedRecord.getStationLatitude());
            vals[4] = instances.attribute(4).addStringValue(processedRecord.getStationLongitude());
            vals[5] = processedRecord.getTemperature() == null ? 0.0 : processedRecord.getTemperature();
            vals[6] = processedRecord.getWindspeed() == null ? 0.0 : processedRecord.getWindspeed();
            vals[7] = processedRecord.getPressure() == null ? 0.0 : processedRecord.getPressure();
            instances.add(new DenseInstance(1.0, vals));
        }

        return instances;
    }

    private ArrayList<Attribute> createAttributes() {
        ArrayList<Attribute> atts = new ArrayList<>();
        atts.add(new Attribute("demand"));
        atts.add(new Attribute("stationId"));
        atts.add(new Attribute("stationName", (ArrayList<String>) null));
        atts.add(new Attribute("stationLatitude", (ArrayList<String>) null));
        atts.add(new Attribute("stationLongitude", (ArrayList<String>) null));
        atts.add(new Attribute("temperature"));
        atts.add(new Attribute("windspeed"));
        atts.add(new Attribute("pressure"));
        return atts;
    }

}
