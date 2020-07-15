package com.wzh.maoliang.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectFileIOUtils {
	public Object readObjectFromFile(String filename)
    {
		System.out.println("will read object");
        Object temp=null;
        File file =new File(filename+".txt");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
	
	public void writeObjectToFile(Object obj,String fileName)
    {
		System.out.println("will write file");
        File file =new File(fileName+".txt");
        FileOutputStream out;
        try {
		    out = new FileOutputStream(file);
		    ObjectOutputStream objOut=new ObjectOutputStream(out);
		    objOut.writeObject(obj);
		    objOut.flush();
		    objOut.close();
		    System.out.println("write object success!");
		} catch (IOException e) {
		    System.out.println("write object failed");
	        e.printStackTrace();
    }
}
}
