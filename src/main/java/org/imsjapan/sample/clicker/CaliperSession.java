package org.imsjapan.sample.clicker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imsglobal.caliper.Client;
import org.imsglobal.caliper.Options;
import org.imsglobal.caliper.Sensor;
import org.imsglobal.caliper.actions.Action;
import org.imsglobal.caliper.entities.agent.Person;
import org.imsglobal.caliper.entities.agent.SoftwareApplication;
import org.imsglobal.caliper.entities.media.MediaLocation;
import org.imsglobal.caliper.entities.session.Session;
import org.imsglobal.caliper.events.SessionEvent;
import org.imsglobal.lti.launch.LtiUser;
import org.joda.time.DateTime;

public class CaliperSession {

    private static final Logger logger = LogManager.getLogger(CaliperSession.class);

    private static final String ENDPOINT = "http://localhost:9966/key/caliper"; // 送信先URL

    private static final String API_KEY = "apikey";  // APIキー

    private static final String SENSOR_ID = "sensorId";

    private static final String CLIENT_ID = "clientId";

    private static final String APP_ID = "http://localhost:8080";

    private static final String APP_NAME = "Clicker Java";

    private static Sensor<String> sensor;

    static {
        sensor = new Sensor<String>(SENSOR_ID);
        Options options = new Options();
        options.setHost(ENDPOINT);
        options.setApiKey(API_KEY);
        sensor.registerClient(CLIENT_ID, new Client(CLIENT_ID, options));
    }

    private static SoftwareApplication app;

    static {
        app = SoftwareApplication.builder()
                .id(APP_ID)
                .name(APP_NAME)
                .build();
    }

    /**
     * ログインのSessionEventを送信する。
     * 送信成功時にtrue、失敗時にfalseを返す。
     *
     * @return boolean
     */
    public static boolean sendSessionLoggedIn(LtiUser user, String sessionId, DateTime loginTime) {
        final DateTime now = DateTime.now();

        // 操作しているユーザの情報をセット
        // id: URL + ユーザID
        // name: ユーザID
        final Person actor = Person.builder()
                .id(APP_ID + "/users/" + user.getId())
                .name(user.getId())
                .build();

        // セッションの情報をセット
        // id: URL + セッションID
        final Session session = Session.builder()
                .id(APP_ID + "/sessions/" + sessionId)
                .actor(actor)
                .dateCreated(loginTime)
                .dateModified(loginTime)
                .startedAtTime(loginTime)
                .build();

        // ページの情報をセット
        // Java版のAPIでは指定が必須
        final MediaLocation location = MediaLocation.builder()
                .id(APP_ID + "/launch")
                .name("Launch Page")
                .build();

        // Caliper に送信するイベントオブジェクトを作成
        final SessionEvent sessionEvent = SessionEvent.builder()
                .actor(actor)
                .action(Action.LOGGED_IN.getValue())
                .object(app)
                .generated(session)
                .target(location)
                .eventTime(now)
                .build();

        try {
            sensor.send(sensor, sessionEvent);
            return true;
        } catch (Error e) {
            logger.error(e.getStackTrace().toString());
            return false;
        }
    }

}
