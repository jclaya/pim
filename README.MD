How to use opencv on netbeans
1.


 https://www.researchgate.net/publication/260392128_Multiview_banknote_recognition_with_component_and_shape_analysis

detection of a banknote in multiple banknote cluster

tools - c++, opencv 

method
a. preprocess 
-> noise reduction using bilateral filter - preserves the edges of the image blobs, which are very valuable structures in the detection of feature points. 
->  CLAHE  (Contrast  Limited Adaptive Histogram Equalization) - to increase contrast improves low light recognition

b. reference image in database
-> To  improve  the  detection  of  the  relevant  parts  of  the banknotes and to avoid the usage of sections that  are similar across several  banknotes, the keypoint  detection algorithm is only applied inside the masks associated with each banknote. Only relevant parts  such as the banknote  number and  unique textures or patterns are  included in the  banknotes masks. ref to fig.1

C. recognition(it can recognize any euro banknote)
-> To improve the robustness of the system, 3 levels of detail for each banknote are provided (256, 512 and 1024 pixels wide images).
-> matching  technique,  the  image resolution of both the reference and the target images should be the same.
-> feature detection 
-SIFT [2], SURF [24], GFTT [21], FAST [22], ORB [31], BRISK [32], STAR [33] and MSER [23].
-> keypoint extraction 
-SIFT [2],  SURF  [24],  FREAK  [34], BRIEF  [35],  ORB  [31]  and BRISK [32] feature descriptors. 
-> The initial matching can be performed using either a brute force or a heuristic approach. 
-> In the brute force approach, each descriptor in the image is compared with all descriptors in the reference image to find the best correspondence. In the heuristic approach, such as the FLANN library [36], several  optimizations  are  employed  to  speed  up  the computations.  These  optimizations  can  be  related  to  the appropriate selection of which keypoints to match, and  to the use of efficient data structures to speed up the search (such as k-d trees).  
-> ransac
-> ratio test
->(last step is shape analysis) this is a post process for detectimg the top most banknote in multi
ple banknote.