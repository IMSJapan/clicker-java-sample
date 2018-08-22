package org.imsjapan.sample.clicker.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imsglobal.aspect.Lti;
import org.imsglobal.lti.launch.LtiLaunch;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsjapan.sample.clicker.CaliperSession;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LaunchController {

    private static final Logger logger = LogManager.getLogger(LaunchController.class);

    @Autowired
    HttpSession session;

    // POST /launch
    // LTI で LMS から起動する際のエンドポイント
    @Lti
    @RequestMapping(value = "/launch", method = RequestMethod.POST)
    public String launch(final HttpServletRequest request, final LtiVerificationResult result, final Model model) {
        // Set session value
        // LMS から遷移したときしかパラメータは取得できないためセッションに格納して利用
        final LtiLaunch launch = result.getLtiLaunchResult();   // LTI におけるパラメータを LtiLaunch オブジェクトとして取得
        session.setAttribute("ltiLaunch", launch);  // そのままセッションに格納

        // Logging with Caliper
        // Get current DateTime
        final DateTime now = new DateTime();
        if (CaliperSession.sendSessionLoggedIn(launch.getUser(), session.getId(), now)) {
            // イベント送信成功
            logger.info("SessionEvent(Logged In) was sent successfully.");
        } else {
            // イベント送信失敗
            logger.error("Failed to send SessionEvent(Logged In).");
        }

        return "redirect:/clicker"; // クリッカーのメイン画面にリダイレクトします
    }

}
