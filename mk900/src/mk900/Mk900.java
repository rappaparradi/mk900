package mk900;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import javax.swing.JOptionPane;

import entities.Setting;
import mk900.Main;
import models.SettingsModel;

public class Mk900 {

	public String lin, lin1, lin2, datrel, f_out1, f_out2;

	public int w_ioresult;

	public StringBuilder status_str = new StringBuilder();

	public static String SettingsFilePath = "\\settings.txt";

	public Boolean flag_H, /* {признак "большой"/"малой" выборки } */
	flag_L, /* {признак "малой" выборки } */
	flag_R, /* {признак "большой" выборки } */
	flag_s, /* {признак экстремума } */
	flag_O, /* {признак оптимальности/неоптимальности процесса } */
	flag_V, /* {признак увеличения/уменьшения скорости } */
	flag_I, /* {признак увеличения/неизменности номера шага оптимиз.} */
	bfinish; /* Признак завершение расчетов */

	public int n, /* параметр цикла опроса датчиков */
	p_int, /* макс. значение параметра цикла опроса датчиков */
	j, /* параметр цикла "большой" и "малой" выборок */
	j_max, /* макс. значение параметра цикла "бол." и "мал." выборок */
	s, /* признак рысканий */
	L, /* макс. значение параметра цикла "малой" выборки */
	H, /* макс. значение параметра цикла "большой" выборки */
	i, /* номер шага оптимизации */
	nom; /* номер эксперимента */

	double t_dv, /* время включения исполнительного механизма [с] */
	v, /* скорость комбайна [м/с] */
	tau, /* темп опроса датчиков [с] */
	T_H, /* время наблюдения для формиров. "большой" выборки [с] */
	T_L, /* время наблюдения для формиров. "малой" выборки [с] */
	p, /* макс. значение цикла опроса датчиков */
	IpzI, /* допустимые потери зерна (ПЗ) */
	IyI, /* допуск на ПЗ */
	IwI, /* допуск на значение угловой скорости (УС) */
	lam, /* стандартное отношение зерна к соломе */
	lam_1, /* истинное отношение зерна к соломе */
	q_nom, /* номинальная подача хлебной массы (ПХМ) */
	q_dat, /* значение ПХМ считанное с датчика */
	q_sr_n, /* значение ПХМ скользяще-усред. по циклу опроса датчика */
	q_sr, /* значение ПХМ скользяще-усред. по циклу выборки */
	q_opt, /* оптимальное значение ПХМ */
	y_min, /* допуст. уровень помех в измер-ном канале ПЗ IyI*0.65 */
	y_dat, /* значение ПЗ считанное с датчика */
	y_sr_n, /* значение ПЗ скользяще-усред. по циклу опроса датчика */
	y_sr, /* значение ПЗ скользяще-усред. по циклу выборки */
	y_opt, /* оптимальное значение ПЗ */
	w_dat, /* значение УС считанное с датчика */
	w_sr_n, /* значение УС скользяще-усред. по циклу опроса датчика */
	w_sr, /* значение УС скользяще-усред. по циклу выборки */
	w_opt, /* оптимальное значение УС */
	w_0, /* начальное значение УС */
	delt, /* допустимая величина изменения угловой скорости (УС) */
	delt_y, /* допуск на дрейф ПЗ */
	delt_w, /* допуск на дрейф УС */
	delt_K, /* допуск на дрейф целевой функции */
	delt_K_L, /* значение (K_H - K_L) */
	d, /* значение (delt_K_L / drob) */
	d_1, /* буфер для значения d */
	V0, V1, V2, /* буферы для реализации задержки считывания V */
	drob, /* значение 1-(1/(exp(T_L/T0))) */
	K_L, /* значение целевой функции "малой" выборки */
	K_H, /* значение целевой функции "большой" выборки */
	K_p, /* буфер для временного хранения K_L или K_H */
	K_opt, /* оптимальное значение целевой функции */
	R, /* коэффициент настройки системы на оптимальный режим */
	priz_R, /* признак "большой"(1),"малой"(0) выборки после нового R */
	sigm_w, /* допуск на значение w_opt */
	sigm_q, /* допуск на значение q_opt */
	xfrom, /* случайное число с норм.законом распределения из файла */
	R_opt, /* оптимальное значение R */
	T0, /* постоянная времени объекта [с] */
	K_L_1, /* */
	K_t, /* */
	Tsec, /* Текущее время наблюдения процесса [с] */
	r_f, /* кол-во считываний Q */
	q_min, /* min значение урожайности [ц/га] */
	q_max; /* max значение урожайности [ц/га] */

