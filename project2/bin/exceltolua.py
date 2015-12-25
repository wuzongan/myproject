
# -*- coding: UTF-8 -*- 

# Author:wza

import xlrd



config_name_list = ["buykubi"]

# 让py可以读取文件中的中文
import sys
reload(sys)
sys.setdefaultencoding("utf-8")

# 可以在这里写一些固定的注释代码之类的
writeData = "-- @author:wza\n\n\n"

workbook = xlrd.open_workbook('charge.xlsm')
print "There are {} sheets in the workbook".format(workbook.nsheets)

for booksheet in workbook.sheets():
	print "Current Booksheet:[" + booksheet.name + "]"

	if booksheet.name not in config_name_list:
		continue
	fileOutput = open(booksheet.name+'.lua','w')
	writeData = writeData + 'local ' + booksheet.name + ' = {\n\t'
	for row in xrange(booksheet.nrows):
		# if not any(booksheet.cell(0,col).value) :
		# 	continue
		for col in xrange(booksheet.ncols):
			value = booksheet.cell(row, col).value
			# print value
			if row == 0:
				if col == 0:
					continue
			else :
				if col == 0:
					writeData = writeData + '[' + str(int(value)) + ']={'
				else :
					writeData = writeData + "['" + booksheet.cell(0, col).value + "']='" \
					+ str(booksheet.cell(row, col).value) + "',"
		else :
			if row != 0:
				writeData = writeData + '} ,\n\t'
	else :
		writeData = writeData + '}\n\n return ' + booksheet.name
if fileOutput :
	fileOutput.write(writeData)
	fileOutput.close()