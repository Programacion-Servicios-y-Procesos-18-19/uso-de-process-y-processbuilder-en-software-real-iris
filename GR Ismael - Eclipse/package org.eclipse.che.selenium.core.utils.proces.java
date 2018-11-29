package org.eclipse.che.selenium.core.utils.process;

import static java.lang.String.format;

import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Esta clase lo que hace es lanzar un proceso con una función que pasamos como cadena por parametro, registrando sus salidas como sus errores de forma 
que se pueda realizar un correcto control del proceso sin afectar al propio proyecto en caso de errores.
*/

/** @author Dmytro Nochevnov */
@Singleton
public class ProcessAgent {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessAgent.class);

  public String process(String command) throws ProcessAgentException {
    try {
      Process process = getProcess(command); // llamamos al metodo getProcess, que recive una String con la funcionalidad definida del proceso, que nos devuelve un ProcessBuilder en ejecución

      int exitStatus = process.waitFor(); // guardamos la respuesta final del proceso, así como lo que devuelve al terminar, haciendo esperar al programa a que termine
      InputStream in = process.getInputStream(); // guardamos dicha salida del proceso
      InputStream err = process.getErrorStream(); // guardamos la salida de errores del proceso

      return processOutput(exitStatus, in, err); // devolvemos lo que nos devuelve el metodo processOutput, al cual le pasamos la salida de finalización, las salidas y errores del proceso
    } catch (Exception e) {
      String errMessage = format("Can't process command '%s'.", command);
      throw new ProcessAgentException(errMessage, e);
    }
  }

  private Process getProcess(String command) throws IOException { // nos crea un proceso con la funcion que pasamos por cadena por parametro y lo devolvemos en ejecución. En caso de fallo nos tira Exception para arriba
    ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
    return pb.start();
  }

  private String processOutput(int exitStatus, InputStream in, InputStream error) throws Exception { // preparamos las cadenas de salidas y errores para que sea legible. Si no hubo errores o termino correctamente el proceso, devolvemos solo la salida del proceso
    String output = readOutput(in);
    String errorOutput = readOutput(error);

    if (exitStatus == 0 && errorOutput.isEmpty()) {
      return output;
    }

    throw new Exception(format("Output: %s; Error: %s.", output, errorOutput)); // en caso de haber error, mandamos la excepción con el error del proceso correspondiente
  }

  private String readOutput(InputStream in) throws IOException { // prepara los inputstream para convertirlos a cadenas legibles. En caso de que falle, igualmente cerramos ese inputstream.
    try {
      String output = IOUtils.toString(in, Charset.forName("UTF-8"));
      if (output.endsWith("\n")) {
        output = output.substring(0, output.length() - 1);
      }
      return output;
    } finally {
      in.close();
    }
  }
}