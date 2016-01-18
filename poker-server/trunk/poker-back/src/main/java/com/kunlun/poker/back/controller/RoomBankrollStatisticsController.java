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

import com.kunlun.poker.back.domain.CsvRoomBankrollStatisticsLog;
import com.kunlun.poker.back.service.CsvRoomBankrollService;

/****
 * 
 * @author ljx
 */
@Controller
public class RoomBankrollStatisticsController {

    @Autowired
    private CsvRoomBankrollService csvRoomBankrollService;
    
    @RequestMapping("/roomBankroll/list.htm")
    public ModelAndView listData(HttpServletRequest request,
            HttpServletResponse respose) {
        int from = Integer.valueOf(request.getParameter("from"));
        int pageSize = Integer.valueOf(request.getParameter("size"));
        int startTime = Integer.valueOf(request.getParameter("startTime"));
        int endTime = Integer.valueOf(request.getParameter("endTime"));
        try {
            startTime = 1411712425;
            endTime = 1411726777;
            if(csvRoomBankrollService.isReading()){
                Map<String, Object> model = new HashMap<>();
                model.put("currentLine", csvRoomBankrollService.getCurrentLine());
                model.put("totalLine", csvRoomBankrollService.getTotalLine());
                return JSONView.createModelAndView(model);
            }
            Map<Integer, CsvRoomBankrollStatisticsLog> dataMaps = csvRoomBankrollService.obtainLogDataInFile(startTime, endTime, pageSize, from);
            if(dataMaps == null){
                return null;
            }
            List<Map<String, Object>> dataMapList = new ArrayList<>();
            for(CsvRoomBankrollStatisticsLog log : dataMaps.values()){
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("roomId", log.getId());
                dataMap.put("totalBankroll", log.getTotalBankroll());
                dataMap.put("serviceBankroll", log.getServiceBankroll());
                dataMapList.add(dataMap);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("from", from);
            model.put("total", csvRoomBankrollService.getTotalSize());
            model.put("list", dataMapList);
            return JSONView.createModelAndView(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
