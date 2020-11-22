package com.happy3w.codemap.model;

import com.happy3w.persistence.core.filter.IFilter;
import com.happy3w.persistence.core.filter.IFilterAble;
import com.happy3w.persistence.core.filter.impl.CombineFilter;
import com.happy3w.persistence.core.filter.impl.StringInFilter;
import com.happy3w.persistence.core.filter.impl.StringLikeInFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ClassRelationFilter implements IFilterAble {
    private Set<String> includes = new HashSet<>();
    private Set<String> excludes = new HashSet<>();
    private Set<String> relationTypes = new HashSet<>();
    private int refCount = 2;

    private Set<String> fileRanges = new HashSet<>();

    @Override
    public List<IFilter> toFilterList() {
        List<IFilter> filters = new ArrayList<>();
        filters.add(new StringInFilter("fileRanges", fileRanges));

        List<IFilter> fieldFilter = new ArrayList<>();
        fieldFilter.add(matchField("classA"));
        fieldFilter.add(matchField("classB"));

        IFilter combineFilter = CombineFilter.create(CombineFilter.Ops.Or, filters);
        if (combineFilter != null) {
            filters.add(combineFilter);
        }

        return filters;
    }

    private IFilter matchField(String field) {
        List<IFilter> combineFilters = new ArrayList<>();
        if (!CollectionUtils.isEmpty(includes)) {
            combineFilters.add(new StringLikeInFilter(field, includes));
        }
        if (!CollectionUtils.isEmpty(excludes)) {
            combineFilters.add(new StringLikeInFilter(field, excludes, false));
        }
        return CombineFilter.create(CombineFilter.Ops.And, combineFilters);
    }
}