	Random random;

	public Mk900() {
		
		Tsec = Double.parseDouble(GetSettingByName("Tsec"));
		v = Double.parseDouble(GetSettingByName("v"));
		nom = Integer.parseInt(GetSettingByName("nom")) /* 1 - 100 */;
		tau = Double.parseDouble(GetSettingByName("tau")) /* 0.2 - 1.0 */;
		T_H = Double.parseDouble(GetSettingByName("T_H")) /* 5 - 100 */;
		T_L = Double.parseDouble(GetSettingByName("T_L")) /* 1 - 5 */;
		p = Double.parseDouble(GetSettingByName("p")) /* 5 - 10 */;
		IpzI = Double.parseDouble(GetSettingByName("IpzI"));
		lam = Double.parseDouble(GetSettingByName("lam"));
		lam_1 = Double.parseDouble(GetSettingByName("lam_1")) /* 0.2 - 1.2 */;
		q_nom = Double.parseDouble(GetSettingByName("q_nom")) /* 4 - 12 */;
		delt = Double.parseDouble(GetSettingByName("delt")) /* 0.04 - 0.12 */;
		delt_y = Double.parseDouble(GetSettingByName("delt_y")) /* 0.01 - 0.1 */;
		delt_w = Double.parseDouble(GetSettingByName("delt_w")) /* 0.01 - 0.03 */;
		delt_K = Double.parseDouble(GetSettingByName("delt_K")) /* 0.02 - 0.15 */;
		sigm_w = Double.parseDouble(GetSettingByName("sigm_w"));
		sigm_q = Double.parseDouble(GetSettingByName("sigm_q"));
		R = Double.parseDouble(GetSettingByName("R"));
		t_dv = Double.parseDouble(GetSettingByName("t_dv")) /* 0.1 - 5 */;
		T0 = Double.parseDouble(GetSettingByName("T0")) /* 1 - 20 */;
		r_f = Double.parseDouble(GetSettingByName("r_f"));
		q_min = Double.parseDouble(GetSettingByName("q_min")) /*  */;
		q_max = Double.parseDouble(GetSettingByName("q_max")) /*  */;
		
		
		
		
	}

	/*                                                              */
	/* Вычисление q,y,w */
	/*                                                              */
	/*--------------------------------------------------------------*/
	/* Описание переменных: */
	/*                                                              */
	/* b - ширина захвата комбайна [м] */
	/* v_pro - текущая скорость комбайна, скорость на момент */
	/* съёма показателей по q,y,w */
	/* xin - случайное число из массива случайных чисел, сгенери- */
	/* рованных процедурой RAND */
	/* la1 - соломистость хлебной массы (расчётный коэффициент) */
	/* q - подача хлебной массы [кг/с] */
	/* y - потери зерна [гр./с] */
	/* w - угловая скорость молотильного барабана [1/c] */
	/*                                                              */

	public void ran(double xin, double la1, double v_pro, String q_name,
			String y_name, String w_name)

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


