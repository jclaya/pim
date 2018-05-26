
import org.opencv.core.Core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jhance
 */


public class Main {
    public static void main(String[] args) {
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        String method = "pre_process"; //TODO: change this to switch method
        String pim_root_path = "C:\\Users\\jhance\\Documents\\NetBeansProjects\\pim\\"; //TODO: put path of the pim here
        
        switch(method) {
            case "pre_process":
                new pre_process.PreProcessMain().start(pim_root_path);
                break;
            case "features_matching_with_masking":
                new features_matching_with_masking.FeaturesMatchingMaskingMain().start(pim_root_path);
                break;
        }
        
    }
}
