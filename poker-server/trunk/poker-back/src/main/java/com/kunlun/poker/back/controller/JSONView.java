package com.kunlun.poker.back.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class JSONView implements View {
	private final static JSONView instance = new JSONView();
	
	public static ModelAndView createModelAndView(Map<String, ?> model)
	{
		return new ModelAndView(instance, model);
	}

	private JSONView() {
	}
	
	@Override
	public String getContentType() {
		return "text/json";
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json"); 

		System.out.println(JSONObject.toJSONString(model));
		response.getOutputStream().write(JSONObject.toJSONString(model).getBytes("UTF-8"));
	}

}
