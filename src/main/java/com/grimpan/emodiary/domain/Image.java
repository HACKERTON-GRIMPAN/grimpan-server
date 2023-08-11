package com.grimpan.emodiary.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "diary_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Diary diary;

    @Column(name = "uuid_name")
    private String uuidName;

    // ------------------------------------------------------------------ //


    @Builder
    public Image(Object object, String uuidName) {
        if (object.getClass().equals(User.class)) {
            this.user = (User) object;
            this.diary = null;
        } else if (object.getClass().equals(Diary.class)) {
            this.user = null;
            this.diary = (Diary) object;
        }

        this.uuidName = uuidName;
    }

    public void updateImage(String uuidName) {
        this.uuidName = uuidName;
    }
}
