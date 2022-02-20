package com.thimat.sockettelkarnet.socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Socket_Connect {

    //--------------------------------------
//    private static final String IP = "94.130.71.170";
    private static final String IP = "telcarnet.com";
    private static final int PORT = 8080;
    private static SendSMS sendSMS;
    Socket socket;
    private static boolean ConnectFlag = true;
    private static String Err;
    String SMS;
    private static boolean S_Flag = false;
    private static Socket _socket;
    static Handler uiHandler;
    private static InputStream dis;
    private static OutputStream dos;
    TextView edt;
    private static DataInputStream inp;
    public BufferedReader input;
    public String msg = null;
    public static DataOutputStream out;
    private static final byte[] rxb = new byte[130720];
    String sms;
    private static String temp = null;
    private static SessionManager sessionManager;
    private static Context context;

    //-----------------------------------
    public Socket_Connect(Context context, Handler uiHandler) {
        Socket_Connect.uiHandler = uiHandler;
        Socket_Connect.context = context;
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(context);
        S_Flag = false;
        new Thread(new Socket_Connect.CommunicationThread()).start();

    }

    public void socketDisconnect() {
        try {
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean isConnected() {
       return _socket.isConnected();

    }


    //-------------------------------------------------------------- send message to socket
    public static void SendMessage(String msg, String sms_Text, boolean register) {

            if (sessionManager.getConnectType() == Util.sendASMS) {
                if (!sms_Text.equals(""))
                    sendSMS.sendsms(sms_Text, sessionManager.getPhonenumbr());
                else   sendMessageSocket(msg, sms_Text, register);

            } else if (sessionManager.getConnectType() == Util.sendAutomatic) {
                sendMessageAutomatic(msg, sms_Text, register);
            } else if (sessionManager.getConnectType() == Util.sendSocket) {
                sendMessageSocket(msg, sms_Text, register);
            }


    }

    //----------------------------------------------------------sendMessageAutomatic
    private static void sendMessageAutomatic(String msg, String sms_Text, boolean register) {
        if (Util.checkInternet(context)) {
            try {

                new Thread(new Runnable() {
                    public void run() {

                        String str = "Send SMS Error! : ";
                        StringBuilder sb = new StringBuilder();
                        sb.append(msg);

                        sb.append("\r\n");
                        String st = sb.toString();
                        try {
                            if (_socket.isConnected() && S_Flag) {
                                PrintWriter out1 = new PrintWriter(new BufferedWriter(
                                        new OutputStreamWriter(_socket.getOutputStream())), true);
                                out1.print(st);
                                out1.flush();

                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Send SMS Success! : ");

                                sb2.append(st);
                                CreateMessage(sb2.toString(), 0, 6);
                            } else {
                                if (!register) {
                                    if (!sms_Text.equals(""))
                                        sendSMS.sendsms(sms_Text, sessionManager.getPhonenumbr());
                                }
                            }
                        } catch (IOException ex) {
                            Err = ex.getMessage();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(Err);
                            CreateMessage(sb3.toString(), 0, 7);
                            if (!register) {
                                if (!sms_Text.equals(""))
                                    sendSMS.sendsms(sms_Text, sessionManager.getPhonenumbr());
                            }
                        } catch (Exception c) {
                            Err = c.getMessage();
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str);
                            sb4.append(Err);
                            CreateMessage(sb4.toString(), 0, 8);
                            if (!register) {
                                if (!sms_Text.equals(""))
                                    sendSMS.sendsms(sms_Text, sessionManager.getPhonenumbr());
                            }
                        }
                    }
                }).start();
            } catch (Exception e) {
                Err = e.getMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("Send SMS Error! : ");
                sb.append(Err);
                CreateMessage(sb.toString(), 0, 9);
                if (!register) {
                    if (!sms_Text.equals(""))
                        sendSMS.sendsms(sms_Text, sessionManager.getPhonenumbr());
                }
            }
        } else {
            if (!register) {
                if (!sms_Text.equals(""))
                    sendSMS.sendsms(sms_Text, sessionManager.getPhonenumbr());
            }
        }
    }

    //----------------------------------------------------------sendMessageSocket
    private static void sendMessageSocket(String msg, String sms_Text, boolean register) {

        if (Util.checkInternet(context)) {
            try {


                new Thread(new Runnable() {

                    public void run() {

                        String str = "Send SMS Error! : ";
                        StringBuilder sb = new StringBuilder();
                        sb.append(msg);
                        sb.append("\r\n");
                        String st = sb.toString();
                        try {
                            if (_socket.isConnected() && S_Flag) {
                                PrintWriter out1 = new PrintWriter(new BufferedWriter(
                                        new OutputStreamWriter(_socket.getOutputStream())), true);
                                out1.print(st);
                                out1.flush();

                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Send SMS Success! : ");
                                sb2.append(st);
                                CreateMessage(sb2.toString(), 0, 6);
                            }
                        } catch (IOException ex) {
                            Err = ex.getMessage();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(Err);
                            CreateMessage(sb3.toString(), 0, 7);
                        } catch (Exception c) {
                            Err = c.getMessage();
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str);
                            sb4.append(Err);
                            CreateMessage(sb4.toString(), 0, 8);

                        }
                    }
                }).start();
            } catch (Exception e) {
                Err = e.getMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("Send SMS Error! : ");
                sb.append(Err);
                CreateMessage(sb.toString(), 0, 9);
            }
        }
    }

    //----------------------------------------------------------------
    public static class CommunicationThread implements Runnable {
        String send_msg = "";

        public CommunicationThread() {
        }

        public void run() {

            try {
                ConnectFlag = true;
                while (!Thread.currentThread().isInterrupted() && ConnectFlag) {
                    try {
                        if (!S_Flag) {
                            try {
                                _socket = new Socket(IP, PORT);
                                dos = _socket.getOutputStream();
                                out = new DataOutputStream(dos);
                                dis = _socket.getInputStream();
                                inp = new DataInputStream(dis);
                                S_Flag = true;
                                CreateMessage(PublicClass.Msg_Connect, 0, 0);
                                //----------------------------------- for register to socket

                            } catch (IOException exp) {
                                S_Flag = false;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Connect Failed!  ");
                                sb.append(exp.getMessage());
                                CreateMessage(sb.toString(), 0, 1);
                                Thread.sleep(60000, 1);
                            } catch (Exception e) {
                                S_Flag = false;
                                CreateMessage(PublicClass.Msg_ConnectFailure, 0, 2);

                                Thread.sleep(120000);
                            }
                        } else {
                            try {
                                temp = null;
                                Arrays.fill(rxb, (byte) 0);
                                int k = inp.read(rxb);
                                temp = new String(rxb, 0, k, StandardCharsets.UTF_8);
                                Log.e("SocketConnect","temp==="+temp);

                                CreateMessage(temp, 1, k);
                                S_Flag =true;


                            } catch (Exception e2) {
                                CreateMessage(PublicClass.Msg_ReadingError, 0, 3);
                                Thread.sleep(120000);
                                S_Flag = false;
                            }
                        }
                    } catch (Exception e3) {
                        S_Flag = false;
                        CreateMessage(PublicClass.Msg_ExitWhile, 0, 4);

                        Thread.sleep(120000);
                    }
                }
            } catch (Exception e4) {
                CreateMessage(PublicClass.Msg_unKnownError, 0, 5);
                S_Flag = false;

            }
        }
    }


    static void CreateMessage(String Obj, int Parm1, int Parm2) {
        if (uiHandler != null) {

            Message msg2 = Message.obtain(uiHandler);
            msg2.obj = Obj;
            msg2.arg1 = Parm1;
            msg2.arg2 = Parm2;
            uiHandler.sendMessage(msg2);

        }

    }

}