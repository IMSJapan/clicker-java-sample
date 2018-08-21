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
    // クリッカーのメイン画面
    @GetMapping
    public String index(final Model model) {
        // Get launch params
        final LtiLaunch ltiLaunch = (LtiLaunch) session.getAttribute("ltiLaunch");  // launch params をセッションから復元
        final String resourceLinkId = ltiLaunch.getResourceLinkId();

        // Check user's role
        final boolean isInstructor = ltiLaunch.getUser().getRoles().contains("Instructor"); // ユーザを取得し、ロールに教員権限が含まれているか確認
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
    // 新規作成画面
    @GetMapping(value = "new")
    public String newClickerItem(final Model model) {
        return "clicker/new";
    }

    // POST /clicker/create
    // 新規作成処理
    @PostMapping
    public String create(@ModelAttribute final ClickerItem clickerItem) {
        // Get launch params
        final LtiLaunch ltiLaunch = (LtiLaunch) session.getAttribute("ltiLaunch");  // launch params をセッションから復元

        // Persist a clicker item
        clickerItem.setResourceLinkId(ltiLaunch.getResourceLinkId());   // LTI 経由で起動したコースのリソースIDを取得
        clickerItem.setStatus(ClickerItem.Status.NEW);
        clickerItemService.save(clickerItem);

        return "redirect:/clicker";
    }

    // GET /clicker/{clickerItemId}
    // 設問の個別画面
    @GetMapping(value = "{clickerItemId}")
    public String show(@PathVariable("clickerItemId") final Long clickerItemId, final Model model) {
        // Get a clicker item
        final Optional<ClickerItem> clickerItemOptional = clickerItemService.findById(clickerItemId);
        clickerItemOptional.ifPresent(clickerItem -> model.addAttribute("clickerItem", clickerItem));

        return "clicker/show";
    }

    // POST /clicker/{clickerItemId}/answer
    // 回答処理
    @PostMapping(value = "{clickerItemId}/answer")
    public String answer(@PathVariable("clickerItemId") final Long clickerItemId, @ModelAttribute final Answer answer) {
        // Get launch params
        final LtiLaunch ltiLaunch = (LtiLaunch) session.getAttribute("ltiLaunch"); // launch params をセッションから復元
        answer.setUserId(ltiLaunch.getUser().getId());  // LMS 側のユーザIDを取得

        // Get a clicker item
        final Optional<ClickerItem> clickerItem = clickerItemService.findById(clickerItemId);
        answer.setClickerItem(clickerItem.orElse(null));

        // Persist an answer
        answerService.save(answer);

        return "redirect:/clicker";
    }

    // POST /clicker/{clickerItemId}/start
    // 開始処理
    @PostMapping(value = "{clickerItemId}/start")
    public String start(@PathVariable("clickerItemId") final Long clickerItemId){
        // Set status ONGOING (enabled)
        clickerItemService.updateStatus(clickerItemId, ClickerItem.Status.ONGOING);

        return "redirect:/clicker";
    }

    // POST /clicker/{clickerItemId}/stop
    // 終了処理
    @PostMapping(value = "{clickerItemId}/stop")
    public String stop(@PathVariable("clickerItemId") final Long clickerItemId){
        // Set status COMPLETED (disabled)
        clickerItemService.updateStatus(clickerItemId, ClickerItem.Status.COMPLETED);

        return "redirect:/clicker";
    }

}
