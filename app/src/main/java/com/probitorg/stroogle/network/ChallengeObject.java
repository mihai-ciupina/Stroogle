package com.probitorg.stroogle.network;

/**
 * Represents a challenge object
 */
public class ChallengeObject
{
    private Integer id;
    private Integer userIdFrom;
    private Integer userIdTo;
    private String gameName;
    private String gameStringId;
    private Integer points;
    private String status;
    private Integer winnerId;
    private Integer isWinner;
    private Integer time;
    private Integer canvas;
    private Integer difficultyLevel;


    public Integer getId()
    {
        return id;
    }
    public Integer getUserIdFrom()
    {
        return userIdFrom;
    }
    public Integer getUserIdTo()
    {
        return userIdTo;
    }
    public String getGameName()
    {
        return gameName;
    }
    public String getGameStringId()
    {
        return gameStringId;
    }
    public Integer getPoints()
    {
        return points;
    }
    public String getStatus()
    {
        return status;
    }
    public Integer getWinnerId()
    {
        return winnerId;
    }
    public Integer getIsWinner()
    {
        return isWinner;
    }
    public Integer getTime()
    {
        return time;
    }
    public Integer getCanvas()
    {
        return canvas;
    }
    public Integer getDifficultyLevel()
    {
        return difficultyLevel;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    public void setUserIdFrom(Integer userIdFrom)
    {
        this.userIdFrom = userIdFrom;
    }
    public void setUserIdTo(Integer userIdTo)
    {
        this.userIdTo = userIdTo;
    }
    public void setName(String gameName)
    {
        this.gameName = gameName;
    }
    public void setGameStringId(String gameStringId)
    {
        this.gameStringId = gameStringId;
    }
    public void setPoints(Integer points)
    {
        this.points = points;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public void setWinnerId(Integer winnerId)
    {
        this.winnerId = winnerId;
    }
    public void setIsWinner(Integer isWinner)
    {
        this.isWinner = isWinner;
    }
    public void setTime(Integer time)
    {
        this.time = time;
    }
    public void setCanvas(Integer canvas)
    {
        this.canvas = canvas;
    }
    public void setDifficultyLevel(Integer difficultyLevel)
    {
        this.difficultyLevel=difficultyLevel;
    }









}
