/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pre_process;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author jhance
 */
public class PreProcessMain {
    
    private String image_name = "whole_o_1000.jpg"; //TODO: this is the name of the image to read
    private String smooth_name = "smooth_image.jpg"; //TODO: this is the name of the output image in the ~/pim/src/output_images/output_image_name.jpg
    private String clahe_name = "clahe_image.jpg"; //TODO: this is the name of the output image in the ~/pim/src/output_images/output_image_name.jpg
    private String output_image_name = "output_image.jpg"; //TODO: this is the name of the output image in the ~/pim/src/output_images/output_image_name.jpg
    
    public void start(String pim_root_path) {
        Mat image = readImage(pim_root_path);
        
        //apply smooth only
        Mat smooth_image = smoothImage(image);
        
        saveImage(pim_root_path, smooth_name, smooth_image); 
        
        //apply clahe only
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);//clahe needs to be gray scale
        
        Mat clahe_image = new Mat();
        CLAHE c = Imgproc.createCLAHE();
        c.setClipLimit(4); //TODO: calibrate
        c.apply( image, clahe_image);
        
        //restore to rgb
        Imgproc.cvtColor(clahe_image, clahe_image, Imgproc.COLOR_GRAY2BGR);//cannot restore
        
        saveImage(pim_root_path, clahe_name, clahe_image); 
        
        //apply both
        Imgproc.cvtColor(smooth_image, smooth_image, Imgproc.COLOR_RGB2GRAY);//clahe needs to be gray scale
        c.setClipLimit(4); //TODO: calibrate
        c.apply(smooth_image, clahe_image);
        
        //restore to rgb
        Imgproc.cvtColor(clahe_image, clahe_image, Imgproc.COLOR_GRAY2BGR); //cannot restore
        
        saveImage(pim_root_path, output_image_name, clahe_image); 
        
    }
    
    private Mat readImage(String pim_root_path) {
        Mat image = Imgcodecs.imread(pim_root_path + "\\src\\images\\" + image_name);
        System.out.println("Read Image = " + pim_root_path + "\\src\\images\\" + image_name);
        System.out.println(image);
        return image;
    }
    
    private Mat smoothImage(Mat image) {
        Mat output_image = new Mat();
        
        Imgproc.bilateralFilter(image, output_image, 15, 80, 80, Core.BORDER_DEFAULT); //TODO: change this variables for better smooth of image
        
        System.out.println("Smoothened image - "+ output_image);
        return output_image;
    }
    
    private void saveImage(String pim_root_path, String output_name, Mat image) {
        Imgcodecs.imwrite(pim_root_path + "\\src\\output_images\\" + output_name, image);
        System.out.println("saved image at = " +pim_root_path + "\\src\\output_images\\" + output_name);
    }

}
