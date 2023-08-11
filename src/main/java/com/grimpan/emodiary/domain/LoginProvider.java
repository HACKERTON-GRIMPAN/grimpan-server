package com.grimpan.emodiary.domain;

import com.grimpan.emodiary.type.ELoginProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "login_providers")
public class LoginProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private ELoginProvider provider;

    @Builder
    public LoginProvider(User user, String socialId, ELoginProvider provider) {
        this.user = user;
        this.socialId = socialId;
        this.provider = provider;
    }
}
