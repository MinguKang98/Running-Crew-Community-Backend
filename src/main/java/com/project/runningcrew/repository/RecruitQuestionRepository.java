package com.project.runningcrew.repository;

import com.project.runningcrew.entity.RecruitQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitQuestionRepository extends JpaRepository<RecruitQuestion, Long> {

    /**
     * @param crewId
     * @return list of RecruitQuestion
     * 특정 Crew 의 가입 질문을 offset 순으로 정렬하여 반환한다.
     */
    @Query("select rq from RecruitQuestion rq where rq.crew.id = :crewId order by rq.questionOffset")
    List<RecruitQuestion> findAllByCrewId(@Param("crewId") Long crewId);

}