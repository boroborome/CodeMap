package com.happy3w.codemap.service;

import com.happy3w.codemap.component.WorkspaceAnalyzer;
import com.happy3w.codemap.model.BackendTask;
import com.happy3w.codemap.model.CmWorkspace;
import com.happy3w.persistence.core.filter.impl.StringEqualFilter;
import com.happy3w.persistence.es.EsAssistant;
import com.happy3w.toolkits.message.MessageRecorderException;
import com.happy3w.toolkits.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CmWorkspaceService {
    private final EsAssistant esAssistant;
    private final BackendTaskService backendTaskService;
    private final WorkspaceAnalyzer workspaceAnalyzer;

    public CmWorkspaceService(EsAssistant esAssistant, BackendTaskService backendTaskService, WorkspaceAnalyzer workspaceAnalyzer) {
        this.esAssistant = esAssistant;
        this.backendTaskService = backendTaskService;
        this.workspaceAnalyzer = workspaceAnalyzer;
    }

    public List<CmWorkspace> queryAllWorkspaces() {
        return esAssistant.queryStream(CmWorkspace.class, Collections.emptyList(), null)
                .collect(Collectors.toList());
    }

    public CmWorkspace createWorkspaces(CmWorkspace newWorkspace) {
        if (StringUtils.isEmpty(newWorkspace.getName())) {
            throw new MessageRecorderException("Name is required in Workspace.");
        }
        newWorkspace.setId(newWorkspace.getName());
        newWorkspace.setDirty(true);
        boolean nameExisted = esAssistant.queryStream(
                CmWorkspace.class,
                Arrays.asList(new StringEqualFilter("name", newWorkspace.getName())),
                null)
                .findFirst()
                .isPresent();
        if (nameExisted) {
            throw new MessageRecorderException(
                    MessageFormat.format("Name:{0} is existed.", newWorkspace.getName())
            );
        }
        esAssistant.saveData(newWorkspace);
        startBackendTask(newWorkspace);
        return newWorkspace;
    }

    private void startBackendTask(CmWorkspace workspace) {
        BackendTask task = new BackendTask();
        task.setRemark("Analyze workspace:" + workspace.getName());
        backendTaskService.createTask(task, t -> workspaceAnalyzer.analyze(workspace, task));
    }

    public CmWorkspace querySingle(String name) {
        Optional<CmWorkspace> resultOp = esAssistant.queryStream(CmWorkspace.class,
                Arrays.asList(new StringEqualFilter("name", name)), null)
                .findFirst();
        if (resultOp.isPresent()) {
            return resultOp.get();
        } else {
            return new CmWorkspace();
        }
    }
}
