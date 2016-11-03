package com.iamsubhranil.personal;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;

/**
 * Author : Nil
 * Date : 11/3/2016 at 8:07 AM.
 * Project : HammingStream
 */
public class HammingWriter {

    private static final Object locker = new Object();
    private static final int bitLimit = 500;
    private static final LinkedList<Bit> bitLinkedList = new LinkedList<>();
    public static SimpleStringProperty readProperty = new SimpleStringProperty("Starting read..");
    public static SimpleStringProperty writeProperty = new SimpleStringProperty("Starting write..");
    private static WriterThread queueWriter = new WriterThread();
    private static boolean write = true;
    private static boolean read = true;
    private static int writeCount = 0;
    private static int readCount = 0;
    private static File rootDir = new File(".hstemp");
    private static String FILE_NAME = "test.txt";
    private static boolean isCompleted = false;
    private static BitStream bitSynchronousQueue = new BitStream();

    static {
        if (rootDir.exists()) {
            if (!rootDir.delete()) {
                System.err.println("Unable to initiate! Close any other instance of this program if running, and try again.");
                System.exit(1);
            }
        }
        try {
            rootDir = Files.createTempDirectory(new File("").toPath(), ".hstream").toFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to initiate! Close any other instance of this program if running, and try again.");
            System.exit(1);
        }
    }

    private static BitStream convertIntToBit(int i) {
        int counter = 7;
        BitStream bits = new BitStream();
        bits.ensureMinimumExtraCapacity(8);
        while (counter >= 0) {
            bits.set(counter, new Bit(i % 2));
            i = (i - (i % 2)) / 2;
            counter--;
        }
        return bits;
    }

    private static void readAndProduce() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        int i;
        System.out.println("Reader : Reading original file and dumping to temp.bits..");
        while ((i = fileInputStream.read()) != -1) {
            convertIntToBit(i).forEach(bit -> {
                //       bitSynchronousQueue.add(bit);
                bitLinkedList.addLast(bit);
                readCount++;
                //         StatusViewer.refreshStatus();

                if (!queueWriter.isAlive()) {
                    System.out.println("Reader : Starting queueWriter with " + (readCount - writeCount) + " pending bits..");
                    queueWriter = new WriterThread();
                    queueWriter.start();
                }
                if (bitLinkedList.size() > bitLimit) {
                    System.out.println("Reader : Waiting for writer to finish..\nReader : Current Size : " + bitLinkedList.size());
                    synchronized (locker) {
                        try {
                            locker.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Reader : Resuming read..");
                }
            });
        }
        System.out.println("Reader : Reading completed..");
        fileInputStream.close();
        read = false;
    }

    public static int[] getStatus() {
        return new int[]{readCount, writeCount};
    }

    public static void main(String[] args) {
        ReaderThread readerThread = new ReaderThread();
        readerThread.start();
        UpdaterThread updaterThread = new UpdaterThread();
        updaterThread.start();
    }

    private static class ReaderThread extends Thread {

        public void run() {
            try {
                readAndProduce();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class UpdaterThread extends Thread {
        public void run() {
            while (read || write) {
                Platform.runLater(() -> {
                    readProperty.setValue(!read ? "Reading completed.." : "Read " + readCount + " bits..");
                    writeProperty.setValue(!write ? "Writing completed.." : "Wrote " + writeCount + " bits..");
                });

                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class WriterThread extends Thread {

        public void run() {
            int sessionCount = 0;
            write = true;
            System.out.println(getName() + " Writer : Started..");
            while (bitLinkedList.size() > 0) {
                //      Bit bit = bitSynchronousQueue.get(writeCount);
                Bit bit;
                synchronized (bitLinkedList) {
                    bit = bitLinkedList.removeFirst();
                }
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(rootDir + "\\" + writeCount + ".bit"));
                    objectOutputStream.writeObject(bit);
                    objectOutputStream.close();
                    //            System.out.println("Wrote bit "+(writeCount)+"..");
                    //            bitSynchronousQueue.set(writeCount,null);
                    writeCount++;
                    sessionCount++;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println(getName() + " Writer : Unable to write bit " + (writeCount + 1) + " with " + bit + " information!");
                }
            }
            write = false;
            System.out.println(getName() + " Writer : Wrote " + sessionCount + " bits in current session..");
            System.out.println(getName() + " Writer : Notifying reader if waiting..)");
            synchronized (locker) {
                try {
                    locker.notify();
                } catch (Exception e) {
                    System.out.println(getName() + " Writer : Noone was waiting!");
                }
            }
            System.out.println(getName() + " Writer : Session completed..");
        }

    }

}
