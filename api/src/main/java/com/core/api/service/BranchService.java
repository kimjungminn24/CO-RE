package com.core.api.service;


import com.core.api.client.GitHubClient;
import com.core.api.data.dto.ChangeDto;
import com.core.api.data.dto.FileDto;
import com.core.api.data.dto.github.CommitServerDto;
import com.core.api.data.dto.response.BranchResponseDto;
import com.core.api.data.dto.response.CompareBranchResponseDto;
import com.core.api.utils.DecodingUtils;
import com.core.api.utils.URLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchService {


    private final GitHubClient gitHubClient;

    public List<BranchResponseDto> getBranches(String owner, String repo) {
        return gitHubClient.getBranches(owner, repo)
                .stream()
                .map(branch -> BranchResponseDto.from(
                        (String) branch.get("name"),
                        CommitServerDto.fromApiResponse(
                                gitHubClient.getCommit(owner, repo, (String) ((Map<?, ?>) branch.get("commit")).get("sha"))
                        )
                ))
                .toList();
    }

    public List<CompareBranchResponseDto> compareBranchHead(String owner, String repo, String baseHead) {
        Map<String, Object> map = gitHubClient.compareBranchHead(owner, repo, baseHead);
        return CompareBranchResponseDto.from(map);
    }

    public CommitServerDto getCommit(String owner, String repo, String sha) {
        Map<?, ?> data = gitHubClient.getCommit(owner, repo, sha);
        return CommitServerDto.fromApiResponse(data);
    }

    public List<ChangeDto> getChangeFiles(String owner, String repo, String baseHead) {
        Map<?, ?> data = gitHubClient.compareBranchHead(owner, repo, baseHead);
        List<?> changeFiles = (List<?>) data.get("files");
        return changeFiles.stream()
                .map(file -> {
                    FileDto fileDto = FileDto.of((Map<?, ?>) file);
                    String path = URLUtils.parseFileName(fileDto.contentsUrl());
                    String ref = URLUtils.parseRefValue(fileDto.contentsUrl());
                    return Optional.ofNullable(gitHubClient.getContents(owner, repo, path, ref))
                            .map(contentMap -> (String) contentMap.get("content"))
                            .map(DecodingUtils::decodeBase64)
                            .map(decodedContent -> ChangeDto.of(fileDto, decodedContent))
                            .orElseThrow(() -> new RuntimeException("Content not found for path: " + path));
                })
                .toList();
    }


}
