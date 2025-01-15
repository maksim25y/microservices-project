-- changeset maksim25y:1
CREATE TABLE IF NOT EXISTS app_users
(
    id          BIGSERIAL PRIMARY KEY,
    firstname text,
    lastname text,
    email text
);
-- changeset maksim25y:2
CREATE TABLE IF NOT EXISTS  posts
(
    id              BIGSERIAL PRIMARY KEY,
    title text,
    image_url text,
    description text,
    user_id BIGINT REFERENCES app_users(id) ON DELETE CASCADE
);
-- changeset maksim25y:3
CREATE TABLE IF NOT EXISTS  comments
(
    id              BIGSERIAL PRIMARY KEY,
    description text,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE
);
-- changeset maksim25y:5
CREATE TABLE IF NOT EXISTS likes
(
    id              BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES app_users(id) ON DELETE CASCADE,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE
);