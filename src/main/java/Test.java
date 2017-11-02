import com.lengjing.info.C2Number;
import com.lengjing.info.TestBean;
import org.apache.derby.impl.store.raw.data.SyncOnCommit;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String [] agrs) throws java.io.IOException{
        String data="一、限被告周秋平、彭道清、邝选华、况彦青、张如于本判决生效之日始三十日内偿还所欠原告汪金林的借款本息共计713982.2元，其中由周秋平承担174142元、由彭道清承担174142元、由邝选华承担121899.4元、由况彦青承担121899.4元、由张如承担121899.4元，并按照各自股份比例偿还后期相应股份比例的利息（本金500000元按月利率2%计息，自2016年10月9日始计算至清偿之日止；本金150000元按月利率1%计息，自2016年10月9日始计算至清偿之日止）。 二、驳回原告汪金林的其他诉讼请求。 如未按本判决指定的期限内履行金钱给付义务，应当依照《中华人民共和国民事诉讼法》第二百五十三条之规定，加倍支付迟延履行期间的债务利息。 案件受理费12350元，减半收取，计币6175元，由被告周秋平负担882元、彭道清负担882元、邝选华负担617.4元、况彦青负担617.4元、张如负担617.4元，由原告汪金林负担2558.8元。 如不服本判决，可在判决书送达之日起十五日内，向本院递交上诉状，并按对方当事人的人数提出副本，上诉于江西省宜春市中级人民法院。在递交上诉状之日起七日内，缴纳上诉费至江西宜春市中级人民法院设于中国农业银行宜春市分行袁山大道分理处14024401040000848账上，逾期不缴纳，按自动放弃上诉处理。";
        String ybperson="汪金林";
        String beigaoperson="周秋平 邝选华 黄小林 张如 彭道清 况彦青";
        //String ws=data.replaceAll("(?<=元)(。|.)(?=(由原告|原告|由被告|被告|合计|由))",",").replaceAll("（([^（）]+|（([^（）]+)*）)*）", "").replaceAll("【[^】]*[】]","").replaceAll("［[^］]*[］]","").replaceAll("\\[[^\\]]*\\]","").replaceAll("\\n","").replaceAll("\\(([^()]+|\\(([^()]+)*\\))*\\)","");
        String ws="";
        /*if(data.indexOf("）")!=data.lastIndexOf("）")){
            ws=data.replaceAll("(?<=元)(。|.)(?=(由原告|原告|由被告|被告|合计|由))",",").replaceAll("（[^）]*[）]", "").replaceAll("【[^】]*[】]","").replaceAll("［[^］]*[］]","").replaceAll("\\[[^\\]]*\\]","").replaceAll("\\n","").replaceAll("（(（([^（）]+)*）)*）",""); //
        }else{
            ws=data.replaceAll("(?<=元)(。|.)(?=(由原告|原告|由被告|被告|合计|由))",",").replaceAll("（[^）]*[）]", "").replaceAll("【[^】]*[】]","").replaceAll("［[^］]*[］]","").replaceAll("\\[[^\\]]*\\]","").replaceAll("\\n","");
        }    */
        ws=data.replaceAll("(?<=元)(。|.)(?=(由原告|原告|由被告|被告|合计|由))",",").replaceAll("（[^（）]*(（[^（）]*）[^（）]*)*）", "").replaceAll("【[^】]*[】]","").replaceAll("［[^］]*[］]","").replaceAll("\\[[^\\]]*\\]","").replaceAll("\\n","");

        String str[]=ws.split("。|；");
        /**** 解析文书金额正则表达式*/
        //String reg="((?!判决|案件)+(本判决)?((?!赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).)+(赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).*[0-9.]+.*)|([^在]+在+[^0-9]+[0-9]+[^范围内，承担连带]+范围内，承担连带+[^责任].+)|([^对]+对+[^0-9]+[0-9]+[^承担连带]+承担+(连带)?(责任)?.*)|([^向]+向((?!欠|清还|偿清|借款).)+(欠|清还|偿清|借款).*[0-9.]+.*)";
        String reg="((?!判决|案件)+(本判决)?((?!赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).)+(赔偿|偿付|支付|偿还|归还|给付|返还|清偿|付还|欠|清还|偿清|退还|赔还|付给|还清|付清|还付|赔付|抵偿|借|向|赔付|还).*(([0-9.]+)|(([一二三四五六七八九十百千]*亿)?([一二三四五六七八九十百千零]*万)?([一二三四五六七八九十零]?千)?([一二三四五六七八九十]?百)?[一二三四五六七八九十零]*(元)[一二三四五六七八九十]?(角)?[一二三四五六七八九十]?(分)?)).*)|([^在]+在+[^0-9]+[0-9]+[^范围内，承担连带]+范围内，承担连带+[^责任].+)|([^对]+对+[^0-9]+[0-9]+[^承担连带]+承担+(连带)?(责任)?.*)|([^向]+向((?!欠|清还|偿清|借款).)+(欠|清还|偿清|借款).*[0-9.]+.*)";
        /***** 解析胜败正则表达式*/ //([^受理费]+((?!由).)+(由).*(承担|负担).*)
        String succfailreg="(((?!受理).)*(受理|诉讼)((?!由).)*(由)?.*(承担|负担).*)|(((?!受理费).)*(受理费|诉讼费)((?!由).)*(由).*)|((?!受理).)*(受理|诉讼).*.被告.*(承担|负担).*";
        String regMoney="((本金|借款|欠款|款项|租金|利息|复利|违约金|孳息|罚息|货款|保证金)+(人民币)?[0-9.]+(，|,)?[0-9.，,]*(，|,)?[0-9]*(万元|万|元)?)(按)?(以)?(月利率)?(年息)?(年)?(计算)?(的)?(利息)?(为基数)?(还清之日)?(基数)?(参照)?|(合计)?(诉讼费)?(受理费)?(上诉费)?(用)?([0-9.，]+(，)?[0-9.]*(金)?(元|万元|亿元|整))(元)?(计算)?(的)?(利息)?(为基数)?(为)?(借款)?(本金)?(计付)?(限)?|([0-9]+%.{0,5}[0-9.]+(元))(为基准)?|(一|二|三|四|五|六|七|八|九)+分之(一|二|三|四|五|六|七|八|九)+.{0,5}[0-9.]+(万)?元(?!为基数)";
        String totalRegMoney="(共计|合计|计)+(本息)?(人民)?(币)?[0-9.]+(，|,)?[0-9.，,]*(，|,)?[0-9]*(万元|万|元)?";
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
           // System.out.println("datajx:"+datajx);
            if(datajx.matches(reg)) {
                System.out.println("reg:"+datajx);
                Matcher ma = pattern.matcher(datajx.replaceAll("（[^）]*[）]", ""));
                Matcher maTotal = patternTotal.matcher(datajx);
               while(maTotal.find()){
                   System.out.println("zongji:"+maTotal.group());
                 flag = true;
                 if(totalMoney>0.0 && !flag){
                     totalMoney += parseMoneyType(maTotal.group());
                 }else{
                     Double dhj=parseMoneyType(maTotal.group());
                     if(dhj>totalMoney){
                         totalMoney=dhj;
                     }
                 }
             }
                while (ma.find()) {
                    strflag=true;
                    if(!ma.group().startsWith("合计") && !ma.group().endsWith("基数") && !ma.group().endsWith("利息") && !ma.group().endsWith("为基准") && !ma.group().endsWith("还清之日") && !ma.group().endsWith("年") && !ma.group().endsWith("参照") && !ma.group().endsWith("本金") && !ma.group().endsWith("年息") && !ma.group().endsWith("月利率") && !ma.group().endsWith("计付") && !ma.group().startsWith("诉讼费") && !ma.group().startsWith("受理费") && !ma.group().startsWith("上诉费")) {
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
                    String zhongwenReg="(本金|借款|欠款|款项|租金|利息|复利|违约金|孳息|罚息|货款|保证金|首付款)+(人民币)?([一二三四五六七八九十百千]*亿)?([一二三四五六七八九十百千零]*万)?[一二三四五六七八九十零]*千?([一二三四五六七八九十]?百)?[一二三四五六七八九十零]*(元)[一二三四五六七八九十零]?(角)?[一二三四五六七八九十]?(分)?(为基数)?|([一二三四五六七八九十百千]*亿)?([一二三四五六七八九十百千零]*万)?([一二三四五六七八九十零]?千)?([一二三四五六七八九十]?百)?[一二三四五六七八九十零]*(元)[一二三四五六七八九十零]?(角)?[一二三四五六七八九十]?(分)?(为基数)?|[壹贰叁肆伍陆柒捌玖拾佰仟]*(亿)?[壹贰叁肆伍陆柒捌玖拾佰仟]*(万)?[壹贰叁肆伍陆柒捌玖拾零]*(仟)?[壹贰叁肆伍陆柒捌玖拾]*(佰)?[壹贰叁肆伍陆柒捌玖拾佰仟]*元";
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
                 System.out.println("dd:"+datajx);
                    // eg:被告黄维明和被告杨国贵负担73，68元
                    String brr[]=datajx.replaceAll(("(?<=[0-9])(，|,)(?=[0-9])"),"").split("[，,]");
                    String ygreg="原告.*(负担|承担)[0-9.]*元";
                    String bgreg="被告.*(负担|承担)[0-9.]*元";
                    Pattern yg=Pattern.compile(ygreg);
                    Pattern bgm=Pattern.compile(bgreg);
                    String ygmoney="";
                    String bgmoney="";
                    String bgpeopel="";
                    for(int j=0;j<brr.length;j++){
                        Matcher myg=yg.matcher(brr[j]);
                        Matcher mbg=bgm.matcher(brr[j]);
                        if(myg.find()){
                            ygmoney=dealWithMoney(myg.group().replaceAll("[^0-9\\.]",""));
                        }else if(mbg.find()){
                             bgmoney=dealWithMoney(mbg.group().replaceAll("[^0-9\\.]",""));
                            /***
                             * 获取被告人
                             */
                            if(beigaoperson.equals("")) {
                                String bgpeoplereg = "(?<=被告).*(?=(负担|承担))";
                                Pattern bgpeople = Pattern.compile(bgpeoplereg);
                                Matcher matcherpeople = bgpeople.matcher(mbg.group());
                                if (matcherpeople.find()) {
                                    bgpeopel = matcherpeople.group().replaceAll("被告", "");
                                }
                            }
                        }
                    }
                    if(!ygmoney.equals("") && !bgmoney.equals("")){
                        if(Double.parseDouble(ygmoney)>Double.parseDouble(bgmoney)){
                            fail="原告";
                        }else{
                            if(beigaoperson.equals("")){
                                    fail=bgpeopel;
                            }else{
                                fail="被告";
                            }
                        }
                    }else{
                      Matcher m=patternsf.matcher(datajx.replaceAll(("(?<=[0-9])(，|,)(?=[0-9])"),""));
                      if(m.find()){
                          String maxmoney="0";
                          String maxperon="";
                          if(m.group().indexOf("由") != m.group().lastIndexOf("由")){
                                String crr[]=m.group().split(",|，");
                                for(int z=0;z<crr.length;z++){
                                    String personregcrr = "(?<=由).*(?=(负担|承担))";
                                    Pattern patterncrr = Pattern.compile(personregcrr);
                                    Matcher matcherpersoncrr = patterncrr.matcher(crr[z]);
                                    String peoplecrr = "";
                                    if (matcherpersoncrr.find()) {
                                        peoplecrr = matcherpersoncrr.group().replaceAll("[、]", " ");
                                    }
                                    String ymoney=dealWithMoney(crr[z].replaceAll("[^0-9\\.]",""));
                                    if(matcherpersoncrr.find() && !crr[z].contains("受理费") && !ymoney.equals("") && Double.parseDouble(ymoney)>Double.parseDouble(maxmoney)){
                                        maxmoney=crr[z].replaceAll("[^0-9\\.]","");
                                        maxperon=peoplecrr;
                                    }
                                }
                                if(maxperon.contains(ybperson)){
                                    fail = "原告";
                                }else{
                                    fail = "被告";
                                }
                          }else {
                              if(m.group().indexOf(",")>=0 || m.group().indexOf("，")>=0){
                                  String crr[]=m.group().split(",|，");
                                  String peoplecrr = "";
                                  for(int z=0;z<crr.length;z++){
                                      String personregcrr = "(?<=由).*(?=(负担|承担))";
                                      Pattern patterncrr = Pattern.compile(personregcrr);
                                      Matcher matcherpersoncrr = patterncrr.matcher(crr[z]);
                                      if (matcherpersoncrr.find()) {
                                          System.out.println("crrz:"+crr[z]);
                                          peoplecrr = matcherpersoncrr.group().replaceAll("[、]", " ");
                                          maxperon=peoplecrr;
                                      }
                                      String youmoney=dealWithMoney(crr[z].replaceAll("[^0-9\\.]",""));
                                      if(matcherpersoncrr.find()  && !youmoney.equals("") && Double.parseDouble(youmoney)>Double.parseDouble(maxmoney)){
                                          maxmoney=crr[z].replaceAll("[^0-9\\.]","");
                                          maxperon=peoplecrr;
                                      }

                                  }

                                  if(!maxperon.equals("") && maxperon.contains(ybperson)){
                                      fail = "原告";
                                  }else if(!maxperon.equals("") && beigaoperson.contains(maxperon)){
                                      fail = "被告";
                                  }else if(peoplecrr.contains("被告")){
                                      fail = "被告";
                                  }else if(peoplecrr.contains("原告")){
                                      fail = "原告";
                                  }
                              }else{
                                  String personreg = "(?<=由).*(?=(负担|承担))";
                                  Pattern pattern2 = Pattern.compile(personreg);
                                  Matcher matcherperson = pattern2.matcher(m.group());
                                  String people = "";
                                  if (matcherperson.find()) {
                                      people = matcherperson.group().replaceAll("[、]", " ");
                                  }
                                  if (people.indexOf("被告")>= 0) {
                                      if(beigaoperson.equals("")){
                                          fail=people.replaceAll("被告","");
                                      }else{
                                          fail="被告";
                                      }
                                  } else {
                                      if (beigaoperson.indexOf(people)>=0) {
                                          fail = "被告";
                                      } else if(ybperson.indexOf(people)>=0) {
                                          fail = "原告";
                                      }
                                  }
                              }
                          }
                      }

                    }
            }
        }
        System.out.println("判决金额:"+Double.parseDouble(String.format("%.6f",totalMoney))+"    "+"败诉方："+fail);
    }

    public static String dealWithMoney(String str){
        String data="";
        if(str.indexOf(".")!=str.lastIndexOf(".")){
            data=str.substring(0,str.indexOf("."))+str.substring(str.indexOf(".")+1);
            return dealWithMoney(data);
        }else{
            data=str;
            return data;
        }

    }

    public static Double parseMoneyType(String data){
        Double result=0.0;
        String str=dealWithMoney(data);
        System.out.println("str:"+str);
        String regEx="[^0-9\\.]";
        if(str.contains("万")){
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


}
