package com.project.runningcrew.member.entity;

import com.project.runningcrew.common.BaseEntity;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberRole role;

    public Member(User user, Crew crew, MemberRole role) {
        if (user != null) {
            setUser(user);
        }

        this.crew = crew;
        this.role = role;
    }

    public Member(Long id, User user, Crew crew, MemberRole role) {
        if (user != null) {
            setUser(user);
        }

        this.id = id;
        this.crew = crew;
        this.role = role;
    }

    private void setUser(User user) {
        this.user = user;
        user.getMembers().add(this);
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }

}