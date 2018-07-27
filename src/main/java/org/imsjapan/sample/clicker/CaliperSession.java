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
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;

public class CaliperSession {

    private static final Logger logger = LogManager.getLogger(CaliperSession.class);

    private static String ENDPOINT = "http://localhost:9966/key/caliper"; // 送信先URL

    private static String API_KEY = "apikey";  // APIキー

    private static String SENSOR_ID = "sensorId";

    private static String CLIENT_ID = "clientId";

    private static String APP_ID = "https://example.com/app/123456789";

    private static String APP_NAME = "Clicker Java";

    private static Sensor<String> sensor;

    static {
        sensor = new Sensor<String>(SENSOR_ID);
        Options options = new Options();
        options.setHost(ENDPOINT);
        options.setApiKey(API_KEY);
        sensor.registerClient(CLIENT_ID, new Client(CLIENT_ID, options));
    };

    private static SoftwareApplication app;

    static {
        app = SoftwareApplication.builder()
                .id(APP_ID)
                .name(APP_NAME)
                .build();
    };

    /**
     * ログインのSessionEventを送信する。
     * 送信成功時にtrue、失敗時にfalseを返す。
     *
     * @return boolean
     */
    public static boolean sendSessionLoggedIn(String userName, DateTime loginTime) {
        DateTime now = DateTime.now();

        Person actor = Person.builder()
                .id("https://example.com/persons/" + userName)
                .name(userName)
                .build();

        Session session = Session.builder()
                .id("https://example.com/sessions/12345")
                .actor(actor)
                .dateCreated(loginTime)
                .dateModified(loginTime)
                .startedAtTime(loginTime)
                .build();

        // Java版のAPIでは指定が必須
        MediaLocation location = MediaLocation.builder()
                .id("https://example.com/page/default")
                .name("Default Page")
                .build();

        SessionEvent sessionEvent = SessionEvent.builder()
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

    /**
     * ログアウトのSessionEventを送信する。
     * 送信成功時にtrue、失敗時にfalseを返す。
     *
     * @return boolean
     */
    public static boolean sendSessionLoggedOut(String userName, DateTime loginTime) {
        DateTime now = DateTime.now();
        String duration = ISOPeriodFormat.standard().print(Seconds.secondsBetween(loginTime, now));

        // BEGIN: 送信用イベントの構築
        // この部分でLoggedOutイベントの構築を行う。
        // ログイン時とは異なり、session に endedAtTime, duration が必要となる










        SessionEvent sessionEvent = null;


        // END: 送信用イベントの構築

        try {
            sensor.send(sensor, sessionEvent);
            return true;
        } catch (Throwable e) {
            logger.error(e.getStackTrace().toString());
            return false;
        }
    }

}
