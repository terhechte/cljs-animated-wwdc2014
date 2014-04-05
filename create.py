#!/usr/bin/env python
import png #requires pypng library
import json
import os
import sys


folder = None
try:
    folder = sys.argv[1]
except:
    print "Please define a folder in which to search the frames"
    print "Usage: ./create.py bird > ./project/assets/javascripts/bird.json"
    sys.exit()

if not os.path.exists(folder):
    print "Folder doesn't exist", folder
    sys.exit()

def getFrames():
    l = os.listdir(folder + "/")
    p = []
    for x in l:
        if x.split(".")[-1].lower() == "png":
            p.append(folder + "/" + x)
    return p

def frameRep(fn):
    image = png.Reader(filename = fn)
    data = image.asFloat()
    #print data[2]
    outframes = []
    for y in data[2]:
        for x in y:
            outframes.append(float("%0.1f" % (x,)))
    return outframes

fx = []
for fr in getFrames():
    fu = frameRep(fr)
    fx.append(fu)

print json.dumps(fx)
