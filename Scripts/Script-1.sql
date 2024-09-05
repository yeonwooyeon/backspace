CREATE TABLE `Users` (
    `mem_no` INT(20) NOT NULL AUTO_INCREMENT,
    `mem_id` VARCHAR(255) NOT NULL,
    `mem_pw` VARCHAR(100) NOT NULL,
    `mem_name` VARCHAR(20) NOT NULL,
    `mem_email` VARCHAR(255) NULL,
    `mem_phone` VARCHAR(11) NOT NULL, -- Changed to VARCHAR to accommodate phone numbers with country codes or special characters
    `mem_sign` TIMESTAMP NOT NULL,
    `us_date` TIMESTAMP NULL,
    `up_date` TIMESTAMP NOT NULL,
    `mem_addr` VARCHAR(200) NOT NULL,
    `mem_animal` BOOLEAN NULL,
    `pw_question` VARCHAR(100) NOT NULL,
    `pw_answer` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`mem_no`)
);

CREATE TABLE `Admin` (
    `admin_key` INT(20) NOT NULL AUTO_INCREMENT,
    `admin_id` VARCHAR(10) NOT NULL,
    `admin_pass` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`admin_key`)
);

CREATE TABLE `Wish` (
    `info_no` INT(11) NOT NULL,
    `mem_no` INT(20) NOT NULL,
    `agent_no` VARCHAR(20) NOT NULL,
    `wish_date` TIMESTAMP NULL,
    `wish_status` VARCHAR(10) NULL,
    `wish_regi` VARCHAR(15) NULL,
    `wish_modi` VARCHAR(15) NULL,
    `wish_day` TIMESTAMP NULL,
    `wish_modiday` TIMESTAMP NULL,
    PRIMARY KEY (`info_no`)
);

CREATE TABLE `Notice` (
    `no_no` INT NOT NULL AUTO_INCREMENT,
    `admin_key2` INT NOT NULL,
    `no_title` VARCHAR(200) NOT NULL,
    `no_writer` VARCHAR(15) NOT NULL,
    `no_img` BOOLEAN NULL,
    `no_hit` INT NULL,
    `no_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `no_att` INT NULL,
    `no_post` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`no_no`)
);

CREATE TABLE `Pp_option` (
    `info_no` INT(11) NOT NULL,
    `option_op` ENUM('세탁기','에어컨','인덕션','가스렌지', 'TV', '침대') NULL,
    `option_money` INT NULL,
    `option_cost` INT NOT NULL,
    `option_etc` ENUM('분리형','복층','옥탑','신축', '주차', '엘리베이터', '반려동물','전세대출') NULL,
    PRIMARY KEY (`info_no`)
);

CREATE TABLE `Pp_info` (
    `info_no` INT(11) NOT NULL AUTO_INCREMENT,
    `mem_no` INT(20) NOT NULL,
    `info_name` VARCHAR(100) NOT NULL,
    `info_add` VARCHAR(500) NOT NULL,
    `info_size` FLOAT NOT NULL,
    `info_allfl` INT(11) NOT NULL,
    `info_fl` INT(11) NOT NULL,
    `info_count` INT(11) NOT NULL,
    `info_upload` TIMESTAMP NOT NULL,
    `info_update` TIMESTAMP NOT NULL,
    `info_delflag` BOOLEAN NOT NULL,
    `info_piry` TIMESTAMP NULL,
    `info_option` ENUM('주택','오피스텔','사무실','상가') NOT NULL,
    `info_type` ENUM('매매','전세','월세') NOT NULL,
    `info_sell` INT NOT NULL,
    `info_year` INT NOT NULL,
    `info_month` INT NOT NULL,
    `info_deposit` INT NULL,
    `info_hits` INT NOT NULL,
    `info_comp` DATE NOT NULL,
    `info_ok` TIMESTAMP NOT NULL,
    `info_move` DATE NOT NULL,
    PRIMARY KEY (`info_no`)
);

CREATE TABLE `Pp_img` (
    `img_no` INT(11) NOT NULL AUTO_INCREMENT,
    `info_no` INT(11) NOT NULL,
    `si_img` BOOLEAN NOT NULL,
    `si_name` VARCHAR(100) NOT NULL,
    `si_create` TIMESTAMP NOT NULL,
    `si_update` TIMESTAMP NOT NULL,
    `si_deleted` TIMESTAMP NOT NULL,
    PRIMARY KEY (`img_no`)
);

CREATE TABLE `Question` (
    `q_no` INT(10) NOT NULL AUTO_INCREMENT,
    `mem_no` INT(20) NOT NULL,
    `q_type` VARCHAR(15) NOT NULL,
    `q_title` VARCHAR(200) NULL,
    `q_ques` VARCHAR(255) NULL,
    `q_answ` VARCHAR(255) NULL,
    `q_date` TIMESTAMP NOT NULL,
    `q_update` TIMESTAMP NOT NULL,
    PRIMARY KEY (`q_no`)
);

CREATE TABLE `Agent` (
    `agent_no` VARCHAR(20) NOT NULL,
    `agent_id` VARCHAR(20) NOT NULL,
    `agent_pass` VARCHAR(20) NOT NULL,
    `agent_name` VARCHAR(50) NOT NULL,
    `agent_com` VARCHAR(50) NOT NULL,
    `agent_content` VARCHAR(50) NULL,
    `agent_phonenumber` VARCHAR(13) NOT NULL,
    `agent_YN` BOOLEAN NULL,
    PRIMARY KEY (`agent_no`)
);

-- Add foreign key constraints
ALTER TABLE `Wish` ADD CONSTRAINT `FK_Pp_info_TO_Wish_1` FOREIGN KEY (`info_no`) REFERENCES `Pp_info` (`info_no`);
ALTER TABLE `Notice` ADD CONSTRAINT `FK_Admin_TO_Notice_1` FOREIGN KEY (`admin_key2`) REFERENCES `Admin` (`admin_key`);
ALTER TABLE `Pp_option` ADD CONSTRAINT `FK_Pp_info_TO_Pp_option_1` FOREIGN KEY (`info_no`) REFERENCES `Pp_info` (`info_no`);
ALTER TABLE `Pp_img` ADD CONSTRAINT `FK_Pp_info_TO_Pp_img_1` FOREIGN KEY (`info_no`) REFERENCES `Pp_info` (`info_no`);