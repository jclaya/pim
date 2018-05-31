/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import org.opencv.core.Mat;


public interface Processs {
    public void processImage(Mat image, String path);
    public Result getResult();
}
