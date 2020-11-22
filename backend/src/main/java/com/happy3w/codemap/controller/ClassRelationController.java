package com.happy3w.codemap.controller;

import com.happy3w.codemap.model.ClassRelationFilter;
import com.happy3w.codemap.model.RelationResult;
import com.happy3w.codemap.service.ClassRelationService;
import com.happy3w.toolkits.message.MessageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("${code-map.service-path}/class-relation")
public class ClassRelationController {
    private final ClassRelationService classRelationService;

    public ClassRelationController(ClassRelationService classRelationService) {
        this.classRelationService = classRelationService;
    }

    @ResponseBody
    @PostMapping(headers = "cmd=new-workspace")
    public MessageResponse<RelationResult> createWorkspace(@RequestBody ClassRelationFilter filter) {
        RelationResult result = classRelationService.queryRelation(filter);
        return MessageResponse.fromData(result);
    }
}