		status_str
				.append("--------------- Исходные данные -----------------\n");
		status_str.append("V     =" + v + ": скорость комбайна [м/с]\n");
		status_str.append("nom   =" + nom + " : номер эксперимента\n");
		status_str.append("tau   =" + tau + " : темп опроса датчиков [с]\n");
		status_str
				.append("T_H   ="
						+ T_H
						+ " : время наблюдения для формирования 'большой' выборки [с]\n");
		status_str
				.append("T_L   ="
						+ T_L
						+ " : время наблюдения для формирования 'малой' выборки   [с]\n");
		status_str.append("p     =" + p
				+ " : макс. значение цикла опроса датчиков\n");
		status_str.append("IpzI  =" + IpzI
				+ " : допустимые потери зерна (ПЗ)\n");
		status_str.append("lam   =" + lam
				+ " : стандартное отношение зерна к соломе\n");
		status_str.append("lam_1 =" + lam
				+ " : истинное отношение зерна к соломе\n");
		status_str.append("q_nom =" + q_nom
				+ " : номинальная подача хлебной массы (ПХМ)\n");
		status_str.append("delt  =" + delt
				+ " : допустимая величина изменения угловой скорости (УС)\n");
		status_str.append("delt_y=" + delt_y + " : допуск на дрейф ПЗ\n");
		status_str.append("delt_w=" + delt_w + " : допуск на дрейф УС\n");
		status_str.append("delt_K=" + delt_K
				+ " : допуск на дрейф целевой функции\n");
		status_str.append("sigm_w=" + sigm_w + " : допуск на значение w_opt\n");
		status_str.append("sigm_q=" + sigm_q + " : допуск на значение q_opt\n");
		status_str.append("R     =" + R
				+ " : коэффициент настройки системы на оптимальный режим\n");
		status_str.append("t_dv  =" + t_dv
				+ " : время включения исполнительного механизма [с]\n");
		status_str.append("T0    =" + T0
				+ " : постоянная времени объекта [с]\n");
		status_str
				.append("-------------------------------------------------\n");

		bfinish = false;

		random = new Random();
		// for (int idx = 1; idx <= 10; ++idx){
		// status_str.append("" + showRandomInteger((int) q_min,(int) q_max,
		// random) + "\n");
		// }

		// { задание начальных значений: w_dat, q_dat, y_dat }

		xfrom = showRandomInteger((int) q_min, (int) q_max, random);

		ran(xfrom, lam_1, v, "q_dat", "y_dat", "w_dat");
		r_f = r_f + 1;

		// { --- начальные значения задали --- }

		p_int = (int) Math.round(p);

		// {измерение w_0 при q_dat = 0}
		for (int n = 1; n < p_int; n++) {

			Tsec = Tsec + tau; // {Текущее время наблюдения процесса [с]};
			if (n == 1)
				w_sr_n = w_dat;

			xfrom = showRandomInteger((int) q_min, (int) q_max, random);
			r_f = r_f + 1;

			status_str.append("xfrom=" + xfrom + " r_f=" + r_f + "\n");

			ran(xfrom, lam_1, v, "q_dat", "y_dat", "w_dat");

			w_sr_n = (w_sr_n + w_dat) / 2.;

			status_str.append("w_sr_n=" + w_sr_n + " q_dat=" + q_dat
					+ " y_dat=" + y_dat + " w_dat=" + w_dat + "\n");

		}

		w_0 = w_sr_n;
		IyI = ((IpzI * (1.0 - lam_1) * lam * q_nom * 1000) / lam_1);
		y_min = 0.01 * IyI;
		IwI = w_0 * (1.0 - delt);

		status_str.append("w_0=" + w_0 + " IyI=" + IyI + " IwI=" + IwI
				+ " y_min=" + y_min + "\n");
		status_str.append("IyI-IyI*delt_y=" + (IyI - IyI * delt_y) + "\n");
		status_str.append("IyI+IyI*delt_y=" + (IyI + IyI * delt_y) + "\n");
		status_str.append("IwI-IwI*delt_w=" + (IwI - IwI * delt_w) + "\n");
		status_str.append("IwI+IwI*delt_w=" + (IwI + IwI * delt_w) + "\n");

