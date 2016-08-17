package getopendata;

import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kizax
 */
public class MapUtils {

    public static Map<Integer, String> getItemIdMap() {
        Map<Integer, String> itemIdMap = new HashMap();
        itemIdMap.put(1, "SO2");
        itemIdMap.put(2, "CO");
        itemIdMap.put(3, "O3");
        itemIdMap.put(4, "PM10");
        itemIdMap.put(5, "NOx");

        itemIdMap.put(6, "NO");
        itemIdMap.put(7, "NO2");
        itemIdMap.put(14, "AMB_TEMP");
        itemIdMap.put(23, "RAINFALL");
        itemIdMap.put(33, "PM2.5");

        itemIdMap.put(38, "RH");

        return itemIdMap;
    }

    public static Map<Integer, String> getSiteMap() {
        Map<Integer, String> siteMap = new HashMap();
        siteMap.put(1, "基隆");
        siteMap.put(2, "汐止");
        siteMap.put(3, "萬里");
        siteMap.put(4, "新店");
        siteMap.put(5, "土城");
        siteMap.put(6, "板橋");
        siteMap.put(7, "新莊");
        siteMap.put(8, "菜寮");
        siteMap.put(9, "林口");
        siteMap.put(10, "淡水");
        siteMap.put(11, "士林");
        siteMap.put(12, "中山");
        siteMap.put(13, "萬華");
        siteMap.put(14, "古亭");
        siteMap.put(15, "松山");
        siteMap.put(16, "大同");
        siteMap.put(17, "桃園");
        siteMap.put(18, "大園");
        siteMap.put(19, "觀音");
        siteMap.put(20, "平鎮");
        siteMap.put(21, "龍潭");
        siteMap.put(22, "湖口");
        siteMap.put(23, "竹東");
        siteMap.put(24, "新竹");
        siteMap.put(25, "頭份");
        siteMap.put(26, "苗栗");
        siteMap.put(27, "三義");
        siteMap.put(28, "豐原");
        siteMap.put(29, "沙鹿");
        siteMap.put(30, "大里");
        siteMap.put(31, "忠明");
        siteMap.put(32, "西屯");
        siteMap.put(33, "彰化");
        siteMap.put(34, "線西");
        siteMap.put(35, "二林");
        siteMap.put(36, "南投");
        siteMap.put(37, "斗六");
        siteMap.put(38, "崙背");
        siteMap.put(39, "新港");
        siteMap.put(40, "朴子");
        siteMap.put(41, "臺西");
        siteMap.put(42, "嘉義");
        siteMap.put(43, "新營");
        siteMap.put(44, "善化");
        siteMap.put(45, "安南");
        siteMap.put(46, "臺南");
        siteMap.put(47, "美濃");
        siteMap.put(48, "橋頭");
        siteMap.put(49, "仁武");
        siteMap.put(50, "鳳山");
        siteMap.put(51, "大寮");
        siteMap.put(52, "林園");
        siteMap.put(53, "楠梓");
        siteMap.put(54, "左營");
        siteMap.put(56, "前金");
        siteMap.put(57, "前鎮");
        siteMap.put(58, "小港");
        siteMap.put(59, "屏東");
        siteMap.put(60, "潮州");
        siteMap.put(61, "恆春");
        siteMap.put(62, "臺東");
        siteMap.put(63, "花蓮");
        siteMap.put(64, "陽明");
        siteMap.put(65, "宜蘭");
        siteMap.put(66, "冬山");
        siteMap.put(67, "三重");
        siteMap.put(68, "中壢");
        siteMap.put(69, "竹山");
        siteMap.put(70, "永和");
        siteMap.put(71, "復興");
        siteMap.put(72, "埔里");
        siteMap.put(75, "馬祖");
        siteMap.put(77, "金門");
        siteMap.put(78, "馬公");
        siteMap.put(80, "關山");
        siteMap.put(83, "麥寮");

        return siteMap;
    }

