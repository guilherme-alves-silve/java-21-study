package exercise3;

import exercise3.LongRunningTask.TaskResponse;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

import static java.util.stream.Collectors.toMap;

public class STaskScopeOnFailureExecutor {

  /**
   * Exercise:
   * Create a class called STaskScopeOnFailureExecutor which has a single static method called
   * execute which takes in a Collection of LongRunningTask objects and returns a Map of reponses.
   * It would throw an InterruptedException if interrupted or ExecutionException in case any Task fails.
   * Question:
   * 1. What challenges did you face to complete this assignment? The challenge was to maintain the
   * LongRunningTask name attached to the result of the task, but using the class
   * StructuredTaskScope.ShutdownOnFailure, was easy to manage the results and if occured any
   * exception in any of the tasks, a exception is thrown when you use the method throwIfFailed
   */
  public static Map<String, TaskResponse> execute(List<LongRunningTask> tasks)
      throws ExecutionException, InterruptedException {
    if (tasks.isEmpty())
      throw new IllegalArgumentException("No tasks was sent!");

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

      var taskNameAndFork = tasks.stream()
          .collect(toMap(LongRunningTask::getName, scope::fork));

      scope.join();
      scope.throwIfFailed();

      return taskNameAndFork.entrySet()
          .stream()
          .collect(toMap(Entry::getKey,
              entry -> entry.getValue().get()));
    }
  }
}
