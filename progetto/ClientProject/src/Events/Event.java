package Events;

/** Event è un Tipo generale di oggetto implementato dalle due classi astratte Window e ClientEvent
 *
 * Window è esteso dalle seguenti classi:
 *  -AccountLoginWindow
 *  -AnonymousLoginWindow
 *  -ForgotPasswordWindow
 *  -ForumWindow
 *  -NewAccountWindow
 * Queste classi sono un'astrazione di tutte le finestre che possono apparire nella GUI.
 * Permettono quindi l'esecuzione dei comandi richiesti aggiungendo un layer di astrazione e un interfaccia modulare( l'interfaccia grafica può infatti essere TerminalInterface oppure GUI)
 *
 * ClientEvent è esteso dalle seguenti classi:
 *   -Disconnect
 *   -GetAllTopics
 *   -GetTopics
 *   -Publish
 *   -ShutDown
 *   -Subscribe
 *   -UnSubscribe
 *   -NewTopicNotification
 *   Questi eventi non esistendo come finestre nell'interfaccia GUI non estendono Window ma ClientEvent
 */
public interface Event {
}
