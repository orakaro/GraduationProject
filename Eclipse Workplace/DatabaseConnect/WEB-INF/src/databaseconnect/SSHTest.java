package databaseconnect;

import java.io.*;
import ch.ethz.ssh2.*;

public class SSHTest {

    private static final long TIMEOUT = 12;

    public static void main(String[] arg) {
        try {
            SSHTest test = new SSHTest();
            test.doProc();
        } catch (InterruptedException ex) { ex.printStackTrace();
        } catch (IOException ex) { ex.printStackTrace();
        }
    }

    public void doProc() throws IOException, InterruptedException {

        // login
        Connection conn = new Connection("tbtex");
        ConnectionInfo info = conn.connect();   //IO
        boolean result = conn.authenticateWithPassword("root","h!ked@");  //IO
        if (result) {
            SshCommandExecute ssh1 = new SshCommandExecute(conn.openSession());
            ssh1.exec("ls -l");

            SshCommandExecute ssh2 = new SshCommandExecute(conn.openSession());
            ssh2.exec("cat /root/v-minh/hello.c", TIMEOUT);
            System.out.println(ssh2.getStdout());
            ssh2.close();

            ssh1.join(TIMEOUT);
            System.out.println(ssh1.getStdout());
            ssh1.close();

            conn.close();
        }
    }


    class SshCommandExecute {
        private Session session;
        private InputStreamBuffering stdout;
        private InputStreamBuffering stderr;
        private OutputStreamWriter stdin;

        public SshCommandExecute(Session _session) {
            session = _session;
        }

        public void exec(String command) throws IOException {
            session.execCommand(command);

            stdout = new InputStreamBuffering(session.getStdout());
            stderr = new InputStreamBuffering(session.getStderr());
            stdin = new OutputStreamWriter(session.getStdin());
        }

        public void exec(String command, long timeout) throws IOException, InterruptedException {
            exec(command);
            stdout.join(timeout);
        }

        public void join(long timeout) throws InterruptedException {
            stdout.join(timeout);
        }

        public OutputStreamWriter getStdin() {
            return stdin;
        }

        public InputStreamBuffering getStdout() {
            return stdout;
        }

        public InputStreamBuffering getStderr() {
            return stderr;
        }

        public int getExitStatus() {
            return session.getExitStatus().intValue();
        }

        public void close() {
            session.close();
        }
    }

    class InputStreamBuffering extends Thread {
        private InputStream input;
        private ByteArrayOutputStream output = new ByteArrayOutputStream();
        private Exception exception = null;
        private static final int BUFFER_SIZE = 4096;

        public InputStreamBuffering(InputStream _input) {
            input = _input;
            start();
        }
        
        public void run() {
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0) synchronized (output) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception ex) {
                exception = ex;
            }
        }
        
        public Exception getException() {
            return exception;
        }
        
        public byte[] getByteArray() {
            return output.toByteArray();
        }

        public String toString() {
            synchronized(output) { try{return output.toString();} finally {output.reset();} }
        }

        public String toString(String encoding) throws UnsupportedEncodingException {
            synchronized(output) { try{return output.toString(encoding);} finally {output.reset();} }
        }
    }

}
