import com.lengjing.info.C2Number;
import com.lengjing.info.TestBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.*;
public class Parse {
    public static void main(String [] args) throws java.io.FileNotFoundException,java.io.IOException{

      File f=new File("d:\\yubing.lei\\Desktop\\判决金额解析规则");
        File[] flist=f.listFiles();
        for(File l:flist){
			System.out.println(l.getAbsoluteFile());

        }
        InputStream stream = new FileInputStream("d:\\yubing.lei\\Desktop\\判决金额解析规则\\test.xls");
        Workbook wb = null;
        String fileType="xls";
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook(stream);
        } else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook(stream);
        } else {
            System.out.println("您输入的excel格式不正确");
        }
        List<TestBean> list =new ArrayList<TestBean>();
        for(int i=0;i<wb.getNumberOfSheets();i++){
            Sheet sheet1 = wb.getSheetAt(i);
            for (Row row : sheet1) {
                Cell cell=row.getCell(0);
                if(cell!=null){
                    String result=parseMoney(cell.getStringCellValue());
                    TestBean b=new TestBean();
                    b.setData1(cell.getStringCellValue());
                    String data[]=result.split("_");
                    b.setData2(data[0]);
                    if(data.length==2) {
                        b.setData3(data[1]);
                    }
                    list.add(b);
                }

            }
        }
        String title[] = {"判决内容","金额","胜败诉"};
        Parse.writer("d:\\yubing.lei\\Desktop\\判决金额解析规则","test_result","xls",list,title);

    }

    public static Double parseMoneyType(String str){
        Double result=0.0;
        String regEx="[^0-9\\.]";
        if(str.indexOf(".")!=str.lastIndexOf(".")){
            str=str.substring(0,str.indexOf("."))+str.substring(str.indexOf(".")+1);
        }
        if(str.contains("万")){
            System.out.println(str.replaceAll(regEx,""));
            result=Double.parseDouble(str.replaceAll(regEx,""));
        }else if(str.contains("亿")){
            result=Double.parseDouble(str.replaceAll(regEx,""))*10000;
        }else{
            if(!str.replaceAll(regEx,"").equals(".")) {
                result = Double.parseDouble(String.format("%.6f",Double.parseDouble(str.replaceAll(regEx, "")) / 10000));
            }
        }
        return result;
    }
    public static String parseMoney(String data){
        String ws=data.replaceAll("(?<=元)(。|.)(?=(由原告|原告|由被告|被告|合计))",",").replaceAll("（[^）]*[）]", "").replaceAll("【[^】]*[】]","").replaceAll("［[^］]*[］]","").replaceAll("\\n","");
        String str[]=ws.split("。|；");
        /**** 解析文书金额正则表达式*/
        //String reg="((?!判决|案件)+(本判决)?((?!赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).)+(赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).*[0-9.]+.*)|([^在]+在+[^0-9]+[0-9]+[^范围内，承担连带]+范围内，承担连带+[^责任].+)|([^对]+对+[^0-9]+[0-9]+[^承担连带]+承担+(连带)?(责任)?.*)|([^向]+向((?!欠|清还|偿清|借款).)+(欠|清还|偿清|借款).*[0-9.]+.*)";
        String reg="((?!判决|案件)+(本判决)?((?!赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).)+(赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).*(([0-9.]+)|(([一二三四五六七八九十百千]*亿)?([一二三四五六七八九十百千零]*万)?([一二三四五六七八九十零]?千)?([一二三四五六七八九十]?百)?[一二三四五六七八九十零]*(元)[一二三四五六七八九十]?(角)?[一二三四五六七八九十]?(分)?)).*)|([^在]+在+[^0-9]+[0-9]+[^范围内，承担连带]+范围内，承担连带+[^责任].+)|([^对]+对+[^0-9]+[0-9]+[^承担连带]+承担+(连带)?(责任)?.*)|([^向]+向((?!欠|清还|偿清|借款).)+(欠|清还|偿清|借款).*[0-9.]+.*)";
        /***** 解析胜败正则表达式*/ //([^受理费]+((?!由).)+(由).*(承担|负担).*)
        String succfailreg="(((?!受理).)*(受理|诉讼)((?!由).)*(由)?.*(承担|负担).*)|(((?!受理费).)*(受理费|诉讼费)((?!由).)*(由).*)|被告.*(承担|负担).*";
        String regMoney="((本金|借款|欠款|款项|租金|利息|复利|违约金|孳息|罚息|货款|保证金)+(人民币)?[0-9.]+(，|,)?[0-9.，,]*(，|,)?[0-9]*(万元|万|元)?)(按)?(月利率)?(年息)?(年)?(计算)?(的)?(为基数)?(还清之日)?(基数)?(参照)?|(合计)?([0-9.，]+(，)?[0-9.]*(金)?(元|万元|亿元|整))(元)?(计算)?(的)?(利息)?(为基数)?(为)?(本金)?(计付)?|([0-9]+%.{0,5}[0-9.]+(元))(为基准)?|(一|二|三|四|五|六|七|八|九)+分之(一|二|三|四|五|六|七|八|九)+.{0,5}[0-9.]+(万)?元(?!为基数)";
        String totalRegMoney="(共计|合计|计)+(本息)?(人民币)?[0-9.]+(，|,)?[0-9.，,]*(，|,)?[0-9]*(万元|万|元)?";
        // 解析金额
        Pattern  pattern= Pattern.compile(regMoney);
        //解析总金额
        Pattern  patternTotal=Pattern.compile(totalRegMoney);
        // 定义胜败诉方
        Pattern patternsf=Pattern.compile(succfailreg);

        Double totalMoney=0.0;
        String resultMoney="";
        boolean flag=false;
        Double result=0.0;
        List<Double> list=new ArrayList<Double>();
        boolean strflag=false;
        String fail="";
        String datajx="";
        for(int i=0;i<str.length;i++){
            // 去除开头是判决 案件的关键词
            if(str[i].startsWith("判决") || str[i].startsWith("案件")){
                datajx=" "+str[i];
            }else{
                datajx=str[i];
            }
            System.out.println("datajx:"+datajx);
            if(datajx.matches(reg)) {
                Matcher ma = pattern.matcher(datajx.replaceAll("（[^）]*[）]", ""));
                Matcher maTotal = patternTotal.matcher(datajx);
                if (maTotal.find()) {
                    flag = true;
                    if(totalMoney>0.0){
                        totalMoney += parseMoneyType(maTotal.group());
                    }else{
                        totalMoney = parseMoneyType(maTotal.group());
                    }
                }
                while (ma.find()) {
                    strflag=true;
                    System.out.println("ma.group():"+ma.group());
                    if(!ma.group().startsWith("合计") && !ma.group().endsWith("基数") && !ma.group().endsWith("利息") && !ma.group().endsWith("为基准") && !ma.group().endsWith("还清之日") && !ma.group().endsWith("年") && !ma.group().endsWith("参照") && !ma.group().endsWith("本金") && !ma.group().endsWith("年息") && !ma.group().endsWith("月利率") && !ma.group().endsWith("计付")) {
                        if (ma.group().contains("%")) {
                            Pattern patterna = Pattern.compile("(?<=%).*");
                            Matcher matchera = patterna.matcher(ma.group());
                            if (matchera.find()) {
                                result = parseMoneyType(matchera.group());
                            }
                        } else {
                            result = parseMoneyType(ma.group());
                        }
                        list.add(result);
                    }

                    if (flag && result > totalMoney) {
                        flag = false;
                    }
                }
                if(!strflag){
                    String zhongwenReg="(本金|借款|欠款|款项|租金|利息|复利|违约金|孳息|罚息|货款|保证金|首付款)+(人民币)?([一二三四五六七八九十百千]*亿)?([一二三四五六七八九十百千零]*万)?[一二三四五六七八九十零]?千?([一二三四五六七八九十]?百)?[一二三四五六七八九十零]*(元)[一二三四五六七八九十]?(角)?[一二三四五六七八九十]?(分)?(为基数)?|([一二三四五六七八九十百]*亿)?([一二三四五六七八九十百千]*万)?([一二三四五六七八九十]?千)?([一二三四五六七八九十]?百)?[零一二三四五六七八九十]*(元)[一二三四五六七八九十]?(角)?[一二三四五六七八九十]?(分)?(为基数)?|[壹贰叁肆伍陆柒捌玖拾佰仟]*(亿)?[壹贰叁肆伍陆柒捌玖拾佰仟]*(万)?[壹贰叁肆伍陆柒捌玖拾零]*(仟)?[壹贰叁肆伍陆柒捌玖拾]*(佰)?[壹贰叁肆伍陆柒捌玖拾佰仟]*元";
                    Pattern  patternZhongWen= Pattern.compile(zhongwenReg);
                    Matcher maZhongWen = patternZhongWen.matcher(datajx.replaceAll("（[^）]*[）]", ""));
                    while(maZhongWen.find()){
                        if(!maZhongWen.group().endsWith("为基数")) {
                            String maZhongWenResult= maZhongWen.group().replaceAll("(?!一|二|三|四|五|六|七|八|九|分|角|元|十|百|千|万|十万|百万|千万|亿|十亿|百亿|千亿|零|壹|贰|叁|肆|伍|陆|柒|捌|玖|拾|佰|仟)[\\u4e00-\\u9fa5]", "");
                            String zhmoney=maZhongWenResult.replace("仟","千").replace("佰","百");
                            String zwResult=C2Number.chinese2Num(zhmoney);
                            list.add(parseMoneyType(zwResult));
                        }
                    }
                }
                if (!flag) {
                    for (int j = 0; j < list.size(); j++) {
                        totalMoney += list.get(j);
                    }
                }
                list.clear();
            }
            if(datajx.matches(succfailreg)){
                System.out.println("strsuccfailreg:"+datajx.replaceAll(("(?<=[0-9])(，|,)(?=[0-9])"),""));
                // eg:被告黄维明和被告杨国贵负担73，68元
                String brr[]=datajx.replaceAll(("(?<=[0-9])(，|,)(?=[0-9])"),"").split("[，,]");
                String ygreg="原告.*(负担|承担)[0-9.]*元";
                String bgreg="被告.*(负担|承担)[0-9.]*元";
                Pattern yg=Pattern.compile(ygreg);
                Pattern bgm=Pattern.compile(bgreg);
                String ygmoney="";
                String bgmoney="";
                for(int j=0;j<brr.length;j++){
                    Matcher myg=yg.matcher(brr[j]);
                    Matcher mbg=bgm.matcher(brr[j]);
                    if(myg.find()){
                        ygmoney= myg.group().replaceAll("[^0-9\\.]","");
                    }else if(mbg.find()){
                        bgmoney=mbg.group().replaceAll("[^0-9\\.]","");
                    }
                }
                if(!ygmoney.equals("") && !bgmoney.equals("")){
                    if(Double.parseDouble(ygmoney)>Double.parseDouble(bgmoney)){
                        fail="原告";
                    }else{
                        fail="被告";
                    }
                }else{
                    Matcher m=patternsf.matcher(datajx);
                    if(m.find()){
                        fail=m.group().indexOf("被告")>0 ? "被告":"原告";
                    }

                }
            }
            data=Double.parseDouble(String.format("%.6f",totalMoney))+"";
        }
        return data+"_"+fail;
    }

    public static void writer(String path, String fileName, String fileType, List<TestBean> list, String titleRow[]) throws IOException {
        Workbook wb = null;
        String excelPath = path+ File.separator+fileName+"."+fileType;
        File file = new File(excelPath);
        Sheet sheet =null;
        //创建工作文档对象
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if(fileType.equals("xlsx")) {

                wb = new XSSFWorkbook();
            } else {

            }
            //创建sheet对象
            sheet = (Sheet) wb.createSheet("sheet1");
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } else {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if(fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();

            }
        }
        //创建sheet对象
        if (sheet==null) {
            sheet = (Sheet) wb.createSheet("sheet1");
        }

        //添加表头
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        row.setHeight((short) 540);
        cell.setCellValue("数据结果");    //创建第一行

        CellStyle style = wb.createCellStyle(); // 样式对象
        // 设置单元格的背景颜色为淡蓝色
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);

        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

        cell.setCellStyle(style); // 样式，居中

        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 280);
        style.setFont(font);
        // 单元格合并
        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.autoSizeColumn(5200);

        row = sheet.createRow(1);    //创建第二行
        for(int i = 0;i < titleRow.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(titleRow[i]);
            cell.setCellStyle(style); // 样式，居中
            //sheet.setColumnWidth(i, 20 * 256);
        }
        row.setHeight((short) 540);

        //循环写入行数据
        for (int i = 0; i < list.size(); i++) {
            row = (Row) sheet.createRow(i+2);
            row.setHeight((short) 500);
            row.createCell(0).setCellValue(( list.get(i).getData1()));
            row.createCell(1).setCellValue(( list.get(i)).getData2());
            row.createCell(2).setCellValue(( list.get(i)).getData3());

        }

        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }
}
