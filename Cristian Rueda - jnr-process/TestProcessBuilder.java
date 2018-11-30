package jnr.process;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by headius on 1/19/15.
 */
public class TestProcessBuilder {
    @Test
    public void testBasicProcess() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "echo hello"); // CREA EL PROCESO PADRE

        Process p = pb.start(); // INICIA EL PROCESO HIJO

        byte[] hello = new byte[5];
        p.getInputStream().read(hello); // GUARDA EN EL ARRAY HELLO EL TEXTO QUE HAY EN EL TERMINAL

        assertEquals(0, p.waitFor()); // ESPERA A QUE FINALICE EL PROCESO HIJO
        
        assertArrayEquals("hello".getBytes(), hello);
    }
}
