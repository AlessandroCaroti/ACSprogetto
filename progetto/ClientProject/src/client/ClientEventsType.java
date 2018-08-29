package client;

/**
 * Tutte le tipologie di evento che estendono la classe astratta ClientEvent
 */
public enum ClientEventsType {
    SHUTDOWN,
    DISCONNECT,
    GETALLTOPICS,
    GETTOPICS,
    SUBSCRIBE,
    UNSUBSCRIBE,
    PUBLISH,
    NEWTOPICNOTIFICATION
}
