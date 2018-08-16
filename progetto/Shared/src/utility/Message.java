/*
    This file is part of ACSprogetto.

    ACSprogetto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ACSprogetto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.

*/
package utility;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable
{
    final private String title;
    final private String author;
    final private String text;
    final private String topic;
    final private Date date;

    public Message(String title, String author, String text, String topicName) throws IllegalArgumentException, NullPointerException {
        if(title==null||author==null||text==null||topicName==null)
            throw new NullPointerException("Null reference during the  message creation");
        if(author.isEmpty())
            throw new IllegalArgumentException("The author parameter can not be empty");
        if(topicName.isEmpty())
            throw new IllegalArgumentException("The topic parameter can not be empty");
        if(text.isEmpty())
            throw new IllegalArgumentException("The text parameter can not be empty");
        if(title.isEmpty())
            throw new IllegalArgumentException("The title parameter can not be empty");
        this.date = new Date();
        this.title = title;
        this.author = author;
        this.text = text;
        this.topic = topicName;
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

    @Override
    public String toString() {
        return  "From: "     + author          +
                " - Date: "  + date.toString() +
                " - Topic: " + topic           +"\n"+
                "Title: "    + title           +"\n"+
                text;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof Message))
            return false;
        Message anotherMessage = (Message)obj;
        return date.equals(anotherMessage.date) && author.equals(anotherMessage.author) && title.equals(anotherMessage.title) && topic.equals(anotherMessage.topic);
    }
}
