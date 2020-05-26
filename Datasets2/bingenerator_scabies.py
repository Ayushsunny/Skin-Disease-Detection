#!/usr/bin/python

import os, sys
from PIL import Image
import numpy as np

dir={}

label1=[1]	#Since we are making a bin file only for all
			#scabies images here and scabies belongs to label 1
label2=[2]	#For all other diseases

first=True
scabies=True
label=label1
#SCABIES

path_list = ["/home/pratik/Desktop/Skinzy Code SVM/Datasets2/train/Scabies/", "/home/pratik/Desktop/Skinzy Code SVM/Datasets2/train/Psoriasis/"]

for path in path_list:
	dir[path] = os.listdir(path)
	dir[path].sort()
	print dir[path]

	for image in dir[path]:
		print path + image
		im=Image.open(path + image)
		im=np.array(im)
		r = im[:,:,0].flatten()
		g = im[:,:,1].flatten()
		b = im[:,:,2].flatten()
		if not first:
			#out=np.append(out, list(label))
			out=np.append(out, list(r))
			out=np.append(out, list(g))
			out=np.append(out, list(b))
			print "Append, len =",len(out)
		else:
			out = np.array(list(r) + list(g) + list(b),np.uint8)
			first=False
			print "Array, len =",len(out)
		print out

	if scabies:
		scabies=False
		label=label2

out.tofile("scabies_train.bin")
# #!/usr/bin/python

# import os, sys
# from PIL import Image
# import numpy as np

# path = "/home/rohit/Skinzy/eczema/"
# eczema_dir = os.listdir(path)
# eczema_dir.sort()
# print (eczema_dir)

# label=[1]	#Since we are making a bin file only for all
# 			#ezema images here and eczema belongs to label 1

# first=True

# for image in eczema_dir:
# 	print(path + image)
# 	im=Image.open(path + image)
# 	im=np.array(im)
# 	r = im[:,:,0].flatten()
# 	g = im[:,:,1].flatten()
# 	b = im[:,:,2].flatten()
# 	if not first:
# 		out = np.append(out, list(label), list(r), list(g), list(b))
#       print "Append, len=",len(out)
# 	else:
# 	    out = np.array(list(label) + list(r) + list(g) + list(b),np.uint8)
#         print "Array, len=",len(out)
#         first=False
#     print out
