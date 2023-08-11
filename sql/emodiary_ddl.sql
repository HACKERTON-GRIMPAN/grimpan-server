USE emodiary;

DROP TABLE IF EXISTS `images`;
DROP TABLE IF EXISTS `diaries`;
DROP TABLE IF EXISTS `socialInfos`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `id` integer AUTO_INCREMENT,
                         `created_at` timestamp not null,
                         `name` char(30) not null,
                         `nickname` char(15) not null,
                         `phone_number` char(11) not null,
                         `role` enum('USER', 'ADMIN') not null,
                         `is_login` boolean not null,
                         `refresh_token` varchar(300),
                         `device_token` varchar(300),
                         CONSTRAINT USERS_PK PRIMARY KEY (`id`)
);

CREATE TABLE `socialInfos` (
                               `id` integer AUTO_INCREMENT,
                               `user_id` integer not null,
                               `social_id` varchar(300) not null,
                               `provider` enum('KAKAO', 'GOOGLE', 'APPLE', 'DEFAULT') not null,
                               CONSTRAINT SOCIALINFO_PK PRIMARY KEY (`id`),
                               CONSTRAINT SOCIALINFO_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `diaries` (
                           `id` integer AUTO_INCREMENT,
                           `user_id` integer ,
                           `title` varchar(500) not null,
                           `content` varchar(500) not null,
                           `created_at` timestamp not null,
                           `status` boolean not null,
                           `keywords` varchar(300),
                           `emotion_score` integer,
                           CONSTRAINT DIARYS_PK PRIMARY KEY (`id`),
                           CONSTRAINT DIARYS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `images` (
                          `id` integer AUTO_INCREMENT,
                          `user_id` integer,
                          `diary_id` integer,
                          `uuid_name` varchar(255) not null,
                          CONSTRAINT IMAGES_PK PRIMARY KEY (`id`),
                          CONSTRAINT IMAGES_CK UNIQUE(`user_id`, `diary_id`),
                          CONSTRAINT IMAGES_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                          CONSTRAINT IMAGES_DIARY_FK FOREIGN KEY (`diary_id`) REFERENCES `diaries` (`id`) ON DELETE CASCADE
);