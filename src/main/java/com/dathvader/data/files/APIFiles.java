package com.dathvader.data.files;

import java.io.*;

public abstract class APIFiles {

    private File file;
    private boolean doExit = false;

    public APIFiles(String fileName) throws IOException {
        this.file = new File(fileName);

        if(!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.file.createNewFile();

            System.out.println("Config "+fileName+" doesn't yet exist, creating ...");

            this.writeFully(getDefaultConfig());

            doExit = true;
        } else {
            this.readFully();
        }
    }

    private void readFully() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));

        String line = "";

        while ((line = bufferedReader.readLine()) != null) lineRead(line);

        bufferedReader.close();
    }

    private void writeFully(String[] lines) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(this.file));

        for(String line : lines) printWriter.println(line);

        printWriter.close();
    }

    public abstract void lineRead(String line);

    public abstract String[] getDefaultConfig();

    public boolean isDoExit() {
        return doExit;
    }
}
