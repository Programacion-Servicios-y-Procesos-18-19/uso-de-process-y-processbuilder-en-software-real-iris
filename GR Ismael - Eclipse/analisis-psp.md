# ECLIPSE/CHE

## Información del proyecto
Este proyecto se basa en una herramienta multiplataforma que se basa en la nube para que los desarrolladores puedan trabajar de manera local o remota.

## Che soporta cualquier framework y lenguaje de programación, y es expansible, lo que quiere decir que los propios desarrolladores pueden crear sus propias extensiones o modificar los plug-ins existentes a su antojo. También tiene una función de vista previa para poder ver el proyecto de la misma manera que lo harán los usuarios.

## Enlace a la web oficial
[Pagina oficial](https://www.eclipse.org/che/)

## Enlace a la documentación oficial
[Documentacion](https://www.eclipse.org/che/docs/che-6/index.html)

## Nº de estrellas / seguidores en gitHub

Nº estrellas/seguidores: 5144
Contributors: 109

## Número de Commits
7318 commits.

## Incidencias (issues) actuales  del proyecto: abiertas y cerradas
Incidencias actuales abiertas: 930
Incidencias cerradas: 6537

## Uso de Process y ProcessBuilder: 
### ¿Cuentas ocurrencias totales en el proyecto aparecen para las clases Process y ProcessBuilder? (si no apareciera ninguna, documéntalo y elige otro proyecto del listado)
Para ProcessBuilder: 24
Para Process: 517

## código original github del ejemplo de uso (enlace a la línea en sí)
[Codigo original](https://github.com/eclipse/che/blob/07263f1e30089689d71b057f747a44a29283e3c4/selenium/che-selenium-core/src/main/java/org/eclipse/che/selenium/core/utils/process/ProcessAgent.java)

## código comentado del repositorio de esta actividad
[Codigo comentado](https://github.com/Programacion-Servicios-y-Procesos-18-19/uso-de-process-y-processbuilder-en-software-real-iris/blob/master/GR%20Ismael%20-%20Eclipse/package%20org.eclipse.che.selenium.core.utils.proces.java)

1. ¿De qué forma se crea el proceso hijo? comando de invocación, parámetros…
Lo crea pasandole la función como cadena por parámetro, haciendo uso del método principal “process” que devuelve una cadena, ya sea la salida del proceso o si ha tenido fallos, el error del proceso.
Hace uso de un segundo método donde le vuelve a pasar la cadena donde se define la función del proceso y devuelve el Process en ejecución.

2. ¿Se conecta el proceso padre a los flujos de entrada, salida o errores del proceso hijo?
No, simplemente se tiene un control del hijo, indicando que devuelve y si falla, en qué falla, de forma controlada sin afectar directamente al padre. Solamente los resultados de este proceso son enviados al padre durante su ejecución en caso de fallo, o cuando finaliza correctamente.

3. ¿Se emplea algún mecanismo de comunicación entre procesos?
No, solo se enfoca en el proceso hijo, aunque las salidas de ésta se lanzan hacia el proceso padre.

4. ¿Espera el padre al fin de la ejecución del hijo? ¿Se realiza alguna acción con su valor devuelto?
Si mediante el waitfor, recogiendo el entero que devuelve como salida para definir cómo se finalizó el proceso.

5. ¿En qué contexto se utilizan los ejemplos encontrados? ¿En qué módulo o clase de la aplicación? (puede ser útil consultar la documentación del proyecto en el propio github, en su página oficial...)
Se utilizan para lanzar procesos de una forma fácil y rápida con un control razonable donde poder realizar seguimiento y registros del mismo proceso.

6. ¿Para qué fin se crean los procesos? (investiga razonablemente y en lo posible da una respuesta documentada, o si no es posible una respuesta intuitiva)
Se utiliza para tener una concurrencia del programa, para mejorar el rendimiento aprovechando los recursos del sistema, para así tener más desahogado al equipo corriendo de una forma más fluida.
También para poder realizar múltiples funciones que se requieran sin necesidad de parar el proceso padre. A demás que por motivos de organización y gestión del propio programa, conviene realizar por separado las tareas.

Además, como en el caso del ejemplo, tener una clase dedicada a la ejecución de subprocesos, facilita mucho el desarrollo del programa ya que en nuestro programa principal, podemos realizar la ejecución de procesos en una sola corta línea teniendo un registro de ésta.