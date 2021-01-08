package com.jpl.games.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private String hostname;
    private int port;
    private Rubik rubik;

    public void setDatas(dataModel Cdata) {
        this.datas = Cdata;
    }

    public dataModel datas = new dataModel();
    private String userName;

    public Client(Socket socket, Rubik rubik) {
        this.socket = socket;
        this.rubik = rubik;
//               this.student=student;
    }


    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }

    @Override
    public void run() {
        try {
            System.out.println("连接上服务器了!");
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true) {
                datas = (dataModel) ois.readObject();
                System.out.println(ois.readObject().toString());
                switch (datas.order){
                    case 1:
                        rubik.content.cameraXform2.setTx(datas.Tx);
                        rubik.content.cameraXform2.setTy(datas.Ty);
                        break;
                    case 2:
                        rubik.content.cameraXform.setRy(datas.ry);
                        rubik.content.cameraXform.setRx(datas.rx);
                        break;
                    case 3:
                        rubik.content.cameraPosition.setZ(datas.dragZ);
                        break;
                    case 4:
                        rubik.content.cameraPosition.setZ(datas.getzScroll());
                        break;
                    case 5:
                        rubik.content.cameraPosition.setZ(datas.getzZoom());
                        break;
                    case 0:
                        break;
                }
                if (!datas.change.equals("A")){
                    rubik.rotateFace(datas.change);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

