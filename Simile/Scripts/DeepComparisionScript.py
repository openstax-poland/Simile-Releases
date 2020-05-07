from utils.image_diff import ImageDiff
import sys

img = ImageDiff()
img.increment_diff(sys.argv[1], sys.argv[2], sys.argv[3])
