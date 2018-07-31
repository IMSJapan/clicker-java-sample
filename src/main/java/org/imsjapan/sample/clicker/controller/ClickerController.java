package org.imsjapan.sample.clicker.controller;

import org.imsglobal.lti.launch.LtiLaunch;
import org.imsjapan.sample.clicker.model.Answer;
import org.imsjapan.sample.clicker.model.ClickerItem;
import org.imsjapan.sample.clicker.service.AnswerService;
import org.imsjapan.sample.clicker.service.ClickerItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clicker")
public class ClickerController {

    @Autowired
    HttpSession session;

    @Autowired
    ClickerItemService clickerItemService;

    @Autowired
    AnswerService answerService;

    // GET /clicker
    @GetMapping
    public String index(final Model model) {
        // Get launch params
        final LtiLaunch ltiLaunch = (LtiLaunch) session.getAttribute("ltiLaunch");
        final String resourceLinkId = ltiLaunch.getResourceLinkId();

        // Check user's role
        final boolean isInstructor = ltiLaunch.getUser().getRoles().contains("Instructor");
        model.addAttribute("isInstructor", isInstructor);

        // Get active items
        final List<ClickerItem> activeClickerItemList = clickerItemService.findByResourceLinkIdAndStatus(resourceLinkId, ClickerItem.Status.ONGOING);
        if (activeClickerItemList.size() > 0) {
            // If active items exists
            final ClickerItem activeClickerItem = activeClickerItemList.get(0); // Only first item
            model.addAttribute("activeClickerItem", activeClickerItem);

            final Answer answer = answerService.findByUserIdAndClickerItem(ltiLaunch.getUser().getId(), activeClickerItem);
            model.addAttribute("answer", answer);
        }

        // Get all items
        final List<ClickerItem> clickerItemList = clickerItemService.findByResourceLinkId(resourceLinkId);
        model.addAttribute("clickerItemList", clickerItemList);

        return "clicker/index";
    }

    // GET /clicker/new
    @GetMapping(value = "new")
    public String newClickerItem(final Model model) {
        return "clicker/new";
    }

    // POST /clicker/create
    @PostMapping
    public String create(@ModelAttribute final ClickerItem clickerItem) {
        // Get launch params
        final LtiLaunch ltiLaunch = (LtiLaunch) session.getAttribute("ltiLaunch");

        // Persist a clicker item
        clickerItem.setResourceLinkId(ltiLaunch.getResourceLinkId());
        clickerItem.setStatus(ClickerItem.Status.NEW);
        clickerItemService.save(clickerItem);

        return "redirect:/clicker";
    }

    // GET /clicker/{clickerItemId}
    @GetMapping(value = "{clickerItemId}")
    public String show(@PathVariable("clickerItemId") final Long clickerItemId, final Model model) {
        // Get a clicker item
        final Optional<ClickerItem> clickerItemOptional = clickerItemService.findById(clickerItemId);
        clickerItemOptional.ifPresent(clickerItem -> model.addAttribute("clickerItem", clickerItem));

        return "clicker/show";
    }

    // POST /clicker/{clickerItemId}/answer
    @PostMapping(value = "{clickerItemId}/answer")
    public String answer(@PathVariable("clickerItemId") final Long clickerItemId, @ModelAttribute final Answer answer) {
        // Get launch params
        final LtiLaunch ltiLaunch = (LtiLaunch) session.getAttribute("ltiLaunch");
        answer.setUserId(ltiLaunch.getUser().getId());

        // Get a clicker item
        final Optional<ClickerItem> clickerItem = clickerItemService.findById(clickerItemId);
        answer.setClickerItem(clickerItem.orElse(null));

        // Persist an answer
        answerService.save(answer);

        return "redirect:/clicker";
    }

    // POST /clicker/{clickerItemId}/start
    @PostMapping(value = "{clickerItemId}/start")
    public String start(@PathVariable("clickerItemId") final Long clickerItemId){
        // Set status ONGOING (enabled)
        clickerItemService.updateStatus(clickerItemId, ClickerItem.Status.ONGOING);

        return "redirect:/clicker";
    }

    // POST /clicker/{clickerItemId}/stop
    @PostMapping(value = "{clickerItemId}/stop")
    public String stop(@PathVariable("clickerItemId") final Long clickerItemId){
        // Set status COMPLETED (disabled)
        clickerItemService.updateStatus(clickerItemId, ClickerItem.Status.COMPLETED);

        return "redirect:/clicker";
    }

}
