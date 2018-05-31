package api;

import org.opencv.core.Core;
import org.opencv.core.CvType;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by jpbalboa on 19 Jan 2018.
 */

//TODO: Remove getSecurityFeatures and todo parameters
public class ImageManipulator {

    public static void toCannyEdge(Mat image, int threshold) {
        Imgproc.Canny(image, image, threshold, threshold * 3, 3, false);
    }

    public static Mat sharpen(Mat img) { //TODO: add parameters here

        Mat sharp = img;

       /*
            // sharpen image using "unsharp mask" algorithm
            Mat blurred;
            double sigma = 1, threshold = 5, amount = 1;
            GaussianBlur(img, blurred, Size(), sigma, sigma);
            Mat lowContrastMask = abs(img - blurred) < threshold;
            Mat sharpened = img*(1+amount) + blurred*(-amount);
            img.copyTo(sharpened, lowContrastMask);
        */

        Mat blurred = new Mat();
        double sigma = 3;
        double threshold = 3;
        double amount = 1;

        // GaussianBlur(img, blurred, Size(), sigma, sigma);
        Imgproc.GaussianBlur(img, blurred, new Size(), sigma, sigma);

        // abs(img - blurred)
        Mat absdiff1 = new Mat();
        Core.absdiff(img, blurred, absdiff1);
        // absdiff1 < threshold
        Mat compare1 = new Mat();
        Core.compare(absdiff1, new Scalar(threshold), compare1, Core.CMP_GT);
        // Mat lowContrastMask = abs(img - blurred) < threshold
        Mat lowContrastMask = compare1;

        // img * (1 + amount)
        Mat multiply1 = new Mat();
        Core.multiply(img, new Scalar(1 + amount), multiply1);
        // blurred * (-amount)
        Mat multiply2 = new Mat();
        Core.multiply(blurred, new Scalar(-amount), multiply2);
        // multiply1 + multiply2
        Mat add1 = new Mat();
        Core.add(multiply1, multiply2, add1);
        // Mat sharpened = img * (1 + amount) + blurred * (-amount);
        Mat sharpened = add1;

        // img.copyTo(sharpened, lowContrastMask);
        img.copyTo(sharpened, lowContrastMask);

        //Imgproc.GaussianBlur(image, sharp, new Size(0, 0), 3);
        //Core.addWeighted(image, 1.5 , sharp, -0.5, 0, sharp);
        return sharpened;

    }

    public static void toBlur(Mat image) {
        Imgproc.blur(image, image, new Size(3, 3));
    }

    public static Mat crop(Mat image_original, Point p1, Point p4) {
        return image_original.submat(new Rect(p1, p4));
    }

    public static void rescale(Mat image, float cols_scale, float rows_scale) {
        Imgproc.resize( image,
                        image,
                        new Size( image.cols() * cols_scale, image.rows() * rows_scale),
                        0,
                        0,
                        Imgproc.INTER_LANCZOS4);
     }
    public static void resize(Mat image, Size new_size) {
        Imgproc.resize( image,
                        image,
                        new_size,
                        0,
                        0,
                        Imgproc.INTER_LANCZOS4);
    }

    public static void toGrey(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
    }

    public static void filter(Mat image) {  //TODO: add parameters here
        Mat kernel = new Mat(3,3,CvType.CV_8SC1);

        kernel.put(0,0,0);
        kernel.put(0,1,1);
        kernel.put(0,2,2);

        kernel.put(1,0,-1);
        kernel.put(1,1,0);
        kernel.put(1,2,1);

        kernel.put(2,0,-2);
        kernel.put(2,1,-1);
        kernel.put(2,2,0);

        Imgproc.filter2D(image, image, image.depth(), kernel);
    }


    public static void line(Mat original_image, Point point, Point point1) {
        Imgproc.line(original_image, point, point1, new Scalar(255, 0 , 0));
    }

    public static Mat rgb2hsv(Mat image) {
        Mat m = new Mat();
        Imgproc.cvtColor(image, m, Imgproc.COLOR_RGB2HSV);
        return m;
    }
}
