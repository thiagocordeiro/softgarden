CREATE TABLE IF NOT EXISTS optimal_meeting_computations
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `created_at` datetime(6)                    NOT NULL
);

CREATE TABLE IF NOT EXISTS participant_availabilities
(
    `compute_id`   INT          NOT NULL,
    `participant`  varchar(255) NOT NULL,
    `available_at` datetime(6)  NOT NULL,
    `created_at`   datetime(6)  NOT NULL,

    index `compute_id_index` (`compute_id`),
    index `participant_availability_index` (`participant`, `available_at`)
);

CREATE TABLE IF NOT EXISTS meetings
(
    `compute_id`    INT PRIMARY KEY NOT NULL,
    `participants`  varchar(2048)   NOT NULL,
    `optimal_range` varchar(255)    NOT NULL,

    index `compute_id_index` (`compute_id`)
);
