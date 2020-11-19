package com.happy3w.codemap.controller;

import com.happy3w.codemap.model.BackendTask;
import com.happy3w.codemap.service.BackendTaskService;
import com.happy3w.toolkits.message.MessageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("${code-map.service-path}/backend-task")
public class BackendTaskController {
    private final BackendTaskService backendTaskService;

    public BackendTaskController(BackendTaskService backendTaskService) {
        this.backendTaskService = backendTaskService;
    }

    @ResponseBody
    @PostMapping(headers = "cmd=query-all")
    public MessageResponse<List<BackendTask>> queryWorkspaces() {
        List<BackendTask> allTasks = backendTaskService.queryAllTask();
        return MessageResponse.fromData(allTasks);
    }

}
