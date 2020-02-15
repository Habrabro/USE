package com.yasdalteam.yasdalege.database;

import android.os.AsyncTask;

import androidx.room.Room;

import com.yasdalteam.yasdalege.App;
import com.yasdalteam.yasdalege.IDbOperationable;
import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.IResponseReceivable;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Subject;
import com.yasdalteam.yasdalege.Networking.SubjectsResponse;
import com.yasdalteam.yasdalege.DbUpdateManager;
import com.yasdalteam.yasdalege.Topic;
import com.yasdalteam.yasdalege.Networking.TopicResponse;
import com.yasdalteam.yasdalege.Update;
import com.yasdalteam.yasdalege.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbService
{
    private static DbService instance;
    private Db database;
    private Date lastUpdate;
    private DbUpdateManager dbUpdateManager;

    DbService()
    {
        database = Room
                .databaseBuilder(App.getInstance(), Db.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dbUpdateManager = new DbUpdateManager(database, tableOperationsMapInit());
    }

    public static DbService getInstance()
    {
        if (instance == null) {
            instance = new DbService();
        }
        return instance;
    }

    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
        App.getInstance().getUser().setLastUpdate(lastUpdate);
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                UserDao userDao = getDatabase().userDao();
                userDao.update(App.getInstance().getUser());
                return null;
            }
        }.execute();
    }

    public Db getDatabase() { return database; }

    public DbUpdateManager getUpdateManager() { return dbUpdateManager; }

    public void updateDb(DbRequestListener listener)
    {
        dbUpdateManager.updateDb(lastUpdate, listener);
    }

    public void insertOrUpdateUser(User user)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                UserDao userDao = database.userDao();
                User currentUser = userDao.getUserById(user.getId());
                if (currentUser == null)
                {
                    userDao.insert(user);
                }
                else
                {
                    userDao.update(user);
                }
                return null;
            }
        }.execute();
    }

    public void getUser(DbRequestListener<User> listener)
    {
        new AsyncTask<Void, Void, User>()
        {
            @Override
            protected User doInBackground(Void... voids)
            {
                UserDao userDao = getDatabase().userDao();
                User user = userDao.getUser();
                if (user == null)
                {
                    user = new User(new Date(0));
                    userDao.insert(user);
                }
                lastUpdate = user.getLastUpdate();
                return user;
            }

            @Override
            protected void onPostExecute(User user)
            {
                super.onPostExecute(user);
                listener.onRequestCompleted(user);
            }
        }.execute();
    }

    public void getSubjects(DbRequestListener listener)
    {
        new AsyncTask<Void, Void, List<Subject>>()
        {
            @Override
            protected List<Subject> doInBackground(Void... voids)
            {
                SubjectDao subjectDao = DbService.getInstance().getDatabase().subjectsDao();
                List<Subject> _subjects = subjectDao.getAll();
                return _subjects;
            }

            @Override
            protected void onPostExecute(List<Subject> _subjects)
            {
                super.onPostExecute(_subjects);
                listener.onRequestCompleted(_subjects);
            }
        }.execute();
    }

    public void getTopics(long subjectId, DbRequestListener listener)
    {
        new AsyncTask<Void, Void, List<Topic>>()
        {
            @Override
            protected List<Topic> doInBackground(Void... voids)
            {
                TopicDao topicDao = DbService.getInstance().getDatabase().topicDao();
                List<Topic> _topics = topicDao.getTopics(subjectId);
                return _topics;
            }

            @Override
            protected void onPostExecute(List<Topic> _topics)
            {
                super.onPostExecute(_topics);
                listener.onRequestCompleted(_topics);
            }
        }.execute();
    }

    public void getTopic(long topicId, DbRequestListener listener)
    {
        new AsyncTask<Void, Void, Topic>()
        {
            @Override
            protected Topic doInBackground(Void... voids)
            {
                TopicDao topicDao = DbService.getInstance().getDatabase().topicDao();
                Topic _topic = topicDao.getTopic(topicId);
                return _topic;
            }

            @Override
            protected void onPostExecute(Topic _topic)
            {
                super.onPostExecute(_topic);
                listener.onRequestCompleted(_topic);
            }
        }.execute();
    }

    HashMap<String, IDbOperationable> tableOperationsMapInit()
    {
        HashMap<String, IDbOperationable> tableOperationsMap = new HashMap<>();

        tableOperationsMap.put("subjects", new IDbOperationable()
        {
            @Override
            public void insertOrUpdate(Update update, DbRequestListener listener)
            {
                NetworkService.getInstance(new IResponseReceivable()
                {
                    @Override
                    public void onResponse(BaseResponse response)
                    {
                        Subject serverSubject = ((SubjectsResponse)response).getData().get(0);
                        new AsyncTask<Subject, Void, Void>()
                        {
                            @Override
                            protected Void doInBackground(Subject... subjects)
                            {
                                Subject serverSubject = subjects[0];
                                Subject subject = new Subject(
                                        serverSubject.getId(),
                                        serverSubject.getName(),
                                        serverSubject.getImg(),
                                        serverSubject.isActive(),
                                        serverSubject.hasDirectoryTopics());
                                SubjectDao subjectDao = database.subjectsDao();
                                Subject localSubject = subjectDao.getSubject(serverSubject.getId());
                                if (localSubject == null)
                                {
                                    subjectDao.insert(subject);
                                }
                                else
                                {
                                    subjectDao.update(subject);
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid)
                            {
                                super.onPostExecute(aVoid);
                                listener.onRequestCompleted(update.getId());
                            }
                        }.execute(serverSubject);
                    }
                    @Override public void onFailure(Throwable t) { }
                    @Override public void onError(String error) { }
                    @Override public void onDisconnected() { }
                }).getSubjects(update.getRowId());
            }

            public void delete(Update update, DbRequestListener listener)
            {
                new AsyncTask<Subject, Void, Void>()
                {
                    @Override
                    protected Void doInBackground(Subject... subjects)
                    {
                        SubjectDao subjectDao = database.subjectsDao();
                        subjectDao.delete(update.getRowId());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid)
                    {
                        super.onPostExecute(aVoid);
                        listener.onRequestCompleted(update.getId());
                    }
                }.execute();
            }
        });

        tableOperationsMap.put("topics", new IDbOperationable()
        {
            @Override
            public void insertOrUpdate(Update update, DbRequestListener listener)
            {
                long id = update.getRowId();
                NetworkService.getInstance(new IResponseReceivable()
                {
                    @Override
                    public void onResponse(BaseResponse response)
                    {
                        Topic serverTopic = ((TopicResponse)response).getData().get(0);
                        new AsyncTask<Topic, Void, Void>()
                        {
                            @Override
                            protected Void doInBackground(Topic... topics)
                            {
                                Topic serverTopic = topics[0];
                                Topic topic = new Topic(
                                        serverTopic.getId(),
                                        serverTopic.getNumber(),
                                        serverTopic.getTitle(),
                                        serverTopic.getSubjectId());
                                TopicDao topicDao = database.topicDao();
                                Topic localTopic = topicDao.getTopic(serverTopic.getId());
                                if (localTopic == null)
                                {
                                    topicDao.insert(topic);
                                }
                                else
                                {
                                    topicDao.update(topic);
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid)
                            {
                                super.onPostExecute(aVoid);
                                listener.onRequestCompleted(update.getId());
                            }
                        }.execute(serverTopic);
                    }
                    @Override public void onFailure(Throwable t) { }
                    @Override public void onError(String error) { }
                    @Override public void onDisconnected() { }
                }).getTopics(update.getRowId(), null);
            }

            public void delete(Update update, DbRequestListener listener)
            {
                new AsyncTask<Topic, Void, Void>()
                {
                    @Override
                    protected Void doInBackground(Topic... topics)
                    {
                        TopicDao topicDao = database.topicDao();
                        topicDao.delete(update.getRowId());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid)
                    {
                        super.onPostExecute(aVoid);
                        listener.onRequestCompleted(update.getId());
                    }
                }.execute();
            }
        });

        return tableOperationsMap;
    }
}