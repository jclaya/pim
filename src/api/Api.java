/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_svm_rotation.api;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author jhance
 */
public class Api implements Processs{

    private Result result;
    private SecuDetector secu_detector;
    private ObjectDetector object_detector;

    
    public Api() {
        secu_detector = new SecuDetector();
        object_detector = new ObjectDetector();
        result = new Result();
    }

    @Override
    public void processImage(Mat query_image) {
        Mat reference_image = Imgcodecs.imread("C:\\Users\\jhance\\Documents\\NetBeansProjects\\FinalBanknote\\src\\test_svm_rotation\\images\\ref_image_1.jpg");
        
        MatOfKeyPoint query_features = object_detector.detectFeatures(query_image);
        MatOfKeyPoint ref_features = object_detector.detectFeatures(reference_image);
        
        Mat query_desc = object_detector.extractDescriptors(query_image, query_features);        
        Mat ref_desc = object_detector.extractDescriptors(reference_image, ref_features);
        
        List<MatOfDMatch> list_matches = object_detector.knnMatch(query_desc, ref_desc);
        
        Mat points = secu_detector.detectCornertPoints(query_image, reference_image, query_features, ref_features, list_matches);
        Mat new_image = secu_detector.draw(points);
        
        result.set("main", new_image);
    }

    @Override
    public Result getResult() {
        return result;
    }
}
