import numpy as np
import cv2
import sys


def color_quant(input,K,output):
    img = cv2.imread(input)
    Z = img.reshape((-1,3))
    # convert to np.float32
    Z = np.float32(Z)
    # define criteria, number of clusters(K) and apply kmeans()
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 15, 1.0)

    ret,label,center=cv2.kmeans(Z,K,None,criteria,10,cv2.KMEANS_RANDOM_CENTERS)

    # Now convert back into uint8, and make original image
    center = np.uint8(center)
    res = center[label.flatten()]
    res2 = res.reshape((img.shape))

    cv2.imshow('res2',res2)
    cv2.waitKey(0)
    cv2.imwrite(output, res2)
    cv2.destroyAllWindows()

	
def main():
	if(len(sys.argv)==4):
		color_quant(str(sys.argv[1]), int(sys.argv[2]), str(sys.argv[3]))
	else:
		print ("usage: color_quantization.py <imagefile> <number of clusters> <outputfile>")
		
print(sys.argv)


if __name__ == "__main__":
	main()