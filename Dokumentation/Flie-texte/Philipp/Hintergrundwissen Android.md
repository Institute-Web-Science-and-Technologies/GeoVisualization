##Allgemeines

Android ist ein Betriebssystem für mobile Geräte, welches von Google entwickelt wird. Basierend auf dem Linux Kernel 
wurde es primär für mobile Geräte mit Touchscreen, wie Smartphones und Tablet-PCs entwickelt.Spezialisierte 
User-Interfaces für Fernseher  (Android TV), Autos (Android Car) und Uhren/Smartwatches (Android Wear) sind vorhanden. 
Außerdem kann Android auch auf Geräten ohne Touchscreen eingesetzt werden. Der Sourcecode ist frei zugänglich und
Läuft unter Open Source Lizenzen.

Der momentane Marktanteil (Stand 2. Quartal 2014) liegt bei 84,6. Dieser Fakt, sowie auch die Verbreitung von Android 
Geräten inerhalb unserer Projektgruppe war ein Grund die App für dieses Projektpraktikum für Android zu entwickeln.


## Entwicklung
In diesem Projektpraktikum wurde mit Java und dem Android Software Development kit entwickelt. Hauptsächlich wurde das offiziel unterstütze Eclipse mit dem Andoird Developemt Tools (ADT) Plugin verwendet. Hierbei hat man unter anderem die
Möglichkeit einen Geräte Emulator zu verwenden. Da die damit erstellten virtuellen Geräte nur sehr langsam reagiert haben
und das testen mit einem virtuellen GPS-Sensor sehr mühselig ist, wurde darauf weitestgehend verzichtet.

### Layout

Das Layout setzt sich aus einer Hierarchie aus View und ViewGroup Objekten zusammen. View Objekte sind hierbei normalerweise UI Widgets, z.B. Buttons oder Textfelder. ViewGroups Objekte sind hingegen unsichtbare View Container, die das Layout ihrer Kinder bestimmen.
![alt text](https://developer.android.com/images/viewgroup.png "viewgroup")
