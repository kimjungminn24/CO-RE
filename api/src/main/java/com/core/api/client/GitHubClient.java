package com.core.api.client;

import com.core.api.data.dto.ContentDto;
import com.core.api.data.dto.FileDto;
import com.core.api.data.dto.response.MergeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "github", url = "https://api.github.com")
public interface GitHubClient {

    @PostMapping("/repos/{owner}/{repo}/pulls")
    void createPullRequest(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @RequestBody PullRequestInputServerDto pullRequestInputServerDto
    );

    @GetMapping("/repos/{owner}/{repo}/pulls/{pullId}/files")
    List<FileDto> getChangeFiles(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("pullId") int pullId
    );

    @GetMapping("/repos/{owner}/{repo}/contents/{path}")
    ResponseEntity<ContentDto> getContents(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("path") String path,
            @RequestParam("ref") String ref
    );

    @PutMapping("/repos/{owner}/{repo}/pulls/{pullId}/merge")
    ResponseEntity<MergeResponseDto> mergePullRequest(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("pullId") int pullId,
            @RequestBody CommitMessageServerDto commitMessage
    );

}
