package com.learn.chatai.api.admin.job;

import com.learn.chatai.api.admin.job.dto.JobStatusResponseDto;
import com.learn.chatai.core.response.SuccessResponse;
import com.learn.chatai.domain.job.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-admin/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<SuccessResponse<JobStatusResponseDto>> getJob(@PathVariable String jobId) {
        return ResponseEntity.ok(SuccessResponse.of(JobStatusResponseDto.from(jobService.getOrThrow(jobId))));
    }
}
