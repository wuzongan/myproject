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

import com.kunlun.poker.back.domain.CsvUserStatisticsLog;
import com.kunlun.poker.back.service.CsvUserStatisticsService;

/****
 * 
 * @author ljx
 */
@Controller
public class UserStatisticsController {

    @Autowired
    private CsvUserStatisticsService csvUserStatisticsService;

    @RequestMapping("/userStatistics/list.htm")
    public ModelAndView listData(HttpServletRequest request,
            HttpServletResponse respose) {
        int from = Integer.valueOf(request.getParameter("from"));
        int pageSize = Integer.valueOf(request.getParameter("size"));
        int startTime = Integer.valueOf(request.getParameter("startTime"));
        int endTime = Integer.valueOf(request.getParameter("endTime"));
        try {
            //TODO test
            startTime = 1411896080;
            endTime = 1411975920;
            if(csvUserStatisticsService.isReading()){
                Map<String, Object> model = new HashMap<>();
                model.put("currentLine", csvUserStatisticsService.getCurrentLine());
                model.put("totalLine", csvUserStatisticsService.getTotalLine());
                return JSONView.createModelAndView(model);
            }
            Map<Integer, CsvUserStatisticsLog> dataMaps = csvUserStatisticsService.obtainLogDataInFile(startTime, endTime, pageSize, from);
            if(dataMaps == null){
                Map<String, Object> model = new HashMap<>();
                model.put("from", from);
                model.put("total", 0);
                model.put("list", null);
                return JSONView.createModelAndView(model);
            }
            List<Map<String, Object>> dataMapList = new ArrayList<>();
            for(CsvUserStatisticsLog log : dataMaps.values()){
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("roomId", log.getId());
                dataMap.put("userCount", log.getUserIdSet().size());
                dataMap.put("userFrequencyCount", log.getUserFrequencyCount());
                dataMap.put("handChooseUserCount", log.getHandChooseUserIdSet().size());
                dataMap.put("quickStartUserCount", log.getQuickStartUserIdSet().size());
                dataMap.put("handChooseCount", log.getHandChooseCount());
                dataMap.put("quickStartCount", log.getQuickStartCount());
                dataMapList.add(dataMap);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("from", from);
            model.put("total", csvUserStatisticsService.getTotalSize());
            model.put("list", dataMapList);
            return JSONView.createModelAndView(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
