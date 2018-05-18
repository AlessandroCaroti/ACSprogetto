package sheredInterface;

import java.util.Date;

public class Message {
    private String title;
    private String author;
    private String text;
    private String topic;
    private Date date;

    public Message(String title, String author, String text, String topic)
    {
        this.date = new Date();
        this.title = title;
        this.author = author;
        this.text = text;
        this.topic = topic;
    }

    public Message(String title, String text, String topic)
    {
        this.date = new Date();
        this.title = title;
        this.author = "<Anonymous>";
        this.text = text;
        this.topic = topic;
    }

    public String getTitle()
    {
        return title;
    }

    public String getText(){
        return text;
    }

    public String getAuthor()
    {
        return author;
    }

    public Date getDate()
    {
        return date;
    }

    public String getTopic() { return  topic; }

}
