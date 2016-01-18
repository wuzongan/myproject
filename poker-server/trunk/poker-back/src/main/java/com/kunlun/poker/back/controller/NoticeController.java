package com.kunlun.poker.back.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kunlun.poker.back.service.NoticeService;
import com.kunlun.poker.domain.Notice;


@Controller
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping("/notice/list.htm")
    public ModelAndView listNotice(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	int from = Integer.parseInt(request.getParameter("from"));
    	int size = Integer.parseInt(request.getParameter("size"));
    	
    	List<Notice> notices = noticeService.listNotice(from, size);
    	
    	List<Map<String, Object>> noticeMaps = new ArrayList<>();
    	for(Notice notice : notices)
    	{
    		Map<String, Object> noticeMap = new HashMap<String, Object>();
    		noticeMap.put("id", notice.getId());
    		noticeMap.put("title", notice.getTitle());
    		noticeMap.put("content", notice.getContent());
    		noticeMap.put("createTime", notice.getCreateTime());
    		noticeMaps.add(noticeMap);
    	}
    	
    	
    	Map<String, Object> model = new HashMap<>();
    	model.put("from", from);
    	model.put("total", noticeService.countNotice());
    	model.put("list", noticeMaps);
    	
		return JSONView.createModelAndView(model);
	}

	@RequestMapping("/notice/update.htm")
	public ModelAndView updateNotice(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Notice notice = new Notice();
		notice.setId(Integer.parseInt(request.getParameter("id")));
		notice.setTitle(request.getParameter("title"));
		notice.setContent(request.getParameter("content"));
		notice.setCreateTime(System.currentTimeMillis() / 1000);
		
		noticeService.updateNotice(notice);

		return null;
	}

	@RequestMapping("/notice/add.htm")
	public ModelAndView addNotice(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Notice notice = new Notice();
		notice.setTitle(request.getParameter("title"));
		notice.setContent(request.getParameter("content"));
		notice.setCreateTime(System.currentTimeMillis() / 1000);
		
		noticeService.addNotice(notice);

		return null;
	}

	@RequestMapping("/notice/remove.htm")
	public ModelAndView deleteNotice(HttpServletRequest request, HttpServletResponse response) throws Exception{
		noticeService.removeNotice(Integer.parseInt(request.getParameter("id")));
		
		return null;
	}
}
