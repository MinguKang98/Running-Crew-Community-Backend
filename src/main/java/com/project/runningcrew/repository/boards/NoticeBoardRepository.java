package com.project.runningcrew.repository.boards;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.boards.NoticeBoard;
import com.project.runningcrew.entity.members.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {

    /**
     * 공지게시판 전체 게시물 출력에 paging 적용
     * @param pageable
     * @return slice of NoticeBoard
     */
    @Query("select nb from NoticeBoard nb where nb.member.crew = :crew")
    Slice<NoticeBoard> findNoticeBoardByCrew(@Param("crew") Crew crew, Pageable pageable);

}
