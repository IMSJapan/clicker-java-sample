package org.imsjapan.sample.clicker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ClickerOption")
@Table(name = "clicker_options")
public class ClickerOption implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "clicker_option_id")
    private List<Answer> answers = new ArrayList<>();    // one-to-many

    @ManyToOne
    private ClickerItem clickerItem;    // many-to-one

    protected ClickerOption() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public ClickerItem getClickerItem() {
        return clickerItem;
    }

    public void setClickerItem(ClickerItem clickerItem) {
        this.clickerItem = clickerItem;
    }

}
