package test;

import java.util.ArrayList;
import java.util.Scanner;

import com.ToolBox.Crawler;
import com.ToolBox.net.HttpUtils;
import com.ToolBox.util.HtmlTool;
import com.ToolBox.util.StringTool;

/**
 * <p>
 * ����ʱ�䣺2019��9��8�� ����11:36:13
 * <p>
 * ��Ŀ���ƣ�TestProjectDir
 * 
 * <p>
 * ��˵����
 *��ͼͼƬ��ȡ
 * @version 1.0
 * @since JDK 1.8 �ļ����ƣ�DouTu.java
 */

public class DouTu {

	private final static StringTool st = new StringTool();
	private static int count = 0;
	private static ArrayList<String> list = new ArrayList<>();
	/**</p>�����������ӽ�*/
	public static String searchLink(String searchName) {
		return "https://www.doutugou.net/?s=" + st.urlencode(searchName);
	}
	/**</p>��ȡ��ҳԴ���*/
	public static String getWebPage(String url_name) {
		return new Crawler().getPage(url_name);
	}
	/**</p>ɸѡ����һҳ����*/
	public static String getNextPage(String page) {
		String pageData = new HtmlTool(page).getByElement("a").toString();
		for (String nextPage : pageData.split("\n")) {
			if (nextPage.indexOf("next") != -1) {
				return new HtmlTool(nextPage).getByElement("a").getHref().toString();
			}
		}
		return null;
	}
	/**</p>�����ṩ���߳�������������*/
	public static void tD(int num, ArrayList<String> list, String savePath) {
		int cc = 0;
		for (int i = 0; i < list.size(); i++) {
			if (cc < num) {
				String srt = list.get(i);
				new HttpUtils().threadDown(srt, savePath, srt.substring(srt.lastIndexOf("/") + 1));
				cc++;
			} else {
				cc = 0;
				try {
					new Thread().sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("download count : " + count + " ---- part : " + list.size());
		list.clear();
	}
	/**</p>��ȡ���ݲ�����*/
	public static void getData(int num, String data, String savePath) {
		String imgData = new HtmlTool(data).getByElement("a").getAllValue("src").toString();
		// System.out.println(getNextPage(data));
		String imgArray[] = imgData.split("\n");
		if (imgArray.length > 0) {
			for (String s : imgArray) {
				if(s.indexOf("logo") == -1||s.indexOf("\\&") == -1) {
					list.add(s);
				}
			}
			count++;
			tD(num, list, savePath);
			if (getNextPage(data) != null) {
				getData(num, getWebPage(getNextPage(data)), savePath);
			}
		} else {
			System.out.println("data is get ok !");
		}
	}
	/**</p>���г���*/
	public static void Run(int num, String searchName, String savePath) {
		String data = getWebPage(searchLink(searchName));
		getData(num, data, savePath);
	}

	// public static void start() {
	// Run(4,"��è��", "e:\\test\\files\\imgs\\");
	// }
	public static void help() {
		System.out.println("\n\n��һ������ : �̲߳�������(����Ϊ���֣�����ᱨ��) \n�ڶ������� : ����Ҫ����������(���磺��è��) \n���������� : ����Ҫ�����·��(�������Ѿ����ڵ�)\n");
	}
	public static void main(String[] args) {
		if (args.length >= 3) {
			Run(Integer.parseInt(args[0].replaceAll("\\s+", "")), args[1], args[2]);
		} else {
			help();
			Scanner s = new Scanner(System.in);
			System.out.print("�����̲߳������� (����Ϊ���֣�����ᱨ��): ");
			String data = s.nextLine();
			s = new Scanner(System.in);
			System.out.print("������������ (���磺��è��): ");
			String data2 = s.nextLine();
			s = new Scanner(System.in);
			System.out.print("���뱣���·�� (�������Ѿ����ڵ�): ");
			String data3 = s.nextLine();
			Run(Integer.parseInt(data.replaceAll("\\s+", "")), data2, data3);
		}
	}

}
