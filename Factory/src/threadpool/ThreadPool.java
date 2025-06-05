package threadpool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final LinkedList<Task> taskQueue;
    private final List<Worker> workers;
    private volatile boolean isRunning;
  //  private final int maxQueueSize;

    public ThreadPool(int numThreads) {
        if (numThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be positive");
        }
        taskQueue = new LinkedList<>();
        workers = new ArrayList<>();
        isRunning = true;
  //      maxQueueSize = capacity;

        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
    }

    public void submit(Task task) {
        synchronized (taskQueue) {

//            while (isRunning && !isEnoughSpace()) {
//                try {
//                    taskQueue.wait();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    return;
//                }
//            }

            if (!isRunning) return;
            taskQueue.add(task);
            taskQueue.notifyAll();
        }
    }

//    public boolean isEnoughSpace(){
//        return taskQueue.size() + getTasksInProcess() < maxQueueSize;
//    }

    public synchronized int getQueueSize() {
        return taskQueue.size();
    }

    public int getTasksCompletedByWorkers() {
        synchronized (workers) {
            List<Integer> tmp = workers.stream().map(Worker::getTasksCompleted).toList();
            return tmp.stream().mapToInt(Integer::intValue).sum();
        }
    }

    public int getTasksInProcess() {
        synchronized (workers) {
            List<Integer> tmp = workers.stream().map(Worker::getInProcess).toList();
            return tmp.stream().mapToInt(Integer::intValue).sum();
        }
    }

    public void shutdown() {
        isRunning = false;
        synchronized (taskQueue) {
            taskQueue.clear();
            taskQueue.notifyAll();
        }
        for (Worker worker : workers) {
            worker.interrupt();
            }

    }

    private class Worker extends Thread {
        private int tasksCompleted;
        private boolean isInProcess = false;

        Worker() {
            this.tasksCompleted = 0;
        }

        public int getTasksCompleted() {
            return tasksCompleted;
        }
        public int getInProcess(){ return isInProcess ? 1: 0; }

        @Override
        public void run() {
            while (isRunning && !this.isInterrupted()) {
                Task task = null;
                synchronized (taskQueue) {
                    while (isRunning && taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            this.interrupt();
                            return;
                        }
                    }
                    if (isRunning && !taskQueue.isEmpty()) {
                        isInProcess = true;
                        task = taskQueue.removeFirst();
                        taskQueue.notifyAll();
                    }
                }
                if (task != null) {
                    try {
                        task.execute();
                        tasksCompleted++;
                        isInProcess = false;
                    } catch (Exception e) {
                        System.err.println("Error executing task: " + e.getMessage());
                    }
                }
                isInProcess = false;


            }
        }
    }
}