# freedomotic / freedomotic 
### [Alumno:  José J. Morillo :metal:]

## Datos básicos del proyecto:

* Freedomotic es un FrameWork de código abierto para crear y administrar proyectos de domótica. Es una solución dirigida a fabricantes de hardware y desarrolladores de software para crear fácilmente sistemas de automatización.
Se puede integrar con tecnologías populares de automatización como BTicino OpenWebNet, Modbus RTU, Z-wave, así como proyectos de automatización personalizados con dispositivos Arduino o Raspberry Pi.


* [Enlace a la web oficial](http://freedomotic.com/)

* [Enlace a la documentación oficial](https://freedomotic-user-manual.readthedocs.io/en/latest/)

* Nº de estrellas: 267

* Commits: 1503 commits

* Incidencias: 64 Open - 117 Closed


## Uso de Process y ProcessBuilder: 
**¿Cuentas ocurrencias totales en el proyecto aparecen para las clases Process y ProcessBuilder?**

> Uno en la Clase: PresenceDetection.java

* [código original github del ejemplo de uso](https://github.com/freedomotic/freedomotic/blob/be631f6045df67d89d6210b6a89e345983d83172/plugins/devices/presence-detection/src/main/java/com/freedomotic/plugins/devices/presencedetection/PresenceDetection.java#L143)

* [código comentado del repositorio de esta actividad](https://github.com/Programacion-Servicios-y-Procesos-18-19/uso-de-process-y-processbuilder-en-software-real-iris/blob/18d545c8ce8132247e384ea18c8531eae5b80c74/freedomotic_JoseJMorillo/PresenceDetection.java#L144)


* Ejemplo del código   
  ![Captura](https://drive.google.com/open?id=1LkolJrT1ue7amS_qW5oS4ZHn2YFKmwaA)

**¿De qué forma se crea el proceso hijo? comando de invocación, parámetros… Se crea dentro de un método denominado “ping”, que a su vez está dentro de la clase PresenceDetection.java.** 

> Por parámetros entra un String con el host, que a su vez formará parte de los parámetros del proceso.
>ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", host).
>El comando de invocación es un Ping a un Host, y dentro del mismo un operador ternario envía un parámetro distinto dependiendo de si el sistema operativo es windows u otro.

**¿Se conecta el proceso padre a los flujos de entrada, salida o errores del proceso hijo?**

> No.

**¿Se emplea algún mecanismo de comunicación entre procesos?**

> No, sólo hay un proceso.

**¿Espera el padre al fin de la ejecución del hijo? ¿Se realiza alguna acción con su valor devuelto?**

> Sí, espera al fin de la ejecución con el comando waitFor();
> Con el valor devuelto se compara con 0, que es el valor devuelto cuando el proceso ha terminado correctamente. Si es así el método devuelve True y si no False;

**¿En qué contexto se utilizan los ejemplos encontrados? ¿En qué módulo o clase de la aplicación? (puede ser útil consultar la documentación del proyecto en el propio github, en su página oficial...)**

> Dentro de la clase PresenceDetection.java; que es uno de los muchos Plugins que incorpora el Framework “Freedomotic”.

**¿Para qué fin se crean los procesos? (investiga razonablemente y en lo posible da una respuesta documentada, o si no es posible una respuesta intuitiva)**
> Por el nombre de la Clase y el comando de invocación del proceso se deduce que la función es detectar mediante un comando ping si hay conexión con el anfitrión. Es decir, si dos máquinas (servidores, webcams, equipos…) tienen comunicación a través de la red.