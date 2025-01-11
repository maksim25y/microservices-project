-- Ready
CREATE TABLE classes
(
    id          BIGSERIAL PRIMARY KEY,
    letter      VARCHAR(10),
    number      INTEGER,
    description VARCHAR
);
CREATE TABLE parents
(
    id              BIGSERIAL PRIMARY KEY,
    firstname       VARCHAR NOT NULL,
    lastname        VARCHAR NOT NULL,
    email           VARCHAR NOT NULL,
    patronymic      VARCHAR NOT NULL,
    hashed_password VARCHAR NOT NULL
);
-- Ready
CREATE TABLE students
(
    id              BIGSERIAL PRIMARY KEY,
    firstname       VARCHAR(255),
    lastname        VARCHAR(255),
    patronymic      VARCHAR(255),
    email           varchar(255),
    hashed_password VARCHAR NOT NULL,
    class_id        BIGINT,
    parent_id       BIGINT,
    FOREIGN KEY (class_id) REFERENCES classes (id) ON DELETE SET NULL,
    FOREIGN KEY (parent_id) REFERENCES parents (id) ON DELETE SET NULL
);
CREATE TABLE teachers
(
    id              BIGSERIAL PRIMARY KEY,
    firstname       VARCHAR(255) NOT NULL,
    lastname        VARCHAR(255) NOT NULL,
    patronymic      VARCHAR(255) NOT NULL,
    email           VARCHAR      NOT NULL,
    hashed_password VARCHAR      NOT NULL
);
-- Ready
CREATE TABLE subjects
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255),
    type        VARCHAR(50),
    code        VARCHAR(50) UNIQUE,
    description VARCHAR(255),
    class_id    BIGINT,
    teacher_id  BIGINT,
    FOREIGN KEY (class_id)  REFERENCES classes (id) ON DELETE SET NULL ,
    FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON DELETE SET NULL
);

CREATE TABLE schedules
(
    id                  BIGSERIAL PRIMARY KEY,
    day_of_week         INTEGER,
    start_time          TIME,
    number_of_classroom INTEGER,
    class_id            BIGINT,
    subject_id          BIGINT,
    FOREIGN KEY (class_id) REFERENCES classes (id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);

CREATE TABLE homeworks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR,
    deadline    DATE,
    class_id    BIGINT,
    subject_id  BIGINT,
    FOREIGN KEY (class_id) REFERENCES classes (id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);

CREATE TABLE grades
(
    id           BIGSERIAL PRIMARY KEY,
    mark         INTEGER,
    date_of_mark DATE,
    comment      VARCHAR,
    student_id   BIGINT DEFAULT NULL,
    subject_id   BIGINT DEFAULT NULL,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);

CREATE TABLE app_users
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT,
    role_name VARCHAR NOT NULL,
    email     VARCHAR NOT NULL
);
CREATE TABLE admins
(
    id              BIGSERIAL PRIMARY KEY,
    firstname       VARCHAR NOT NULL,
    lastname        VARCHAR NOT NULL,
    patronymic      VARCHAR NOT NULL,
    email           VARCHAR NOT NULL,
    hashed_password VARCHAR NOT NULL
);