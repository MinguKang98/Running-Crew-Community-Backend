package com.project.runningcrew.runningmember.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.runningnotice.entity.RunningNotice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "running_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningMember extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "running_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "running_notice_id", nullable = false)
    private RunningNotice runningNotice;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public RunningMember(RunningNotice runningNotice, Member member) {
        this.runningNotice = runningNotice;
        this.member = member;
    }

    public RunningMember(Long id, RunningNotice runningNotice, Member member) {
        this.id = id;
        this.runningNotice = runningNotice;
        this.member = member;
    }

}