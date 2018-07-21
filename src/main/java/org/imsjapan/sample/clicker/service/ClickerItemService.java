package org.imsjapan.sample.clicker.service;

import org.imsjapan.sample.clicker.model.ClickerItem;
import org.imsjapan.sample.clicker.repository.ClickerItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClickerItemService {

    @Autowired
    ClickerItemRepository clickerItemRepository;

    public Optional<ClickerItem> findById(final Long id) {
        return clickerItemRepository.findById(id);
    }

    public List<ClickerItem> findByResourceLinkId(final String resourceLinkId) {
        return clickerItemRepository.findByResourceLinkId(resourceLinkId);
    }

    public List<ClickerItem> findByResourceLinkIdAndStatus(final String resourceLinkId, final ClickerItem.Status status) {
        return clickerItemRepository.findByResourceLinkIdAndStatus(resourceLinkId, status);
    }

    public ClickerItem save(final ClickerItem clickerItem) {
        return clickerItemRepository.save(clickerItem);
    }

    public Optional<ClickerItem> updateStatus(final Long id, final ClickerItem.Status status) {
        final Optional<ClickerItem> clickerItem = clickerItemRepository.findById(id);
        clickerItem.ifPresent(item -> {
            item.setStatus(status);
            clickerItemRepository.save(item);
        });

        return clickerItem;
    }
}
