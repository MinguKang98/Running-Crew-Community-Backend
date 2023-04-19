package com.project.runningcrew.reported.comment.dto.request;

import com.project.runningcrew.reported.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CreateReportedCommentRequest {

    @Schema(description = "댓글 신고 멤버의 아이디 값", example = "1")
    @NotNull(message = "댓글 신고 멤버의 아이디 값은 필수입니다.")
    private Long reporterMemberId;

    @Schema(description = "신고된 댓글의 아이디 값", example = "1")
    @NotNull(message = "댓글 아이디 값은 필수입니다.")
    private Long commentId;

    @Schema(description = "댓글 신고 사유", example = "abuse(전부 소문자)")
    @NotBlank(message = "댓글 신고 사유 값은 필수입니다.")
    private String reportType;

}
