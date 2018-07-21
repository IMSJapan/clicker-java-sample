package org.imsjapan.sample.clicker.repository;

import org.imsjapan.sample.clicker.model.Answer;
import org.imsjapan.sample.clicker.model.ClickerItem;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepository extends CrudRepository<Answer, Long> {

    Answer findByUserIdAndClickerItem(final String userId, final ClickerItem clickerItem);

    Answer save(final Answer answer);

}
