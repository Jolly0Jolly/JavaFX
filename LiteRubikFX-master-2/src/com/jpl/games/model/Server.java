package com.jpl.games.model;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;


public class Server implements Runnable {
    private int port;
    public dataModel datas = new dataModel();
    public dataModel Orderdata = new dataModel();
    //  private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inStream = null;
    private Set<String> userNames = new HashSet<>();


    public Server(Socket socket, dataModel data) {
        this.socket = socket;
        this.datas = data;
    }

    @Override
    public void run() {
        System.out.println("新对象即将上传");
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        try {
            System.out.println("有客户端连入");
            is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                System.out.println("服务端");
                System.out.println(datas.toString());
                outputStream.writeObject(datas.cloneThis());

                datas.change = "A";

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源即相关socket
            try {
                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * Delivers a message from one user to others (broadcasting)
//     */
//    void broadcast(String message, UserThread excludeUser) {
//        for (UserThread aUser : userThreads) {
//            if (aUser != excludeUser) {
//                aUser.sendMessage(message);
//            }
//        }
//    }
//
//    /**
//     * Stores username of the newly connected client.
//     */
//    void addUserName(String userName) {
//        userNames.add(userName);
//    }
//
//    /**
//     * When a client is disconneted, removes the associated username and UserThread
//     */
//    void removeUser(String userName, UserThread aUser) {
//        boolean removed = userNames.remove(userName);
//        if (removed) {
//            userThreads.remove(aUser);
//            System.out.println("The user " + userName + " quitted");
//        }
//    }
//
//    Set<String> getUserNames() {
//        return this.userNames;
//    }
//
//    /**
//     * Returns true if there are other users connected (not count the currently connected user)
//     */
//    boolean hasUsers() {
//        return !this.userNames.isEmpty();
//    }
//

}
