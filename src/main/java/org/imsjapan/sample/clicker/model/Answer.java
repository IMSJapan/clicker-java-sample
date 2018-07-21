package org.imsjapan.sample.clicker.model;

import javax.persistence.*;

@Entity(name = "Answer")
@Table(name = "answers", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "clicker_item_id"})})
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    private ClickerItem clickerItem;        // many-to-one

    @ManyToOne
    private ClickerOption clickerOption;    // many-to-one

    protected Answer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public ClickerItem getClickerItem() {
        return clickerItem;
    }

    public void setClickerItem(final ClickerItem clickerItem) {
        this.clickerItem = clickerItem;
    }

    public ClickerOption getClickerOption() {
        return clickerOption;
    }

    public void setClickerOption(final ClickerOption clickerOption) {
        this.clickerOption = clickerOption;
    }

}
