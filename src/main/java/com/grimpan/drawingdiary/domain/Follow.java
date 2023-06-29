package com.grimpan.drawingdiary.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_user_id")
    private User followerUser;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Builder
    public Follow(User followingUser, User followerUser, Timestamp createdDate) {
        this.followingUser = followingUser;
        this.followerUser = followerUser;
        this.createdDate = createdDate;
    }
}
