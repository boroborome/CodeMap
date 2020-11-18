package com.happy3w.codemap.service;

import com.happy3w.codemap.model.CmWorkspace;
import com.happy3w.persistence.core.filter.impl.StringEqualFilter;
import com.happy3w.persistence.es.EsAssistant;
import com.happy3w.toolkits.message.MessageRecorderException;
import com.happy3w.toolkits.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CmWorkspaceService {
    @Autowired
    private EsAssistant esAssistant;

    public List<CmWorkspace> queryAllWorkspaces() {
        return esAssistant.queryStream(CmWorkspace.class, Collections.emptyList(), null)
                .collect(Collectors.toList());
    }

    public CmWorkspace createWorkspaces(CmWorkspace newWorkspace) {
        if (StringUtils.isEmpty(newWorkspace.getName())) {
            throw new MessageRecorderException("Name is required in Workspace.");
        }
        newWorkspace.setId(newWorkspace.getName());
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
        return newWorkspace;
    }
}
