/**
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

**/
package utility;

import java.security.NoSuchAlgorithmException;

public class Topic {
    final private String name_topic;
    final private StringHashed id_topic;

    public Topic(String topicName) throws NoSuchAlgorithmException {
        if(topicName==null)
        {
            name_topic="UNKNOWN_TOPIC";
        }
        else{
            name_topic=topicName;
        }
        id_topic = new StringHashed(topicName);
    }

    public String getTopicName() {
        return name_topic;
    }
    public byte[] getId_topic(){return id_topic.getStringHashed();}
}
