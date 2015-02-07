package com.rappaparradi.mk900;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

public class Mk900 {
	
	public String lin, lin1, lin2, datrel, f_out1, f_out2;
	
	public int w_ioresult;
	
	public StringBuilder status_str = new StringBuilder();
	
	public static String SettingsFilePath =  "/Settings.txt";
	
	public Boolean  
	flag_H, 	/*{признак     "большой"/"малой" выборки        }*/
	flag_L, 			/*{признак     "малой" выборки                           }*/
	flag_R, 			/*{признак     "большой" выборки                         }*/
	flag_s, 			/*{признак экстремума                                    }*/
	flag_O, 			/*{признак оптимальности/неоптимальности процесса        }*/
	flag_V, 			/*{признак   увеличения/уменьшения скорости              }*/
	flag_I; 			/*{признак   увеличения/неизменности номера шага оптимиз.}*/ 
	
	
	public int
	n,      		/*параметр цикла опроса датчиков                    */
	p_int,  			/*макс. значение параметра цикла опроса датчиков        */
	j,      			/*параметр цикла "большой" и "малой" выборок            */
	j_max,  			/*макс. значение параметра цикла "бол." и "мал." выборок*/
	s,      			/*признак рысканий                                      */
	L,      			/*макс. значение параметра цикла "малой" выборки        */
	H,      			/*макс. значение параметра цикла "большой" выборки      */
	i,      			/*номер шага оптимизации                                */
	nom;     			/*номер эксперимента                                    */ 
	
	double
	t_dv,   			/*время включения исполнительного механизма [с]         */
	v,      			/*скорость комбайна [м/с]                               */
	tau,    			/*темп опроса датчиков [с]                              */
	T_H,    			/*время наблюдения для формиров. "большой" выборки [с]  */
	T_L,    			/*время наблюдения для формиров. "малой" выборки   [с]  */
	p,      			/*макс. значение цикла опроса датчиков                  */
	IpzI,   			/*допустимые потери зерна (ПЗ)                          */
	IyI,    			/*допуск на ПЗ                                          */
	IwI,    			/*допуск на значение угловой скорости (УС)              */
	lam,    			/*стандартное отношение зерна к соломе                  */
	lam_1,  			/*истинное отношение зерна к соломе                     */
	q_nom,  			/*номинальная подача хлебной массы (ПХМ)                */
	q_dat,  			/*значение ПХМ считанное с датчика                      */
	q_sr_n, 			/*значение ПХМ скользяще-усред. по циклу опроса датчика */
	q_sr,   			/*значение ПХМ скользяще-усред. по циклу выборки        */
	q_opt,  			/*оптимальное значение ПХМ                              */
	y_min,  			/*допуст. уровень помех в измер-ном канале ПЗ IyI*0.65  */
	y_dat,  			/*значение ПЗ считанное с датчика                       */
	y_sr_n, 			/*значение ПЗ  скользяще-усред. по циклу опроса датчика */
	y_sr,   			/*значение ПЗ  скользяще-усред. по циклу выборки        */
	y_opt,  			/*оптимальное значение ПЗ                               */
	w_dat,  			/*значение УС  считанное с датчика                      */
	w_sr_n, 			/*значение УС  скользяще-усред. по циклу опроса датчика */
	w_sr,   			/*значение УС  скользяще-усред. по циклу выборки        */
	w_opt,  			/*оптимальное значение УС                               */
	w_0,    			/*начальное значение УС                                 */
	delt,   			/*допустимая величина изменения угловой скорости (УС)   */
	delt_y, 			/*допуск на дрейф ПЗ                                    */
	delt_w, 			/*допуск на дрейф УС                                    */
	delt_K, 			/*допуск на дрейф целевой функции                       */
	delt_K_L,			/*значение (K_H - K_L)                                 */
	d,      			/*значение  (delt_K_L / drob)                           */
	d_1,    			/*буфер для значения d                                  */
	V0,V1,V2,			/*буферы для реализации задержки считывания V          */
	drob,   			/*значение 1-(1/(exp(T_L/T0)))                          */
	K_L,    			/*значение целевой функции "малой" выборки              */
	K_H,    			/*значение целевой функции "большой" выборки            */
	K_p,    			/*буфер для временного хранения K_L или K_H             */
	K_opt,  			/*оптимальное значение целевой функции                  */
	R,      			/*коэффициент настройки системы на оптимальный режим    */
	priz_R, 			/*признак "большой"(1),"малой"(0) выборки после нового R*/
	sigm_w, 			/*допуск на значение w_opt                              */
	sigm_q, 			/*допуск на значение q_opt                              */
	xfrom,  			/*случайное число с норм.законом распределения из файла */
	R_opt,  			/*оптимальное значение R                                */
	T0,     			/*постоянная времени объекта [с]                        */
	K_L_1,  			/* */
	K_t,    			/* */
	Tsec,   			/*Текущее время наблюдения процесса [с]                 */
	r_f,    			/*кол-во считываний Q                                   */
	q_min,  			/*min значение урожайности [ц/га]                       */
	q_max;  			/*max значение урожайности [ц/га]                       */ 
	
