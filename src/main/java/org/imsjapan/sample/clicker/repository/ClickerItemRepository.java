package org.imsjapan.sample.clicker.repository;

import org.imsjapan.sample.clicker.model.ClickerItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ClickerItemRepository extends CrudRepository<ClickerItem, Long> {

    Optional<ClickerItem> findById(final Long id);

    List<ClickerItem> findByResourceLinkId(final String resourceLinkId);

    List<ClickerItem> findByResourceLinkIdAndStatus(final String resourceLinkId, final ClickerItem.Status status);

    ClickerItem save(final ClickerItem clickerItem);

}
