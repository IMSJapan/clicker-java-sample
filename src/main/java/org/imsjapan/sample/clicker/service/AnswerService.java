package org.imsjapan.sample.clicker.service;

import org.imsjapan.sample.clicker.model.Answer;
import org.imsjapan.sample.clicker.model.ClickerItem;
import org.imsjapan.sample.clicker.repository.AnswerRepository;
import org.imsjapan.sample.clicker.repository.ClickerItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AnswerService {

    @Autowired
    ClickerItemRepository clickerItemRepository;

    @Autowired
    AnswerRepository answerRepository;

    public Answer findByUserIdAndClickerItem(final String userId, final ClickerItem clickerItem) {
        return answerRepository.findByUserIdAndClickerItem(userId, clickerItem);
    }

    public Answer save(final Answer answer) {
        final Answer savedAnswer = answerRepository.findByUserIdAndClickerItem(answer.getUserId(), answer.getClickerItem());
        if (savedAnswer != null) {
            // Update if already answered
            answer.setId(savedAnswer.getId());
        }
        return answerRepository.save(answer);
    }

}
