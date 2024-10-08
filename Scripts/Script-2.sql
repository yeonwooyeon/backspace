USE vacancy;
SHOW TABLES;
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    -- 기타 필요한 필드를 추가
);



select * 
from board;

ALTER TABLE user ADD COLUMN received_like_cnt INT DEFAULT 0;
ALTER TABLE user ADD COLUMN user_email VARCHAR(255) NOT NULL ;
DESCRIBE users;

ALTER TABLE user ADD COLUMN user_role VARCHAR(50);

DESCRIBE users;

CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    body TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
ALTER TABLE board
ADD COLUMN category VARCHAR(255) NOT NULL DEFAULT 'default_category';
