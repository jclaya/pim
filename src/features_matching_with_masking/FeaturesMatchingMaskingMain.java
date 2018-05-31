/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package features_matching_with_masking;

import api.Api;
import api.ImageManipulator;
import api.Result;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author jhance
 */
public class FeaturesMatchingMaskingMain {
    /*
    String path = "C:\\Users\\jhance\\Documents\\NetBeansProjects\\pim\\src\\images\\whole_1k.jpg";

    public void start(String pim_root_path) {
        Api api = new Api();
        
        Mat query_image = Imgcodecs.imread(pim_root_path + "\\src\\images\\whole_o_1000.jpg");
        System.out.println("query_image loaded" + pim_root_path +  "\\src\\images\\scene_1.jpg    " + query_image);
        
        ImageManipulator.rescale(query_image, (float)0.25, (float)0.25);
        
        api.processImage(query_image, path);
        
        Result result = api.getResult();
        
        Imgcodecs.imwrite("test.jpg", result.get("main"));
        
        
    }
    */ //uncomment for manual masking
    
    String path = "C:\\Users\\jhance\\Documents\\NetBeansProjects\\pim\\src\\images\\whole_1k.jpg";

    public void start(String pim_root_path) {
        Api api = new Api();
        
        Mat query_image = Imgcodecs.imread(pim_root_path + "\\src\\images\\whole_o_1000.jpg");
        System.out.println("query_image loaded" + pim_root_path +  "\\src\\images\\scene_1.jpg    " + query_image);
        
        ImageManipulator.rescale(query_image, (float)0.25, (float)0.25);
        
        api.processImage(query_image, path);
        
        Result result = api.getResult();
        
        Imgcodecs.imwrite("test.jpg", result.get("main"));
        
        
    }
}