	Random random;

	
	public Mk900() {}
	

	/*                                                              */
	/* Вычисление q,y,w                                             */
	/*                                                              */
	/*--------------------------------------------------------------*/
	/* Описание переменных:                                         */
	/*                                                              */
	/*   b - ширина захвата комбайна [м]                            */
	/*   v_pro - текущая скорость комбайна, скорость на момент      */
	/*           съёма показателей по q,y,w                         */
	/*   xin - случайное число из массива случайных чисел, сгенери- */
	/*         рованных процедурой RAND                             */
	/*   la1 - соломистость хлебной массы (расчётный коэффициент)   */
	/*   q - подача хлебной массы [кг/с]                            */
	/*   y - потери зерна [гр./с]                                   */
	/*   w - угловая скорость молотильного барабана [1/c]           */
	/*                                                              */

	public void ran(double xin, double la1, double v_pro, String q_name, String y_name,
			String w_name)

	{

		double b, q, y, w;

		b = 6.0;
		q = 0.01 * b * v_pro * xin / (1.0 - la1);
		y = -0.02 * q + 2.7158 * q * q - 0.4288 * q * q * q + 0.022917 * q * q
				* q * q;
		/*
		 * w = 91.0 -6.543*q +3.0256*q*q -0.56194*q*q*q +0.04514*q*q*q*q
		 * -0.001343*q*q*q*q*q;
		 */
		w = 91.0 - 3.149 * q + 0.14 * q * q;
		try {
			
			setDoubleField(q_name, q);
			setDoubleField(y_name, y);
			setDoubleField(w_name, w);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		

	}
	
	public void setDoubleField(String fieldName, double value)
	        throws NoSuchFieldException, IllegalAccessException {
	    Field field = getClass().getDeclaredField(fieldName);
	    field.setDouble(this, value);
	}
	
	public void main() {
		
	  Tsec  = Double.parseDouble(GetSettingByName("Tsec"));
	  v     = Double.parseDouble(GetSettingByName("v"));
	  nom   = Integer.parseInt(GetSettingByName("nom"))     /* 1 - 100*/;
	  tau   = Double.parseDouble(GetSettingByName("tau"))   /* 0.2 - 1.0 */;
	  T_H   = Double.parseDouble(GetSettingByName("T_H"))     /* 5 - 100 */;
	  T_L   = Double.parseDouble(GetSettingByName("T_L"))     /* 1 - 5 */;
	  p     = Double.parseDouble(GetSettingByName("p"))     /* 5 - 10 */;
	  IpzI  = Double.parseDouble(GetSettingByName("IpzI"));
	  lam   = Double.parseDouble(GetSettingByName("lam"));
	  lam_1 = Double.parseDouble(GetSettingByName("lam_1"))   /* 0.2 - 1.2 */;
	  q_nom = Double.parseDouble(GetSettingByName("q_nom"))   /* 4 - 12 */;
	  delt  = Double.parseDouble(GetSettingByName("delt"))  /* 0.04 - 0.12*/;
	  delt_y= Double.parseDouble(GetSettingByName("delt_y"))  /* 0.01 - 0.1 */;
	  delt_w= Double.parseDouble(GetSettingByName("delt_w"))  /* 0.01 - 0.03 */;
	  delt_K= Double.parseDouble(GetSettingByName("delt_K"))  /* 0.02 - 0.15 */;
	  sigm_w= Double.parseDouble(GetSettingByName("sigm_w"));
	  sigm_q= Double.parseDouble(GetSettingByName("sigm_q"));
	  R     = Double.parseDouble(GetSettingByName("R"));
	  t_dv  = Double.parseDouble(GetSettingByName("t_dv"))   /* 0.1 - 5 */;
	  T0    = Double.parseDouble(GetSettingByName("T0"))   /* 1 - 20 */;
	  r_f   = Double.parseDouble(GetSettingByName("r_f"));
	  q_min = Double.parseDouble(GetSettingByName("q_min"))  /*  */;
	  q_max = Double.parseDouble(GetSettingByName("q_max")) /*  */;
	  status_str.append("--------------- Исходные данные -----------------\n");
	  status_str.append("V     =" +  v + ": скорость комбайна [м/с]\n");
	  status_str.append("nom   =" +  nom + " : номер эксперимента\n");
	  status_str.append("tau   =" +  tau + " : темп опроса датчиков [с]\n");
	  status_str.append("T_H   =" +  T_H + " : время наблюдения для формирования 'большой' выборки [с]\n");
	  status_str.append("T_L   =" +  T_L + " : время наблюдения для формирования 'малой' выборки   [с]\n");
	  status_str.append("p     =" +  p + " : макс. значение цикла опроса датчиков\n");
	  status_str.append("IpzI  =" +  IpzI + " : допустимые потери зерна (ПЗ)\n");
	  status_str.append("lam   =" +  lam + " : стандартное отношение зерна к соломе\n");
	  status_str.append("lam_1 =" +  lam + " : истинное отношение зерна к соломе\n");
	  status_str.append("q_nom =" +  q_nom + " : номинальная подача хлебной массы (ПХМ)\n");
	  status_str.append("delt  =" +  delt + " : допустимая величина изменения угловой скорости (УС)\n");
	  status_str.append("delt_y=" +  delt_y + " : допуск на дрейф ПЗ\n");
	  status_str.append("delt_w=" +  delt_w + " : допуск на дрейф УС\n");
	  status_str.append("delt_K=" +  delt_K + " : допуск на дрейф целевой функции\n");
	  status_str.append("sigm_w=" +  sigm_w + " : допуск на значение w_opt\n");
	  status_str.append("sigm_q=" +  sigm_q + " : допуск на значение q_opt\n");
	  status_str.append("R     =" +  R + " : коэффициент настройки системы на оптимальный режим\n");
	  status_str.append("t_dv  =" +  t_dv + " : время включения исполнительного механизма [с]\n");
	  status_str.append("T0    =" +  T0 + " : постоянная времени объекта [с]\n");
	  status_str.append("-------------------------------------------------\n");
	  
	   random = new Random();
//	    for (int idx = 1; idx <= 10; ++idx){
//	    	status_str.append("" + showRandomInteger((int) q_min,(int) q_max, random) + "\n");
//	    }
	    
	    //{ задание начальных значений: w_dat, q_dat, y_dat }
	
	    xfrom = showRandomInteger((int) q_min,(int) q_max, random);	  

	    ran(xfrom, lam_1, v, "q_dat", "y_dat", "w_dat");
	    r_f = r_f + 1;
	    
	    //{ --- начальные значения задали --- }

	    p_int = (int) Math.round(p);
	    
	    //{измерение w_0 при q_dat = 0}
        for (int n = 1; n < p_int; n++) {
        	
          Tsec = Tsec + tau; //{Текущее время наблюдения процесса [с]};
  	      if (n==1)   w_sr_n = w_dat;
  	      
  	      xfrom = showRandomInteger((int) q_min,(int) q_max, random);
  	      r_f = r_f + 1;

  	    status_str.append("xfrom=" + xfrom + " r_f=" + r_f + "\n");


  	      ran(xfrom, lam_1, v, "q_dat", "y_dat", "w_dat");

  	      w_sr_n = (w_sr_n+w_dat)/2.;
  	      
  	      status_str.append("w_sr_n=" + w_sr_n + " q_dat=" + q_dat + " y_dat=" + y_dat + " w_dat=" + w_dat + "\n");

			
		}
	

	    w_0 = w_sr_n;
	    IyI = ((IpzI * ( 1.0 - lam_1 ) * lam * q_nom*1000) / lam_1);
	    y_min = 0.01 * IyI;
	    IwI = w_0 * ( 1.0 - delt );

	    status_str.append("w_0=" + w_0 + " IyI=" + IyI + " IwI=" + IwI + " y_min=" + y_min + "\n");
	    status_str.append("IyI-IyI*delt_y=" + (IyI-IyI*delt_y) + "\n");
	    status_str.append("IyI+IyI*delt_y=" + (IyI+IyI*delt_y) + "\n");
	    status_str.append("IwI-IwI*delt_w=" + (IwI-IwI*delt_w) + "\n");
	    status_str.append("IwI+IwI*delt_w=" + (IwI+IwI*delt_w) + "\n");


	    H = (int) Math.round(T_H / tau);
	    L = (int) Math.round(T_L / tau);
	    i = 1;
	    flag_O = false; //{признак оптимальности/неоптимальности процесса}
	    flag_L = true;  //{признак "малой" выборки}
	    flag_R = false; //{признак "большой" выборки}
	    flag_I = false; //{признак    увеличения/неизменности номера шага оптимиз.}
	    flag_H = false;
	    K_L_1  = 0.0;
	    d_1    = 0.0;
	    drob   = 1.-(1./Math.exp(T_L / T0));
	    s  = 0;
	    
	    
	    mBase();
	    
	    mOptimum();
	    
	    mAnalysis();
	    
	    mExtremum();
	    
	    mSpeed();
		
		
	}
	
	public void mBase() {
		
		status_str.append("*** Вошли в модуль База *** i=" + i + " v=" + v + "\n");
		flag_V = true;

		  if (flag_L || !flag_L && !flag_R && i!=1 && !flag_O)
		  { //{ база }
		    
		      j_max = L;
		      status_str.append("Малая выборка *** j_max:= L=" + L + "\n");
		      flag_H = false;  //{ признак "большой"/"малой" выборки }
		      
		  }
		  else
		    {
		      j_max = H;
		      status_str.append("Большая выборка *** j_max:= H=" + H + "\n");
		      flag_H = true;  //{ признак "большой"/"малой" выборки }
		    }
		  
		  for(j = 1; j < j_max; j++) {
			  for(j = 1; j < j_max; j++)//{ цикл выборки }
		    {

//				  for(n = 1; n < p_int; n++)   //{ цикл опроса датчиков }
//		      {
//		        Tsec = Tsec + tau; //{ Текущее время наблюдения процесса [с] }
//		        //if EOF(datrel) THEN reset(datrel);
//
//		        r_f = r_f + 1;
//		        xfrom = showRandomInteger((int) q_min,(int) q_max, random);
//
////		        IF xfrom=0.0 
////		        THEN
////		          BEGIN
////		  {          WRITELN('1: xfrom=',xfrom:7:4,' r_f=',r_f:5); }
////		            RESET(datrel);
////		  {         r_f := r_f + 1; }
////		            read(datrel,xfrom);
////		  {          WRITELN('2: xfrom=',xfrom:7:4,' r_f=',r_f:5); }
////		          END;
//
//		        ran(xfrom, lam_1, v, q_dat, y_dat, w_dat);
//
//		        IF (n=1)
//		        THEN
//		          BEGIN
//		            w_sr_n := w_dat;
//		            y_sr_n := y_dat;
//		            q_sr_n := q_dat;
//		          END
//		        ELSE
//		          BEGIN
//		            w_sr_n := (w_sr_n + w_dat) / 2.;
//		            q_sr_n := (q_sr_n + q_dat) / 2.;
//		            y_sr_n := (y_sr_n + y_dat) / 2.;
//		          END;
//
//		      END;
//
//		      WRITELN(f_out1,'w_sr_n=',w_sr_n:7:3,' q_sr_n=',q_sr_n:7:3,' y_sr_n=',y_sr_n:7:3,' w_dat=',w_dat:7:3);
//		      WRITELN(f_out1,'Текущее время наблюдения процесса [с] Tsec=',Tsec:7:1,'<--');
//
//		    IF (j=1)
//		    THEN
//		      BEGIN
//		        w_sr := w_sr_n;
//		        y_sr := y_sr_n;
//		        q_sr := q_sr_n;
//		      END
//		    ELSE
//		      BEGIN
//		        w_sr := (w_sr_n + w_sr) / 2.;
//		        y_sr := (y_sr_n + y_sr) / 2.;
//		        q_sr := (q_sr_n + q_sr) / 2.;
//		      END;
//
//		    IF NOT(flag_H)
//		    THEN {***}
//		      BEGIN
//		        IF y_sr_n > (IyI+(IyI*delt_y)) 
//		        THEN
//		          BEGIN
//		            flag_V := FALSE;
//		            WRITELN(f_out1,'Выход Б 1.1  R=',R:7:3);
//		            GOTO 1  { скор }
//		         END;
//
//		  {      IF ( y_sr_n < IyI )  THEN }
//		        IF ( y_sr_n < (IyI-(IyI*delt_y)) )
//		        THEN
//		          BEGIN
//		            IF ( w_sr_n < (IwI-(IwI*delt_w)) ) 
//		            THEN
//		              BEGIN
//		                flag_V := FALSE;
//		                WRITELN(f_out1,'Выход Б 1:1  R=',R:7:3);
//		                GOTO 1  { скор }
//		              END
//		            ELSE
//		              BEGIN
//		                flag_V := TRUE;
//		                WRITELN(f_out1,'Выход Б 1_1  R=',R:7:3);
//		                GOTO 1 { скор }
//		            END;
//
//		          END;
//
//		        IF ( w_sr_n < (IwI-(IwI*delt_w)) )
//		        THEN
//		          BEGIN
//		            flag_V := FALSE;
//		            WRITELN(f_out1,'Выход Б 1.2  R=',R:7:3);
//		            GOTO 1  { скор }
//		          END;
//
//		        IF (y_sr_n <= y_min) 
//		        THEN
//		          BEGIN
//		            flag_V := TRUE;
//		            WRITELN(f_out1,'Выход Б 1 2   R=',R:7:3);
//		            GOTO 1 { скор }
//		          END;

		    } //{***}

	} //{ конец цикла выборки }

	}

	public void mOptimum() {

	}
	
	public void mAnalysis() {

	}
	
	public void mExtremum() {

	}
	
	public void mSpeed() {

	}
	
	 public int showRandomInteger(int aStart, int aEnd, Random aRandom){
		    if (aStart > aEnd) {
		      throw new IllegalArgumentException("Start cannot exceed End.");
		    }
		    //get the range, casting to long to avoid overflow problems
		    long range = (long)aEnd - (long)aStart + 1;
		    // compute a fraction of the range, 0 <= frac < range
		    long fraction = (long)(range * aRandom.nextDouble());
		    return  (int)(fraction + aStart);    
		   
		  }
	 
public String GetSettingByName_FromFile(String Name_par) throws FileNotFoundException{
		 
		 StringBuilder sb = new StringBuilder();
		 
		   
		    File file = new File("F:\\Android\\workspace\\mk900" + this.SettingsFilePath); 
		    if (!file.exists()){
		    	throw new FileNotFoundException(file.getName());
		    }
		 
		    try {
		        //Объект для чтения файла в буфер
		        BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
		        try {
		            //В цикле построчно считываем файл
		            String s;
		            while ((s = in.readLine()) != null) {
		                
		            	if (s.contains("[" + Name_par + "]")) {
		            		
		            		int p1 = s.indexOf("=") + 1;
		            		int p2 = s.indexOf(";");
		            		return s.substring(p1, p2).trim(); 
		            		
		            	}
		            	
		            }
		        } finally {
		            //Также не забываем закрыть файл
		            in.close();
		        }
		    } catch(IOException e) {
		        throw new RuntimeException(e);
		    } 
		    
		    return "";
		 
	 }
		  
	 public String GetSettingByName(String Name_par){
		 
		 String rez = "";
		 
		 try {
			 
			rez = GetSettingByName_FromFile(Name_par);
		 }
		 catch(IOException e) {
		        throw new RuntimeException(e);
		    };
		 
		 return rez; 
		 
	 }
	 


}
