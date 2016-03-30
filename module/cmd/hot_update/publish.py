#!/usr/bin/python
# -*- coding: latin-1 -*-
import os,os.path
import time
import shutil
import re
import zipfile
import sys
import string
import hashlib
#reload(sys)
#sys.setdefaultencoding('utf-8')


#路径最后斜杠！！！！
project_path =  os.path.join(os.path.expanduser("~"), 'Documents') + "/myproject/qishituan_client/cb_lua_v001/"

output = ""
platform = ""
backup = ""
platform_dir = ""

versionListName = "versionList"

curtime = time.strftime('%Y-%m-%d %H:%M',time.localtime(time.time()))
	
	
def clear_dir(dirname) :
	if not os.path.exists(dirname):
		return
	for file in os.listdir(dirname):
		filepath = dirname + "/" + file
		if re.match('^\.', file):
			continue
		if os.path.isdir(filepath):
			clear_dir(filepath) 
			os.rmdir(filepath)
		elif os.path.isfile(filepath):
			os.remove(filepath)
				

def get_next_version(curentVersion,type):
	big ,mid ,small = curentVersion.split(".")
	if type == 1 :
		big = int(big) + 1
		mid = 0
		small = 0
	elif type == 2 :
		mid = int(mid) + 1
		small = 0
	elif type == 3 :
		small = int(small) +1
		
	return str(big) + "." + str(mid) + "." + str(small)
	
	
def copyDirWithClean(src,des):
	if os.path.exists(des):
		clear_dir(des)
	else:
		os.system("mkdir " + des )
	os.system("cp " + src + "/* " + des)
	
	
def copy_files(src, dst):

    for item in os.listdir(src):
        path = os.path.join(src, item)
        if item.startswith('.') :
                continue
        if os.path.isfile(path) :
            shutil.copy(path, dst)
        if os.path.isdir(path) :
            new_dst = os.path.join(dst, item)
            if not os.path.exists(new_dst):
                os.mkdir(new_dst)
            copy_files(path, new_dst)	


def gen_apk(plantform,version):
	#配置工程
	appdir = os.getcwd() + "/android/" + plantform +"/"
	print 'gen_apk========',appdir
	clear_dir(appdir)
	#config_plantform(plantform,appdir,version)
	#gen apk 文件
	curdir =  os.getcwd()
	os.chdir(appdir)
	print "buiding apk ..."
	#os.system(appdir + "buildapk >nul")
	os.chdir(curdir)
	if os.path.exists(appdir + "bin/Dzpk-release.apk"):
		print "building apk success!!!!"
		return True,appdir + "bin/Dzpk-release.apk"
	else:
		print "building apk failed------------------"
		return False,None
	#clear_dir(appdir)
def filemd5(filename) :
	md5 = hashlib.md5()
	fd = open(filename,"rb")
	md5hex = hashlib.md5(fd.read())
	fd.close()
	return md5hex.hexdigest()
	
def getMd5FromXmlFile(rootNode,name) :
	for node in rootNode.iter("file"):
		if node.get("name", False) == name :
			return node.get("md5")
	return ""
	
def gen_filelist(dir_list,version,noconfirm):
    print "正在生成版本差异文件..."
    from xml.etree.ElementTree import ElementTree,Element
    book =  ElementTree()
    filelist = []
    rootNode = Element("filelist",{"version":version,"time":curtime})
    rootNode.tail  = "\r\n"
    book._setroot(rootNode)
    preRootNode = None
    isExist = os.path.exists("filelist.xml")
    if isExist:
        preRootNode = ElementTree().parse("filelist.xml") 
    book._setroot(rootNode)
    #代码资源
    # dir_list = ['res','scripts']
    for dirname in dir_list:
        dirname = project_path+dirname
		# compare_file(project_path+dirname, rootNode, preRootNode, filelist)
        if  os.path.isdir(dirname):
            for root, dirs, files in os.walk(dirname):
                for name in files:
                    namekey = os.path.relpath(os.path.join(root, name),project_path)
                    if name[0] != "." and namekey[0] != "." :
                        md5now = filemd5(os.path.join(root, name))
                        md5pre = getMd5FromXmlFile(preRootNode,namekey)
                        if preRootNode != None and md5now != md5pre:
                            if not noconfirm:
                                    yesorno = input("确定更新文件--->   " + namekey + "(y/n):")
                            if noconfirm or yesorno.lower() == "y" :
                                filelist.append(namekey)
                                element = Element("file",{"name":namekey,"md5":md5now})
                                element.tail  = "\r"
                            elif md5pre != "" :
                                element = Element("file",{"name":namekey,"md5":md5now})
                                element.tail  = "\r"
                            rootNode.append(element)
    else :
        print "新资源路径错误"

    book.write("filelist.xml","UTF-8")
    shutil.copy("filelist.xml",backoutput)
    return 	filelist

		
def zip_file(path, filelist,zipfilename,other_file):  
    zf = zipfile.ZipFile(path + zipfilename, "w", zipfile.ZIP_DEFLATED)
    zf.write(path + other_file, other_file)
    for file in filelist:
        if file != other_file:
            #zf.write(project_path + "assets" +"\\" + file,file)
            zf.write(path + file,file)
    zf.close()
def back_up(file):
	if not os.path.exists(backup + "filelist-" + str.replace(str.replace(curtime," ","-"),":","-") +".xml"):
		shutil.copy(file ,backup)
		os.rename(backup +"filelist.xml" , backup + "filelist-" + str.replace(str.replace(curtime," ","-"),":","-") +".xml")
