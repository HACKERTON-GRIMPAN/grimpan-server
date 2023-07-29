package com.grimpan.emodiary.domain;

import com.grimpan.emodiary.domain.type.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_login", columnDefinition = "TINYINT(1)")
    private Boolean isLogin;

    @Column(name = "refresh_Token")
    private String refreshToken;

    @Column(name = "device_token")
    private String deviceToken;

    // ------------------------------------------------------------------ //

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LoginProvider> providers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Diary> diaryList = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Image image;


    @Builder
    public User(String name, String nickname, String phoneNumber,
                UserRole role, String refreshToken) {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isLogin = Boolean.TRUE;
        this.refreshToken = refreshToken;
        this.deviceToken = null;
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
