package utility;

public class Topic {
    private String topicName;
    private int id_topic;
    static private int serial_id;
    static private boolean reachedMaxNumTopic = false;

    public Topic(String topicName) throws Exception {

        this.id_topic=getAndIncrementTopicId();
        if(topicName==null)
        {
            this.topicName="UNKNOWN_TOPIC";
        }
        else{
            this.topicName=topicName;
        }


    }

    public String getTopicName() {
        return topicName;
    }
    public int getId_topic(){return this.id_topic;}

    /**
     * Ritorna il nuovo serial id,se  reached max num topic-> false   incrementa di uno serial_id
     * @return topicid
     * @throws Exception se il numero massimo di topic è stato raggiunto
     */
    private synchronized int getAndIncrementTopicId() throws Exception
    {
        int id;
        if(reachedMaxNumTopic){
            throw new Exception("ERROR:MAX TOPIC NUMBER REACHED");
        }
        else
        {
            id=++serial_id;
            if (id==0)//overflow
            {
                reachedMaxNumTopic=true;
                throw new Exception("ERROR:MAX TOPIC NUMBER REACHED");
            }
            return id;
        }
    }
}
