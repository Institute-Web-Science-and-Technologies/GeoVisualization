# Anforderungsliste


###1. Client (C)
C1.1 Darstellung der Kartendaten

C1.2 Abfragen von GPS-Koordinaten des Aufenthaltsortes

C1.3 Darstellung des Quizmoduls mit Fragen und Antworten, Kommunikation mit dem Server

C1.4 Darstellung von Storyinhalten

C1.5 Anzeige des Timers

C1.6 Darstellung der Highscoreliste, Kommunikation mit dem Server (Datenbankabfrage)



###2. Server (S)
S2.1 Quizmodul
* S2.1.1 Bereitstellung der Fragen an den Client
  
* S2.1.2 Server nimmt die gewählte Antwort entgegen und wertet diese aus (Antwort an den Client / Punktevergabe)
  
S2.2 Der Server stellt die Kartendaten für den Client bereit

S2.3 Bereistellung der GPS-Koordinaten von Zielen (Denkmäler/Sehenswürdigkeiten) und Powerups

S2.4 Verarbeitung der Aufnhame von Powerups durch den Client




###3. Datenbank (D)

D3.1 Die Datenbank enthält die Fragen und die Antworten, die vom Quizmodul verarbeitet werden.

D3.2 Die Datenbank enthält die GPS-Koordinaten der Ziele (Denkmäler / Sehenswürdigkeiten)

D3.2 Die Datenbank enthält eine Highscoreliste mit Gruppennamen und Punktzahl
