package com.lengjing.test;

import com.lengjing.info.TestBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Rename {
    static String sql = null;
    static DBHelper db1 = null;
    static ResultSet ret = null;
    public static void main(String [] args) throws IOException {

        File f=new File("d:\\yubing.lei\\Desktop\\判决金额解析规则");
        File[] flist=f.listFiles();
        for(File l:flist){
            System.out.println(l.getAbsoluteFile());

        }
        InputStream stream = new FileInputStream("d:\\yubing.lei\\Desktop\\判决金额解析规则\\query_result_rename.xls");
        Workbook wb = null;
        String fileType="xls";
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook(stream);
        } else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook(stream);
        } else {
            System.out.println("您输入的excel格式不正确");
        }
        for(int i=0;i<wb.getNumberOfSheets();i++){
            Sheet sheet1 = wb.getSheetAt(i);
            for (Row row : sheet1) {
                Cell cell=row.getCell(1);
                System.out.println("cell:"+cell.getStringCellValue());
                if(cell!=null){
                    String companyname=cell.getStringCellValue();
                    //sql = "select * from mc_result where oldmc='"+companyname+"';";//SQL语句
                    sql="select * from mc_result limit 200";
                    db1 = new DBHelper(sql);//创建DBHelper对象
                    System.out.println("sql:"+sql);
                    try {
                        ret = db1.pst.executeQuery();//执行语句，得到结果集
                        while (ret.next()) {
                            System.out.println("dd");
                            String uid = ret.getString(1);
                            String ufname = ret.getString(2);
                            String ulname = ret.getString(3);
                            String udate = ret.getString(4);
                            System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );
                        }//显示数据
                        ret.close();
                        db1.close();//关闭连接
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