		H = (int) Math.round(T_H / tau);
		L = (int) Math.round(T_L / tau);
		i = 1;
		flag_O = false; // {признак оптимальности/неоптимальности процесса}
		flag_L = true; // {признак "малой" выборки}
		flag_R = false; // {признак "большой" выборки}
		flag_I = false; // {признак увеличения/неизменности номера шага
						// оптимиз.}
		flag_H = false;
		K_L_1 = 0.0;
		d_1 = 0.0;
		drob = 1. - (1. / Math.exp(T_L / T0));
		s = 0;

		mBase();

		mOptimum();

		mAnalysis();

		mExtremum();

		mSpeed();

	}

	public void mBase() { // 2

		if (bfinish) {

			return;

		}

		status_str.append("*** Вошли в модуль База *** i=" + i + " v=" + v
				+ "\n");
		flag_V = true;

		if (flag_L || !flag_L && !flag_R && i != 1 && !flag_O) { // { база }

			j_max = L;
			status_str.append("Малая выборка *** j_max:= L=" + L + "\n");
			flag_H = false; // { признак "большой"/"малой" выборки }

		} else {
			j_max = H;
			status_str.append("Большая выборка *** j_max:= H=" + H + "\n");
			flag_H = true; // { признак "большой"/"малой" выборки }
		}

		for (j = 1; j < j_max; j++) // { цикл выборки }
		{
			for (n = 1; n < p_int; n++) // { цикл опроса датчиков }
			{
				Tsec = Tsec + tau; // { Текущее время наблюдения процесса [с] }
				// if EOF(datrel) THEN reset(datrel);

				r_f = r_f + 1;
				xfrom = showRandomInteger((int) q_min, (int) q_max, random);

				if (xfrom == 0.0) {

					// { status_str.append("1: xfrom=" + xfrom + " r_f=" + r_f +
					// "\n"); }
					// RESET(datrel);
					// { r_f = r_f + 1; }
					xfrom = showRandomInteger((int) q_min, (int) q_max, random);
					// { status_str.append("2: xfrom=" + xfrom + " r_f=" + r_f +
					// "\n"); }
				}

				ran(xfrom, lam_1, v, "q_dat", "y_dat", "w_dat");

				if (n == 1) {
					w_sr_n = w_dat;
					y_sr_n = y_dat;
					q_sr_n = q_dat;
				} else {
					w_sr_n = (w_sr_n + w_dat) / 2;
					q_sr_n = (q_sr_n + q_dat) / 2;
					y_sr_n = (y_sr_n + y_dat) / 2;
				}

			}
			// f_out1
			status_str.append("w_sr_n=" + w_sr_n + " q_sr_n= " + q_sr_n
					+ " y_sr_n=" + y_sr_n + " w_dat=" + w_dat + "\n");
			status_str.append("Текущее время наблюдения процесса [с] Tsec="
					+ Tsec + "<--" + "\n");

			if (j == 1) {

				w_sr = w_sr_n;
				y_sr = y_sr_n;
				q_sr = q_sr_n;
			} else {
				w_sr = (w_sr_n + w_sr) / 2;
				y_sr = (y_sr_n + y_sr) / 2;
				q_sr = (q_sr_n + q_sr) / 2;
			}

			if (!flag_H) { // {***}

				if (y_sr_n > (IyI + (IyI * delt_y))) {

					flag_V = false;
					status_str.append("Выход Б 1.1  R=" + R + "\n");
					mSpeed();// GOTO 1 { скор }
				}

				// { if ( y_sr_n < IyI ) THEN }
				if (y_sr_n < (IyI - (IyI * delt_y))) {
					if (w_sr_n < (IwI - (IwI * delt_w))) {
						flag_V = false;
						status_str.append("Выход Б 1:1  R=" + R + "\n");
						mSpeed();// GOTO 1 { скор }
					} else {
						flag_V = true;
						status_str.append("Выход Б 1_1  R=" + R + "\n");
						// WRITELN(f_out1,'Выход Б 1_1 R=',R:7:3);
						mSpeed();// GOTO 1 { скор }
					}
				}

				if (w_sr_n < (IwI - (IwI * delt_w))) {
					flag_V = false;
					status_str.append("Выход Б 1.2  R=" + R + "\n");

					mSpeed();// GOTO 1 { скор }
				}

				if (y_sr_n <= y_min) {
					flag_V = true;
					status_str.append("Выход Б 1 2   R=" + R + "\n");
					mSpeed();// GOTO 1 { скор }
				}

			} // {***}

		} // { конец цикла выборки }

		flag_R = false; // {признак "большой" выборки}
		flag_L = false; // {признак "малой" выборки}
		status_str.append("w_sr=" + w_sr + " q_sr=" + q_sr + " y_sr=" + y_sr
				+ "\n");
		status_str.append("Текущее время наблюдения процесса [с] Tsec=" + Tsec
				+ "<--" + "\n");

		K_p = Math.abs((w_sr / w_0) - R * (y_sr / q_sr));
		status_str.append("i=" + i + " K_p=" + K_p + " R=" + R + "\n");

		if (flag_H)
			K_H = K_p;
		else {
			K_L_1 = K_L;
			K_L = K_p;
		}

		status_str.append("Выход Б 3" + "\n");

		if (flag_O) { // { анализ, если было оптимально }

			status_str.append("Вошли в модуль *** Оптимум ***  i=" + i + " v="
					+ v + "\n");
			if ((w_sr < w_opt + sigm_w * w_opt)
					&& (w_sr > w_opt - sigm_w * w_opt)) {
				if ((q_sr < q_opt + sigm_q * q_opt)
						&& (q_sr > q_opt - sigm_q * q_opt)) {
					status_str.append("Выход O 1" + "\n");

					if (K_p <= K_opt)
						flag_R = false;
					{
						flag_O = true;
						mAnalysis();// goto 8 { в анализ }
					}

					status_str.append("**** ВХОД В БАЗУ ИЗ ** ОПТИМУМА **"
							+ "\n");
					mBase();// GOTO 2 { база }
				} else {
					if ((q_sr - (q_opt - sigm_q * q_opt)) > 0
							&& (q_sr - (q_opt + sigm_q * q_opt)) > 0) {
						flag_V = false;
						status_str.append("Выход О 2" + "\n");// f_out1
						flag_O = false;
						mSpeed();// GOTO 1 { скорость }
					} else {
						flag_V = true;
						status_str.append("Выход О 3" + "\n");// f_out1
						flag_O = true;
						mSpeed();// GOTO 1 { скорость }
					}
				}
			} else {
				if ((w_sr - (w_opt - sigm_w * w_opt)) > 0
						&& (w_sr - (w_opt + sigm_w * w_opt)) > 0) {
					flag_V = true;
					status_str.append("Выход О 4" + "\n");// f_out1
					flag_O = false;
					mSpeed();// GOTO 1 { скорость }
				} else {
					flag_V = false;
					status_str.append("Выход О 5" + "\n");// f_out1
					flag_O = false;
					mSpeed();// GOTO 1 { скорость }
				}
			}
		}

	}

	public void mOptimum() { // 3

		if (bfinish) {

			return;

		}

		status_str.append("**** Вошли в модуль Оптимум i=" + i + " v=" + v
				+ "\n"); // f_out1
		K_opt = K_p;
		q_opt = q_nom;
		w_opt = w_sr;
		R_opt = R; // {* (w_sr*q_sr)/(w_0*y_sr); *}

		flag_O = true;
		flag_R = true;

		status_str.append("           *************************" + "\n"); // f_out1
		status_str.append("Выход А 6  *** процесс оптимален ***" + "\n"); // f_out1
		status_str.append("Текущее время наблюдения процесса [с] Tsec=" + Tsec
				+ "<--" + "\n"); // f_out1
		status_str.append("w_sr=" + w_sr + " q_sr=" + q_sr + " y_sr=" + y_sr
				+ " v=" + v + " R=" + R + " K_p=" + K_p + "\n"); // f_out1
		status_str.append("" + Tsec + " " + i + " " + w_sr + " " + q_sr + " "
				+ y_sr + " " + v + " " + R + " " + K_p + " оптимум" + "\n"); // f_out2
		// { GOTO 2 } { база };
		mExit();// GOTO 9;

	}

	public void mAnalysis() { // 8

		if (bfinish) {

			return;

		}

		// WRITELN(f_out1,'*** Вошли в модуль Анализ *** i=',i:4,' v=',v:7:4);
		status_str.append("*** Вошли в модуль Анализ *** i=" + i + " v=" + v
				+ "\n"); // f_out1
		status_str.append("K_p=" + K_p + " <??> delt_K=" + delt_K + "\n"); // f_out1

		if (K_p <= delt_K) { // { анализ, если не было оптимально }

			if ((y_sr < (IyI + delt_y * IyI)) && (y_sr > (IyI - delt_y * IyI))) {
				if (w_sr >= (IwI + delt_w * IwI)) { // {***}

					status_str.append("ВХОД В ОПТИМУМ 1" + "\n"); // f_out1
					mOptimum();// GOTO 3 { оптимум };
				} else {
					mNeopt1(); // GOTO 4 { неопт 1 }
				}
			} else {
				if (y_sr < (IyI - delt_y * IyI)) {
					if ((w_sr < (IwI + delt_w * IwI))
							&& (w_sr > (IwI - delt_w * IwI))) {
						status_str.append("ВХОД В ОПТИМУМ 2" + "\n"); // f_out1
						mOptimum(); // GOTO 3 //{ оптимум };
					} else {
						if (w_sr > (IwI + delt_w * IwI))
							mNeopt2(); // GOTO 5 //{ неопт 2 }
						else
							mNeopt1(); // GOTO 4 //{ неопт 1 };
					}
				} else
					mNeopt1();// GOTO 4 {неопт 1}

			}
		}

		// { условие K_p <= delt_K не выполнено }
		status_str.append("K_p=" + K_p + " НЕ !!! <= delt_K=" + delt_K + "\n"); // f_out1

		if ((y_sr < (IyI + delt_y * IyI)) && (y_sr > (IyI - delt_y * IyI))) {
			if (w_sr > (IwI + delt_w * IwI))
				mR_new();// GOTO 6 //{ R новое };
			else
				mNeopt1();// GOTO 4 //{ неопт 1 }

		} else {
			if (y_sr < (IyI - delt_y * IyI)) {
				if ((w_sr < (IwI + delt_w * IwI))
						&& (w_sr > (IwI - delt_w * IwI)))
					mR_new();// GOTO 6 //{ R новое }
				else {
					if (w_sr > (IwI + delt_w * IwI))
						mNeopt2();// GOTO 5 //{ неопт 2 }
					else
						mNeopt1(); // GOTO 4 //{ неопт 1 }
				}
			} else
				mNeopt1();// GOTO 4 //{ неопт 1 }

		}

	}

	public void mExtremum() { // 7

		if (bfinish) {

			return;

		}

		status_str.append("**** Вошли в модуль Экстремум i=" + i + " v=" + v
				+ "\n");

		if (i == 2)
			delt_K_L = K_H - K_L;
		else
			delt_K_L = K_L - K_L_1;

		status_str.append("delt_K_L=" + delt_K_L + " K_H=" + K_H + " K_L="
				+ K_L + "\n"); // f_out1
		d_1 = d;
		d = delt_K_L / drob;
		status_str.append("d=" + d + " drob=" + drob + "\n"); // f_out1

		if (i == 2) {
			status_str.append("Выход Э 1 и Э 2" + "\n"); // f_out1
			if (delt_K_L > 0)
				flag_V = false;
			else
				flag_V = true;

			flag_I = true;
			flag_L = true;
			// {* R:=(w_sr*q_sr)/(w_0*y_sr); *}
			mSpeed();// GOTO 1 { скорость }
		}

		// { delt_2K1 := delt_2_K; }

		if (d_1 * d <= 0) {
			status_str.append("*  МИНИМУМ  *  ВЫХОД Э 3.3   d_1*d=" + d_1 * d
					+ "\n"); // f_out1
			flag_V = true;
			t_dv = 0.1;
			mSpeed();// GOTO 1 { скорость };
		}

		if (flag_V) {
			status_str.append("Выход Э 3.1 или Э 3.2" + "\n"); // f_out1

			if (delt_K_L > 0.0)
				flag_V = false;
			else
				flag_V = true;

			flag_I = true;
			flag_L = true;
			mSpeed();// GOTO 1 { скорость }
		} else {
			flag_L = true;
			flag_I = true;

			if (delt_K_L > 0.0)
				flag_V = true;
			else
				flag_V = false;

			status_str.append("Выход Э 3.3'" + "\n");
			// {* R:=(w_sr*q_sr)/(w_0*y_sr); *}
			// { GOTO 2 }
			mSpeed(); // GOTO 1 { скорость };
		}

	}

	public void mSpeed() {// 1

		if (bfinish) {

			return;

		}

		status_str.append("**** Вошли в модуль Скорость i=" + i + " v=" + v
				+ "\n");// f_out1
		status_str.append("Текущее время наблюдения процесса [с] Tsec=" + Tsec
				+ "<--" + "\n");// f_out1

		V0 = V1;
		V1 = V2;
		V2 = v;
		K_t = 0.0;

		if (q_sr != 0.0)
			K_t = Math.abs((w_sr / w_0) - R * (y_sr / q_sr));

		status_str.append("" + Tsec + " " + i + " " + w_sr + " " + q_sr + " "
				+ y_sr + " " + v + " " + R + " " + K_t + "\n");// f_out2
		if (V0 == V2)
			s = s + 1;

		if (s > 2) {

			status_str.append("Зациклив. процесса!  s>2, t_dv=" + t_dv + "\n");
			mExit();// GOTO 9;
		}

		if ((s == 2) && (t_dv > 0.0125)) {
			t_dv = t_dv / 2.;
			flag_s = true;
			s = 0;
			flag_H = true; // { <- }
			flag_L = false;// { <- }
		}

		if (flag_V)
			v = v + t_dv;
		else
			v = v - t_dv;

		status_str.append("v=" + v + "\n");// f_out1
		flag_R = true;

		if (flag_I) {
			i = i + 1;
			if (i > 400)
				mExit();// GOTO 9;

			flag_I = false;
			flag_O = false;
			status_str.append("Выход С 2  i=" + i + "\n");// f_out1
		} else
			status_str.append("Выход С 1  i=" + i + "\n");// f_out1

		mBase();// GOTO 2 { база };

	}

	public void mNeopt1() {// 4

		if (bfinish) {

			return;

		}

		status_str.append("**** Вошли в модуль Неопт 1  i=" + i + " v=" + v
				+ "\n");// f_out1
		flag_I = true;
		flag_V = true;
		flag_O = false;

		status_str.append("Выход А 4" + "\n");// f_out1

		mSpeed();// GOTO 1 //{ скорость };

	}

	public void mR_new() {// 6

		if (bfinish) {

			return;

		}

		status_str.append("**** Вошли в модуль R новое i=" + i + " v=" + v
				+ "\n");// f_out1

		R = (w_sr * q_sr) / (w_0 * y_sr);
		status_str.append("перевычисляем R =" + R + "\n");// /f_out1
		flag_R = true;
		flag_L = false;

		status_str.append("Выход А 1" + "\n");// /f_out1
		K_p = 0.0;
		mAnalysis();// GOTO 8;

	}

	public void mNeopt2() {// 5

		if (bfinish) {

			return;

		}

		status_str.append("**** Вошли в модуль Неопт 2  i" + i + " v=" + v
				+ "\n");// /f_out1
		flag_O = false;

		if (i == 1) {
			flag_I = true;
			flag_V = true;

			status_str.append("Выход А 3" + "\n");// /f_out1
			mSpeed();// GOTO 1 { скорость }
		} else {
			status_str.append("Выход А 5" + "\n");// /f_out1
			mExtremum();// GOTO 7 //{ Экстремум }
		}

	}

	public void mExit() {// 9

		status_str.append("Расчет завершен  i=" + i
				+ ", кол-во считываний Q: r_f= " + r_f + "\n");
		bfinish = true;

	}

	public int showRandomInteger(int aStart, int aEnd, Random aRandom) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		// get the range, casting to long to avoid overflow problems
		long range = (long) aEnd - (long) aStart + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * aRandom.nextDouble());
		return (int) (fraction + aStart);

	}

	public String GetSettingByName_FromFile(String Name_par)
			throws FileNotFoundException {

		StringBuilder sb = new StringBuilder();

		File file = new File("C:\\Users\\radin\\git\\mk900_src\\mk900"
				+ this.SettingsFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException(file.getName());
		}

		try {
			// Объект для чтения файла в буфер
			BufferedReader in = new BufferedReader(new FileReader(
					file.getAbsoluteFile()));
			try {
				// В цикле построчно считываем файл
				String s;
				while ((s = in.readLine()) != null) {

					if (s.contains("[" + Name_par + "]")) {

						int p1 = s.indexOf("=") + 1;
						int p2 = s.indexOf(";");
						return s.substring(p1, p2).trim();

					}

				}
			} finally {
				// Также не забываем закрыть файл
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return "";

	}
	
	public void SetSettingByName_FromFile(String Name_par,String value_par)
			throws FileNotFoundException {
		BufferedWriter writer;
		StringBuilder sb = new StringBuilder();

		File rfile = new File("C:\\Users\\radin\\git\\mk900_src\\mk900"
				+ this.SettingsFilePath);
		if (!rfile.exists()) {
			throw new FileNotFoundException(rfile.getName());
		}

		try {
			File wfile = new File("C:\\Users\\radin\\git\\mk900_src\\mk900\\f_buffer.txt");
			wfile.createNewFile();

		    writer = new BufferedWriter(new FileWriter(wfile));

		   
			// Объект для чтения файла в буфер
			BufferedReader in = new BufferedReader(new FileReader(
					rfile.getAbsoluteFile()));
			try {
				// В цикле построчно считываем файл
				String s;
				while ((s = in.readLine()) != null) {
					int p1, p2;
					if (s.contains("[" + Name_par + "]")) {

						p1 = s.indexOf("=");
						writer.write(s.substring(0, p1+1) + value_par + ";");
						writer.newLine();

					} else {
						writer.write(s);
						writer.newLine();
					}

				}
				
				rfile.delete();
				
				
			} finally {
				// Также не забываем закрыть файл
				in.close();
				
				
				writer.flush();
				writer.close();
				
				wfile.renameTo(new File("C:\\Users\\radin\\git\\mk900_src\\mk900"
						+ this.SettingsFilePath));
				
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		

	}

	public String GetSettingByName(String Name_par) {

		String rez = "";

		try {

			//rez = GetSettingByName_FromFile(Name_par);
			SettingsModel sm = new SettingsModel();
			Setting set_obj = sm.find(Name_par);
			if (set_obj!=null){
				
				rez = set_obj.getValue();
				
			} else throw new IOException();
					
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Не задано значение параметра " + Name_par + " !!!");
			status_str.append(e + "\n");
			throw new RuntimeException(e);
		}

		return rez;

	}
	
	public void SetSettingByName(String Name_par, String value_par) {

		String rez = "";

		try {

			SetSettingByName_FromFile(Name_par, value_par);
		} catch (IOException e) {
			status_str.append(e + "\n");
			throw new RuntimeException(e);
		}

		

	}

}
