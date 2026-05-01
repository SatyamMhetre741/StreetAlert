package com.StreetAlert.Street_Alert.event;

import com.StreetAlert.Street_Alert.entity.Article;
import org.springframework.context.ApplicationEvent;

public class ArticleSavedEvent extends ApplicationEvent {

    private final Article article;

    public ArticleSavedEvent(Object source, Article article) {
        super(source);
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }
}
