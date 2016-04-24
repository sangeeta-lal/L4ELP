
"""
#This file is used to create listing of all the files present in the directory and to write it to the file
"""
import os

#"""

#rootdir = "F:\\Research\\L4ELP\\dataset\\tomcat-8.0.9"
#rootdir = "F:\\Research\\L4ELP\\dataset\\cloudstack-4.3.0"
rootdir = "F:\\Research\\L4ELP\\dataset\\hd"
path = "F:\\Research\\L4ELP\\result\\"

"""
##Server 
rootdir = "E:\\Sangeeta\\Research\\L4ELP\\dataset\\tomcat-8.0.9"
#rootdir = "E:\\Sangeeta\\Research\\L4ELP\\dataset\\cloudstack-4.3.0"
rootdir = "E:\\Sangeeta\\Research\\L4ELP\\dataset\\hd"
path = "E:\\Sangeeta\\Research\\L4ELP\\result\\"

#"""

##
file_name = rootdir.rsplit("\\", 1)[1]
file_name= path+(str)(file_name)+"_java_files.txt"

java_file_count =0
new_file = open(file_name, 'w+')
for root, subFolders, files in os.walk(rootdir):
    for file in files:
        if(file.endswith('.java')):
            print "writing file name:", file
            new_file.write(root+"\\"+file)
            new_file.write("\n")
            java_file_count=java_file_count+1

new_file.close()
print "java file count=", java_file_count