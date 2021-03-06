class CreateQueries
  attr_accessor :citext, :user, :forum, :thread, :post, :vote

  def initialize
    @citext = "CREATE EXTENSION citext"
    @user = "CREATE TABLE IF NOT EXISTS userprofiles (
      id       SERIAL PRIMARY KEY,
      about    TEXT DEFAULT NULL,
      email    CITEXT UNIQUE,
      fullname TEXT DEFAULT NULL,
      nickname CITEXT UNIQUE
    )"

    @forum="CREATE TABLE IF NOT EXISTS forums (
      id      SERIAL PRIMARY KEY,
      owner_id INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE NOT NULL,
      title   TEXT NOT NULL,
      slug    CITEXT UNIQUE                                   NOT NULL,
      posts   INTEGER DEFAULT 0,
      threads INTEGER DEFAULT 0
    )"

    @thread="CREATE TABLE IF NOT EXISTS threads (
      id SERIAL PRIMARY KEY,
      author_id  INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE  NOT NULL,
      forum_id INTEGER REFERENCES forums (id) ON DELETE CASCADE NOT NULL,
      title    TEXT  NOT NULL,
      created  TIMESTAMPTZ DEFAULT NOW(),
      message  TEXT        DEFAULT NULL,
      votes    INTEGER     DEFAULT 0,
      slug     CITEXT UNIQUE
    )"

    @post="CREATE TABLE IF NOT EXISTS posts (
      id SERIAL PRIMARY KEY,
      parent    INTEGER     DEFAULT 0,
      author_id   INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE   NOT NULL,
      created   TIMESTAMPTZ DEFAULT NOW(),
      forum_id  INTEGER REFERENCES forums (id) ON DELETE CASCADE  NOT NULL,
      is_edited BOOLEAN     DEFAULT FALSE,
      message   TEXT        DEFAULT NULL,
      thread_id INTEGER REFERENCES threads (id) ON DELETE CASCADE NOT NULL
    )"

    @vote="CREATE TABLE IF NOT EXISTS votes (
      owner_id INTEGER REFERENCES userprofiles (id) ON DELETE CASCADE,
      thread_id  INTEGER REFERENCES threads (id) ON DELETE CASCADE,
      vote INTEGER DEFAULT 0,
      CONSTRAINT one_owner_thread_pair UNIQUE (owner_id, thread_id)
    )"
  end
end
