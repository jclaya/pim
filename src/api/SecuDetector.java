/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_svm_rotation.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

/**
 *
 * @author jhance
 */

public class SecuDetector {
    public Mat detectCornertPoints(Mat image_scene,
                                   Mat image_object,
                                   MatOfKeyPoint scene_keypoints,
                                   MatOfKeyPoint object_keypoints,
                                   List<MatOfDMatch> list_matches) {

        LinkedList<DMatch> good_matches = new LinkedList<>();

        for (Iterator<MatOfDMatch> iterator = list_matches.iterator(); iterator.hasNext();) {
            MatOfDMatch matOfDMatch = (MatOfDMatch) iterator.next();
            if (matOfDMatch.toArray()[0].distance / matOfDMatch.toArray()[1].distance < 0.9) {
                good_matches.add(matOfDMatch.toArray()[0]);
            }
        }
        List<Point> pts1 = new ArrayList<Point>();
        List<Point> pts2 = new ArrayList<Point>();

        List<KeyPoint> list_scene_keypoints = scene_keypoints.toList();
        List<KeyPoint> list_object_keypoints = object_keypoints.toList();

        for(int i = 0; i<good_matches.size(); i++){
            pts1.add(list_scene_keypoints.get(good_matches.get(i).queryIdx).pt);
            pts2.add(list_object_keypoints.get(good_matches.get(i).trainIdx).pt);
        }

        Mat outputMask = new Mat();
        MatOfPoint2f pts1Mat = new MatOfPoint2f();
        pts1Mat.fromList(pts1);
        MatOfPoint2f pts2Mat = new MatOfPoint2f();
        pts2Mat.fromList(pts2);

        Mat Homog = Calib3d.findHomography(pts1Mat, pts2Mat, Calib3d.RANSAC, 15, outputMask, 2000, 0.995);

        LinkedList<DMatch> better_matches = new LinkedList<DMatch>();
        for (int i = 0; i < good_matches.size(); i++) {
            if (outputMask.get(i, 0)[0] != 0.0) {
                better_matches.add(good_matches.get(i));
            }
        }

        MatOfDMatch better_matches_mat = new MatOfDMatch();
        better_matches_mat.fromList(better_matches);

        List<KeyPoint> keypoints1_List = scene_keypoints.toList();
        List<KeyPoint> keypoints2_List = object_keypoints.toList();

        LinkedList<Point> objList = new LinkedList<>();
        LinkedList<Point> sceneList = new LinkedList<>();

        for(int i=0;i<better_matches.size();i++) {
            objList.addLast(keypoints2_List.get(better_matches.get(i).trainIdx).pt); //queryIdx
            sceneList.addLast(keypoints1_List.get(better_matches.get(i).queryIdx).pt); //trainIdx
        }


        MatOfPoint2f obj = new MatOfPoint2f();
        MatOfPoint2f scene = new MatOfPoint2f();
        obj.fromList(objList);
        scene.fromList(sceneList);

        MatOfByte drawnMatches = new MatOfByte();

        Mat outputImg = new Mat();

        Features2d.drawMatches(
                image_scene,
                scene_keypoints,
                image_object,
                object_keypoints,
                better_matches_mat,
                outputImg,
                Scalar.all(-1),
                Scalar.all(-1),
                drawnMatches,
                Features2d.NOT_DRAW_SINGLE_POINTS);

        out = outputImg;

        Mat H = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);
        Mat tmp_corners = new Mat(4,1, CvType.CV_32FC2);
        Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);

        tmp_corners.put(0, 0, new double[] {0,0});
        tmp_corners.put(1, 0, new double[] {image_object.cols(),0});
        tmp_corners.put(2, 0, new double[] {image_object.cols(),image_object.rows()});
        tmp_corners.put(3, 0, new double[] {0,image_object.rows()});

        Core.perspectiveTransform(tmp_corners, scene_corners, H);

        return scene_corners;

    }

    public Mat extractBanknote(Mat scene_corners,
                               Mat image_scene,
                               int width,
                               int height) {
        double[] p1 = scene_corners.get(0,0);
        double[] p2 = scene_corners.get(3,0);
        double[] p3 = scene_corners.get(2,0);
        double[] p4 = scene_corners.get(1,0);

        int resultWidth = width;
        int resultHeight = height;

        //Mat inputMat = new Mat(this.image_scene.height(), this.image_scene.width(), CvType.CV_8UC4);

        Mat inputMat = image_scene;

        //Mat out = new Mat(resultWidth, resultHeight, CvType.CV_8UC4);

        Point ocvPIn1 = new Point(scene_corners.get(0, 0)); //1
        Point ocvPIn2 = new Point(scene_corners.get(3, 0)); //2
        Point ocvPIn3 = new Point(scene_corners.get(2, 0)); //3
        Point ocvPIn4 = new Point(scene_corners.get(1, 0)); //0
        List<Point> source = new ArrayList<Point>();
        source.add(ocvPIn1);
        source.add(ocvPIn2);
        source.add(ocvPIn3);
        source.add(ocvPIn4);
        Mat startM = Converters.vector_Point2f_to_Mat(source);

        Point ocvPOut1 = new Point(0, 0);
        Point ocvPOut2 = new Point(0, resultHeight);
        Point ocvPOut3 = new Point(resultWidth, resultHeight);
        Point ocvPOut4 = new Point(resultWidth, 0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Mat out = new Mat();

        Imgproc.warpPerspective(inputMat,
                out,
                perspectiveTransform,
                new Size(resultWidth, resultHeight)
                ,Imgproc.INTER_CUBIC
        );

        return out;

    }

    public int detectDenomination(MatOfKeyPoint query_keypoints,
                                  List<MatOfKeyPoint> db_keypoints,
                                  MatOfDMatch db_matches ) {


        List<DMatch> list_db_matches = db_matches.toList();
        List<KeyPoint> list_key_point = query_keypoints.toList();

        LinkedList<Point> linked_list_point1 = new LinkedList<>();
        LinkedList<Point> linked_list_point2 = new LinkedList<>();

        LinkedList<Point> linked_list_trainpts1 = new LinkedList<>();
        LinkedList<Point> linked_list_trainpts2 = new LinkedList<>();

        List<KeyPoint> keypoints_1000 = db_keypoints.get(0).toList();
        List<KeyPoint> keypoints_500 = db_keypoints.get(1).toList();

        MatOfPoint2f trainpts1 = new MatOfPoint2f();
        MatOfPoint2f trainpts2 = new MatOfPoint2f();
        MatOfPoint2f points1 = new MatOfPoint2f();
        MatOfPoint2f points2 = new MatOfPoint2f();

        Mat mask1 = new Mat();
        Mat mask2 = new Mat();

        System.out.println("check 1");

        for (int j = 0; j < list_db_matches.size(); j++) {

            //System.out.println(list_db_matches.size() + " " + j);

            if (list_db_matches.get(j).imgIdx == 0) {
                linked_list_point1.addLast(list_key_point.get(list_db_matches.get(j).queryIdx).pt);
                linked_list_trainpts1.addLast(keypoints_1000.get(list_db_matches.get(j).trainIdx).pt);
            }else {
                linked_list_point2.addLast(list_key_point.get(list_db_matches.get(j).queryIdx).pt);
                linked_list_trainpts2.addLast(keypoints_500.get(list_db_matches.get(j).trainIdx).pt);
            }
        }
        System.out.println("check 2");

        points1.fromList(linked_list_point1);
        points2.fromList(linked_list_point2);
        trainpts1.fromList(linked_list_trainpts1);
        trainpts2.fromList(linked_list_trainpts2);
        System.out.println("check 3");

        Calib3d.findHomography(points1, trainpts1, Calib3d.RANSAC, 3, mask1, 2000, 0.995);
        Calib3d.findHomography(points2, trainpts2, Calib3d.RANSAC, 3, mask2, 2000, 0.995);

        Scalar sum1 = Core.sumElems(mask1);
        Scalar sum2 = Core.sumElems(mask2);

        System.out.println(sum1 + " " + sum2);

        int matched_indx = -1;
        if (sum1.val[0] > sum2.val[0]) {
            matched_indx = 1000;

        } else {
            matched_indx = 500;

        }
        System.out.println(matched_indx);

        return matched_indx;

    }

    Mat out;

    public Mat draw(Mat scene_corners) {
        Imgproc.line(out, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),4); //3
        Imgproc.line(out, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 0, 255),4); //4
        Imgproc.line(out, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(0,0)), new Scalar(255, 255, 0),4); //1
        Imgproc.line(out, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(3,0)), new Scalar(255, 0 , 0),4); //2

        return out;
    }


    public List<Point> getContourCorners(Mat image) {

        Mat img = new Mat();
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
        Imgproc.cvtColor(image, imHsv, Imgproc.COLOR_RGB2HSV);
        //rgb2hsv
        //getIntensityOnly
        List<Mat> channels = new ArrayList<>(3);
        int value = 2;

        Core.split(imHsv, channels);

        imInten =  channels.get(value);
        //getIntensityOnly
        mean = Core.mean(imInten);

        //binarize
        Imgproc.threshold(imInten, imBinarized, mean.val[0] + 30, 255, Imgproc.THRESH_BINARY);
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


        return points;
    }
}

