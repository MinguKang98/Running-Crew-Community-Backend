package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.runningrecord.entity.PersonalRunningRecord;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.TestEntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonalRunningRecordRepositoryTest {

    @Autowired
    PersonalRunningRecordRepository personalRunningRecordRepository;

    @Autowired
    TestEntityFactory testEntityFactory;

    @DisplayName("save 테스트")
    @Test
    public void saveTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("personal")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();

        ///when
        PersonalRunningRecord savedPersonalRunningRecord = personalRunningRecordRepository.save(personalRunningRecord);

        //then
        assertThat(savedPersonalRunningRecord).isEqualTo(personalRunningRecord);
    }

    @DisplayName("findById 테스트")
    @Test
    public void findByIdTest() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("personal")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        personalRunningRecordRepository.save(personalRunningRecord);

        ///when
        Optional<PersonalRunningRecord> optPersonalRunningRecord = personalRunningRecordRepository
                .findById(personalRunningRecord.getId());

        //then
        assertThat(optPersonalRunningRecord).isNotEmpty();
        assertThat(optPersonalRunningRecord).hasValue(personalRunningRecord);
    }

    @DisplayName("delete 테스트")
    @Test
    public void delete() {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = testEntityFactory.getUser(dongArea, 0);
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .title("personal")
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .location("location")
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(user)
                .build();
        personalRunningRecordRepository.save(personalRunningRecord);

        ///when
        personalRunningRecordRepository.delete(personalRunningRecord);

        //then
        Optional<PersonalRunningRecord> optPersonalRunningRecord = personalRunningRecordRepository
                .findById(personalRunningRecord.getId());
        assertThat(optPersonalRunningRecord).isEmpty();
    }

}