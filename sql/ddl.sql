DROP TABLE IF EXISTS `diarys`;
DROP TABLE IF EXISTS `follows`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `id` integer AUTO_INCREMENT,
                         `social_id` varchar(100) NOT NULL,
                         `provider` enum('KAKAO', 'GOOGLE', 'DEFAULT') NOT NULL,
                         `name` varchar(20) NOT NULL,
                         `role` enum('USER', 'ADMIN') DEFAULT 'USER',
                         `created_date` timestamp NOT NULL,
                         `is_login` boolean NOT NULL,
                         `refresh_token` varchar(300),
                         CONSTRAINT USERS_PK PRIMARY KEY (`id`),
                         CONSTRAINT USERS_CK UNIQUE(`social_id`, `provider`)
);

CREATE TABLE `follows` (
                           `following_user_id` integer NOT NULL,
                           `followed_user_id` integer NOT NULL,
                           `created_at` timestamp NOT NULL,
                           CONSTRAINT FOLLOWS_PK PRIMARY KEY (`following_user_id`, `followed_user_id`),
                           CONSTRAINT FOLLOWS_FOLLOWING_FK FOREIGN KEY (`following_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                           CONSTRAINT FOLLOWS_FOLLOWED_FK FOREIGN KEY (`followed_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `diarys` (
                          `id` integer AUTO_INCREMENT,
                          `user_id` integer NOT NULL,
                          `art_name` varchar(300) NOT NULL,
                          `content` varchar(1000) NOT NULL,
                          `created_date` timestamp NOT NULL,
                          `keywords` varchar(200) NOT NULL,
                          CONSTRAINT DIARYS_PK PRIMARY KEY (`id`),
                          CONSTRAINT DIARYS_USER_FK FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

INSERT INTO users (`social_id`, `provider`, `name`, `created_date`, `is_login`) VALUES ("00000000", 'DEFAULT', "ADMIN", now(), true);
