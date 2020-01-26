package com.system.prediction.services;

import com.system.pojo.ProcessedRecord;
import com.system.prediction.BikeSharingClassifier;
import com.system.settings.AppSettings;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PredictionModelService {

    private static final String DEFAULT_ATTRIBUTE = "first-last";
    private final ArrayList<Attribute> atts;
    private Instances trainingSet;
    private Instances testSet;
    private final Attribute predictedAttribute;
    private Classifier bestClassifier;

    public PredictionModelService() {
        this.atts = createAttributes();
        this.predictedAttribute = atts.get(1);
    }

    public void createTrainTestData(List<ProcessedRecord> processedRecords) {
        Instances data = createInstances(atts, processedRecords);
        this.trainingSet = data.trainCV(AppSettings.testDataRatio + AppSettings.trainDataRatio, AppSettings.trainDataRatio);
        this.testSet = data.testCV(AppSettings.testDataRatio + AppSettings.trainDataRatio, AppSettings.trainDataRatio);
    }

    public void train() throws Exception {
        Pair<Classifier, Double> knnResult = trainKNN();
        Pair<Classifier, Double> logisticResult = trainLogistic();

        if (knnResult.getRight() > logisticResult.getRight()) {
            bestClassifier = knnResult.getLeft();
        } else {
            bestClassifier = logisticResult.getLeft();
        }
    }

    public void saveClassifier() throws IOException {
        BikeSharingClassifier.dumpClassifier(AppSettings.classifierPath, bestClassifier, trainingSet);
    }

    private Pair<Classifier, Double> trainLogistic() throws Exception {
        Instances logisticTrainData = filterString(this.trainingSet);
        logisticTrainData = filterNumeric(logisticTrainData);

        Instances logisticTestData = filterString(this.testSet);
        logisticTestData = filterNumeric(logisticTestData);

        logisticTrainData.setClassIndex(testSet.numAttributes() - 1);
        logisticTestData.setClassIndex(testSet.numAttributes() - 1);

        logisticTrainData.setClass(predictedAttribute);
        logisticTestData.setClass(predictedAttribute);

        Classifier logistic = new Logistic();
        logistic.buildClassifier(logisticTrainData);
        Evaluation eval = new Evaluation(logisticTestData);
        eval.evaluateModel(logistic, logisticTestData);
        printClassifierResults(eval);
        return new MutablePair<>(logistic, eval.correct());
    }

    private Pair<Classifier, Double> trainKNN() throws Exception {
        Instances knnTrainData = filterString(this.trainingSet);
        Instances knnTestData = filterString(this.testSet);

        knnTrainData.setClassIndex(testSet.numAttributes() - 1);
        knnTestData.setClassIndex(testSet.numAttributes() - 1);

        knnTrainData.setClass(predictedAttribute);
        knnTestData.setClass(predictedAttribute);

        Classifier knn = new IBk(AppSettings.bestK);
        knn.buildClassifier(knnTrainData);
        Evaluation eval = new Evaluation(knnTestData);
        eval.evaluateModel(knn, knnTestData);
        printClassifierResults(eval);
        return new MutablePair<>(knn, eval.correct());
    }

    private static Instances filterNumeric(Instances input) throws Exception {
        NumericToNominal ntn = new NumericToNominal();
        ntn.setInputFormat(input);
        return Filter.useFilter(input, ntn);
    }

    private static Instances filterString(Instances input) throws Exception {
        StringToNominal stn = new StringToNominal();
        stn.setAttributeRange(DEFAULT_ATTRIBUTE);
        stn.setInputFormat(input);
        return Filter.useFilter(input, stn);
    }

    private void printClassifierResults(Evaluation eval) throws Exception {
        String strSummary = eval.toSummaryString();
        System.out.println(strSummary);

        String cMatrix = eval.toMatrixString();
        System.out.println(cMatrix);
    }

    private Instances createInstances(ArrayList<Attribute> atts, List<ProcessedRecord> recordList) {
        Instances instances = new Instances("MyRelation", atts, 0);

        for (ProcessedRecord processedRecord : recordList) {
            double[] vals = new double[instances.numAttributes()];
            vals[0] = 0;
            vals[1] = processedRecord.getActionType() == 0 ? instances.attribute(1).addStringValue("departure")
                    : instances.attribute(1).addStringValue("arrival");
            vals[2] = instances.attribute(2).addStringValue(processedRecord.getActionTime());
            vals[3] = processedRecord.getStationId();
            vals[4] = instances.attribute(4).addStringValue(processedRecord.getStationName());
            vals[5] = instances.attribute(5).addStringValue(processedRecord.getStationLatitude());
            vals[6] = instances.attribute(6).addStringValue(processedRecord.getStationLongitude());
            vals[7] = processedRecord.getBikeId();
            vals[8] = processedRecord.getCurrentBikeCount();
            vals[9] = processedRecord.getInitialBikeCount();
            vals[10] = instances.attribute(10).addStringValue(processedRecord.getUserType());
            vals[11] = processedRecord.getBirthYear();
            vals[12] = processedRecord.getGender();
            vals[13] = processedRecord.getTemperature() == null ? 0.0 : processedRecord.getTemperature();
            vals[14] = processedRecord.getWindspeed() == null ? 0.0 : processedRecord.getWindspeed();
            vals[15] = processedRecord.getPressure() == null ? 0.0 : processedRecord.getPressure();
            instances.add(new DenseInstance(1.0, vals));
        }

        return instances;
    }

    private ArrayList<Attribute> createAttributes() {
        ArrayList<Attribute> atts = new ArrayList<>();
        atts.add(new Attribute("id"));
        atts.add(new Attribute("actionType", (ArrayList<String>) null));
        atts.add(new Attribute("actionTime", (ArrayList<String>) null));
        atts.add(new Attribute("stationId"));
        atts.add(new Attribute("stationName", (ArrayList<String>) null));
        atts.add(new Attribute("stationLatitude", (ArrayList<String>) null));
        atts.add(new Attribute("stationLongitude", (ArrayList<String>) null));
        atts.add(new Attribute("bikeId"));
        atts.add(new Attribute("currentBikeCount"));
        atts.add(new Attribute("initialBikeCount"));
        atts.add(new Attribute("userType", (ArrayList<String>) null));
        atts.add(new Attribute("birthYear"));
        atts.add(new Attribute("gender"));
        atts.add(new Attribute("temperature"));
        atts.add(new Attribute("windspeed"));
        atts.add(new Attribute("pressure"));
        return atts;
    }

    public void predict(List<ProcessedRecord> processedRecords) throws Exception {
        BikeSharingClassifier classifier = BikeSharingClassifier.loadClassifier(AppSettings.classifierPath);
        bestClassifier = classifier.getClassifier();
        trainingSet = classifier.getDataSet();
        testSet = createInstances(atts, processedRecords);

        testSet = filterString(testSet);
        if (bestClassifier instanceof Logistic) {
            testSet = filterNumeric(testSet);
        }

        testSet.setClassIndex(testSet.numAttributes() - 1);
        testSet.setClass(predictedAttribute);

        Evaluation eval = new Evaluation(testSet);
        eval.evaluateModel(bestClassifier, testSet);
        printClassifierResults(eval);
    }

}
