package org.imsjapan.sample.clicker.controller;

import org.imsglobal.aspect.Lti;
import org.imsglobal.lti.launch.LtiLaunch;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LaunchController {

    @Autowired
    HttpSession session;

    @Lti
    @RequestMapping(value = "/launch", method = RequestMethod.POST)
    public String launch(final HttpServletRequest request, final LtiVerificationResult result, final Model model) {
        // Set session value
        LtiLaunch launch = result.getLtiLaunchResult();
        session.setAttribute("ltiLaunch", launch);

        return "redirect:/clicker";
    }

}
