package com.lengjing.info.util;

import com.lengjing.info.C2Number;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A {
    public static void main(String [] args){
        // 叁佰叁拾陆万陆仟陆佰伍拾元
       /* String result= C2Number.chinese2Num("拾万元");
        System.out.println(result);*/
      /* String abc="ddd.";
       System.out.println(abc.indexOf(".")+"---"+abc.lastIndexOf("."));*/
     String value=ParseUtil.parseMoney("一、被告在本判决生效之日起十日内，向原告清还欠款本金106971元及利息（利息从2013年9月15日起至清偿本金之日止，以本金106971元为基数，按年利率24%的标准计付，利息总额中应扣减643元）； 二、驳回原告的其他诉讼请求。 如果当事人未按本判决指定的期间履行给付金钱义务，应当依照《中华人民共和国民事诉讼法》第二百五十三条之规定，加倍支付迟延履行期间的债务利息。 本案诉讼费3413元（含受理费1978元，财产保全费1435元）由原告负担979元，被告负担2434元。被告在本判决生效之日起十日内，将应负担的诉讼费直接支付给原告。 如不服本判决，可在判决书送达之日起十五日内，向本院递交上诉状，并按对方当事人的人数提出副本，上诉于广州市中级人民法院。 当事人上诉的，应在递交上诉状次日起七日内向广州市中级人民法院预交上诉案件受理费。逾期不交的，按自动撤回上诉处理。","铁岭方向集团电子科技有限公司","铁岭铄鑫金属制品有限公司");
        String aa="一、被告在本判决生效之日起十日内，向原告清还欠款本金106971元及利息（利息从2013年9月15日起至清偿本金之日止，以本金106971元为基数，按年利率24%的标准计付，利息总额中应扣减643元）； 二、驳回原告的其他诉讼请求。 如果当事人未按本判决指定的期间履行给付金钱义务，应当依照《中华人民共和国民事诉讼法》第二百五十三条之规定，加倍支付迟延履行期间的债务利息。 本案诉讼费3413元（含受理费1978元，财产保全费1435元）由原告负担979元，被告负担2434元。被告在本判决生效之日起十日内，将应负担的诉讼费直接支付给原告。 如不服本判决，可在判决书送达之日起十五日内，向本院递交上诉状，并按对方当事人的人数提出副本，上诉于广州市中级人民法院。 当事人上诉的，应在递交上诉状次日起七日内向广州市中级人民法院预交上诉案件受理费。逾期不交的，按自动撤回上诉处理。";
       String  ws=aa.replaceAll("(?<=元)(。|.)(?=(由原告|原告|由被告|被告|合计|由))",",").replaceAll("\\{[^\\}]*[\\}]","").replaceAll("【[^】]*[】]","").replaceAll("［[^］]*[］]","").replaceAll("\\[[^\\]]*\\]","").replaceAll("\\n","").replaceAll("（([^（）]+|（([^（）]+)*）)*）",""); //
        System.out.println("ws:"+ws);
        System.out.println("value:"+value);
        String brr[]=value.split("_");
        System.out.println(brr.length);
        System.out.println("dd:"+brr[0]);
       //String reg="（([^（）]+|（([^（）]+)*）)*）";
       //String reg="（([^（）]+|（([^（）]+)*）)*）";
        //String reg="\\([^()]*(\\([^()]*\\)[^()]*)*\\)";
        String reg="（[^（）]*(（[^（）]*）[^（）]*)*）";
        String str="aa（c（dd）cc）dddd（dd）";
        Pattern pattern= Pattern.compile(reg);
        Matcher matcher=pattern.matcher(str);
        if(matcher.find()){
            System.out.println("DD:"+matcher.group());
        }
        String maxmoney="0";
        System.out.println(Double.parseDouble(maxmoney));

    }
}
