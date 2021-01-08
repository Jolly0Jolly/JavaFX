package com.jpl.games;

import com.jpl.games.model.Client;
import com.jpl.games.model.Rubik;
import com.jpl.games.model.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

/**
 *
 * @author by me
 */
public class LiteRubikFX extends Application {
    
    private final BorderPane pane=new BorderPane();
    private Rubik rubik;
    
    @Override
    public void start(Stage stage) {
        /*
        Import Rubik's Cube model and arrows
        */
        rubik=new Rubik();
//        create the connection Button

        // create toolbars
        ToolBar tbTop=new ToolBar(new Button("U"),new Button("Ui"),new Button("F"),
                new Button("Fi"),new Separator(),new Button("Y"),
                new Button("Yi"),new Button("Z"),new Button("Zi"),new Button("B"),new Button("Bi"),new Button("D"),
                new Button("Di"),new Button("E"),new Button("Ei"));
        pane.setTop(tbTop);



        ToolBar tbRight=new ToolBar(new Button("R"),new Button("Ri"),new Separator(),
                new Button("X"),new Button("Xi"));
        tbRight.setOrientation(Orientation.VERTICAL);
        pane.setRight(tbRight);
        ToolBar tbLeft=new ToolBar(new Button("L"),new Button("Li"),new Button("M"),
                new Button("Mi"),new Button("S"),new Button("Si"));
        tbLeft.setOrientation(Orientation.VERTICAL);
        pane.setLeft(tbLeft);

        pane.setCenter(rubik.getSubScene());

        pane.getChildren().stream()
                .filter(n -> (n instanceof ToolBar))
                .forEach(tb->{
                    ((ToolBar)tb).getItems().stream()
                            .filter(n -> (n instanceof Button))
                            .forEach(n->((Button)n).setOnAction(e->rubik.rotateFace(((Button)n).getText())));
                });
        rubik.isOnRotation().addListener((ov,b,b1)->{
            pane.getChildren().stream()
                    .filter(n -> (n instanceof ToolBar))
                    .forEach(tb->tb.setDisable(b1));
        });
        final Scene scene = new Scene(pane, 880, 680, true);
        scene.setFill(Color.ALICEBLUE);
        stage.setTitle("Rubik's Cube - JavaFX3D");
        stage.setScene(scene);
//
        Button SeverMachine = new Button("Sever");
//        SeverMachine.setOnAction(
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        final Stage dialog = new Stage();
//                        dialog.initModality(Modality.APPLICATION_MODAL);
//                        dialog.initOwner(stage);
//                        Button okbtn = new Button("OK");
//                        okbtn.setDisable(true);
//                        FlowPane pane = new FlowPane();
//
//                        pane.setPadding(new Insets(11, 12, 13, 14));
//
//                        pane.setHgap(5);//设置控件之间的垂直间隔距离
//
//                        pane.setVgap(5);//设置控件之间的水平间隔距离
//                        Label lbName = new Label("Please input a IP address:");
//
//                        TextField tfName = new TextField();
//
//                        Label lbPassword = new Label("Please input a port number:");
//
//                        TextField tfPassword = new TextField();
//                        tfPassword.textProperty().addListener(new ChangeListener<String>() {
//                            @Override
//                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                                if (tfPassword.getLength() == 0||tfName.getLength()==0){
//                                    okbtn.setDisable(true);
//                                }else {
//                                    okbtn.setDisable(false);
//                                }
//                            }
//                        });
//                        okbtn.setOnAction(new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent event) {
//                                dialog.close();
////                                Client client = new Client(tfName.getText(),Integer.valueOf(tfPassword.getText()));
////                                while(true) {
////                                    client.setDatas(rubik.content.datas);
////                                    client.communicate();
////                                }
//                            }
//                        });
//
//
//
//                        pane.getChildren().addAll(lbName,tfName,lbPassword,tfPassword,okbtn);
//
//                        Scene dialogScene = new Scene(pane, 200, 200);
//                        dialog.setScene(dialogScene);
//                        dialog.show();
//                    }
//                });
        Button Connection = new Button("Connection");
        Connection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // IP Port String Pair and Name String consisted pair
                Dialog<Pair<Pair<String, String>, String>> dialog = new Dialog<>();
                dialog.setTitle("连接主机");
                dialog.setHeaderText("请输入你想要连接的主机和端口号,并且输入你的名字");

                // 设定两个btn的type
                ButtonType startConnBtnType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(startConnBtnType, ButtonType.CANCEL);

                // 初始化 gridPane
                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);

                // 三个需要输入的东西的TextField 以及他们旁边放的label
                TextField ip = new TextField();
                ip.setPromptText("输入你想连接的主机IP");
                TextField port = new TextField();
                port.setPromptText("输入你想连接的主机的端口号");
                TextField name = new TextField();
                name.setPromptText("输入你的名字");

                gridPane.add(new Label("Host IP:"), 0, 0);
                gridPane.add(new Label("Host port:"), 0, 1);
                gridPane.add(new Label("Name:"), 0, 2);
                gridPane.add(ip, 1, 0);
                gridPane.add(port, 1, 1);
                gridPane.add(name, 1, 2);

                // Enable/Disable login button depending on whether infos was entered
                Node connButton = dialog.getDialogPane().lookupButton(startConnBtnType);
                connButton.setDisable(true);
                name.textProperty().addListener((observable, oldValue, newValue) -> {
                    String ipStr = ip.textProperty().get();
                    String portStr = port.textProperty().get();
                    // 用trim去掉空格,防止只有空格的情况,其他的不做判断了..
                    if (!(ipStr.trim().isEmpty() || portStr.trim().isEmpty())) {
                        connButton.setDisable(newValue.trim().isEmpty());
                    } else
                        connButton.setDisable(true);
                });

