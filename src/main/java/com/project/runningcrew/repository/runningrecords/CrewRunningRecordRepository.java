package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningrecords.CrewRunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRunningRecordRepository extends JpaRepository<CrewRunningRecord, Long> {

    List<CrewRunningRecord> findAllByMember(Member member);

}
