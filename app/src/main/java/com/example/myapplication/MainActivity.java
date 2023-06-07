package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.SuppressLint;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    //CLIENT
    Thread Thread1 = null;
    EditText etIP, etPort;
    TextView tvMessages;
    EditText etMessage;
    Button btnSend;
    String SERVER_IP;
    int SERVER_PORT;
    int s1=0;
    int s2=0;
    int filecount=0;
    String slaveid,tid;

   Double minimum=Double.MAX_VALUE;
    HashMap<String, Double> hm;
    int numOfProcessors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        tvMessages = findViewById(R.id.tvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        Button btnConnect = findViewById(R.id.btnConnect);

        hm=new HashMap<String, Double>();
        numOfProcessors =Runtime.getRuntime().availableProcessors();
        Log.d("Number of processors", String.valueOf(numOfProcessors));

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    new Thread(new Thread3(message)).start();
                }
            }
        });
    }
    DataInputStream input = null;
    DataOutputStream output = null;
     BufferedReader in =null ;
    class Thread1 implements Runnable {
        @Override
        public void run() {
            Socket socket;
            try {

                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new DataOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               // input = new DataInputStream((socket.getInputStream()));
                output.writeInt(numOfProcessors);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessages.setText("Connected ");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    int p=0;
    HashMap<String,Integer> hashmap=new HashMap<String,Integer>();
    HashMap<Integer,String> hashmap2=new HashMap<Integer,String>();
    ArrayList<String> seq1=new ArrayList<String>();
    ArrayList<String> seq2=new ArrayList<String>();
    class Thread2 implements Runnable {







        @Override
        public void run() {

            try {
                boolean flag=false;
               FileWriter fw;
                String line="";
                fw = new FileWriter("/sdcard/testsig1.txt");
              //  fw = new FileWriter("/sdcard/temperatureregion.txt");
                //  BufferedWriter writer = new BufferedWriter(new FileWriter("/sdcard/temperature.txt"));
                //from get input in string
filecount=1;
                while ((line = in.readLine()) != null) {
                    Log.d("Line",line);
                    /*//new
                    if(!hashmap.containsKey(line.split(",")[0]))
                    {
                        hashmap2.put(index,line.split(",")[0]);
                        hashmap.put(line.split(",")[0],index++);
                    }

                    //end*/

                    if(line.equals("end"))
                    {
                        fw.close();
                       // line = in.readLine();
                      //  slaveid=Integer.parseInt(line);
                        fw = new FileWriter("/sdcard/seq"+filecount+".txt");
                        flag=true;
                        continue;
                    }
                    if(line.equals("cons"))
                    {
                        fw.close();
                        filecount++;
                        // line = in.readLine();
                        //  slaveid=Integer.parseInt(line);
                        fw = new FileWriter("/sdcard/seq"+filecount+".txt");
                        flag=true;
                        continue;
                    }

                    Log.d("Deubg4",line);
                    if(line.equals(""))
                    {
                        Log.d("Deubg1","debug");
                        break;
                    }
                    fw.write(line+"\n");
                    if(!flag)
                    {
                        seq1.add(line);
                        s1++;
                    }


                }
                fw.close();
                Log.d("Deubg1","debug");

                Log.d("s1",""+s1);
               // Log.d("s2",""+s2);






              /*  Log.d("Deubg4",String.valueOf(p));

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int bytesRead;
                int totalbytes=0;
                Log.d("Deubg1","debug");
                while (true) {
                    if(input.available()>0)
                    {
                        p=(input.readInt());
                        byte[] data = new byte[p];
                        if((bytesRead = input.read(data, 0, p)) != -1)
                        {
                            buffer.write(data, 0, p);
                            totalbytes+=bytesRead;
                        }
                    }
                    else
                    {
                        break;
                    }




                }
                Log.d("Deubg2","debug");
                Log.d("Deubg3",buffer.toString());
                fw.write(buffer.toByteArray());
                fw.close();*/
                //  fw.write("1");
                // writer.close();
                  /* if (message != null) {
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tvMessages.setText("server: " + j + message + " ");
                              j++;
                         }
                      });*/
               split();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    int rw=0;
    int count=0;






    void split() {
        try {
            byte b[] = new byte[p/numOfProcessors];
            int x = 0, y = 0;
            String s = "";
            // InputStreamReader ins = new InputStreamReader(System.in);
            //BufferedReader br = new BufferedReader(ins);

            //System.out.println("Enter path of the file ");
            //String path = br.readLine();
            File f = null;
            f = new File("/sdcard/temperature.txt");

            long k=f.length();
            Log.d("Number of processors",String.valueOf(numOfProcessors));
            long currentFileSize = 0;
            long parts=(int)(k/numOfProcessors);
            String path = "/sdcard/temperature.txt";
            BufferedReader reader = new BufferedReader(new FileReader("/sdcard/seq2.txt"));
            int read_bytes;
            String line="";
//new
          /*  BufferedWriter writer[]=new BufferedWriter[hashmap.size()];
            for(int i=0;i<hashmap.size();i++)
            {
                s = "/sdcard/" + hashmap2.get(i) + ".txt";
                Log.d("Client",s);
                File outputFiles = new File(s);
                writer[i]= new BufferedWriter(new FileWriter(outputFiles));
            }

            while ((line = reader.readLine()) != null) {
            String data[]=line.split(",");

                x=hashmap.get(data[0]);
                writer[x].write( data[1]+","+data[2]+","+data[3]+","+data[4]);
                writer[x].newLine();


            }
            for(int i=0;i<hashmap.size();i++)
            {
                //  s = "/sdcard/new" + i + ".txt";
                writer[i].close();
            }
*/
            //end
//old
           /* int window=(s2-s1)+1;
            BufferedWriter writerx[]=new BufferedWriter[filecount];
            for(int i=1;i<=filecount;i++)
            {
                s = "/sdcard/seq" + i + ".txt";
                Log.d("Client",s);
                File outputFiles = new File(s);
                writerx[i-1]= new BufferedWriter(new FileWriter(outputFiles));
            }

         for(int i=0;i<s2-s1+1;i++)
         {

            for (int in=i;in<s1+i;in++) {
                writerx[x].write(seq2.get(in));
                writerx[x].newLine();
            }
            x=x+1;

            }
            for(int i=0;i<s2-s1+1;i++)
            {
                //  s = "/sdcard/new" + i + ".txt";
                writerx[i].close();
            }*/
//end




              /*  if ((currentFileSize + line.length()) > parts) {
                    // Close the current output file
                    Log.d(line+"Client has","File"+x +"splitted successfully.");

                    writer.close();


                    // Increase the file count and create a new output file and writer
                    x=x+1;
                    s = "/sdcard/new" + x + ".txt";
                    outputFile = new File(s);
                    writer = new BufferedWriter(new FileWriter(outputFile));

                    // Reset the current file size to 0
                    currentFileSize = 0;
                }
                writer.write(line);
                writer.newLine();
                currentFileSize += line.length();
                Log.d("Client",String.valueOf(x));

            }*/

           // writer.close();


            //end

            reader.close();





            // System.out.println("File splitted successfully.");
            //fis.close();

            //searching
//old
            searching search[]=new searching[filecount];
            for(int i=1;i<=filecount;i++)
            {
                search[i-1]=new searching("/sdcard/seq"+i+".txt",i);
                search[i-1].start();

            }
            for(int i=0;i<filecount;i++)
            {
                search[i].join();
            }
      //end

      /*//new
            searching search[]=new searching[hashmap.size()];
            for(int i=0;i<hashmap.size();i++)
            {
                search[i]=new searching("/sdcard/chunk"+i+".txt");
                search[i].start();

            }
            for(int i=0;i< hashmap.size();i++)
            {
                search[i].join();
            }
      //end*/



            ObjectMapper objectMapper = new ObjectMapper();
            hm.put(slaveid+" "+tid,minimum);
            String json = objectMapper.writeValueAsString(hm);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvMessages.setText("\nunique words: "+ hm.size() + "\n");
                    tvMessages.setText((slaveid+tid)+" "+minimum +"\n");
                    rw++;

                }
            });

//output.writeUTF(json);
            output.writeInt(String.valueOf(json).getBytes().length);
            output.write(String.valueOf(json).getBytes());
            count=0;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    Object lock = new Object();
    class searching extends Thread {

        String path;
        int id;
long preva=-1,prevb=-1;
        ArrayList<String> seq1 = new ArrayList<String>();
        ArrayList<String> seq2 = new ArrayList<String>();
        searching(String p,int id)
        {
            path=p;
            this.id=id;
        }

        @Override
        public void run() {
            try {

                String s = "", g;
                FileReader rdr = new FileReader(path);
                BufferedReader br = new BufferedReader(rdr);
                BufferedReader br2 = new BufferedReader(new FileReader("/sdcard/testsig1.txt"));
               // FileReader rdr = new FileReader(path);
             //   BufferedReader br2 = new BufferedReader(new FileReader("/sdcard/seq1.txt"));
                //Toast.makeText( "There is file to read data. ", Toast.LENGTH_SHORT).show();
                //BufferedReader br=new BufferedReader(rdr);
                Log.d("Client",path);
                //  char[] inputBuffer = new char[1024];//get Block size as buffer
                // String s = "",g;
                int i = 0, j = 0;
               double[][] dtwmatrix = new double[s1 + 1][s1 + 1];
                //  tvMessages.setText("");




                    // Initialize the DTW matrix

                    dtwmatrix[0][0] = 0;
int op=1,oj=1;
                while ((g = br.readLine()) != null) {
                    seq1.add(g);

                }
                while((s=br2.readLine())!=null){
                    seq2.add(s);
                }
                    // Compute the DTW matrix

                for(int k=0;k< seq1.size()-seq2.size();k++) {
                    for (i = 0; i <= seq2.size(); i++) {
                        Arrays.fill(dtwmatrix[i], Double.MAX_VALUE);
                    }
                    dtwmatrix[0][0]=0;

                    for ( i = 1; i <=seq2.size(); i++) {

                        //   BufferedReader br2 = new BufferedReader(new FileReader("/sdcard/seq1.txt"));
                        for ( j = 1; j <=seq2.size(); j++) {
                            double cost = Math.abs(Double.parseDouble(seq1.get(k+j-1)) - Double.parseDouble(seq2.get(i-1)));
                           // cost=cost*cost;
                            dtwmatrix[i][j] = cost + Math.min(dtwmatrix[i - 1][j], Math.min(dtwmatrix[i][j - 1], dtwmatrix[i - 1][j - 1]));
                            // Log.d(op+" "+oj,dtwMatrix[op][oj]+"");
                            //  oj++;
                        }
                        //  br2.close();
                        // op++;
                    }
                    synchronized(lock) {
                        if(dtwmatrix[s1][s1]<minimum)
                        {
                            minimum=dtwmatrix[s1][s1];
                            tid=path+" "+k ;
                        }

                    }
                }








                   /* //j++;
                    //((TextView) findViewById(R.id.tv2)).setText(String.valueOf(j));
                    //s+="\n"+g;
                    synchronized(lock) {
                        Integer count = hm.get(g);
                        if (count == null) {
                            count = 0;
                        }

                        hm.put(g, count + 1);
                    }
                    // j++;*/
                br.close();
                rdr.close();

                } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Log.d("Word count",String.valueOf(minimum));

                //((TextView) findViewById(R.id.text1)).setText(String.valueOf(count));
                // ((TextView) findViewById(R.id.tv1)).setText(String.valueOf(i));
                //int charRead = rdr.read(inputBuffer);
                //Read all data one by one by using loop and add it to string created above
                // for (int k = 0; k < charRead; k++) {
                //   savedData += inputBuffer[k];
                //}
                // Data is displayed in the TextView
                // ((TextView) findViewById(R.id.tv2)).setText(s);
              //  br.close();
                //isr.close();
             //   rdr.close();

        }
    }



    class Thread3 implements Runnable {

        private String message;
        Thread3(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                // output.writeUTF(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tvMessages.append("client: " + message + " ");
                    etMessage.setText("");
                }
            });
            try {


                //output.flush();
            }
            catch(Exception e)
            {

            }
        }

    }
}