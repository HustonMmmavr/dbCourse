require 'pg'
require 'faker'
require './create_queries.rb'


class User
    attr_accessor :id, :nickname, :fullname, :email, :about

    def initialize(nick, full, email, about)
        @nickname = nick.to_s
        @fullname = full.to_s
        @email = email.to_s
        @about = String.new#[0].to_s
        about.each do |s|
          @about += s
        end
        p @about

    end

    def toArr()
      arr = Array.new
      arr.push(@about)
      arr.push(@fullname)
      arr.push(@email)
      arr.push(@nickname)
    end


    def toSql()
      str = "VALUES('" + @about + "','" + @fullname + "','" + @email + "','" + @nickname + "')"
      p str
      str
    end
end

class Forum
  attr_accessor :id, :owner_id, :title, :slug, :votes
  def initialize(owner_id, title, slug)
      @owner_id = owner_id
      @title = title
      @slug = slug
  end

  #attrs
  def toArr()
    arr = Array.new
    arr.push(@owner_id)
    arr.push(@title)
    arr.push(@slug)
    arr
  end
end

class Threadt
  attr_accessor :id, :author_id, :forum_id, :title, :created, :message, :votes, :slug
  def initialize(author_id, forum_id, title, created, message, votes, slug)
    @author_id = author_id
    @forum_id = forum_id
    @title = title
    @created = created
    @message = message
    @votes = votes
    @slug = slug
  end

  def toArr()
    arr = Array.new
    arr.push @author_id
    arr.push @forum_id
    arr.push @title
    arr.push @created
    arr.push @message
    arr.push @votes
    arr.push @slug
    arr
  end
end


class Post
  attr_accessor :id, :parent, :author_id, :created, :forum_id, :is_edited, :message, :thread_id
  def initialize(parent, author_id, created, forum_id, is_edited, message, thread_id)
    @parent=parent
    @author_id = author_id
    @created = created
    @forum_id = forum_id
    @is_edited = is_edited
    @message = message
    @thread_id = thread_id
  end

  def toArr
    arr = Array.new
    arr.push(@parent)
    arr.push(@author_id)
    arr.push @created
    arr.push @forum_id
    arr.push @is_edited
    arr.push @message
    arr.push @thread_id
    arr
  end
end

class Vote
  attr_accessor :owner_id, :thread_id, :vote
  def initialize(owner_id, thread_id, vote)
    @owner_id = owner_id
    @thread_id = thread_id
    @vote = vote
  end

  def toArr
    arr = Array.new
    arr.push(@owner_id)
    arr.push(@thread_id)
    arr.push(@vote)
    arr
  end
end

class MakeFake
   attr_accessor :con, :count_users, :count_forums, :count_threads, :count_posts, :count_votes

   def initialize(host, port, dbname, user_name, password, cnt_u, cnt_f, cnt_t, cnt_p, cnt_v)
       @con = con = PG.connect :host => host, :port => port, :user => user_name, :password => password
       @count_users = cnt_u
       @count_forums = cnt_f
       @count_threads = cnt_t
       @count_posts = cnt_p
       @count_votes = cnt_v
       @queries = CreateQueries.new
       create_db(host, port, user_name, password, dbname)
   end

    def create_db(host, port, uname, password, dbname)
        db_exist = con.query("SELECT 1 FROM pg_database WHERE datname = '" + dbname + "'")
        if db_exist.ntuples.zero?
            con.exec("create database " + dbname)
        end
        con.close
        @con = PG.connect :host => host, :port => port, :user => uname, :password => password, :dbname => dbname

        #citext_exist=@con.query("SELECT 1 FROM pg_available_extensions WHERE name='citext'")
        #``@if citext_exist.ntuples.zero?
        #  @con.exec(@queries.citext)
        #end

        tables=get_tables
        tables.each do |t|
            @con.exec(t)
        end
        create_prepared
    end

    def create_prepared
      @con.prepare('insert_user', "INSERT INTO userprofiles (about, email, fullname, nickname) VALUES($1, $2, $3, $4)")
      @con.prepare('insert_forum', "INSERT INTO forums(owner_id, title, slug) VALUES ($1, $2, $3)")
      @con.prepare('insert_thread', "INSERT INTO threads (author_id, forum_id, title, created, message, votes, slug)" +
                "VALUES($1, $2, $3, $4, $5, $6, $7)")
      @con.prepare('insert_post', "INSERT INTO posts (parent, author_id, created, forum_id, is_edited, message, thread_id)"+
       "VALUES($1, $2, $3, $4, $5, $6, $7)")
      @con.prepare('insert_vote', "INSERT INTO votes (owner_id, thread_id, vote) VALUES($1, $2, $3)")
    end

    def get_tables()
        tables=[@queries.user, @queries.forum, @queries.thread,
          @queries.post, @queries.vote]
    end

    def fill_data()
        fill_users
        fill_forums
        fill_threads
        fill_posts
        fill_votes
        @con.close
    end

    def fill_users()
        0.upto(@count_users) do |i|
            user = User.new(Faker::Internet.user_name + i.to_s, Faker::Internet.email, Faker::Name.name, Faker::Lorem.sentences(1))
            @con.exec_prepared('insert_user', user.toArr)
        end
    end

    def fill_forums()
      rnd = Random.new
      0.upto(@count_forums) do |i|
          forum = Forum.new(rnd.rand(1..@count_users), Faker::Vehicle.manufacture, Faker::Vehicle.vin) #=> "LLDWXZLG77VK2LUUF"
          @con.exec_prepared('insert_forum', forum.toArr)
      end
    end

    def fill_threads()
      rnd = Random.new
      0.upto(@count_threads) do |i|
          thread = Threadt.new(rnd.rand(1..@count_users), rnd.rand(1..@count_forums), Faker::Hobbit.character + i.to_s, Faker::Date.backward(14), Faker::Lorem.sentences(1), 0, Faker::Name.name)
          @con.exec_prepared('insert_thread', thread.toArr)
      end
    end

    def fill_posts()
      rnd = Random.new
      0.upto(@count_users) do |i|
          post = Post.new(rnd.rand(1..10), rnd.rand(1..@count_users), Faker::Date.backward(14), rnd.rand(1..@count_forums), false, Faker::Lorem.sentences(1), rnd.rand(1..@count_threads))
          @con.exec_prepared('insert_post', post.toArr)
      end
    end

    def fill_votes()
      rnd = Random.new
      0.upto(@count_users) do |i|
        vote = Vote.new(rnd.rand(1..@count_users), rnd.rand(1..count_threads), [-1, 1].shuffle.first)
          #user = User.new(Faker::Internet.user_name + i.to_s, Faker::Internet.email, Faker::Name.name, Faker::Lorem.sentences(1))
          @con.exec_prepared('insert_vote', vote.toArr)
      end
    end
end

host='localhost'
port='5432'
dbuser='comp'
password='951103'

db_name = 'test'


begin
    fake = MakeFake.new(host, port, db_name, dbuser, password, 1000, 100, 200, 300, 100)
    p fake
    fake.fill_data

rescue PG::Error => e
    puts e.error
ensure
#    fake.con.close
end
