package com.happy3w.codemap.service;

import com.happy3w.codemap.model.BackendTask;
import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
@Service
public class BackendTaskService {
    private int nextIndex = 1;
    private List<Pair<BackendTask, Consumer<BackendTask>>> allBackendTask = new ArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(3);

    public List<BackendTask> queryAllTask() {
        return ListUtils.map(allBackendTask, Pair::getKey);
    }

    public BackendTask createTask(BackendTask newTask, Consumer<BackendTask> action) {
        newTask.setId(String.valueOf(nextIndex++));
        newTask.setStatus(BackendTask.Status.waiting);
        allBackendTask.add(new Pair<>(newTask, action));
        executor.submit(new TaskRunner(newTask, action));
        tryCleanSomeTask();
        return newTask;
    }

    private void tryCleanSomeTask() {
        int countToRemove = allBackendTask.size() - 10;
        Iterator<Pair<BackendTask, Consumer<BackendTask>>> it = allBackendTask.iterator();
        while (countToRemove > 0 && it.hasNext()) {
            BackendTask task = it.next().getKey();
            String status = task.getStatus();
            if (BackendTask.Status.failed.equals(status) ||  BackendTask.Status.finished.equals(status)) {
                it.remove();
                countToRemove--;
            }
        }
    }

    private static class TaskRunner implements Runnable {
        private final BackendTask task;
        private final Consumer<BackendTask> action;

        private TaskRunner(BackendTask task, Consumer<BackendTask> action) {
            this.task = task;
            this.action = action;
        }

        @Override
        public void run() {
            task.setStatus(BackendTask.Status.running);
            try {
                action.accept(task);
                task.setStatus(BackendTask.Status.finished);
            } catch (Exception exp) {
                task.setStatus(BackendTask.Status.failed);
                task.setRemark(exp.getMessage());
                log.error(exp.getMessage(), exp);
            }
        }
    }
}