def create_version_file() :
	print "创建版本文件.."
	version_file = os.getcwd() + "/config.lua"
	version_list = os.getcwd() + "/" + versionListName
	str_version_list = ""
	if os.path.exists(version_list):
		fp = open(version_list,"rb")
		str_version_list = fp.read()
		print "版本列表=" + str_version_list
	if os.path.exists(version_file):
		os.remove(version_file)
	file = open(version_file,"w")
	file.write("local data = { \n")
	file.write("	version = \"" + version + "\",\n")
	file.write("	version_list = {")
	if str_version_list != "":
		s_list = str_version_list.split(',')
		for i in range(len(s_list)):
			file.write("\""+ s_list[i] + "\"")
			if i != len(s_list) - 1:
				file.write(",")
	file.write("}\n")
	# file.write("	package = \"http://192.168.1.141/update/"+ version +"/update.zip\",\n")
	file.write("}\n")
	file.write("return data")
	file.close()
def create_update_file(path,filelist) :
	print "创建更新文件..",path
	# if not os.path.exists(path + "res"):
	# 	os.mkdir(path + "res")
	update_file = path + "resinfo.lua"
	if os.path.exists(update_file):
		os.remove(update_file)
	file = open(update_file,"w")
	file.write("local data = { \n")
	file.write("	version = \"" + version + "\",\n")
	file.write("	lib = {},\n")
	file.write("	oth = { \n")
	for m_file in filelist:
			dirs = m_file.split('\/')
			tempath = ""
			for i in range(len(dirs)):
					tempath += dirs[i]
					if i != len(dirs) - 1: 
						tempath += "/"
			file.write("		\""+ tempath + "\",\n")
	file.write("	},\n}\n")
	file.write("return data")
	file.close()
	return "resinfo.lua"
def remove_file(output) :
	if os.path.exists(output + "res"):	
		shutil.rmtree(output + "res")
	if os.path.exists(output + "scripts"):	
		shutil.rmtree(output + "scripts")
def create_version_list(version,type) :
	print "创建版本列表文件.."
	version_list = os.getcwd() + "/" + versionListName
	if os.path.exists(version_list):
		shutil.copy(version_list, backup)
		os.rename(backup + versionListName , backup + versionListName + "-" + str.replace(str.replace(curtime," ","-"),":","-"))
	m_str = ""
	if type != 1:
		file = open(version_list,"ab+")
		m_str = ","
	else:
		file = open(version_list,"wb+")		
	file.write(m_str+version)
	file.close()
if __name__ == '__main__':
	version = os.sys.argv[1]
	# platform = os.sys.argv[2]
	print "当前版本号为：",version
	# print "当前平台为：",platform
	print project_path
	# platform_dir = os.getcwd() + "/" + platform
	# output = platform_dir + "/publish/"
	# backup = platform_dir + "/backup/"
	platform_dir = os.getcwd()
	output = platform_dir + "/publish/"
	backup = platform_dir + "/backup/"
	
	if not os.path.exists(platform_dir):	
		os.mkdir(platform_dir)
	if not os.path.exists(output):	
		os.mkdir(output)
	if not os.path.exists(backup):	
		os.mkdir(backup)
	noconfirm = True
	if "-a" in os.sys.argv :
		noconfirm = True
	if os.path.exists("filelist.xml"):
		from xml.etree.ElementTree import ElementTree,Element
		version1 =  ElementTree().parse("filelist.xml").get("version")
		if version.split('.')[0] !=  version1.split('.')[0] :
			type = 1
		else :
			type = 3
	else :
		os.system("cp file_bak/filelist.xml .")
		type = 1
	print "====output====",globals()['output']
	backoutput =  output + version + "/"
	if not os.path.exists(backoutput):
		os.mkdir(backoutput)
	clear_dir(backoutput)
	if type == 1 :
		noconfirm = True
	create_version_list(version, type)
	create_version_file()
	filelist = gen_filelist(['res','scripts'],version,noconfirm)
	other_file = create_update_file(backoutput,filelist)
	if type != 1 :
		for file in filelist:
			print "----------",file, "----------\n"
			dirs = file.split('/')
			tempath = backoutput
			for i in range(len(dirs)-1):
				tempath += dirs[i] + "/"
				if not os.path.exists(tempath):
						os.mkdir(tempath)
			shutil.copy(project_path + file,backoutput + file)
			if file.find(".lua") != -1 :
				os.system("luajit -b " + backoutput + file + " " + backoutput + file)
		if len(filelist) >0 :
			print "更新文件数量 ："	,len(filelist)
			print "version:",version
			zip_file(backoutput, filelist, "update.zip", other_file)
			back_up(backoutput + "filelist.xml")
		else :
			print "没有文件更新"
			shutil.copy(backoutput + "filelist.xml" ,os.getcwd())
			clear_dir(backoutput)
			os.rmdir(backoutput)
			
	# else :
		# for plantform in plantforms:
		# 	isSuccess,filepath = gen_apk(plantform,version)
		# 	if isSuccess :
		# 		desname = os.path.dirname(filepath) + "/dzpk-" + plantform + "-" + version + "-release.apk"
		# 		os.rename(filepath, desname) 
		# 		shutil.copy(desname,backoutput)
		# 		back_up(backoutput + "filelist.xml")
			#else:
            #                    print("平台",plantform,"打包错误-------")
        #remove_file(backoutput)
