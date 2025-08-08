package com.camunda.training;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

@ExtendWith(ProcessEngineExtension.class)
public class ProcessJUnitTest {

    @Test
    @Deployment(resources = "META-INF/TweetDiagram.bpmn")
    public void testHappyPath(ProcessEngine processEngine) {

        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("WriteAndPostTweet");
        BpmnAwareTests.assertThat(pi).isNotNull();
        BpmnAwareTests.assertThat(pi).isStarted();

        Task write = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .taskName("Write Tweet")
                .singleResult();
        BpmnAwareTests.assertThat(write).isNotNull();

        taskService.complete(write.getId());

        Task review = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .taskName("Review Tweet")
                .singleResult();
        BpmnAwareTests.assertThat(review).isNotNull();

        taskService.complete(review.getId(),
                withVariables("tweetAproved", true));

        BpmnAwareTests.assertThat(pi).isEnded();
        Assertions.assertThat(processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(pi.getId())
                .activityName("Tweet posted")
                .count()).isEqualTo(1);

    }

}
