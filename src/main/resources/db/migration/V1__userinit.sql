CREATE EXTENSION IF NOT EXISTS CITEXT;

-- SET SYNCHRONOUS_COMMIT = 'off';

CREATE TABLE IF NOT EXISTS userprofiles (
  id       SERIAL PRIMARY KEY,
  about    TEXT DEFAULT NULL,
  email    CITEXT UNIQUE,
  fullname TEXT DEFAULT NULL,
  nickname CITEXT COLLATE ucs_basic UNIQUE
);

CREATE TABLE IF NOT EXISTS forums (
  id      SERIAL PRIMARY KEY,
  owner_id INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE NOT NULL,
  owner_name CITEXT,
  title   TEXT NOT NULL,
  slug    CITEXT UNIQUE                                   NOT NULL,
  posts   INTEGER DEFAULT 0,
  threads INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS threads (
  id SERIAL PRIMARY KEY,
  author_id  INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE  NOT NULL,
  author_name CITEXT,
  forum_id INTEGER REFERENCES forums (id) ON DELETE CASCADE NOT NULL,
  forum_slug CITEXT,
  title    TEXT  NOT NULL,
  created  TIMESTAMPTZ DEFAULT NOW(),
  message  TEXT        DEFAULT NULL,
  votes    INTEGER     DEFAULT 0,
  slug     CITEXT UNIQUE
);


CREATE TABLE IF NOT EXISTS posts (
  id SERIAL PRIMARY KEY,
  parent_id    INTEGER     DEFAULT 0,
  author_id   INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE   NOT NULL,
  author_name CITEXT,
  created   TIMESTAMPTZ DEFAULT NOW(),
  forum_id  INTEGER REFERENCES forums (id) ON DELETE CASCADE  NOT NULL,
  forum_slug CITEXT,
  is_edited BOOLEAN     DEFAULT FALSE,
  message   TEXT        DEFAULT NULL,
  thread_id INTEGER REFERENCES threads (id) ON DELETE CASCADE NOT NULL,
  id_of_root INTEGER,
  path_to_post INTEGER []
);

CREATE TABLE IF NOT EXISTS forums_and_users (
  user_id INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE NOT NULL,
  forum_id INTEGER REFERENCES forums (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE IF NOT EXISTS votes (
  owner_id INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE,
  thread_id  INTEGER REFERENCES threads (id) ON DELETE CASCADE,
  vote INTEGER DEFAULT 0
--   CONSTRAINT one_owner_thread_pair UNIQUE (owner_id, thread_id)
);

--------------------- TRIGGER FOR UPDATE forums ------------------------
CREATE OR REPLACE FUNCTION insert_forum_func() RETURNS TRIGGER AS
$insert_forums_trigger$
  BEGIN
    UPDATE forums SET owner_name = (SELECT nickname FROM userprofiles WHERE id = NEW.owner_id)
      WHERE id = NEW.id;
    RETURN NULL;
  END;
$insert_forums_trigger$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS insert_forums_trigger ON forums;
CREATE TRIGGER insert_forums_trigger AFTER INSERT ON forums
  FOR EACH ROW EXECUTE PROCEDURE insert_forum_func();
---------------------------------------------------------------------



------------------- TRIGGER FOR UPDATE threads ---------------
CREATE OR REPLACE FUNCTION insert_threads_func() RETURNS TRIGGER AS
$insert_threads_trigger$
  BEGIN
      UPDATE threads SET author_name = (SELECT nickname FROM userprofiles WHERE id = NEW.author_id),
                      forum_slug = (SELECT slug FROM forums WHERE id = NEW.forum_id)
      WHERE id = NEW.id;
      UPDATE forums SET threads = threads + 1 WHERE id = NEW.forum_id;
       INSERT INTO forums_and_users(user_id, forum_id) VALUES(NEW.author_id, NEW.forum_id);
    RETURN NULL;
  END;
$insert_threads_trigger$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS insert_threads_trigger ON threads;
CREATE TRIGGER insert_threads_trigger AFTER INSERT ON threads
  FOR EACH ROW EXECUTE PROCEDURE insert_threads_func();

-------------------------------------------------------------------


------------------- TRIGGER FOR UPDATE posts ------------------------

CREATE OR REPLACE FUNCTION insert_posts_func() RETURNS TRIGGER AS
$insert_posts_trigger$
  BEGIN
      UPDATE posts SET author_name = (SELECT nickname FROM userprofiles WHERE id = NEW.author_id),
                      forum_slug = (SELECT slug FROM forums WHERE id = NEW.forum_id)
      WHERE id = NEW.id;
      INSERT INTO forums_and_users(user_id, forum_id) VALUES(NEW.author_id, NEW.forum_id);
    RETURN NULL;
  END;
$insert_posts_trigger$ LANGUAGE plpgsql;
--
DROP TRIGGER IF EXISTS insert_posts_trigger ON posts;
CREATE TRIGGER insert_posts_trigger AFTER INSERT ON posts
  FOR EACH ROW EXECUTE PROCEDURE insert_posts_func();

-- -------------------------------------------------------------------
-- CREATE OR REPLACE FUNCTION create_or_update_vote(u_id integer, t_id integer, v integer)
--   RETURNS VOID as '
--   DECLARE
--     flag integer;
--   BEGIN
--     select 1 from tb2 where a = u_id and b = t_id into flag;
--     IF flag = 1 THEN
--       UPDATE tb2 SET c = v WHERE a = u_id and b = t_id;
--     ELSE
--       INSERT into tb2(a, b, c) VALUES(u_id, t_id, v);
--     END IF;
--   END;'
-- LANGUAGE plpgsql;

---------------------- vote -----------------------------------------
CREATE OR REPLACE FUNCTION create_or_update_vote(u_id integer, t_id integer, v integer)
  RETURNS INTEGER as '
  DECLARE
    flag integer;
  BEGIN
    select 1 from votes where owner_id = u_id and thread_id = t_id into flag;
    IF flag = 1 THEN
      UPDATE votes SET vote = v WHERE owner_id = u_id and thread_id = t_id;
    ELSE
      INSERT into votes(owner_id, thread_id, vote) VALUES(u_id, t_id, v);
    END IF;
    UPDATE threads set votes = (SELECT SUM(vote) FROM votes WHERE thread_id = t_id);
    RETURN 1;
   END;'
LANGUAGE plpgsql;
-- CREATE PROCEDURE fill_created_forum();
--
-- CREATE PROCEDURE fill_r
--
-- CREATE PROCEDURE fill_inserted_post();
--
-- CREATE TRIGGER


