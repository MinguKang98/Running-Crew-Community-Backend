package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.FreeBoard;
import com.project.runningcrew.entity.boards.ReviewBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    /**
     * 특정 Crew 의 리뷰게시판 목록 paging 적용 출력
     * @param pageable
     * @return page of ReviewBoard
     */
    @Query("select rb from ReviewBoard rb where rb.member.crew = :crew")
    Slice<ReviewBoard> findReviewBoardByCrew(@Param("crew") Crew crew, Pageable pageable);

}
