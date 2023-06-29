package com.grimpan.drawingdiary.domain;

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
@Table(name = "diarys")
@DynamicUpdate
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "art_name")
    private String artName;

    @Column(name = "content")
    private String content;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Builder
    public Diary(User user, String artName, String content, String keywords, String title) {
        this.user = user;
        this.artName = artName;
        this.content = content;
        this.keywords = keywords;
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
        this.title = title;
    }
}
