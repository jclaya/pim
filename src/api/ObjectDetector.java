/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.util.ArrayList;
import java.util.List;

public class ObjectDetector {
    private FeatureDetector detector;
    private DescriptorExtractor extractor;
    private DescriptorMatcher matcher;


    public ObjectDetector() {
        detector = FeatureDetector.create(FeatureDetector.FAST);
        extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
    }

    //FEATURE DETECTOR EXTRACS KEYPOINTS - MatOfKeyPoint
    public MatOfKeyPoint detectFeatures(Mat image) {
        //Features2d.drawKeypoints(image_scene, scene_keypoints, detected_keypoints_scene);
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(image, keypoints);

        System.out.println("detectFeatures(Mat) - " + keypoints);

        return keypoints;
    }

    public List<MatOfKeyPoint> detectFeatures(List<Mat> images) {
        List<MatOfKeyPoint> keypoints = new ArrayList<>();
        detector.detect(images, keypoints);

        System.out.println("detectFeatures(List<Mat>) - " + keypoints);

        return keypoints;
    }
    //FEATURE DETECTOR EXTRACS KEYPOINTS - MatOfKeyPoint

    //DESCRIPTOR EXTRACTOR EXTRACS DESCRIPTORS - Mat
    public Mat extractDescriptors(Mat image, MatOfKeyPoint keypoints) {
        Mat descriptors = new Mat();
        extractor.compute(image, keypoints, descriptors);

        System.out.println("extractDescriptors(Mat) - " + descriptors);

        return descriptors;
    }

    public List<Mat> extractDescriptors(List<Mat> images, List<MatOfKeyPoint> keypoints) {
        List<Mat> descriptors = new ArrayList<>();
        extractor.compute(images, keypoints, descriptors);

        System.out.println("extractDescriptors(List<Mat>) - " + descriptors);

        return descriptors;
    }
    //DESCRIPTOR EXTRACTOR EXTRACS DESCRIPTORS - Mat

    //DESCIPTOR MATCHER EXTTRACS MATCHES - MatOfDMatch
    public void addDescriptors(List<Mat> descriptors) {
        matcher.add(descriptors);
    }

    public MatOfDMatch match(Mat descriptors) {
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors, matches);

        System.out.println("match(descriptors) - " + matches);

        return matches;
    }

    public MatOfDMatch match(Mat query_descriptors, Mat ref_descriptors) {
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(query_descriptors, ref_descriptors, matches);

        System.out.println("match(descriptors) - " + matches);

        return matches;
    }

    public List<MatOfDMatch> knnMatch(Mat query_descriptors, Mat ref_descriptors) {
        List<MatOfDMatch> matches = new ArrayList<>();
        matcher.knnMatch(query_descriptors, ref_descriptors, matches, 2);

        System.out.println("knnMatch(descriptors) - " + matches);

        return matches;
    }
}