    public static Map<String, Integer> getItemMap() {
        Map<String, Integer> itemMap = new HashMap();
        itemMap.put("SO2", 1);
        itemMap.put("CO", 2);
        itemMap.put("O3", 3);
        itemMap.put("PM10", 4);
        itemMap.put("NOx", 5);

        itemMap.put("NO", 6);
        itemMap.put("NO2", 7);
        itemMap.put("AMB_TEMP", 14);
        itemMap.put("RAINFALL", 23);
        itemMap.put("PM2.5", 33);

        itemMap.put("RH", 38);
        return itemMap;
    }

    public static Map< String, Integer> getSiteIdMap() {
        Map<String, Integer> siteIdMap = new HashMap();
        siteIdMap.put("基隆", 1);
        siteIdMap.put("汐止", 2);
        siteIdMap.put("萬里", 3);
        siteIdMap.put("新店", 4);
        siteIdMap.put("土城", 5);
        siteIdMap.put("板橋", 6);
        siteIdMap.put("新莊", 7);
        siteIdMap.put("菜寮", 8);
        siteIdMap.put("林口", 9);
        siteIdMap.put("淡水", 10);

        siteIdMap.put("士林", 11);
        siteIdMap.put("中山", 12);
        siteIdMap.put("萬華", 13);
        siteIdMap.put("古亭", 14);
        siteIdMap.put("松山", 15);
        siteIdMap.put("大同", 16);
        siteIdMap.put("桃園", 17);
        siteIdMap.put("大園", 18);
        siteIdMap.put("觀音", 19);
        siteIdMap.put("平鎮", 20);

        siteIdMap.put("龍潭", 21);
        siteIdMap.put("湖口", 22);
        siteIdMap.put("竹東", 23);
        siteIdMap.put("新竹", 24);
        siteIdMap.put("頭份", 25);
        siteIdMap.put("苗栗", 26);
        siteIdMap.put("三義", 27);
        siteIdMap.put("豐原", 28);
        siteIdMap.put("沙鹿", 29);
        siteIdMap.put("大里", 30);

        siteIdMap.put("忠明", 31);
        siteIdMap.put("西屯", 32);
        siteIdMap.put("彰化", 33);
        siteIdMap.put("線西", 34);
        siteIdMap.put("二林", 35);
        siteIdMap.put("南投", 36);
        siteIdMap.put("斗六", 37);
        siteIdMap.put("崙背", 38);
        siteIdMap.put("新港", 39);
        siteIdMap.put("朴子", 40);

        siteIdMap.put("臺西", 41);
        siteIdMap.put("嘉義", 42);
        siteIdMap.put("新營", 43);
        siteIdMap.put("善化", 44);
        siteIdMap.put("安南", 45);
        siteIdMap.put("臺南", 46);
        siteIdMap.put("美濃", 47);
        siteIdMap.put("橋頭", 48);
        siteIdMap.put("仁武", 49);
        siteIdMap.put("鳳山", 50);

        siteIdMap.put("大寮", 51);
        siteIdMap.put("林園", 52);
        siteIdMap.put("楠梓", 53);
        siteIdMap.put("左營", 54);
        siteIdMap.put("前金", 56);
        siteIdMap.put("前鎮", 57);
        siteIdMap.put("小港", 58);
        siteIdMap.put("屏東", 59);
        siteIdMap.put("潮州", 60);
        siteIdMap.put("恆春", 61);

        siteIdMap.put("臺東", 62);
        siteIdMap.put("花蓮", 63);
        siteIdMap.put("陽明", 64);
        siteIdMap.put("宜蘭", 65);
        siteIdMap.put("冬山", 66);
        siteIdMap.put("三重", 67);
        siteIdMap.put("中壢", 68);
        siteIdMap.put("竹山", 69);
        siteIdMap.put("永和", 70);
        siteIdMap.put("復興", 71);

        siteIdMap.put("埔里", 72);
        siteIdMap.put("馬祖", 75);
        siteIdMap.put("金門", 77);
        siteIdMap.put("馬公", 78);
        siteIdMap.put("關山", 80);
        siteIdMap.put("麥寮", 83);

        return siteIdMap;
    }

}
