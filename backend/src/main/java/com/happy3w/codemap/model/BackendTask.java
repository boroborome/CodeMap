package com.happy3w.codemap.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BackendTask {
    public static class Status {
        public static final String waiting = "waiting";
        public static final String running = "running";
        public static final String finished = "finished";
        public static final String failed = "failed";
    }

    private String id;
    private String status = Status.waiting;
    private String remark;
    private String refId;
    private String refName;
    private Date startTime;
}
