USE `stock_exchange`;

CREATE TABLE IF NOT EXISTS `company`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) DEFAULT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `user`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `surname` varchar(45) NOT NULL,
    `username` varchar(45) NOT NULL,
    `password` varchar(120) NOT NULL,
    `role` varchar(45) NOT NULL,
    `money` float(45) NOT NULL,
    `email` varchar(45) NOT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `stock`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `company_id` int(11) NOT NULL,
    `user_id` int(11) DEFAULT NULL,
    `amount` int(11) NOT NULL,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`company_id`) REFERENCES `company`(`id`),
    FOREIGN KEY(`user_id`) REFERENCES `user`(`id`)
);

CREATE TABLE IF NOT EXISTS `buy_offer`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `company_id` int(11) NOT NULL,
    `user_id` int(11) NOT NULL,
    `max_price` float(45) NOT NULL,
    `start_amount` int(11) NOT NULL,
    `amount` int(11) NOT NULL,
    `date_limit` date NOT NULL,
    `actual` boolean DEFAULT true,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`company_id`) REFERENCES `company`(`id`),
    FOREIGN KEY(`user_id`) REFERENCES `user`(`id`)
);

CREATE TABLE IF NOT EXISTS `sell_offer`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `stock_id` int(11) NOT NULL,
    `min_price` float(45) NOT NULL,
    `start_amount` int(11) NOT NULL,
    `amount` int(11) NOT NULL,
    `date_limit` date NOT NULL,
    `actual` boolean DEFAULT true,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`stock_id`) REFERENCES `stock`(`id`)
);

CREATE TABLE IF NOT EXISTS `transaction`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `buy_offer_id` int(11) NOT NULL,
    `sell_offer_id` int(11) NOT NULL,
    `amount` int(11) NOT NULL,
    `price` float(45) NOT NULL,
    `transaction_date` date NOT NULL,
    PRIMARY KEY(`id`),
    FOREIGN KEY(`buy_offer_id`) REFERENCES `buy_offer`(`id`),
    FOREIGN KEY(`sell_offer_id`) REFERENCES `sell_offer`(`id`)
);

CREATE TABLE IF NOT EXISTS `stock_rate`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `company_id` int(11) NOT NULL,
    `rate` float(45) NOT NULL,
    `date_inc` date NOT NULL,
    `actual` boolean DEFAULT TRUE,
    PRIMARY KEY(`id`)
);

CREATE TABLE IF NOT EXISTS `test`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `database_time` bigint(45) NOT NULL,
    `application_time` bigint(45) NOT NULL,
    `semaphore_wait_time` bigint(45) NOT NULL,
    PRIMARY KEY(`id`)
);

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE stock_rate;
TRUNCATE user;
TRUNCATE transaction;
TRUNCATE stock;
TRUNCATE sell_offer;
TRUNCATE buy_offer;
TRUNCATE company;
SET FOREIGN_KEY_CHECKS = 1;