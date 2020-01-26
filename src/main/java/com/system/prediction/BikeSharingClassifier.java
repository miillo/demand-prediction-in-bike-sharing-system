package com.system.prediction;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.*;

public class BikeSharingClassifier {

    private Classifier classifier;
    private Instances dataSet;

    public BikeSharingClassifier(Classifier classifier, Instances dataSet) {
        this.classifier = classifier;
        this.dataSet = dataSet;
    }

    public static void dumpClassifier(String path, Classifier classifier, Instances dataSet) throws IOException {
        OutputStream out = new FileOutputStream(path);
        try {
            SerializationHelper.writeAll(out, new Object[]{classifier, dataSet});
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static BikeSharingClassifier loadClassifier(String path) throws Exception {
        InputStream in = new FileInputStream(path);
        Object[] objs = SerializationHelper.readAll(in);
        return new BikeSharingClassifier((Classifier) objs[0], (Instances) objs[1]);
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public Instances getDataSet() {
        return dataSet;
    }
}
