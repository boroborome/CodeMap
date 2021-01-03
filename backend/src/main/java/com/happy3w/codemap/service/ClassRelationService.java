package com.happy3w.codemap.service;

import com.happy3w.codemap.model.ClassInfo;
import com.happy3w.codemap.model.ClassRelation;
import com.happy3w.codemap.model.ClassRelationFilter;
import com.happy3w.codemap.model.RelationResult;
import com.happy3w.persistence.core.assistant.QueryOptions;
import com.happy3w.persistence.es.EsAssistant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassRelationService {
    private final EsAssistant esAssistant;

    public ClassRelationService(EsAssistant esAssistant) {
        this.esAssistant = esAssistant;
    }

    public RelationResult queryRelation(ClassRelationFilter filter) {
        Set<String> existNodeIds = new HashSet<>();
        List<ClassInfo> nodes = new ArrayList<>();
        List<ClassRelation> relations = esAssistant.findByFilter(ClassRelation.class,
                filter.toFilterList(),
                QueryOptions.builder()
                        .maxSize(2001)
                        .build())
                .peek(r -> collectNodes(r, nodes, existNodeIds))
                .filter(relation -> relation.getClassB() != null)
                .collect(Collectors.toList());
        return new RelationResult(nodes, relations, relations.size() > 2000);
    }

    private void collectNodes(ClassRelation relation, List<ClassInfo> nodes, Set<String> existNodeIds) {
        collectNodes(relation.getClassA(), nodes, existNodeIds);
        collectNodes(relation.getClassB(), nodes, existNodeIds);
    }

    private void collectNodes(String nodeId, List<ClassInfo> nodes, Set<String> existNodeIds) {
        if (nodeId == null || existNodeIds.contains(nodeId)) {
            return;
        }
        int index = nodeId.lastIndexOf('/');
        String shortName = index < 0 ? nodeId : nodeId.substring(index + 1);
        nodes.add(new ClassInfo(nodeId, shortName));
        existNodeIds.add(nodeId);
    }
}
