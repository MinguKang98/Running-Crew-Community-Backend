package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RunningMember;
import com.project.runningcrew.entity.images.RunningNoticeImage;
import com.project.runningcrew.entity.members.Member;
import com.project.runningcrew.entity.members.MemberRole;
import com.project.runningcrew.entity.runningnotices.NoticeType;
import com.project.runningcrew.entity.runningnotices.RunningNotice;
import com.project.runningcrew.entity.runningnotices.RunningStatus;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.notFound.RunningNoticeNotFoundException;
import com.project.runningcrew.repository.RunningMemberRepository;
import com.project.runningcrew.repository.RunningNoticeRepository;
import com.project.runningcrew.repository.images.RunningNoticeImageRepository;
import com.project.runningcrew.service.images.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunningNoticeServiceTest {

    @Mock
    private RunningNoticeRepository runningNoticeRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private RunningNoticeImageRepository runningNoticeImageRepository;

    @Mock
    private RunningMemberRepository runningMemberRepository;

    @InjectMocks
    private RunningNoticeService runningNoticeService;

    @DisplayName("id로 런닝기록 가져오기 성공 테스트")
    @Test
    public void findByIdTest1(@Mock Member member) {
        //given
        Long runningNoticeId = 1L;
        RunningNotice runningNotice = RunningNotice.builder()
                .id(runningNoticeId)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        when(runningNoticeRepository.findById(runningNoticeId)).thenReturn(Optional.of(runningNotice));

        ///when
        RunningNotice findRunningNotice = runningNoticeService.findById(runningNoticeId);

        //then
        assertThat(findRunningNotice).isEqualTo(runningNotice);
        verify(runningNoticeRepository, times(1)).findById(runningNoticeId);
    }

    @DisplayName("id로 런닝기록 가져오기 예외 테스트")
    @Test
    public void findByIdTest2() {
        //given
        Long runningNoticeId = 1L;
        when(runningNoticeRepository.findById(runningNoticeId)).thenReturn(Optional.empty());

        ///when
        //then
        assertThatThrownBy(() -> runningNoticeService.findById(runningNoticeId))
                .isInstanceOf(RunningNoticeNotFoundException.class);
        verify(runningNoticeRepository, times(1)).findById(runningNoticeId);
    }

    @DisplayName("런닝기록 저장 테스트")
    @Test
    public void saveRunningNoticeTest(@Mock Member member) {
        //given
        Long id = 1L;
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multipartFiles.add(new MockMultipartFile("image", "".getBytes()));
        }
        RunningNotice runningNotice = RunningNotice.builder()
                .id(id)
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();
        RunningMember runningMember = new RunningMember(runningNotice, runningNotice.getMember());
        when(runningNoticeRepository.save(runningNotice)).thenReturn(runningNotice);
        when(imageService.uploadImage(any(), any())).thenReturn("runningNoticeImgUrl");
        when(runningNoticeImageRepository.save(any()))
                .thenReturn(new RunningNoticeImage("runningNoticeImgUrl", runningNotice));
        when(runningMemberRepository.save(any())).thenReturn(runningMember);

        ///when
        Long runningNoticeId = runningNoticeService.saveRunningNotice(runningNotice, multipartFiles);

        //then
        assertThat(runningNoticeId).isSameAs(id);
        verify(runningNoticeRepository, times(1)).save(any());
        verify(imageService, times(multipartFiles.size())).uploadImage(any(), any());
        verify(runningNoticeImageRepository, times(multipartFiles.size())).save(any());
        verify(runningMemberRepository, times(1)).save(any());
    }

    @DisplayName("런닝기록 수정 테스트")
    @Test
    public void updateRunningNoticeTest(@Mock Member member) {
        //given
        RunningNotice runningNotice1 = RunningNotice.builder()
                .title("title1")
                .detail("detail1")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();

        RunningNotice runningNotice2 = RunningNotice.builder()
                .title("title2")
                .detail("detail2")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 20, 0))
                .runningPersonnel(20)
                .status(RunningStatus.READY)
                .build();

        List<MultipartFile> addFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            addFiles.add(new MockMultipartFile("image", "".getBytes()));
        }
        List<RunningNoticeImage> deleteImages = List.of(
                new RunningNoticeImage("image1", runningNotice1),
                new RunningNoticeImage("image2", runningNotice1),
                new RunningNoticeImage("image3", runningNotice1)
        );

        ///when
        runningNoticeService.updateRunningNotice(runningNotice1, runningNotice2, addFiles, deleteImages);

        //then
        assertThat(runningNotice1.getTitle()).isEqualTo(runningNotice2.getTitle());
        assertThat(runningNotice1.getDetail()).isEqualTo(runningNotice2.getDetail());
        assertThat(runningNotice1.getRunningDateTime()).isEqualTo(runningNotice2.getRunningDateTime());
        assertThat(runningNotice1.getRunningPersonnel()).isEqualTo(runningNotice2.getRunningPersonnel());
        verify(imageService, times(addFiles.size())).uploadImage(any(), any());
        verify(runningNoticeImageRepository, times(addFiles.size())).save(any());
        verify(imageService, times(deleteImages.size())).deleteImage(any());
        verify(runningNoticeImageRepository, times(deleteImages.size())).delete(any());
    }

    @DisplayName("런닝공지 상태 수정 테스트")
    @Test
    public void updateRunningStatusTest(@Mock Member member) {
        //given
        RunningNotice runningNotice = RunningNotice.builder()
                .title("title")
                .detail("detail")
                .member(member)
                .noticeType(NoticeType.REGULAR)
                .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                .runningPersonnel(10)
                .status(RunningStatus.READY)
                .build();

        ///when
        runningNoticeService.updateRunningStatus(runningNotice);

        //then
        assertThat(runningNotice.getStatus()).isSameAs(RunningStatus.DONE);
    }

    @DisplayName("런닝공지 삭제 테스트")
    @Test
    public void deleteRunningNoticeTest() {
        //given

        ///when

        //then
    }

    @DisplayName("정기런닝공지 페이징 테스트")
    @Test
    public void findRegularsByCrewTest(@Mock User user, @Mock Crew crew) {
        //given
        Member member = new Member(user, crew, MemberRole.ROLE_LEADER);
        List<RunningNotice> runningNotices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = RunningNotice.builder()
                    .title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.REGULAR)
                    .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                    .runningPersonnel(10)
                    .status(RunningStatus.READY)
                    .build();
            runningNotices.add(runningNotice);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<RunningNotice> runningNoticeSlice = new SliceImpl<>(runningNotices, pageRequest, true);
        when(runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.REGULAR, crew, pageRequest))
                .thenReturn(runningNoticeSlice);

        ///when
        Slice<RunningNotice> result = runningNoticeService.findRegularsByCrew(crew, pageRequest);

        //then
        for (RunningNotice runningNotice : result) {
            assertThat(runningNotice.getNoticeType()).isSameAs(NoticeType.REGULAR);
        }
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(10);
        assertThat(result.getNumberOfElements()).isSameAs(10);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        verify(runningNoticeRepository, times(1))
                .findAllByCrewAndNoticeType(NoticeType.REGULAR, crew, pageRequest);
    }

    @DisplayName("번개런닝공지 페이징 테스트")
    @Test
    public void findInstantsByCrewTest(@Mock User user, @Mock Crew crew) {
        //given
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        List<RunningNotice> runningNotices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = RunningNotice.builder()
                    .title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.INSTANT)
                    .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                    .runningPersonnel(10)
                    .status(RunningStatus.READY)
                    .build();
            runningNotices.add(runningNotice);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<RunningNotice> runningNoticeSlice = new SliceImpl<>(runningNotices, pageRequest, true);
        when(runningNoticeRepository.findAllByCrewAndNoticeType(NoticeType.INSTANT, crew, pageRequest))
                .thenReturn(runningNoticeSlice);

        ///when
        Slice<RunningNotice> result = runningNoticeService.findInstantsByCrew(crew, pageRequest);

        //then
        for (RunningNotice runningNotice : result) {
            assertThat(runningNotice.getNoticeType()).isSameAs(NoticeType.INSTANT);
        }
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(10);
        assertThat(result.getNumberOfElements()).isSameAs(10);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        verify(runningNoticeRepository, times(1))
                .findAllByCrewAndNoticeType(NoticeType.INSTANT, crew, pageRequest);
    }

    @DisplayName("키워드로 런닝공지 찾기 페이징 테스트")
    @Test
    public void findByCrewAndKeywordTest(@Mock User user, @Mock Crew crew) {
        //given
        String keyword = "tail";
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        List<RunningNotice> runningNotices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = RunningNotice.builder()
                    .title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.INSTANT)
                    .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                    .runningPersonnel(10)
                    .status(RunningStatus.READY)
                    .build();
            runningNotices.add(runningNotice);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        SliceImpl<RunningNotice> runningNoticeSlice = new SliceImpl<>(runningNotices, pageRequest, true);
        when(runningNoticeRepository.findSliceAllByCrewAndKeyWord(keyword, crew, pageRequest))
                .thenReturn(runningNoticeSlice);

        ///when
        Slice<RunningNotice> result = runningNoticeService.findByCrewAndKeyword(crew, keyword, pageRequest);

        //then
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(10);
        assertThat(result.getNumberOfElements()).isSameAs(10);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        verify(runningNoticeRepository, times(1)).findSliceAllByCrewAndKeyWord(keyword, crew, pageRequest);
    }

    @DisplayName("유저의 시작 예정인 런닝공지 모두 반환 테스트")
    @Test
    public void findReadyRunningNoticesByUserTest(@Mock User user, @Mock Member member) {
        //given
        List<RunningNotice> runningNotices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RunningNotice runningNotice = RunningNotice.builder()
                    .title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.INSTANT)
                    .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                    .runningPersonnel(10)
                    .status(RunningStatus.READY)
                    .build();
            runningNotices.add(runningNotice);
        }
        when(runningNoticeRepository.findAllByUserAndStatus(user, RunningStatus.READY)).thenReturn(runningNotices);

        ///when
        List<RunningNotice> result = runningNoticeService.findReadyRunningNoticesByUser(user);

        //then
        for (RunningNotice runningNotice : result) {
            assertThat(runningNotice.getStatus()).isSameAs(RunningStatus.READY);
        }
        verify(runningNoticeRepository, times(1)).findAllByUserAndStatus(user, RunningStatus.READY);
    }

    @DisplayName("크루의 특정날 시행하는 런닝공지 반환 테스트")
    @Test
    public void findAllByCrewAndRunningDateTest(@Mock Crew crew, @Mock Member member) {
        //given
        LocalDate runningDate = LocalDate.of(2023, 2, 27);
        List<LocalDateTime> startDates = List.of(
                LocalDateTime.of(2023, 2, 27, 0, 0),
                LocalDateTime.of(2023, 2, 27, 11, 20),
                LocalDateTime.of(2023, 2, 27, 23, 59));

        List<RunningNotice> runningNotices = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RunningNotice runningNotice = RunningNotice.builder()
                    .title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.INSTANT)
                    .runningDateTime(startDates.get(i))
                    .runningPersonnel(10)
                    .status(RunningStatus.READY)
                    .build();
            runningNotices.add(runningNotice);
        }
        when(runningNoticeRepository.findAllByCrewAndRunningDate(
                LocalDateTime.of(2023, 2, 27, 0, 0),
                LocalDateTime.of(2023, 2, 28, 0, 0),
                crew)).thenReturn(runningNotices);

        ///when
        List<RunningNotice> result = runningNoticeService.findAllByCrewAndRunningDate(crew, runningDate);

        //then
        assertThat(result.size()).isSameAs(3);
        verify(runningNoticeRepository, times(1)).findAllByCrewAndRunningDate(
                LocalDateTime.of(2023, 2, 27, 0, 0),
                LocalDateTime.of(2023, 2, 28, 0, 0),
                crew);
    }

    @DisplayName("특정 멤버가 작성한 모든 런닝공지 페이징 테스트")
    @Test
    public void findAllByMemberTest(@Mock Member member) {
        //given
        List<RunningNotice> runningNotices = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            RunningNotice runningNotice = RunningNotice.builder()
                    .title("title" + i)
                    .detail("detail" + i)
                    .member(member)
                    .noticeType(NoticeType.INSTANT)
                    .runningDateTime(LocalDateTime.of(2023, 2, 26, 18, 0))
                    .runningPersonnel(10)
                    .status(RunningStatus.READY)
                    .build();
            runningNotices.add(runningNotice);
        }
        PageRequest pageRequest = PageRequest.of(0, 7);
        SliceImpl<RunningNotice> runningNoticeSlice = new SliceImpl<>(runningNotices, pageRequest, true);
        when(runningNoticeRepository.findAllByMember(member, pageRequest)).thenReturn(runningNoticeSlice);

        ///when
        Slice<RunningNotice> result = runningNoticeService.findByMember(member, pageRequest);

        //then
        for (RunningNotice runningNotice : result) {
            assertThat(runningNotice.getMember()).isEqualTo(member);
        }
        assertThat(result.getNumber()).isSameAs(0);
        assertThat(result.getSize()).isSameAs(7);
        assertThat(result.getNumberOfElements()).isSameAs(7);
        assertThat(result.hasPrevious()).isFalse();
        assertThat(result.hasNext()).isTrue();
        assertThat(result.isFirst()).isTrue();
        verify(runningNoticeRepository, times(1)).findAllByMember(member, pageRequest);
    }

}