                dialog.getDialogPane().setContent(gridPane);

                // Request focus on the ip field by default.
                Platform.runLater(() -> ip.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == startConnBtnType) {
                        return new Pair<>(new Pair<>(ip.getText(), port.getText()), name.getText());
                    }
                    return null;
                });


                Optional<Pair<Pair<String, String>, String>> result = dialog.showAndWait();
                result.ifPresent(ipPortAndNamePair -> {
                    Pair<String, String> ipPortPair = ipPortAndNamePair.getKey();
                    System.out.println("ip=" + ipPortPair.getKey() + ", port=" + ipPortPair.getValue() + ", name=" + ipPortAndNamePair.getValue());
                });
                Pair<String, String> ipPortPair = result.get().getKey();
                String ipStr = ipPortPair.getKey();
                String portStr = ipPortPair.getValue();
                String nameStr = result.get().getValue();

                // TODO socket part
                try {
                    Socket socket = new Socket(ipStr, new Integer(portStr));
                    Thread thread = new Thread(new Client(socket,rubik));
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*--------------------------------*/


            }
        });
        SeverMachine.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("开放主机");
                dialog.setHeaderText("请输入你想开放的端口号以及你的名字");

                ButtonType openBtnType = new ButtonType("开放", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(openBtnType, ButtonType.CANCEL);

                // 初始化grid
                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);

                TextField port = new TextField();
                TextField name = new TextField();
                port.setPromptText("请输入你想开放的端口");
                name.setPromptText("请输入你的名字");

                gridPane.add(new Label("Host port:"), 0, 0);
                gridPane.add(port, 1, 0);
                gridPane.add(new Label("Name:"), 0, 1);
                gridPane.add(name, 1, 1);

                Node openButton = dialog.getDialogPane().lookupButton(openBtnType);
                openButton.setDisable(true);
                name.textProperty().addListener(((observable, oldValue, newValue) -> {
                    String portStr = port.textProperty().get();
                    if (!portStr.trim().isEmpty())
                        openButton.setDisable(newValue.trim().isEmpty());
                    else
                        openButton.setDisable(true);
                }));

                dialog.getDialogPane().setContent(gridPane);

                // request focus on the port field by default
                Platform.runLater(() -> port.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == openBtnType) {
                        return new Pair<>(port.getText(), name.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(portNamePair -> {
                    System.out.println("port=" + portNamePair.getKey() + ", name=" + portNamePair.getValue());
                });
                String portStr = result.get().getKey();
//                String nameStr = result.get().getValue();

                // TODO socket part

                new Thread() {
                    Socket socket = null; // 和本线程相关的socket

                    ServerSocket serverSocket = null;

                    @Override
                    public void run() {
                        try {
                            serverSocket = new ServerSocket(new Integer(portStr));
                            while (true) {
                                socket = serverSocket.accept();
                                Thread thread = new Thread(new Server(socket,rubik.content.datas));
                                thread.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }.start();

                /*-------------------------------*/
            }
        });
//        SeverMachine.setOnAction(
//                new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        final Stage dialog = new Stage();
//                        dialog.initModality(Modality.APPLICATION_MODAL);
//                        dialog.initOwner(stage);
//                        FlowPane pane = new FlowPane();
//
//                        pane.setPadding(new Insets(11, 12, 13, 14));
//
//                        pane.setHgap(5);//设置控件之间的垂直间隔距离
//
//                        pane.setVgap(5);//设置控件之间的水平间隔距离
//
//                        Label lbPassword = new Label("Please input a port number:");
//
//                        TextField tfPassword = new TextField();
//
//                        Button okbtn = new Button("OK");
//                        okbtn.setDisable(true);
//                        tfPassword.textProperty().addListener(new ChangeListener<String>() {
//                            @Override
//                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                                if (tfPassword.getLength() == 0){
//                                    okbtn.setDisable(true);
//                                }else {
//                                    okbtn.setDisable(false);
//                                }
//                            }
//                        });
//                        okbtn.setOnAction(new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent event) {
//                                dialog.close();
//                                Server server = new Server(Integer.valueOf(tfPassword.getText()));
//                                while(true) {
//                                    server.communicate();
//                                    rubik.content.cameraXform2.setTx(rubik.content.cameraXform2.t.getX() + server.datas.xFlip*server.datas.getMouseDeltaX()*0.3*server.datas.modifier*0.3);
//                                    rubik.content.cameraXform2.setTy(rubik.content.cameraXform2.t.getY() + server.datas.yFlip*server.datas.getMouseDeltaY()*0.3*server.datas.modifier*0.3);
//                                    rubik.content.cameraXform.setRy(rubik.content.cameraXform.ry.getAngle() - server.datas.yFlip*server.datas.getMouseDeltaX()*0.3*server.datas.modifier*2.0);
//                                    rubik.content.cameraXform.setRx(rubik.content.cameraXform.rx.getAngle() + server.datas.xFlip*server.datas.getMouseDeltaY()*0.3*server.datas.modifier*2.0);
//                                    rubik.content.cameraPosition.setZ(server.datas.dragZ);
//                                    rubik.content.cameraPosition.setZ(server.datas.getzScroll());
//                                    rubik.content.cameraPosition.setZ(server.datas.getzZoom());
//                                }
//                            }
//                        });
//
//                        pane.getChildren().addAll(lbPassword,tfPassword,okbtn);
//
//                        Scene dialogScene = new Scene(pane, 200, 150);
//                        dialog.setScene(dialogScene);
//                        dialog.show();
//                    }
//                });
        HBox bottomBox = new HBox(20);
        bottomBox.setPadding(new Insets(5,0,5,20));
        bottomBox.getChildren().addAll(Connection,SeverMachine);
        pane.setBottom(bottomBox);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
