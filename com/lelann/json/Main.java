package com.lelann.json;

import java.io.File;
import java.util.Date;

import com.lelann.json.element.JObject;

public class Main {
	public static void main(String[] args){
		long tempsLecture = 0,
			tempsEcriture = 0,
			tempsTotal = 0;
		int fois = 0, sleepTime = 10;
		long time = new Date().getTime();
		for(int i=0;i<1000;i++){
			long t = new Date().getTime();
			
			JObject jo = JSON.load(new File("file.json"));
			
			long t2 = new Date().getTime();
	
			jo.save(new File("file3.json"));
			
			tempsLecture += (t2 - t);
			tempsEcriture += (new Date().getTime() - t2);
			tempsTotal += (new Date().getTime() - t);
			
//			System.out.println(jo);
			new File("file3.json").delete();
			fois++;
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long time2 = new Date().getTime() - sleepTime * 1000;
		System.out.println("Lecture : " + tempsLecture);
		System.out.println("Enregistrement : " + tempsEcriture);
		System.out.println("Total : " + tempsTotal);
		System.out.println("Fois : " + fois);
		
		System.out.println("Temps d'excution : " + (time2 - time));
	}
}
