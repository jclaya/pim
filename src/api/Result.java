/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_svm_rotation.api;

import org.opencv.core.Mat;

/**
 *
 * @author jhance
 */
public class Result {

    Mat main;
    
    public Mat get(String name) {
        switch(name) {
            case "main":
                return main;
        }
        
        return null;
    }
    public void set(String name, Mat image) {
        switch(name) {
            case "main":
                 this.main = image;
        }
    }
    
}
