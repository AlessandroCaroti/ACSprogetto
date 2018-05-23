package utility;

import java.util.Date;

public class Message
{
    final private String title;
    final private String author;
    final private String text;
    final private Topic topic;
    final private Date date;

    public Message(String title, String author, String text, Topic topic)
    {
        this.date = new Date();
        this.title = title;
        this.author = author;
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

    public Topic getTopic() { return  topic; }
}
