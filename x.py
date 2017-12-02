
import numpy as np
import cv2
   # custom built function to compute edges 
def custom(image, sigma=0.33):
	# compute the median of the single channel pixel intensities
	v = np.median(image)
 
	# apply automatic Canny edge detection using the computed median
	lower = int(max(0, (1.0 - sigma) * v))
	upper = int(min(255, (1.0 + sigma) * v))
	edged = cv2.Canny(image, lower, upper)
 
	# return the edged image
	return edged
 
kernel = np.ones((1,1), np.uint8)
imagePath = "crack.jpg"

# load the image, convert it to grayscale, and blur it slightly
image = cv2.imread(imagePath)
image2 = cv2.imread(imagePath)
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
blurred = cv2.GaussianBlur(gray, (3, 3), 0)
 

custom = custom(blurred)
imge = cv2.erode(custom, kernel, iterations=1)

ret,thresh = cv2.threshold(imge,127,255,0)
(_,cnts, _) = cv2.findContours(thresh.copy(), cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
cnts = sorted(cnts, key = cv2.contourArea, reverse = True)[:10]


cv2.drawContours(image, cnts, -1, (0, 255, 0), 1)

cv2.imshow("Original", image2)
cv2.imshow("Cracks", image)
cv2.waitKey(0)