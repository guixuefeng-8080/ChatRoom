package net.qiujuer.lesson.sample.client;

import net.qiujuer.lesson.sample.client.bean.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClientTest {


    private static boolean done = false;
    public static void main(String[] args) throws IOException {

        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if(info==null){
            return;
        }
        int size = 0;
        final List<TCPClient> tcpClients = new ArrayList<>();
        for (int i = 0;i<50;i++){
            try {
                TCPClient tcpClient = TCPClient.startWith(info);
                if(tcpClient==null){
                    System.out.println("链接异常-continue");
                    continue;
                }
                System.out.println("链接成功 size:"+(++size));
                tcpClients.add(tcpClient);
            } catch (IOException e) {
                System.out.println("链接异常");
            }
            try {
                Thread.sleep(20);//服务器当前正在连接的数量达到50时,就会拒绝后面等待连接的客户端;为了避免此种情况，sleep 20毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int a = Integer.parseInt(reader.readLine());
        System.out.println("a=" + a);
        Runnable runnable = () -> {
            while (!done){
                for(TCPClient tcpClient : tcpClients){
                    tcpClient.send("HELLO");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        int b = Integer.parseInt(reader.readLine());
        System.out.println("b=" + b);
        done = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (TCPClient tcpClient : tcpClients) {
            tcpClient.exit();
        }
    }
}
