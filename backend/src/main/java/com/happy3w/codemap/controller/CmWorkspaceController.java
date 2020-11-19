package com.happy3w.codemap.controller;

import com.happy3w.codemap.model.CmWorkspace;
import com.happy3w.codemap.service.CmWorkspaceService;
import com.happy3w.toolkits.message.MessageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("${code-map.service-path}/workspace")
public class CmWorkspaceController {
    private final CmWorkspaceService cmWorkspaceService;

    public CmWorkspaceController(CmWorkspaceService cmWorkspaceService) {
        this.cmWorkspaceService = cmWorkspaceService;
    }

    @ResponseBody
    @PostMapping(headers = "cmd=query-all")
    public MessageResponse<List<CmWorkspace>> queryWorkspaces() {
        List<CmWorkspace> allWorkspaces = cmWorkspaceService.queryAllWorkspaces();
        return MessageResponse.fromData(allWorkspaces);
    }


    @ResponseBody
    @PostMapping(headers = "cmd=new-workspace")
    public MessageResponse<CmWorkspace> createWorkspace(@RequestBody CmWorkspace newWorkspace) {
        CmWorkspace workspace = cmWorkspaceService.createWorkspaces(newWorkspace);
        return MessageResponse.fromData(workspace);
    }

    @ResponseBody
    @PostMapping(value = "/{id}", headers = "cmd=query-single")
    public MessageResponse<CmWorkspace> querySingle(@PathVariable(name = "id") String id) {
        CmWorkspace workspace = cmWorkspaceService.querySingle(id);
        return MessageResponse.fromData(workspace);
    }
}
