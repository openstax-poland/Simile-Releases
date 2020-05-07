from utils.image_similar import HashSimilar
import numpy as np
import sys, cv2

# Function allows to calculate differences between images:
#   1. ResultImage - image that was created during Comaprison Process in Simile.
#   2. ReferenceImage - image that is the template of created ResultImage.
# Function returns similarity value of provided images, e.g. 0 means those images are exactly the same.
def calculateDifference(pathResultImage, pathReferenceImage):
    resultImage = cv2.imread(pathResultImage)
    #resultImage = cv2.cvtColor(resultImage, cv2.COLOR_BGR2GRAY)
    resultImage_height = resultImage.shape[0]
    resultImage_width = resultImage.shape[1]

    refeferenceImage = cv2.imread(pathReferenceImage)
    refeferenceImage = cv2.cvtColor(refeferenceImage, cv2.COLOR_BGR2GRAY)
    refeferenceImage_height = refeferenceImage.shape[0]
    refeferenceImage_width = refeferenceImage.shape[1]

    cnt = 0
    for y in range(0, resultImage_height):
        for x in range(0, resultImage_width):
            r = np.array([100, 100, 225])
            if resultImage[y, x][2] == r[2] and resultImage[y, x][1] == r[1] and resultImage[y, x][0] == r[0]:
                cnt += 1
    return cnt

# It is necessary to print function's result, because Simile uses result from the console in its calculations.
print(calculateDifference(sys.argv[1], sys.argv[2]))
