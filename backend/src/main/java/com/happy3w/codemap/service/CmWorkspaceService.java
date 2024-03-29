package com.happy3w.codemap.service;

import com.happy3w.codemap.component.WorkspaceAnalyzer;
import com.happy3w.codemap.model.BackendTask;
import com.happy3w.codemap.model.CmWorkspace;
import com.happy3w.java.ext.StringUtils;
import com.happy3w.persistence.core.filter.impl.StringEqualFilter;
import com.happy3w.persistence.es.EsAssistant;
import com.happy3w.toolkits.message.MessageRecorderException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        return esAssistant.findByFilter(CmWorkspace.class, Collections.emptyList(), null)
                .collect(Collectors.toList());
    }

    public CmWorkspace createWorkspaces(CmWorkspace newWorkspace) {
        if (StringUtils.isEmpty(newWorkspace.getName())) {
            throw new MessageRecorderException("Name is required in Workspace.");
        }
        newWorkspace.setDirty(true);
        boolean nameExisted = esAssistant.findByFilter(
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

        newWorkspace = esAssistant.saveData(newWorkspace);
        startBackendTask(newWorkspace);
        return newWorkspace;
    }

    private void startBackendTask(CmWorkspace workspace) {
        BackendTask task = new BackendTask();
        task.setRefId(workspace.getId());
        task.setRefName(workspace.getName());
        task.setRemark("Analyze workspace:" + workspace.getName());
        backendTaskService.createTask(task, t -> workspaceAnalyzer.analyze(workspace, task));
    }

    public CmWorkspace querySingle(String id) {
        return esAssistant.findById(CmWorkspace.class, id);
    }

    public CmWorkspace deleteSingle(String id) {
        CmWorkspace workspace = esAssistant.findById(CmWorkspace.class, id);
        if (workspace == null) {
            return new CmWorkspace();
        } else {
            esAssistant.deleteById(CmWorkspace.class, id);
            return workspace;
        }
    }

    public CmWorkspace updateWorkspace(CmWorkspace workspace) {
        esAssistant.saveData(workspace);
        esAssistant.flush(CmWorkspace.class);
        CmWorkspace updatedWorkspace = esAssistant.findById(CmWorkspace.class, workspace.getId());

        if (!updatedWorkspace.getFileToAnalyze().isEmpty()) {
            startBackendTask(updatedWorkspace);
        }
        return updatedWorkspace;
    }
}
