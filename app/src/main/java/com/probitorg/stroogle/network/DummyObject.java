package com.probitorg.stroogle.network;

/**
 * Represents a dummy object
 */
public class DummyObject
{

    //json= {"id":"7","name":"d","username":"d","email":"d","password":"d","date_created":"2016-07-08 16:39:02","date_updated":null,"points":"0","skills":"0","status":"alien"}

    private Integer id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String dateCreated;
    private String dateUpdated;
    private Integer points;
    private Integer skills;

    private String status;

    public Integer getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getUsername()
    {
        return username;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPassword()
    {
        return password;
    }
    public String getDateCreated()
    {
        return dateCreated;
    }
    public String getDateUpdated()
    {
        return dateUpdated;
    }
    public Integer getPoints()
    {
        return points;
    }
    public Integer getSkills()
    {
        return skills;
    }

    public String getStatus()
    {
        return status;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    public void setDateUpdated(String dateUpdated)
    {
        this.dateUpdated = dateUpdated;
    }
    public void setPoints(Integer points)
    {
        this.points = points;
    }
    public void setSkills(Integer skills)
    {
        this.skills = skills;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
