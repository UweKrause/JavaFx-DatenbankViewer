# JavaFx-DatenbankViewer
Eine kleine JavaFX Anwendung, die sich zu einer Datenbank (hier: Azure) verbindet und die Tabellen sowie die Inhalte der Tabellen anzeigt.

![Screenshot](/doc/screenshot_v0-4.jpg?raw=true "Version 0.4")

## Worum gehts?
Als Aufgabestellung zum Ende der Datenbankvorlesung war eine Anwendung zu entwickeln, die den Oracle-Java-Datenbanktreiber verwendet und eine Verbindung zur Hochschuldatenbank aufbaut.
Anstelle einer Kommandozeilenanwendung habe ich mich für eine JavaFX anwendung entschieden, weil ich bei der Gelegenheit gerne die Möglichkeiten von JavaFX ausprobieren wollte.

Da ich meinen Zugang zur HAW Datenbank nicht weitergeben kann, habe ich inzwischen eine Datenbank mit Beispieleinträgen auf einem Azure-Server eingerichtet.

### Der Zugang lautet
Benutzername: `gtzhjkio`

Passwort: `Ayxcvbgfds2`
(Es sind nur Testdaten)

## Was kann das Programm?
Es wird eine Verbindung zu einem entfernten Datenbankserver aufgebaut.
Im Falle einer korrekten Verbindung werden die vorhandenen Tabellen aufgelistet und die Statusleiste aktualisiert.
Ein Klick auf einen Tabellennamen öffnet die Tabelle im rechten Bereich des Fensters.
Die Spaltenüberschriften erlauben ein Sortieren der angezeigten Daten.
Die Höhe und Breite der Liste und der Tabelle sind frei einstellbar und folgen der Fenstergröße.

## Abhängigkeiten
- Java8
- Die Library für den Datenbanktreiber ist unter /lib
