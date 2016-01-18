package com.kunlun.poker.recharge.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kunlun.poker.Config;
import com.kunlun.poker.recharge.service.PictureService;
import com.kunlun.poker.util.DataUtil;

/**
 * 
 * @author zern
 *
 */
@Controller
@RequestMapping("/fileUpLoad")
public class PictureController {
	@Autowired
	private PictureService pictureService;

	private File tempPathFile;

	@RequestMapping(method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		int uid = 0;
		String fileName = null , firstFold = null,secondFold = null, uploadPath = null;
		String tempPath = null;
		FileItem theFile = null;
		factory.setRepository(tempPathFile);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(4194304);
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> item = items.iterator();
		while (item.hasNext()) {
			FileItem fileItem = (FileItem) item.next();
			fileItem.getInputStream();
			if (!fileItem.isFormField()) {
				theFile = fileItem;
			} else if ("uid".equals(fileItem.getFieldName())) {
				uid = Integer.parseInt(fileItem.getString());
				firstFold = DataUtil.firstFolderAddress(uid);
				secondFold = DataUtil.secondFolderAddress(uid);
				uploadPath = setUploadPath(firstFold, secondFold);
				tempPath = setTempPath();
				init(uploadPath, tempPath);
			}
		}
		
		if(theFile != null)
		{
			String fileItemName = theFile.getName();
			if (fileItemName != null) {
				//String[] suffix = fileItemName.split("\\.");
				fileName = setFileName(uid, "jpg");
			}
		}
		if (fileName != null && theFile != null) {
			File fullFile = new File(fileName);
			File savedFile = new File(uploadPath, fullFile.getName());
			theFile.write(savedFile);
//			//81*81  缩放的方式
//			cpp.init(uploadPath, uploadPath, fileName, outputSmallFileName, 
//					RechargeProtocol.CompressWidth, RechargeProtocol.CompressHeigh);
//			
//			//原比例压缩
//			cpp.init(uploadPath, uploadPath, fileName, outputBigFileName, 
//					RechargeProtocol.CompressWidth, RechargeProtocol.CompressHeigh);
			
			
		}
	}


	@RequestMapping(method = RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.doPost(request, response);
	}

	/**
	 * 
	 * @param uploadPath
	 * @param tempPath
	 * @throws ServletException
	 */
	private void init(String uploadPath, String tempPath)
			throws ServletException {
		File uploadFile = new File(uploadPath);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		tempPathFile = new File(tempPath);
		if (!tempPathFile.exists()) {
			tempPathFile.mkdirs();
		}
	}

	/**
	 * @param id
	 *            生成文件名称
	 */
	private String setFileName(int id, String suffix) {
		if (id != 0) {
			return id +"."+ suffix;
		}
		return null;
	}

	private String setUploadPath(String firstFold, String secondFold) {
		return Config.getInstance().getProperty("uploadPath") + firstFold
				+ File.separator + secondFold;
	}

	private String setTempPath() {
		return Config.getInstance().getProperty("tempPath");
	}
}
