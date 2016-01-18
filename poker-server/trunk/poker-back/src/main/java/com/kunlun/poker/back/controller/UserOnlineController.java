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
import com.kunlun.poker.back.domain.CsvUserOnlineStatisticsLog;
import com.kunlun.poker.back.service.CsvUserOnlineStatisticsService;

/****
 * 
 * @author ljx
 */
@Controller
public class UserOnlineController {
    
    @Autowired
    private CsvUserOnlineStatisticsService csvUserOnlineStatisticsService;
    
    @RequestMapping("/userOnline/list.htm")
    public ModelAndView listData(HttpServletRequest request, HttpServletResponse response){
        int from = Integer.valueOf(request.getParameter("from"));
        int pageSize = Integer.valueOf(request.getParameter("size"));
        int time = Integer.valueOf(request.getParameter("time"));
        try {
            if(csvUserOnlineStatisticsService.isReading()){
                Map<String, Object> model = new HashMap<>();
                model.put("currentLine", csvUserOnlineStatisticsService.getCurrentLine());
                model.put("totalLine", csvUserOnlineStatisticsService.getTotalLine());
                return JSONView.createModelAndView(model);
            }
            //TODO test
            time = 1411885451;
            Map<Integer, CsvUserOnlineStatisticsLog> dataMaps = csvUserOnlineStatisticsService.obtainLogDataInFile(time, pageSize, from);
            if(dataMaps == null){
                return null;
            }
            List<Map<String, Object>> dataMapList = new ArrayList<>();
            for(CsvUserOnlineStatisticsLog log : dataMaps.values()){
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("roomId", log.getId());
                dataMap.put("onlineCount", log.getUserIdSet().size());
                dataMap.put("bankroll", log.getTotalBankroll());
                dataMapList.add(dataMap);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("from", from);
            model.put("total", csvUserOnlineStatisticsService.getTotalSize());
            model.put("list", dataMapList);
            return JSONView.createModelAndView(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
