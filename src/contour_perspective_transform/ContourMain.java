/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contour_perspective_transform;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author 
 */    // translation of this https://pastebin.com/2zLk75ze

public class ContourMain {

    public void start(String pim_root_path) {
        Mat img = Imgcodecs.imread(pim_root_path + "src\\images\\scene_1.jpg");
        /*
        //double r = 500 / img.cols();
        //Size s = new Size(500, img.rows() * r);
        //System.out.println(s);
        //Size dim = new Size(500, int(img.rows() * r));
                
       // Imgproc.resize(img, img, s, 0, 0, Imgproc.INTER_AREA);
 
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(11,11),0);
        
        Mat edge = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        
        Imgproc.Canny(gray, edge, 30, 75);
        Imgproc.findContours(edge, contours, new Mat(), 1, 1);
        
        Imgproc.drawContours( img, contours, -1, new Scalar(0,255,0), 2);
        
        Imgcodecs.imwrite("edge.jpg", edge);
        Imgcodecs.imwrite("contour.jpg", img);
        */
        
        /*
        Mat imHsv = new Mat();
        Mat imInten = new Mat();
        Mat imBinarized = new Mat();
        Mat imContour = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Scalar mean = new Scalar(0);

        double largestContourArea = 0;
        int largestContourIndex = 0;
        Rect largestContourBox = new Rect();

        //rgb2hsv;
        Imgproc.cvtColor(img, imHsv, Imgproc.COLOR_RGB2HSV);
        //rgb2hsv
        //getIntensityOnly
        
        Imgcodecs.imwrite("hsv.jpg", imHsv);
        
        List<Mat> channels = new ArrayList<>(3);
        int value = 2;

        Core.split(imHsv, channels);

        imInten =  channels.get(value);
        //getIntensityOnly
        mean = Core.mean(imInten);

        //binarize
        Imgproc.threshold(imInten, imBinarized, mean.val[0] + 5, 255, Imgproc.THRESH_BINARY);
        
        Imgcodecs.imwrite("imBinarized.jpg", imBinarized);
        
        //binarize
        imContour = imBinarized;




        Imgproc.findContours(imContour, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        for(int i = 0; i < contours.size(); i++) {
            double contourArea = Imgproc.contourArea(contours.get(i), false);

            if (contourArea > largestContourArea) {
                largestContourArea = contourArea;
                largestContourBox = Imgproc.boundingRect(contours.get(i));
            }
        }


        List<Point> points = new ArrayList<>();
        points.add(largestContourBox.tl());
        points.add(largestContourBox.br());
        
        Imgproc.rectangle(img, points.get(0), points.get(1), new Scalar(255, 0, 0), 3);
        
        
        Imgcodecs.imwrite("draw.jpg", img);
        
        */
        
        //https://github.com/opencv/opencv/blob/master/samples/cpp/squares.cpp
        Mat imHsv = new Mat();
        Mat imInten = new Mat();
        Mat imBinarized = new Mat();
        Mat imContour = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Scalar mean = new Scalar(0);

        double largestContourArea = 0;
        int largestContourIndex = 0;
        Rect largestContourBox = new Rect();

        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(11,11),0);
        
        Mat edge = new Mat();
        
        Imgproc.Canny(gray, edge, 30, 80);
        
        Imgcodecs.imwrite("edge.jpg", edge);
        
        //binarize




        Imgproc.findContours( edge, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        
        Imgproc.drawContours( img, contours, -1, new Scalar(0,255,0), 2);
        
        Imgcodecs.imwrite("cont.jpg", img);

        for(int i = 0; i < contours.size(); i++) {
            double contourArea = Imgproc.contourArea(contours.get(i), false);

            if (contourArea > largestContourArea) {
                largestContourArea = contourArea;
                largestContourBox = Imgproc.boundingRect(contours.get(i));
            }
        }


        List<Point> points = new ArrayList<>();
        points.add(largestContourBox.tl());
        points.add(largestContourBox.br());
        
        Imgproc.rectangle(img, points.get(0), points.get(1), new Scalar(255, 0, 0), 3);
        
        
        Imgcodecs.imwrite("draw.jpg", img);
        
        
    }

    
}
