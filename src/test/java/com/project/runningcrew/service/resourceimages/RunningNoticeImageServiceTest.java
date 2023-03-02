package com.project.runningcrew.service.resourceimages;

import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningNoticeImageServiceTest {

    @Mock
    private RunningNoticeImageRepository runningNoticeImageRepository;

    @InjectMocks
    private RunningNoticeImageService runningNoticeImageService;

    @DisplayName("런닝공지에 포함된 모든 이미지 반환 테스트")
    @Test
    public void findAllByRunningNoticeTest(@Mock RunningNotice runningNotice) {
        //given
        List<RunningNoticeImage> runningNoticeImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            runningNoticeImages.add(new RunningNoticeImage("image" + i, runningNotice));
        }
        when(runningNoticeImageRepository.findAllByRunningNotice(runningNotice)).thenReturn(runningNoticeImages);

        ///when
        List<RunningNoticeImage> result = runningNoticeImageService.findAllByRunningNotice(runningNotice);

        //then
        assertThat(result.size()).isSameAs(10);
        verify(runningNoticeImageRepository, times(1)).findAllByRunningNotice(runningNotice);
    }

    @DisplayName("runningNoticeId 리스트를 받아 runningNoticeId 와 RunningNoticeImage 의 Map 반환 테스트")
    @Test
    public void findFirstImagesTest(@Mock Member member) {
        //given
        List<Long> runningNoticeIds = List.of(1L, 2L, 3L);
        List<RunningNoticeImage> runningNoticeImages = new ArrayList<>();
        RunningNotice runningNotice1 = RunningNotice.builder()
                .id(1L)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        for (int i = 0; i < 3; i++) {
            RunningNoticeImage runningNoticeImage = new RunningNoticeImage("image" + i, runningNotice1);
            runningNoticeImages.add(runningNoticeImage);
        }
        RunningNotice runningNotice3 = RunningNotice.builder()
                .id(3L)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        for (int i = 0; i < 2; i++) {
            RunningNoticeImage runningNoticeImage = new RunningNoticeImage("image" + i, runningNotice3);
            runningNoticeImages.add(runningNoticeImage);
        }
        when(runningNoticeImageRepository.findImagesByRunningNoticeIds(runningNoticeIds))
                .thenReturn(runningNoticeImages);

        ///when
        Map<Long, RunningNoticeImage> firstImages = runningNoticeImageService.findFirstImages(runningNoticeIds);

        //then
        assertThat(firstImages.get(1L).getRunningNotice().getId()).isSameAs(1L);
        assertThat(firstImages.get(2L).getFileName()).isEqualTo("defaultImageUrl");
        assertThat(firstImages.get(3L).getRunningNotice().getId()).isSameAs(3L);
        verify(runningNoticeImageRepository, times(1)).findImagesByRunningNoticeIds(runningNoticeIds);
    }

}