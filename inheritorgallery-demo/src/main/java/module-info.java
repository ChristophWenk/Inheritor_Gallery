module inheritorgallery.demo {
    requires inheritorgallery.core;

    requires slf4j.api;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.web;

    opens inheritorgallery to javafx.graphics;
}