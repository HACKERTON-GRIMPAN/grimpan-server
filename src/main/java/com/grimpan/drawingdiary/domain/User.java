package com.grimpan.drawingdiary.domain;

import com.grimpan.drawingdiary.domain.type.LoginProviderType;
import com.grimpan.drawingdiary.domain.type.UserRoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private LoginProviderType provider;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "is_login", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isLogin;

    @Column(name = "refresh_Token")
    private String refreshToken;

    // ------------------------------------------------------------------//

    @OneToMany(mappedBy = "followingUser", fetch = FetchType.LAZY)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser", fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Diary> diarys = new ArrayList<>();

    @Builder
    public User(String socialId, LoginProviderType provider, String name, UserRoleType role, String refreshToken) {
        this.socialId = socialId;
        this.provider = provider;
        this.name = name;
        this.role = role;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.isLogin = Boolean.TRUE;
        this.refreshToken = refreshToken;
    }
}
