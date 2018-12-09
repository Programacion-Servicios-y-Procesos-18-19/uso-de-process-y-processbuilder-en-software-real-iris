# Datos básicos del proyecto:
### 1.	[Descripción resumida del proyecto]
Apache Tika (TM) es un kit de herramientas para detectar y extraer metadatos y contenido de texto estructurado de varios documentos utilizando las bibliotecas de analizadores existentes.
### 2.	[Enlace a la web oficial]
http://tika.apache.org/
### 3.	[Enlace a la documentación oficial]
http://tika.apache.org/1.19.1/api/
### 4.	[Captura de pantalla de la web oficial, o aplicación en ejecución, o página gitHub en su defecto]
![Imagen] (1.jpg)
### 5.	[Nº de estrellas / seguidores en gitHub]
0.
### 6.	[Commits en el Nº de estrellas / seguidores en gitHub]
4.392.
### 7.	[Incidencias (issues) actuales  del proyecto: abiertas y cerradas]
0 abiertas y 0 cerradas.

## Uso de Process y ProcessBuilder: 
### 1.	¿Cuentas ocurrencias totales en el proyecto aparecen para las clases Process y ProcessBuilder? (si no apareciera ninguna, documéntalo y elige otro proyecto del listado)
25 de Process y 29 de ProcessBuilder

### [código original github del ejemplo de uso] 
[Codigo] (https://github.com/Sebastian1984/tika/blob/f2e1937005545da5b9d23683aa0f1c82da1721cc/tika-server/src/main/java/org/apache/tika/server/TikaServerWatchDog.java#L362)


### [código comentado del repositorio de ezsta actividad] (enlace a la línea en sí)
[Codigo] (https://github.com/Programacion-Servicios-y-Procesos-18-19/uso-de-process-y-processbuilder-en-software-real-iris/blob/86e98272209955021ecdfa76ccdd2634b1ea7edd/Apache-Tika_Daniel-Molina/TikaServerWatchDog.java#L362)
### [transcripción del ejemplo del código]  
![Imagen] (2.jpg)
### 2.	¿De qué forma se crea el proceso hijo? comando de invocación, parámetros…
El proceso se crea dentro de una función llamada “startProcess”, que recibe como parámetros un array de Strings y una dirección, que van a ser utilizadas por varias List como argumentos que se le van a añadir al final al ProcessBuilder mediante builder.command(argList), para finalmente crear el proceso usando dicho ProcessBuilder.
### 3.	¿Se conecta el proceso padre a los flujos de entrada, salida o errores del proceso hijo?
Sí, mediante la línea redirectIO(process.getInputStream(), System.err);
### 4.	¿Se emplea algún mecanismo de comunicación entre procesos?
No
### 5.	¿Espera el padre al fin de la ejecución del hijo? ¿Se realiza alguna acción con su valor devuelto?
No en el ejemplo, pero en la misma clase hay una función, destroyChildForcibly(Process process), que recibe un proceso como parámetro y lo destruye, usando el valor de waitFor() para enviar un mensaje por consola.
### 6.	¿En qué contexto se utilizan los ejemplos encontrados? ¿En qué módulo o clase de la aplicación? (puede ser útil consultar la documentación del proyecto en el propio github, en su página oficial...)
Dentro de la clase TikaServerWatchDog.java, de la cual no viene casi información en la documentación, pero parece por su contenido que se encarga de crear procesos para comunicar información del estado del servidor.

### 7.	¿Para qué fin se crean los procesos? (investiga razonablemente y en lo posible da una respuesta documentada, o si no es posible una respuesta intuitiva)
Para comunicar información del estado del servidor.
