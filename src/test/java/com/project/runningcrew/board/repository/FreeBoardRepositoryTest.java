package com.project.runningcrew.board.repository;

import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.board.entity.FreeBoard;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.crew.repository.CrewRepository;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.TestEntityFactory;
import com.project.runningcrew.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FreeBoardRepositoryTest {

    @Mock
    FreeBoardRepository freeBoardRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CrewRepository crewRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    TestEntityFactory testEntityFactory;


    public User testUser(DongArea dongArea, int num) {
        User user = User.builder()
                .email("email@email.com" + num)
                .password("password123!")
                .name("name")
                .nickname("nickname"+ num)
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(dongArea)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        return userRepository.save(user);
    }

    public Crew testCrew(DongArea dongArea, int num) {
        Crew crew = Crew.builder()
                .name("name"+ num)
                .dongArea(dongArea)
                .introduction("introduction")
                .crewImgUrl("crewImgUrl")
                .build();
        return crewRepository.save(crew);
    }

    public Member testMember(User user, Crew crew) {
        Member member = new Member(user, crew, MemberRole.ROLE_NORMAL);
        return memberRepository.save(member);
    }



    @DisplayName("FreeBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard freeBoard = new FreeBoard(member, title, content);

        //when
        FreeBoard savedFreeBoard = freeBoardRepository.save(freeBoard);

        //then
        Assertions.assertThat(savedFreeBoard).isEqualTo(freeBoard);
    }



    @DisplayName("FreeBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard savedFreeBoard = freeBoardRepository.save( new FreeBoard(member, title, content));

        //when
        Optional<FreeBoard> findFreeBoardOpt = freeBoardRepository.findById(savedFreeBoard.getId());

        //then
        Assertions.assertThat(findFreeBoardOpt).isNotEmpty();
        Assertions.assertThat(findFreeBoardOpt).hasValue(savedFreeBoard);
    }



    @DisplayName("FreeBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        int num = 1;
        String title = "title";
        String content = "content";
        SidoArea sidoArea = testEntityFactory.getSidoArea(1);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 1);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 1);
        User user = testUser(dongArea, 1);
        Crew crew = testCrew(dongArea, 1);
        Member member = testMember(user, crew);
        FreeBoard savedFreeBoard = freeBoardRepository.save( new FreeBoard(member, title, content));

        //when
        freeBoardRepository.delete(savedFreeBoard);
        Optional<FreeBoard> findFreeBoardOpt = freeBoardRepository.findById(savedFreeBoard.getId());

        //then
        Assertions.assertThat(findFreeBoardOpt).isEmpty();
    }



    @DisplayName("각 Crew 의 자유게시판 paging 출력 테스트")
    @Test
    void findFreeBoardByCrewTest() throws Exception {

    }


}