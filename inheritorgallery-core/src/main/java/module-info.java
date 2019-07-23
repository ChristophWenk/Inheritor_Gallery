module inheritorgallery.core {
    requires slf4j.api;
    requires javafx.base;
    requires javafx.graphics;
    requires asciidoctorj.api;
    requires asciidoctorj;
    requires jdk.jshell;

    exports presentationmodel;
    exports presentationmodel.instance;
    exports presentationmodel.instruction;
    exports presentationmodel.uml;
    exports service;
    exports service.instruction;

}