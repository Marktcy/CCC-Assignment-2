from collections import Counter
import numpy
import sys
from shapely.geometry import shape, Point
import json

t=[]
with open('/home/lesleyice/cloud/filteredtwitts.txt') as f:
    for line in f:
        newline=line.rstrip(",\n")
        currentline=newline.split(",")
        devicename =currentline[1]
        t.append(devicename)

result={}
result =dict((i, t.count(i)) for i in t)
print result

with open('devicecount.json','w') as outfile:
    json.dump(result,outfile)