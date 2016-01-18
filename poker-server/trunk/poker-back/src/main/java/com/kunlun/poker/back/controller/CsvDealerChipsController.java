package com.kunlun.poker.back.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.kunlun.poker.back.domain.CsvDealerChipsLog;
import com.kunlun.poker.back.service.CsvDealerChipsService;

public class CsvDealerChipsController {
	@Autowired
	private CsvDealerChipsService csvDealerChipsService;
	
	@RequestMapping("/chip/csvDealerChipsByTime.htm")
	public ModelAndView csvDrawChips(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	int from = Integer.parseInt(request.getParameter("from"));
    	int size = Integer.parseInt(request.getParameter("size"));
    	int startTime = Integer.parseInt(request.getParameter("startTime"));
    	int endTime = Integer.parseInt(request.getParameter("endTime"));
    	System.out.println(startTime);
    	System.out.println(endTime);
    	
    	Map<Integer, CsvDealerChipsLog> sourceMap = csvDealerChipsService.obtainLogDataInFile(startTime, endTime, size, from);
    	System.out.println(sourceMap);
    	CsvDealerChipsLog crcl = new CsvDealerChipsLog();
    	List<Map<Object, Object>> crclList = new ArrayList<Map<Object, Object>>();
    	for(Map.Entry<Integer, CsvDealerChipsLog> entry : sourceMap.entrySet()) {
    		Map<Object, Object> crclMap = new HashMap<Object, Object>();
    		crcl = entry.getValue();  
    		crclMap.put("id", crcl.getId());
    		crclMap.put("dealerChips", crcl.getDealerChips());
    		crclMap.put("createTime", crcl.getCreateTime());
    		crclList.add(crclMap);
        }  
    	Map<String, Object> model = new HashMap<>();
    	model.put("from", from);
    	model.put("total", csvDealerChipsService.getTotalSize());
    	model.put("list", crclList);
		return JSONView.createModelAndView(model);
	}
}
