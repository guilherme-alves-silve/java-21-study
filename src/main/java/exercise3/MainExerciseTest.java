package exercise3;

import exercise3.LongRunningTask.TaskResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainExerciseTest {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    var dbTask   = new LongRunningTask("dataTask",  3,  "row1", false);
    var restTask = new LongRunningTask("restTask", 10, "json2", false);
    var extTask  = new LongRunningTask("extTask",   5, "json2", false);

    // execute the sub tasks in parallel.
    // Throw exception if interrupted or any task fails
    Map<String, TaskResponse> result
        = STaskScopeOnFailureExecutor.execute(List.of(dbTask, extTask, restTask));

    // print results of all tasks
    result.forEach((k,v) -> {
      System.out.printf("%s : %s\n", k, v);
    });

    // extract output for an individual task
    TaskResponse extResponse = result.get("extTask");
    System.out.println(extResponse);
  }
}
