package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningRecordImageRepository extends JpaRepository<RunningRecordImage, Long> {

    /**
     * runningRecord 포함된 모든 RunningRecordImage 반환
     *
     * @param runningRecord
     * @return runningRecord 에 포함된 모든 RunningRecordImage 의 list
     */
    List<RunningRecordImage> findAllByRunningRecord(RunningRecord runningRecord);

    /**
     * runningRecord 에 포함된 모든 RunningRecordImage 삭제
     *
     * @param runningRecord
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RunningRecordImage r where r.runningRecord = :runningRecord")
    void deleteAllByRunningRecord(@Param("runningRecord") RunningRecord runningRecord);

    /**
     * runningRecordId 의 리스트에 포함된 runningRecordId 를 가진 RunningRecordImage 반환
     *
     * @param runningRecordIds RunningRecord 의 id 를 가진 리스트
     * @return RunningRecord 의 id 가 runningRecordIds 에 포함된 모든 RunningRecordImage.
     */
    @Query("select r from RunningRecordImage r where r.runningRecord.id in (:runningRecordIds)")
    List<RunningRecordImage> findImagesByRunningRecordIds(@Param("runningRecordIds") List<Long> runningRecordIds);

}
