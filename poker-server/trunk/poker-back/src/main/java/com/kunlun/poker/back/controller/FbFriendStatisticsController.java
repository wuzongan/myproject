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
import com.kunlun.poker.back.domain.CsvUserFbFriendStatisticsLog;
import com.kunlun.poker.back.service.CsvUserFbFriendService;


/****
 * 
 * @author ljx
 */
@Controller
public class FbFriendStatisticsController {

    @Autowired
    private CsvUserFbFriendService csvUserFbFriendService;
    
    @RequestMapping("/fbFriend/list.htm")
    public ModelAndView listData(HttpServletRequest request,
            HttpServletResponse respose) {
        int from = Integer.valueOf(request.getParameter("from"));
        int pageSize = Integer.valueOf(request.getParameter("size"));
        int startTime = Integer.valueOf(request.getParameter("startTime"));
        int endTime = Integer.valueOf(request.getParameter("endTime"));
        try {
            startTime = 1411712425;
            endTime = 1411812424;
            if(csvUserFbFriendService.isReading()){
                Map<String, Object> model = new HashMap<>();
                model.put("currentLine", csvUserFbFriendService.getCurrentLine());
                model.put("totalLine", csvUserFbFriendService.getTotalLine());
                return JSONView.createModelAndView(model);
            }
            long start = System.currentTimeMillis();
            Map<Integer, CsvUserFbFriendStatisticsLog> dataMaps = csvUserFbFriendService.obtainLogDataInFile(startTime, endTime, pageSize, from);
            long end = System.currentTimeMillis();
            System.out.println("耗时：" + (end - start) + "毫秒");
            if(dataMaps == null){
                return null;
            }
            List<Map<String, Object>> dataMapList = new ArrayList<>();
            for(CsvUserFbFriendStatisticsLog log : dataMaps.values()){
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("userId", log.getId());
                dataMap.put("inviteFriendCount", log.getInviteFriendCount());
                dataMap.put("shareFbCount", log.getShareFbCount());
                dataMapList.add(dataMap);
            }
            Map<String, Object> model = new HashMap<>();
            model.put("from", from);
            model.put("total", csvUserFbFriendService.getTotalSize());
            model.put("list", dataMapList);
            return JSONView.createModelAndView(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
