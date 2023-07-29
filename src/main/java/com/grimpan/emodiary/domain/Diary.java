package com.grimpan.emodiary.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "diaries")
@DynamicUpdate
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private Boolean status;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "emotion_score")
    private Integer emotionScore;

    // ---------------------------------------------------------- //

    @OneToOne(mappedBy = "diary", fetch = FetchType.LAZY)
    private Image image;

    @Builder
    public Diary(User user, String title, String content,
                 String keywords, Integer emotionScore) {
        this.user = user;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.title = title;
        this.content = content;
        this.keywords = keywords;
        this.emotionScore = emotionScore;
    }
}
