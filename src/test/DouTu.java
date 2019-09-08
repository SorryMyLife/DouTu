package test;

import java.util.ArrayList;
import java.util.Scanner;

import com.ToolBox.Crawler;
import com.ToolBox.net.HttpUtils;
import com.ToolBox.util.HtmlTool;
import com.ToolBox.util.StringTool;

/**
 * <p>
 * 创建时间：2019年9月8日 上午11:36:13
 * <p>
 * 项目名称：TestProjectDir
 * 
 * <p>
 * 类说明：
 *斗图图片爬取
 * @version 1.0
 * @since JDK 1.8 文件名称：DouTu.java
 */

public class DouTu {

	private final static StringTool st = new StringTool();
	private static int count = 0;
	private static ArrayList<String> list = new ArrayList<>();
	/**</p>配置搜索链接接*/
	public static String searchLink(String searchName) {
		return "https://www.doutugou.net/?s=" + st.urlencode(searchName);
	}
	/**</p>获取网页源码接*/
	public static String getWebPage(String url_name) {
		return new Crawler().getPage(url_name);
	}
	/**</p>筛选出下一页链接*/
	public static String getNextPage(String page) {
		String pageData = new HtmlTool(page).getByElement("a").toString();
		for (String nextPage : pageData.split("\n")) {
			if (nextPage.indexOf("next") != -1) {
				return new HtmlTool(nextPage).getByElement("a").getHref().toString();
			}
		}
		return null;
	}
	/**</p>根据提供的线程数量进行下载*/
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
	/**</p>获取数据并下载*/
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
	/**</p>运行程序*/
	public static void Run(int num, String searchName, String savePath) {
		String data = getWebPage(searchLink(searchName));
		getData(num, data, savePath);
	}

	// public static void start() {
	// Run(4,"熊猫人", "e:\\test\\files\\imgs\\");
	// }
	public static void help() {
		System.out.println("\n\n第一个参数 : 线程并发数量(必须为数字，否则会报错) \n第二个参数 : 你需要搜索的名字(比如：熊猫人) \n第三个参数 : 你需要保存的路径(必须是已经存在的)\n");
	}
	public static void main(String[] args) {
		if (args.length >= 3) {
			Run(Integer.parseInt(args[0].replaceAll("\\s+", "")), args[1], args[2]);
		} else {
			help();
			Scanner s = new Scanner(System.in);
			System.out.print("输入线程并发数量 (必须为数字，否则会报错): ");
			String data = s.nextLine();
			s = new Scanner(System.in);
			System.out.print("输入搜索内容 (比如：熊猫人): ");
			String data2 = s.nextLine();
			s = new Scanner(System.in);
			System.out.print("输入保存的路径 (必须是已经存在的): ");
			String data3 = s.nextLine();
			Run(Integer.parseInt(data.replaceAll("\\s+", "")), data2, data3);
		}
	}

}
