package org.imsjapan.sample.clicker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ClickerItem")
@Table(name = "clicker_items")
public class ClickerItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String resourceLinkId;

    private String body;

    private Status status;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "clicker_item_id")
    private List<ClickerOption> clickerOptions = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "clicker_option_id")
    private List<Answer> answers = new ArrayList<>();    // one-to-many

    protected ClickerItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceLinkId() {
        return resourceLinkId;
    }

    public void setResourceLinkId(String resourceLinkId) {
        this.resourceLinkId = resourceLinkId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<ClickerOption> getClickerOptions() {
        return clickerOptions;
    }

    public void setClickerOptions(List<ClickerOption> clickerOptions) {
        this.clickerOptions = clickerOptions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isActive() {
        return this.status == Status.ONGOING;
    }

    public boolean isNew() {
        return this.status == Status.NEW;
    }

    public enum Status {
        NEW,        // 新規
        ONGOING,    // 実施中
        COMPLETED;  // 完了
    }

}
