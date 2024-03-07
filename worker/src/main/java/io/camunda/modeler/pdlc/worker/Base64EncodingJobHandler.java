package io.camunda.modeler.pdlc.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64EncodingJobHandler implements JobHandler {

    private static final String WORKER_TYPE = "base64";

    @JobWorker(type = WORKER_TYPE)
    @Override
    public void handle(JobClient client, ActivatedJob job) {
        // base64 encode payload and set as output
        if (job.getVariable("base64_input") instanceof String payload) {
            var encodedPayload = Base64.getEncoder().encodeToString(payload.getBytes());
            client.newCompleteCommand(job.getKey()).variable("base64_output", encodedPayload).send().join();
        } else {
            client.newCompleteCommand(job).send().join();
        }
    }
}
