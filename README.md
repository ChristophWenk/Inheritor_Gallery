# InheritorGallery

## General Information
[![Build Status](https://travis-ci.com/FHNW-IP5-IP6/InheritorGallery.svg?token=qWRopjoJzCncJVxseK5R&branch=master)](https://travis-ci.com/FHNW-IP5-IP6/InheritorGallery)

This application has been developed as part of a study project at Fachhochschule Nordwestschweiz FHNW in Brugg, Switzerland.
The [documentation](https://github.com/ChristophWenk/Inheritor_Gallery/blob/master/docs/index.adoc) has been written in German as this was the language of communication at the time of development. It is not planned to translate it anytime soon. Thank you for your understanding.

This application is meant for Java beginners. It should visualize inheritance structures of Java objects and make them better understandable for new programmers.

The following abstract is taken out of the documentation and gives a short introduction to the project in German.

![App](https://github.com/ChristophWenk/Inheritor_Gallery/blob/master/docs/images/app/App.png)

## Ausgangslage

Das Klassendiagramm von UML stellt die Vererbungsbeziehungen zwischen Klassen und Interfaces dar. Dabei werden die Klassen mit ihren Attributen und Methoden als einzelne Quadrate gezeigt. Für die resultierenden Instanzen einer abgeleiteten Klasse ist diese Darstellung irreführend. Für das Verständnis was eine Instanz ausmacht, wäre es für einen Programmieranfänger deutlich einfacher, wenn die Instanz als eine Einheit visualisiert wird und nicht in mehreren Blöcken.

Eine für Programmieranfänger geeignete Visualisierung von Instanzen müsste:

* die verfügbaren Methoden und Attribute und deren Ursprungs-Klasse bzw. Interface nachvollziehbar machen

* den statischen und dynamischen Typ eines Attributs bzw. einer Variablen berücksichtigen

Ein Debugger bietet einige dieser Informationen an. Eine grafische Visualisierung existiert derzeit nicht.

## Ziel der Arbeit

Entwicklung einer interaktiven Lernumgebung für den Einsatz im Unterricht für Programmieranfänger auf Basis JavaFX. Für den Programmieranfänger soll es möglich sein, typische Aufgabenstellungen und Programmier-Übungen im Bereich Vererbung und Polymorphie schrittweise zu implementieren und mit bestehenden Klassenhierarchien zu experimentieren. Die entstehenden Instanzen sollen geeignet graphisch visualisiert werden.

## Problemstellung

InheritorGallery besteht aus zwei Teilen

* Autorensystem mit dem neue Übungen erstellt werden (insbesondere Vererbungsstrukturen)

* Lernsystem mit dem der Programmieranfänger die Übungen durchführt

Für beide Teile

* Evaluation bestehender Ansätze und Lösungen

* Evaluation geeigneter Technologien

Für die Lernumgebung

* Entwurf eines geeigneten User-Interfaces

* systematische Usability-Tests zur Weiterentwicklung der User-Interfaces

* Entwurf der Visualisierung von Instanzen von Klassen mit mehreren Oberklassen und Interfaces

* dynamische Veränderung der Visualisierung je nach Programmzustand
