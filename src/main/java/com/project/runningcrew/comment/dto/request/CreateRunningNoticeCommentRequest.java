package com.project.runningcrew.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRunningNoticeCommentRequest {

    @NotBlank
    @Size(min = 1, max = 200, message = "댓글 내용은 1자 이상 200자 이하입니다.")
    @Schema(description = "작성한 게시글 댓글 내용", example = "detail")
    private String detail;

    @Positive(message = "아이디 값은 1 이상의 수입니다.")
    @Schema(description = "작성자 멤버 id", example = "1")
    private Long memberId;

}
