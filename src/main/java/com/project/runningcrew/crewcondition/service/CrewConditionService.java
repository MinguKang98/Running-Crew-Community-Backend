package com.project.runningcrew.crewcondition.service;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.crewcondition.entity.CrewCondition;
import com.project.runningcrew.crewcondition.repository.CrewConditionRepository;
import com.project.runningcrew.exception.notFound.CrewConditionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewConditionService {

    private final CrewConditionRepository crewConditionRepository;


    /**
     * crew 에 속하는 CrewCondition 반환. 없다면 throw CrewConditionNotFoundException
     *
     * @param crew
     * @return crew 에 속하는 CrewCondition
     * @throws CrewConditionNotFoundException crew 에 속하는 CrewCondition 이 없다면
     */
    public CrewCondition findByCrew(Crew crew) {
        return crewConditionRepository.findByCrew(crew)
                .orElseThrow(CrewConditionNotFoundException::new);
    }

    /**
     * crewCondition 의 joinApply 를 true 로 변경한다.
     *
     * @param crewCondition
     */
    @Transactional
    public void updateJoinApplyTrue(CrewCondition crewCondition) {
        crewCondition.updateJoinApply(true);
    }

    /**
     * crewCondition 의 joinApply 를 false 로 변경한다.
     *
     * @param crewCondition
     */
    @Transactional
    public void updateJoinApplyFalse(CrewCondition crewCondition) {
        crewCondition.updateJoinApply(false);
    }

    /**
     * crewCondition 의 joinQuestion 을 true 로 변경한다.
     *
     * @param crewCondition
     */
    @Transactional
    public void updateJoinQuestionTrue(CrewCondition crewCondition) {
        crewCondition.updateJoinQuestion(true);
    }

    /**
     * crewCondition 의 joinQuestion 을 false 로 변경한다.
     *
     * @param crewCondition
     */
    @Transactional
    public void updateJoinQuestionFalse(CrewCondition crewCondition) {
        crewCondition.updateJoinQuestion(false);
    }

}