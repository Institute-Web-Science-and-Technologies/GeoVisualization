# Vision

Ziel dieses Projektes ist es ein Spiel zu entwickeln, welches mit den Smartphone (Android) gespielt wird. Das Spiel liefert den Spielern GEOGRAFISCHE INFORMATIONEN (MAP ODER PFEILE) und lotst sie zu Sehenswürdigkeiten. In diesem Fall auf die Stadt Koblenz begrenzt. Dort müssen Aufgaben gelöst werden. Diese können Rätsel sein, Puzzels, ... . Die nachdem die Aufgabe gelöst ist werden die Informationen zum nächsten Denkmal übermittelt. Nach einer festgelegten Anzahl Aufgaben erfolgt eine Schlussbewertung. Das Ganze wird in eine FESSELNDE Story verpackt. Zum Softwaresystem gehören ein Client und ein Server mit Datenbank.


###Client
Zu beginn meldet sich der Client beim Server an. Der Client zeigt dem Nutzer die GEOGRAFISCHEN INFORMATIONEN (empfangen vom Server)an um zum Ziel zu gelangen. Dort angekommen öffnet sich ein weiteres Fenster, was die aktuelle Aufgabe (ebenfalls vom Server empfangen) anzeigt. Nachdem die Aufgabe gelöst ist, bekommt der Client die Informationen zum nächsten Ziel Übermittelt.


###Server
Der Server wird die aktiven Clients, sowie die Aufgaben und GEOGRAPHISCHE INFORMATIONEN verwalten. Diese werden in einer Datenbank gespeichert. Aufgaben mit Geographischen INFORMATIONEN werden an den CLIENT gesendet. Es soll möglich sein, die Datenbank um Aufgaben zu erweitern, ohne das am Code oder Datensätzen direkt gearbeiten werden muss (Übermittlung der Daten z.B. mit JSON).